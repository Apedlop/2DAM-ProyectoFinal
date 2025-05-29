package com.example.BackEnd.controller;

import com.example.BackEnd.model.prediction.PredictionDto;
import org.springframework.http.ResponseEntity;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

public interface PredictionAPI {
    // Métodos CRUD
    PredictionDto addPrediction(PredictionDto predictionDto);
    PredictionDto updatePrediction(PredictionDto predictionDto, String id);
    ResponseEntity<?> deletePrediction(String id);
    Optional<PredictionDto> getPredictionById(String id);
    List<PredictionDto> getAllPrediction();
    Optional<PredictionDto> getPredictionByDate(LocalDate date);
    List<PredictionDto> getPredictionBetweenDate(LocalDate from, LocalDate to);

    // Método para predecir el próximo ciclo
    ResponseEntity<PredictionDto> predictNextCycle(String userId);
}
