package com.example.BackEnd.service;

import com.example.BackEnd.model.prediction.PredictionDto;
import org.springframework.http.ResponseEntity;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

public interface PredictionService {
    PredictionDto addPrediction(PredictionDto predictionDto);
    PredictionDto updatePrediction(PredictionDto predictionDto);
    ResponseEntity<?> deletePrediction(String id);
    Optional<PredictionDto> getPredictionById(String id);
    List<PredictionDto> getAllPrediction();
    Optional<PredictionDto> getPredictionByDate(LocalDate date);
    List<PredictionDto> getPredictionBetweenDate(LocalDate from, LocalDate to);
    Optional<PredictionDto> predictNextCycle(String userId);
}
