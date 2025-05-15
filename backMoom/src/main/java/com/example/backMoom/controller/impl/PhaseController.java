package com.example.backMoom.controller.impl;

import com.example.backMoom.controller.PhaseAPI;
import com.example.backMoom.model.phase.PhaseDto;
import com.example.backMoom.service.PhaseService;
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
}
