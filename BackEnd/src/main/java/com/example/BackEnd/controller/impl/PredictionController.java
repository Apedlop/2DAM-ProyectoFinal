package com.example.BackEnd.controller.impl;

import com.example.BackEnd.controller.PredictionAPI;
import com.example.BackEnd.model.prediction.PredictionDto;
import com.example.BackEnd.service.PredictionService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/api/predictions")
@CrossOrigin
public class PredictionController implements PredictionAPI {

    @Autowired
    private PredictionService predictionService;

    @Override
    @PostMapping
    public PredictionDto addPrediction(@RequestBody PredictionDto predictionDto) {
        return predictionService.addPrediction(predictionDto);
    }

    @Override
    @PutMapping("/{id}")
    public PredictionDto updatePrediction(@RequestBody PredictionDto predictionDto, @PathVariable String id) {
        return predictionService.updatePrediction(predictionDto);
    }

    @Override
    @DeleteMapping("/{id}")
    public ResponseEntity<?> deletePrediction(@PathVariable String id) {
        return predictionService.deletePrediction(id);
    }

    @Override
    @GetMapping("/{id}")
    public Optional<PredictionDto> getPredictionById(@PathVariable String id) {
        return predictionService.getPredictionById(id);
    }

    @Override
    @GetMapping("/all")
    public List<PredictionDto> getAllPrediction() {
        return predictionService.getAllPrediction();
    }

    @Override
    @GetMapping("/date")
    public Optional<PredictionDto> getPredictionByDate(@RequestParam LocalDate date) {
        return predictionService.getPredictionByDate(date);
    }

    @Override
    @GetMapping("/range")
    public List<PredictionDto> getPredictionBetweenDate(@RequestParam LocalDate from, @RequestParam LocalDate to) {
        return predictionService.getPredictionBetweenDate(from, to);
    }

    @Override
    @GetMapping("/predict/{userId}")
    public ResponseEntity<PredictionDto> predictNextCycle(@PathVariable String userId) {
        Optional<PredictionDto> prediction = predictionService.predictNextCycle(userId);
        return prediction.map(ResponseEntity::ok)
                .orElseGet(() -> ResponseEntity.notFound().build());
    }
}
