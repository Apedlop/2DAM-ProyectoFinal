package com.example.BackEnd.controller.impl;

import com.example.BackEnd.controller.SymptomAPI;
import com.example.BackEnd.model.symptom.SymptomDto;
import com.example.BackEnd.service.SymptomService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/api/symptoms")
@CrossOrigin
public class SymptomController implements SymptomAPI {

    @Autowired
    private SymptomService symptomService;

    @Override
    @PostMapping
    public SymptomDto addSymptom(@RequestBody SymptomDto symptomDto) {
        return symptomService.addSymptom(symptomDto);
    }

    @Override
    @PutMapping("/{id}")
    public SymptomDto updateSymptom(@RequestBody SymptomDto symptomDto, @PathVariable String id) {
        return symptomService.updateSymptom(symptomDto);
    }

    @Override
    @DeleteMapping("/{id}")
    public ResponseEntity<?> deleteSymptom(@PathVariable String id) {
        return symptomService.deleteSymptom(id);
    }

    @Override
    @GetMapping("/{id}")
    public Optional<SymptomDto> getSymptomById(@PathVariable String id) {
        return symptomService.getSymptomById(id);
    }

    @Override
    @GetMapping("/all")
    public List<SymptomDto> getAllSymptoms() {
        return symptomService.getAllSymptoms();
    }

    @Override
    @GetMapping("/user/{userId}")
    public List<SymptomDto> getSymptomsByUserId(@PathVariable String userId) {
        return symptomService.getSymptomsByUserId(userId);
    }

    @Override
    @GetMapping("/user/{userId}/date")
    public Optional<SymptomDto> getSymptomsByUserIdAndDate(@PathVariable String userId, @RequestParam LocalDate date) {
        return symptomService.getSymptomsByUserIdAndDate(userId, date);
    }

    @Override
    @GetMapping("/user/{userId}/range")
    public List<SymptomDto> getSymptomsBetweenDates(
            @PathVariable String userId,
            @RequestParam LocalDate from,
            @RequestParam LocalDate to
    ) {
        return symptomService.getSymptomsBetweenDates(userId, from, to);
    }
}
