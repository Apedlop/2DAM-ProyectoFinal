package com.example.BackEnd.controller;

import com.example.BackEnd.model.symptom.SymptomDto;
import org.springframework.http.ResponseEntity;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

public interface SymptomAPI {
    // Métodos CRUD
    SymptomDto addSymptom(SymptomDto symptomDto);
    SymptomDto updateSymptom(SymptomDto symptomDto, String id);
    ResponseEntity deleteSymptom(String id);
    Optional<SymptomDto> getSymptomById(String id);
    List<SymptomDto> getAllSymptoms();
    List<SymptomDto> getSymptomsByUserId(String userId);
    Optional<SymptomDto> getSymptomsByUserIdAndDate(String userId, LocalDate date);
    List<SymptomDto> getSymptomsBetweenDates(String userId, LocalDate from, LocalDate to);
}
