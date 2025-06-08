package com.example.BackEnd.service.impl;

import com.example.BackEnd.model.phase.PhaseDto;
import com.example.BackEnd.model.phase.PhaseVO;
import com.example.BackEnd.model.enums.PhaseCycle;
import com.example.BackEnd.model.prediction.PredictionDto;
import com.example.BackEnd.repository.PhaseRepository;
import com.example.BackEnd.service.PhaseService;
import com.example.BackEnd.service.PredictionService;
import com.example.BackEnd.util.PhaseMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.*;
import java.util.stream.Collectors;

@Service
public class PhaseServiceImpl implements PhaseService {

    @Autowired
    private PhaseRepository phaseRepository;

    @Autowired
    private PredictionService predictionService;

    @Override
    public PhaseDto addPhase(PhaseDto phaseDto) {
        PhaseVO phaseVO = PhaseMapper.phaseDtoToPhaseVO(phaseDto);
        PhaseVO createdPhase = phaseRepository.save(phaseVO);
        return PhaseMapper.phaseVOToPhaseDto(createdPhase);
    }

    @Override
    public PhaseDto updatePhase(PhaseDto phaseDto) {
        Optional<PhaseVO> phaseOptional = phaseRepository.findById(phaseDto.getId());
        if (phaseOptional.isPresent()) {
            PhaseVO phaseVO = phaseOptional.get();
            phaseVO.setColor(phaseDto.getColor());
            phaseVO.setPhaseCycle(phaseDto.getPhaseCycle());
            phaseVO.setStartDay(phaseDto.getStartDay());
            phaseVO.setEndDay(phaseDto.getEndDay());
            phaseVO.setDescription(phaseDto.getDescription());
            PhaseVO updatedPhase = phaseRepository.save(phaseVO);
            return PhaseMapper.phaseVOToPhaseDto(updatedPhase);
        } else {
            return null;
        }
    }

    @Override
    public ResponseEntity<?> deletePhase(String id) {
        try {
            Optional<PhaseVO> phaseOptional = phaseRepository.findById(id);
            if (phaseOptional.isPresent()) {
                phaseRepository.deleteById(id);
                return ResponseEntity.ok("Fase eliminada exitosamente");
            } else {
                return ResponseEntity.notFound().build();
            }
        } catch (Exception e) {
            e.printStackTrace();
            return ResponseEntity.status(500).body("Error al eliminar la fase");
        }
    }

    @Override
    public ResponseEntity deleteAllPhase() {
        phaseRepository.deleteAll();
        return ResponseEntity.ok().build();
    }

    @Override
    public Optional<PhaseDto> getPhaseById(String id) {
        Optional<PhaseVO> phaseOptional = phaseRepository.findById(id);
        return phaseOptional.map(PhaseMapper::phaseVOToPhaseDto);
    }

    @Override
    public List<PhaseDto> getAllPhase() {
        List<PhaseVO> phaseVOList = phaseRepository.findAll();
        return phaseVOList.stream().map(PhaseMapper::phaseVOToPhaseDto).collect(Collectors.toList());
    }

    @Override
    public List<PhaseDto> getPhaseByDateRange(LocalDate start, LocalDate end) {
        List<PhaseVO> phaseVOList = phaseRepository.findByStartDayBetween(start, end);
        return phaseVOList.stream().map(PhaseMapper::phaseVOToPhaseDto).collect(Collectors.toList());
    }

    @Override
    public List<PhaseDto> getPhaseByCycle(String phaseCycle) {
        List<PhaseVO> phaseVOList = phaseRepository.findByPhaseCycle(phaseCycle);
        return phaseVOList.stream().map(PhaseMapper::phaseVOToPhaseDto).collect(Collectors.toList());
    }

    @Override
    public List<PhaseDto> generatePhases(LocalDate startDate, int cycleLength, int menstruationLength, String idUser) {
        if (cycleLength < menstruationLength + 14 + 1) {
            throw new IllegalArgumentException("La duración del ciclo es demasiado corta para fases estándar.");
        }

        List<PhaseDto> phases = new ArrayList<>();
        int lutealLength = 14;
        int ovulationLength = 1;

        int remainingDays = cycleLength - menstruationLength - lutealLength - ovulationLength;
        if (remainingDays < 1) {
            throw new IllegalArgumentException("No hay suficientes días para la fase folicular.");
        }

        int follicularLength = remainingDays;
        LocalDate currentStart = startDate;

        // Crear fases en memoria (sin guardar aún)
        phases.add(new PhaseDto(UUID.randomUUID().toString(), idUser, "#D32F2F", PhaseCycle.MENSTRUAL, currentStart, currentStart.plusDays(menstruationLength - 1), "La fase menstrual, también conocida como período, es el sangrado vaginal normal que ocurre como parte del ciclo menstrual femenino. Es la etapa en la que el revestimiento del útero (endometrio) se desprende y se expulsa a través de la vagina si no ha habido fertilización. Este sangrado suele durar entre 3 y 7 días y marca el inicio del ciclo menstrual. \n" +
                "El ciclo menstrual es un proceso hormonal que prepara el cuerpo de la mujer para un posible embarazo. Si no hay fertilización, el revestimiento uterino no es necesario para el embarazo y se desestabiliza, causando el sangrado menstrual. "));
        currentStart = currentStart.plusDays(menstruationLength);

        phases.add(new PhaseDto(UUID.randomUUID().toString(), idUser, "#FBC02D", PhaseCycle.FOLICULAR, currentStart, currentStart.plusDays(follicularLength - 1), "Durante esta fase, se desarrollan varios folículos en los ovarios, uno de los cuales se convierte en el folículo dominante y libera un óvulo. Esta fase es crucial para la preparación del cuerpo para la concepción, ya que es cuando el endometrio se regenera y se engrosa."));
        currentStart = currentStart.plusDays(follicularLength);

        phases.add(new PhaseDto(UUID.randomUUID().toString(), idUser, "#1976D2", PhaseCycle.OVULAR, currentStart, currentStart.plusDays(ovulationLength - 1), "Este proceso, conocido como ovulación, marca el punto medio del ciclo menstrual, y es fundamental para la fertilización y el posible embarazo. La fase ovulatoria es generalmente de corta duración, durando alrededor de 16 a 32 horas, y se desencadena por un pico en la hormona luteinizante (LH). "));
        currentStart = currentStart.plusDays(ovulationLength);

        phases.add(new PhaseDto(UUID.randomUUID().toString(), idUser, "#7B1FA2", PhaseCycle.LUTEA, currentStart, currentStart.plusDays(lutealLength - 1), "En esta fase el folículo vacío que ha quedado en el ovario tras la ovulación da lugar al cuerpo lúteo, que da nombre a esta fase. La fase lútea empieza una vez que ocurre la ovulación y se extiende hasta el momento en que se produce una nueva menstruación si la mujer no se ha quedado embarazada. La llegada de la regla supondrá el inicio de un nuevo ciclo menstrual."));

        return phases;
    }

    @Override
    public List<PhaseDto> generateAndSavePhases(LocalDate startDate, int cycleLength, int menstruationLength, String idUser) {
        LocalDate endDate = startDate.plusDays(cycleLength - 1);

        // Buscar fases que se solapen con el ciclo completo
        List<PhaseVO> existingPhases = phaseRepository.findByIdUserAndStartDayLessThanEqualAndEndDayGreaterThanEqual(
                idUser, endDate, startDate);

        boolean needsUpdate = false;

        if (!existingPhases.isEmpty()) {
            // Comprobar si las fases existentes coinciden con las nuevas medidas
            // Sumar duración total existente
            int totalDaysExisting = existingPhases.stream()
                    .mapToInt(phase -> (int) (phase.getEndDay().toEpochDay() - phase.getStartDay().toEpochDay() + 1))
                    .sum();

            if (totalDaysExisting != cycleLength) {
                needsUpdate = true;
            }
            // Aquí puedes agregar comparaciones más específicas si quieres, p.ej. duración menstruación, fechas exactas, etc.
        }

        if (existingPhases.isEmpty() || needsUpdate) {
            // Si existen fases antiguas y necesitamos actualizar, borrarlas
            if (!existingPhases.isEmpty()) {
                phaseRepository.deleteAll(existingPhases);
            }

            // Generar y guardar nuevas fases
            List<PhaseDto> newPhases = generatePhases(startDate, cycleLength, menstruationLength, idUser);

            List<PhaseVO> savedPhases = phaseRepository.saveAll(
                    newPhases.stream().map(PhaseMapper::phaseDtoToPhaseVO).collect(Collectors.toList())
            );

            return savedPhases.stream().map(PhaseMapper::phaseVOToPhaseDto).collect(Collectors.toList());
        } else {
            // No es necesario actualizar, devolvemos las existentes
            return existingPhases.stream().map(PhaseMapper::phaseVOToPhaseDto).collect(Collectors.toList());
        }
    }

    @Override
    public List<PhaseDto> generatePhasesFromPrediction(String userId) {
        Optional<PredictionDto> predictionOpt = predictionService.predictNextCycle(userId);
        if (predictionOpt.isEmpty()) {
            throw new IllegalArgumentException("No se encontró predicción para el usuario " + userId);
        }

        PredictionDto prediction = predictionOpt.get();
        return generateAndSavePhases(prediction.getNextPeriodDate(), prediction.getCycleLength(), prediction.getMenstruationDuration(), userId);
    }
}
