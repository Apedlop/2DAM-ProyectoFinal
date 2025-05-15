package com.example.backMoom.service.impl;

import com.example.backMoom.model.cycle.CycleVO;
import com.example.backMoom.model.prediction.PredictionDto;
import com.example.backMoom.model.prediction.PredictionVO;
import com.example.backMoom.repository.CycleRepository;
import com.example.backMoom.repository.PredictionRepository;
import com.example.backMoom.service.PredictionService;
import com.example.backMoom.util.PredictionMapper;
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
public class PredictionServiceImpl implements PredictionService {

    @Autowired
    private PredictionRepository predictionRepository;
    @Autowired
    private CycleRepository cycleRepository;


    @Override
    public PredictionDto addPrediction(PredictionDto predictionDto) {
        PredictionVO predictionVO = PredictionMapper.predictionDtoToPredictionVO(predictionDto);
        PredictionVO createPrediction = predictionRepository.save(predictionVO);
        return PredictionMapper.predictionVOToPredictionDto(createPrediction);
    }

    @Override
    public PredictionDto updatePrediction(PredictionDto predictionDto) {
        Optional<PredictionVO> predictionOptional = predictionRepository.findById(predictionDto.getId());

        if (predictionOptional.isPresent()) {
            PredictionVO predictionVO = predictionOptional.get();
            predictionVO.setNextPeriodDate(predictionDto.getNextPeriodDate());
            predictionVO.setNextOvulationDate(predictionDto.getNextOvulationDate());
            predictionVO.setDaysUntilPeriod(predictionDto.getDaysUntilPeriod());
            PredictionVO updatePrediction = predictionRepository.save(predictionVO);
            return PredictionMapper.predictionVOToPredictionDto(updatePrediction);
        } else {
            return null;
        }
    }

    @Override
    public ResponseEntity deletePrediction(String id) {
        System.out.println("ID recibido: " + id);

        try {
            Optional<PredictionVO> predictionOptional = predictionRepository.findById(id);
            if (predictionOptional.isPresent()) {
                predictionRepository.deleteById(id);
                return ResponseEntity.ok("Predicción eliminada exitosamente");
            } else {
                return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Predicción no encontrada con el ID: " + id);
            }
        } catch (Exception e) {
            e.printStackTrace();
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Error al eliminar la predicción");
        }
    }

    @Override
    public Optional<PredictionDto> getPredictionById(String id) {
        Optional<PredictionVO> predictionOptional = predictionRepository.findById(id);
        return predictionOptional.map(PredictionMapper::predictionVOToPredictionDto);
    }

    @Override
    public List<PredictionDto> getAllPrediction() {
        List<PredictionVO> predictionVO = predictionRepository.findAll();
        return PredictionMapper.predictionVOListToPredictionDto(predictionVO);
    }

    @Override
    public Optional<PredictionDto> getPredictionByDate(LocalDate date) {
        Optional<PredictionVO> predictionOptional = predictionRepository.findByNextPeriodDate(date);
        return predictionOptional.map(PredictionMapper::predictionVOToPredictionDto);
    }

    @Override
    public List<PredictionDto> getPredictionBetweenDate(LocalDate from, LocalDate to) {
        List<PredictionVO> predictionVO = predictionRepository.findByNextPeriodDateBetween(from, to);
        return PredictionMapper.predictionVOListToPredictionDto(predictionVO);
    }

    @Override
    public Optional<PredictionDto> calculateNextCyclePrediction(String userId) {
        return cycleRepository.findLastCycleByUserId(userId)
                .flatMap(lastCycle -> {
                    Integer cycleLength = lastCycle.getCycleLength();
                    int defaultLength = (cycleLength != null) ? cycleLength : 28;

                    // Calcular la longitud promedio del ciclo si es posible, si no se usa el valor predeterminado
                    int length = calculateAverageCycleLength(userId).orElse(defaultLength);

                    // Calcular la fecha de inicio del próximo ciclo
                    LocalDate nextStartDate = lastCycle.getStartDate().plusDays(length);

                    // Ajustar la fecha del próximo ciclo si está en el pasado
                    while (nextStartDate.isBefore(LocalDate.now())) {
                        nextStartDate = nextStartDate.plusDays(length);
                    }

                    // Calcular la fecha de ovulación (aproximadamente 14 días antes del próximo ciclo)
                    LocalDate nextOvulationDate = nextStartDate.minusDays(14);

                    // Calcular la fase actual del ciclo
                    String predictedPhase = getPhaseForDate(nextStartDate, nextOvulationDate);

                    // Devolver el DTO con los datos de la predicción
                    PredictionDto prediction = new PredictionDto();
                    prediction.setIdUser(userId);
                    prediction.setNextPeriodDate(nextStartDate);
                    prediction.setNextOvulationDate(nextOvulationDate);
                    prediction.setDaysUntilPeriod((int) LocalDate.now().until(nextStartDate).getDays()); // Días hasta el próximo período

                    return Optional.of(prediction);
                });
    }

    private String getPhaseForDate(LocalDate nextStartDate, LocalDate nextOvulationDate) {
        LocalDate today = LocalDate.now();

        // Fase Menstrual: Si estamos en el período actual
        if (!today.isBefore(nextStartDate)) {
            return "MENSTRUAL";
        }

        // Fase Ovulatoria: Si estamos cerca de la ovulación (aproximadamente 14 días antes del período)
        if (!today.isBefore(nextOvulationDate)) {
            return "OVULATORIA";
        }

        // Fase Lútea: Después de la ovulación y antes del próximo período
        return "LUTEA";
    }

    private Optional<Integer> calculateAverageCycleLength(String userId) {
        return cycleRepository.findByUserId(userId).stream()
                .map(CycleVO::getCycleLength)
                .filter(Objects::nonNull)
                .filter(length -> length > 0)
                .collect(Collectors.collectingAndThen(
                        Collectors.averagingInt(Integer::intValue),
                        avg -> Optional.of((int) Math.round(avg))
                ));
    }
}
