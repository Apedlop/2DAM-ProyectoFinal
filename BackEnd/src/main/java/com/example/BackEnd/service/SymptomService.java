package com.example.BackEnd.service;

import com.example.BackEnd.model.symptom.SymptomDto;
import org.springframework.http.ResponseEntity;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

public interface SymptomService {
    // Métodos CRUD
    SymptomDto addSymptom(SymptomDto symptomDto);
    SymptomDto updateSymptom(SymptomDto symptomDto);
    ResponseEntity deleteSymptom(String id);
    Optional<SymptomDto> getSymptomById(String id);
    List<SymptomDto> getAllSymptoms();
    List<SymptomDto> getSymptomsByUserId(String userId);
    Optional<SymptomDto> getSymptomsByUserIdAndDate(String userId, LocalDate date);
    List<SymptomDto> getSymptomsBetweenDates(String userId, LocalDate from, LocalDate to);
}
