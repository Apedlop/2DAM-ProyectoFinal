package com.example.backMoom.service;

import com.example.backMoom.model.cycle.CycleDto;
import com.example.backMoom.model.cycle.CycleVO;
import org.springframework.http.ResponseEntity;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

public interface CycleService {
    // Funciones CRUD
    CycleDto addCycle(CycleDto cycleDto);
    CycleDto updateCycle(CycleDto cycleDto);
    ResponseEntity deleteCycle(String id);
    Optional<CycleDto> getCycleById(String id);
    List<CycleDto> getCyclesByUserId(String userId);
    List<CycleDto> getAllCycles();

    // Métodos para la logica del proyecto
    Optional<CycleDto> getLastCycleByUserId(String userId);
    Optional<LocalDate> calculateNextPeriodDate(String userId);
    Optional<Integer> calculateAverageCycleLength(String userId);
    boolean existsCycleOnDate(String userId, LocalDate date); // Para que no hayan ciclos duplicados
    List<CycleDto> getCyclesBetweenDates(String UserId, LocalDate from, LocalDate to); // Obtener ciclos dentro de un rango
    int calculatePeriodLength(String userId, String cycleId);
}
