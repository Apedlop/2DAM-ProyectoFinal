package com.example.BackEnd.service.impl;

import com.example.BackEnd.model.cycle.CycleVO;
import com.example.BackEnd.model.prediction.PredictionDto;
import com.example.BackEnd.model.prediction.PredictionVO;
import com.example.BackEnd.repository.CycleRepository;
import com.example.BackEnd.repository.PredictionRepository;
import com.example.BackEnd.service.PredictionService;
import com.example.BackEnd.util.PredictionMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.time.temporal.ChronoUnit;
import java.util.Comparator;
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
        PredictionVO savedPrediction = predictionRepository.save(predictionVO);
        return PredictionMapper.predictionVOToPredictionDto(savedPrediction);
    }

    @Override
    public PredictionDto updatePrediction(PredictionDto predictionDto) {
        return predictionRepository.findById(predictionDto.getId())
                .map(existing -> {
                    existing.setNextPeriodDate(predictionDto.getNextPeriodDate());
                    existing.setNextOvulationDate(predictionDto.getNextOvulationDate());
                    existing.setDaysUntilPeriod(predictionDto.getDaysUntilPeriod());
                    return PredictionMapper.predictionVOToPredictionDto(predictionRepository.save(existing));
                })
                .orElse(null);
    }

    @Override
    public ResponseEntity<?> deletePrediction(String id) {
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
        return predictionRepository.findById(id).map(PredictionMapper::predictionVOToPredictionDto);
    }

    @Override
    public List<PredictionDto> getAllPrediction() {
        return predictionRepository.findAll()
                .stream()
                .map(PredictionMapper::predictionVOToPredictionDto)
                .collect(Collectors.toList());
    }

    @Override
    public Optional<PredictionDto> getPredictionByDate(LocalDate date) {
        return predictionRepository.findByNextPeriodDate(date).map(PredictionMapper::predictionVOToPredictionDto);
    }

    @Override
    public List<PredictionDto> getPredictionBetweenDate(LocalDate from, LocalDate to) {
        return predictionRepository.findByNextPeriodDateBetween(from, to)
                .stream()
                .map(PredictionMapper::predictionVOToPredictionDto)
                .collect(Collectors.toList());
    }

    @Override
    public Optional<PredictionDto> predictNextCycle(String userId) {
        return cycleRepository.findByUserId(userId).stream()
                .max(Comparator.comparing(CycleVO::getStartDate))
                .map(lastCycle -> {
                    int defaultLength = 28;
                    int cycleLength = Optional.ofNullable(lastCycle.getCycleLength()).orElse(defaultLength);
                    int averageLength = calculateAverageCycleLength(userId).orElse(cycleLength);
                    int menstruationDuration = calculateAverageMenstruationDuration(userId).orElse(5);

                    LocalDate nextStartDate = lastCycle.getStartDate().plusDays(averageLength);
                    while (nextStartDate.isBefore(LocalDate.now())) {
                        nextStartDate = nextStartDate.plusDays(averageLength);
                    }

                    LocalDate nextOvulationDate = nextStartDate.minusDays(14);
                    int daysUntilPeriod = (int) ChronoUnit.DAYS.between(LocalDate.now(), nextStartDate);

                    return PredictionDto.builder()
                            .idUser(userId)
                            .nextPeriodDate(nextStartDate)
                            .nextOvulationDate(nextOvulationDate)
                            .cycleLength(averageLength)
                            .menstruationDuration(menstruationDuration)
                            .daysUntilPeriod(daysUntilPeriod)
                            .build();
                });
    }

    private Optional<Integer> calculateAverageCycleLength(String userId) {
        List<Integer> lengths = cycleRepository.findByUserId(userId).stream()
                .map(CycleVO::getCycleLength)
                .filter(Objects::nonNull)
                .filter(length -> length > 0)
                .collect(Collectors.toList());

        if (lengths.isEmpty()) return Optional.empty();

        double avg = lengths.stream().mapToInt(Integer::intValue).average().orElse(28);
        return Optional.of((int) Math.round(avg));
    }

    private Optional<Integer> calculateAverageMenstruationDuration(String userId) {
        List<Integer> durations = cycleRepository.findByUserId(userId).stream()
                .map(CycleVO::getMenstruationDuration)
                .filter(Objects::nonNull)
                .filter(duration -> duration > 0)
                .collect(Collectors.toList());

        if (durations.isEmpty()) return Optional.empty();

        double avg = durations.stream().mapToInt(Integer::intValue).average().orElse(5);
        return Optional.of((int) Math.round(avg));
    }

}
