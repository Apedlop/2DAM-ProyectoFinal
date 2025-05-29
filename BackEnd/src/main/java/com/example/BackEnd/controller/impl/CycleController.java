package com.example.BackEnd.controller.impl;

import com.example.BackEnd.controller.CycleAPI;
import com.example.BackEnd.model.cycle.CycleDto;
import com.example.BackEnd.service.CycleService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/api/cycles")
@CrossOrigin
public class CycleController implements CycleAPI {

    @Autowired
    private CycleService cycleService;

    @Override
    @PostMapping
    public CycleDto addCycle(@RequestBody CycleDto cycleDto) {
        return cycleService.addCycle(cycleDto);
    }

    @Override
    @PutMapping("/{id}")
    public CycleDto updateCycle(@RequestBody CycleDto cycleDto, @PathVariable String id) {
        cycleDto.setId(id);
        return cycleService.updateCycle(cycleDto);
    }

    @Override
    @DeleteMapping("/{id}")
    public ResponseEntity<?> deleteCycle(@PathVariable String id) {
        return cycleService.deleteCycle(id);
    }

    @Override
    @GetMapping("/{id}")
    public Optional<CycleDto> getCycleById(@PathVariable String id) {
        return cycleService.getCycleById(id);
    }

    @Override
    @GetMapping("/user/{userId}")
    public List<CycleDto> getCyclesByUserId(@PathVariable String userId) {
        return cycleService.getCyclesByUserId(userId);
    }

    @Override
    @GetMapping("/all")
    public List<CycleDto> getAllCycles() {
        return cycleService.getAllCycles();
    }

    @Override
    @GetMapping("/user/{userId}/last")
    public Optional<CycleDto> getLastCycleByUserId(@PathVariable String userId) {
        return cycleService.getLastCycleByUserId(userId);
    }

    @Override
    @GetMapping("/user/{userId}/next-period")
    public Optional<LocalDate> calculateNextPeriodDate(@PathVariable String userId) {
        return cycleService.calculateNextPeriodDate(userId);
    }

    @Override
    @GetMapping("/user/{userId}/average-length")
    public Optional<Integer> calculateAverageCycleLength(@PathVariable String userId) {
        return cycleService.calculateAverageCycleLength(userId);
    }

    @Override
    @GetMapping("/user/{userId}/exists-on-date")
    public boolean existsCycleOnDate(@PathVariable String userId,
                                     @RequestParam("date") LocalDate date) {
        return cycleService.existsCycleOnDate(userId, date);
    }

    @Override
    @GetMapping("/user/{userId}/between")
    public List<CycleDto> getCyclesBetweenDates(@PathVariable String userId,
                                                @RequestParam("from") LocalDate from,
                                                @RequestParam("to") LocalDate to) {
        return cycleService.getCyclesBetweenDates(userId, from, to);
    }

    @GetMapping("/{id}/menstruationDuration")
    public ResponseEntity<Integer> getMenstruationDuration(@PathVariable String id) {
        Optional<CycleDto> cycleOpt = cycleService.getCycleById(id);
        if (cycleOpt.isEmpty()) {
            return ResponseEntity.notFound().build();
        }

        CycleDto cycle = cycleOpt.get();
        int duracion = cycleService.calculatePeriodLength(
                cycle.getUserId(),
                cycle.getId()
        );

        return ResponseEntity.ok(duracion);
    }

}
