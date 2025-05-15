package com.example.backMoom.service.impl;

import com.example.backMoom.model.cycle.CycleDto;
import com.example.backMoom.model.cycle.CycleVO;
import com.example.backMoom.repository.CycleRepository;
import com.example.backMoom.service.CycleService;
import com.example.backMoom.util.CycleMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.List;
import java.util.Objects;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class CycleServiceImpl implements CycleService {

    @Autowired
    private CycleRepository cycleRepository;

    @Override
    public CycleDto addCycle(CycleDto cycleDto) {
        CycleVO cycleVO = CycleMapper.cycleDtoToCycleVO(cycleDto);
        CycleVO createCycle = cycleRepository.save(cycleVO);
        return CycleMapper.cycleVOToCycleDto(createCycle);
    }

    @Override
    public CycleDto updateCycle(CycleDto cycleDto) {
        Optional<CycleVO> cycleOptional = cycleRepository.findById(cycleDto.getId());

        if (cycleOptional.isPresent()) {
            CycleVO cycleVO = cycleOptional.get();
            cycleVO.setStartDate(cycleDto.getStartDate());
            cycleVO.setCycleLength(cycleDto.getCycleLength());
            CycleVO updateCycle = cycleRepository.save(cycleVO);
            return CycleMapper.cycleVOToCycleDto(updateCycle);
        } else {
            return null;
        }
    }

    @Override
    public ResponseEntity deleteCycle(String id) {
        System.out.println("ID recibido: " + id);

        try {
            Optional<CycleVO> cycleOptional = cycleRepository.findById(id);
            if (cycleOptional.isPresent()) {
                cycleRepository.deleteById(id);
                return ResponseEntity.ok("Ciclo eliminado exitosamente");
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
}
