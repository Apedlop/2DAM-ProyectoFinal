package com.example.backMoom.controller;

import com.example.backMoom.model.prediction.PredictionDto;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

public interface PredictionAPI {
    // Métodos CRUD
    PredictionDto addPrediction(PredictionDto predictionDto);
    PredictionDto updatePrediction(PredictionDto predictionDto, String id);
    ResponseEntity deletePrediction(String id);
    Optional<PredictionDto> getPredictionById(String id);
    List<PredictionDto> getAllPrediction();
    Optional<PredictionDto> getPredictionByDate(LocalDate date);
    List<PredictionDto> getPredictionBetweenDate(LocalDate from, LocalDate to);
    ResponseEntity<PredictionDto> calculateNextCyclePrediction(@PathVariable String userId);
}
