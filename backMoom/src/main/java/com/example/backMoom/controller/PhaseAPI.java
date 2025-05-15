package com.example.backMoom.controller;

import com.example.backMoom.model.phase.PhaseDto;
import org.springframework.http.ResponseEntity;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

public interface PhaseAPI {
    // Métodos CRUD
    PhaseDto addPhase(PhaseDto phaseDto);
    PhaseDto updatePhase(PhaseDto phaseDto, String id);
    ResponseEntity deletePhase(String id);
    Optional<PhaseDto> getPhaseById(String id);
    List<PhaseDto> getAllPhase();
    List<PhaseDto> getPhaseByDateRange(LocalDate start, LocalDate end);
    List<PhaseDto> getPhaseByCycle(String phaseCycle);
}
