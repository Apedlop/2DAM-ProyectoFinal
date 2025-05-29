package com.example.BackEnd.service.impl;

import com.example.BackEnd.model.cycle.CycleDto;
import com.example.BackEnd.model.cycle.CycleVO;
import com.example.BackEnd.model.enums.TypePeriod;
import com.example.BackEnd.model.phase.PhaseDto;
import com.example.BackEnd.model.phase.PhaseVO;
import com.example.BackEnd.model.symptom.SymptomVO;
import com.example.BackEnd.repository.CycleRepository;
import com.example.BackEnd.repository.PhaseRepository;
import com.example.BackEnd.repository.SymptomRepository;
import com.example.BackEnd.service.CycleService;
import com.example.BackEnd.service.PhaseService;
import com.example.BackEnd.util.CycleMapper;
import com.example.BackEnd.util.PhaseMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class CycleServiceImpl implements CycleService {

    @Autowired
    private CycleRepository cycleRepository;

    @Autowired
    private SymptomRepository symptomRepository;

    @Autowired
    private PhaseRepository phaseRepository;

    @Autowired
    private PhaseService phaseService;

    @Override
    public CycleDto addCycle(CycleDto cycleDto) {
        CycleVO cycleVO = CycleMapper.cycleDtoToCycleVO(cycleDto);

        // Generar fases antes de guardar
        List<PhaseDto> phasesDto = phaseService.generatePhases(
                cycleVO.getStartDate(),
                cycleVO.getCycleLength(),
                cycleVO.getMenstruationDuration(),
                cycleVO.getUserId());

        // Convertir fases a VO
        List<PhaseVO> phasesVO = phasesDto.stream()
                .map(PhaseMapper::phaseDtoToPhaseVO)
                .collect(Collectors.toList());

        // Asignar fases al ciclo
        cycleVO.setPhases(phasesVO);

        // Guardar ciclo con fases embebidas
        CycleVO savedCycle = cycleRepository.save(cycleVO);
        return CycleMapper.cycleVOToCycleDto(savedCycle);
    }

    @Override
    public CycleDto updateCycle(CycleDto cycleDto) {
        return cycleRepository.findById(cycleDto.getId())
                .map(existingCycle -> {
                    // Actualizar propiedades básicas
                    existingCycle.setStartDate(cycleDto.getStartDate());
                    existingCycle.setCycleLength(cycleDto.getCycleLength());
                    existingCycle.setMenstruationDuration(cycleDto.getMenstruationDuration());

                    // Regenerar fases
                    List<PhaseDto> newPhases = phaseService.generatePhases(
                            cycleDto.getStartDate(),
                            cycleDto.getCycleLength(),
                            cycleDto.getMenstruationDuration(),
                            existingCycle.getUserId());

                    // Convertir y asignar nuevas fases
                    List<PhaseVO> phasesVO = newPhases.stream()
                            .map(PhaseMapper::phaseDtoToPhaseVO)
                            .collect(Collectors.toList());
                    existingCycle.setPhases(phasesVO);

                    CycleVO updatedCycle = cycleRepository.save(existingCycle);
                    return CycleMapper.cycleVOToCycleDto(updatedCycle);
                })
                .orElse(null);
    }

    @Override
    public ResponseEntity deleteCycle(String id) {
        System.out.println("ID recibido: " + id);

        try {
            Optional<CycleVO> cycleOptional = cycleRepository.findById(id);
            if (cycleOptional.isPresent()) {
                cycleRepository.deleteById(id);
                symptomRepository.deleteByCycleId(id);
                return ResponseEntity.ok("Ciclo y síntomas eliminados exitosamente");
            } else {
                return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Ciclo no encontrado con el ID: " + id);
            }
        } catch (Exception e) {
            e.printStackTrace();
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Error al eliminar el ciclo");
        }
    }

    @Override
    public Optional<CycleDto> getCycleById(String id) {
        Optional<CycleVO> cycleOptional = cycleRepository.findById(id);
        return cycleOptional.map(CycleMapper::cycleVOToCycleDto);
    }

    @Override
    public List<CycleDto> getCyclesByUserId(String userId) {
        List<CycleVO> cycleVO = cycleRepository.findByUserId(userId);
        return CycleMapper.cycleVOListToCycleDto(cycleVO);
    }

    @Override
    public List<CycleDto> getAllCycles() {
        List<CycleVO> cycleVO = cycleRepository.findAll();
        return cycleVO.stream().map(CycleMapper::cycleVOToCycleDto).collect(Collectors.toList());
    }

    @Override
    public Optional<CycleDto> getLastCycleByUserId(String userId) {
        Optional<CycleVO> cycleOptional = cycleRepository.findLastCycleByUserId(userId);
        return cycleOptional.map(CycleMapper::cycleVOToCycleDto);
    }

    @Override
    public Optional<LocalDate> calculateNextPeriodDate(String userId) {
        return cycleRepository.findLastCycleByUserId(userId)
                .flatMap(lastCycle -> {
                    // Manejo explícito de nulls y conversión
                    Integer cycleLength = lastCycle.getCycleLength();
                    int defaultLength = (cycleLength != null) ? cycleLength : 28;

                    int length = calculateAverageCycleLength(userId).orElse(defaultLength);

                    LocalDate nextDate = lastCycle.getStartDate().plusDays(length);
                    while (nextDate.isBefore(LocalDate.now())) {
                        nextDate = nextDate.plusDays(length);
                    }

                    return Optional.of(nextDate);
                });
    }

    @Override
    public Optional<Integer> calculateAverageCycleLength(String userId) {
        return cycleRepository.findByUserId(userId).stream()
                .map(CycleVO::getCycleLength)
                .filter(Objects::nonNull)
                .filter(length -> length > 0)
                .collect(Collectors.collectingAndThen(
                        Collectors.averagingInt(Integer::intValue),
                        avg -> Optional.of((int) Math.round(avg))
                ));
    }

    @Override
    public boolean existsCycleOnDate(String userId, LocalDate date) {
        return cycleRepository.findByUserId(userId).stream()
                .anyMatch(cycle -> {
                    LocalDate startDate = cycle.getStartDate();
                    Integer length = cycle.getCycleLength();

                    // Si no tiene longitud definida, solo comparamos la fecha de inicio
                    if (length == null || length <= 0) {
                        return startDate.equals(date);
                    }

                    // Calculamos el rango del ciclo (startDate hasta startDate + length - 1)
                    LocalDate endDate = startDate.plusDays(length - 1);
                    return !date.isBefore(startDate) && !date.isAfter(endDate);
                });
    }

    @Override
    public List<CycleDto> getCyclesBetweenDates(String userId, LocalDate from, LocalDate to) {
        // Validación de fechas
        if (from.isAfter(to)) {
            throw new IllegalArgumentException("La fecha 'from' no puede ser posterior a 'to'");
        }

        return cycleRepository.findByUserId(userId).stream()
                .filter(cycle -> {
                    LocalDate startDate = cycle.getStartDate();
                    Integer length = cycle.getCycleLength();

                    // Si no tiene longitud definida, solo verificamos la fecha de inicio
                    if (length == null || length <= 0) {
                        return !startDate.isBefore(from) && !startDate.isAfter(to);
                    }

                    // Calculamos el rango completo del ciclo
                    LocalDate endDate = startDate.plusDays(length - 1);

                    // Verificamos superposición de rangos
                    return !startDate.isAfter(to) && !endDate.isBefore(from);
                })
                .map(CycleMapper::cycleVOToCycleDto)
                .collect(Collectors.toList());
    }

    @Override
    public int calculatePeriodLength(String userId, String cycleId) {
        List<SymptomVO> sintomas = symptomRepository.findByUserIdAndCycleIdOrderByDateAsc(userId, cycleId);

        if (sintomas.isEmpty()) {
            return 0;  // No hay síntomas, duración 0
        }

        LocalDate firstDate = sintomas.get(0).getDate();
        LocalDate lastDate = sintomas.get(sintomas.size() - 1).getDate();

        // Calcular diferencia en días (inclusive)
        int duracion = (int) java.time.temporal.ChronoUnit.DAYS.between(firstDate, lastDate) + 1;

        return duracion;
    }

    // Añade fases al ciclo y guarda el ciclo actualizado.
    @Override
    public CycleDto addPhasesToCycle(CycleVO cycle, List<PhaseVO> phasesToAdd) {
        if (cycle.getPhases() == null) {
            cycle.setPhases(new ArrayList<>());
        }
        cycle.getPhases().addAll(phasesToAdd);
        CycleVO savedCycle = cycleRepository.save(cycle);
        return CycleMapper.cycleVOToCycleDto(savedCycle);
    }

    // Genera las fases para un ciclo, las asigna y guarda el ciclo.
    @Override
    public CycleDto generateAndSavePhasesForCycle(CycleVO cycle) {
        List<PhaseDto> phasesDto = phaseService.generatePhases(
                cycle.getStartDate(),
                cycle.getCycleLength(),
                cycle.getMenstruationDuration(),
                cycle.getUserId());

        List<PhaseVO> phasesVO = phasesDto.stream()
                .map(PhaseMapper::phaseDtoToPhaseVO)
                .collect(Collectors.toList());

        cycle.setPhases(phasesVO);
        CycleVO savedCycle = cycleRepository.save(cycle);
        return CycleMapper.cycleVOToCycleDto(savedCycle);
    }


}
