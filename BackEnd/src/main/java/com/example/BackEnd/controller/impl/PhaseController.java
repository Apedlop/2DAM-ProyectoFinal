package com.example.BackEnd.controller.impl;

import com.example.BackEnd.controller.PhaseAPI;
import com.example.BackEnd.model.phase.PhaseDto;
import com.example.BackEnd.service.PhaseService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/api/phases")
@CrossOrigin
public class PhaseController implements PhaseAPI {

    @Autowired
    private PhaseService phaseService;

    @Override
    @PostMapping
    public PhaseDto addPhase(@RequestBody PhaseDto phaseDto) {
        return phaseService.addPhase(phaseDto);
    }

    @Override
    @PutMapping("/{id}")
    public PhaseDto updatePhase(@RequestBody PhaseDto phaseDto, @PathVariable String id) {
        return phaseService.updatePhase(phaseDto);
    }

    @Override
    @DeleteMapping("/{id}")
    public ResponseEntity<?> deletePhase(@PathVariable String id) {
        return phaseService.deletePhase(id);
    }

    @Override
    @DeleteMapping("/")
    public ResponseEntity<?> deleteAllPhases() {
        return phaseService.deleteAllPhase();
    }

    @Override
    @GetMapping("/{id}")
    public Optional<PhaseDto> getPhaseById(@PathVariable String id) {
        return phaseService.getPhaseById(id);
    }

    @Override
    @GetMapping("/all")
    public List<PhaseDto> getAllPhase() {
        return phaseService.getAllPhase();
    }

    @Override
    @GetMapping("/range")
    public List<PhaseDto> getPhaseByDateRange(@RequestParam LocalDate start, @RequestParam LocalDate end) {
        return phaseService.getPhaseByDateRange(start, end);
    }

    @Override
    @GetMapping("/cycle")
    public List<PhaseDto> getPhaseByCycle(@RequestParam String phaseCycle) {
        return phaseService.getPhaseByCycle(phaseCycle);
    }

    @GetMapping("/")
    public List<PhaseDto> getPhases(@RequestParam String idUser, @RequestParam LocalDate startDate, @RequestParam int cycleLength, @RequestParam int menstruationLength) {
        return phaseService.generatePhases(startDate, cycleLength, menstruationLength, idUser);
    }

    @GetMapping("/generatePhases/")
    public ResponseEntity<List<PhaseDto>> generateAndSavePhasesForUser(@RequestParam String idUser, @RequestParam LocalDate startDate, @RequestParam int cycleLength, @RequestParam int menstruationLength
    ) {
        List<PhaseDto> phases = phaseService.generateAndSavePhases(startDate, cycleLength, menstruationLength, idUser);
        return ResponseEntity.ok(phases);
    }
}
