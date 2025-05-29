package com.example.BackEnd.service;

import com.example.BackEnd.model.phase.PhaseDto;
import org.springframework.http.ResponseEntity;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

public interface PhaseService {
    // Métodos CRUD
    PhaseDto addPhase(PhaseDto phaseDto);
    PhaseDto updatePhase(PhaseDto phaseDto);
    ResponseEntity deletePhase(String id);
    ResponseEntity deleteAllPhase();
    Optional<PhaseDto> getPhaseById(String id);
    List<PhaseDto> getAllPhase();
    List<PhaseDto> getPhaseByDateRange(LocalDate start, LocalDate end);
    List<PhaseDto> getPhaseByCycle(String phaseCycle);
    List<PhaseDto> generatePhases(LocalDate startDate, int cycleLength, int menstruationLength, String idUser);
    List<PhaseDto> generateAndSavePhases(LocalDate startDate, int cycleLength, int menstruationLength, String idUser);
    List<PhaseDto> generatePhasesFromPrediction(String userId);
}
