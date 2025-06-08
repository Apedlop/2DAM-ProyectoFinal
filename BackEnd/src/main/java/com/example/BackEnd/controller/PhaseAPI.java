package com.example.BackEnd.controller;

import com.example.BackEnd.model.phase.PhaseDto;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestParam;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

public interface PhaseAPI {
    // Métodos CRUD
    PhaseDto addPhase(PhaseDto phaseDto);
    PhaseDto updatePhase(PhaseDto phaseDto, String id);
    ResponseEntity deletePhase(String id);
    ResponseEntity deleteAllPhases();
    Optional<PhaseDto> getPhaseById(String id);
    List<PhaseDto> getAllPhase();
    List<PhaseDto> getPhaseByDateRange(LocalDate start, LocalDate end);
    List<PhaseDto> getPhaseByCycle(String phaseCycle);
    List<PhaseDto> getPhases(String idUser, LocalDate startDate, int cycleLength, int menstruationLength);
}
