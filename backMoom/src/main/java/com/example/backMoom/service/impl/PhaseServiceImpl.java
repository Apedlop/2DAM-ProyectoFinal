package com.example.backMoom.service.impl;

import com.example.backMoom.model.phase.PhaseDto;
import com.example.backMoom.model.phase.PhaseVO;
import com.example.backMoom.model.enums.PhaseCycle;
import com.example.backMoom.model.prediction.PredictionDto;
import com.example.backMoom.repository.PhaseRepository;
import com.example.backMoom.service.PhaseService;
import com.example.backMoom.service.PredictionService;
import com.example.backMoom.util.PhaseMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.*;
import java.util.stream.Collectors;

@Service
public class PhaseServiceImpl implements PhaseService {

    @Autowired
    private PhaseRepository phaseRepository;

    @Autowired
    private PredictionService predictionService;

    @Override
    public PhaseDto addPhase(PhaseDto phaseDto) {
        PhaseVO phaseVO = PhaseMapper.phaseDtoToPhaseVO(phaseDto);
        PhaseVO createdPhase = phaseRepository.save(phaseVO);
        return PhaseMapper.phaseVOToPhaseDto(createdPhase);
    }

    @Override
    public PhaseDto updatePhase(PhaseDto phaseDto) {
        Optional<PhaseVO> phaseOptional = phaseRepository.findById(phaseDto.getId());
        if (phaseOptional.isPresent()) {
            PhaseVO phaseVO = phaseOptional.get();
            phaseVO.setColor(phaseDto.getColor());
            phaseVO.setPhaseCycle(phaseDto.getPhaseCycle());
            phaseVO.setStartDay(phaseDto.getStartDay());
            phaseVO.setEndDay(phaseDto.getEndDay());
            phaseVO.setDescription(phaseDto.getDescription());
            PhaseVO updatedPhase = phaseRepository.save(phaseVO);
            return PhaseMapper.phaseVOToPhaseDto(updatedPhase);
        } else {
            return null;
        }
    }

    @Override
    public ResponseEntity<?> deletePhase(String id) {
        try {
            Optional<PhaseVO> phaseOptional = phaseRepository.findById(id);
            if (phaseOptional.isPresent()) {
                phaseRepository.deleteById(id);
                return ResponseEntity.ok("Fase eliminada exitosamente");
            } else {
                return ResponseEntity.notFound().build();
            }
        } catch (Exception e) {
            e.printStackTrace();
            return ResponseEntity.status(500).body("Error al eliminar la fase");
        }
    }

    @Override
    public Optional<PhaseDto> getPhaseById(String id) {
        Optional<PhaseVO> phaseOptional = phaseRepository.findById(id);
        return phaseOptional.map(PhaseMapper::phaseVOToPhaseDto);
    }

    @Override
    public List<PhaseDto> getAllPhase() {
        List<PhaseVO> phaseVOList = phaseRepository.findAll();
        return phaseVOList.stream().map(PhaseMapper::phaseVOToPhaseDto).collect(Collectors.toList());
    }

    @Override
    public List<PhaseDto> getPhaseByDateRange(LocalDate start, LocalDate end) {
        List<PhaseVO> phaseVOList = phaseRepository.findByStartDayBetween(start, end);
        return phaseVOList.stream().map(PhaseMapper::phaseVOToPhaseDto).collect(Collectors.toList());
    }

    @Override
    public List<PhaseDto> getPhaseByCycle(String phaseCycle) {
        List<PhaseVO> phaseVOList = phaseRepository.findByPhaseCycle(phaseCycle);
        return phaseVOList.stream().map(PhaseMapper::phaseVOToPhaseDto).collect(Collectors.toList());
    }

    @Override
    public List<PhaseDto> generatePhases(LocalDate startDate, int cycleLength, int menstruationLength, String idUser) {
        if (cycleLength < menstruationLength + 14 + 1) {
            throw new IllegalArgumentException("La duración del ciclo es demasiado corta para fases estándar.");
        }

        List<PhaseDto> phases = new ArrayList<>();
        int lutealLength = 14;
        int ovulationLength = 1;

        int remainingDays = cycleLength - menstruationLength - lutealLength - ovulationLength;
        if (remainingDays < 1) {
            throw new IllegalArgumentException("No hay suficientes días para la fase folicular.");
        }

        int follicularLength = remainingDays;
        LocalDate currentStart = startDate;

        // Menstrual
        phases.add(new PhaseDto(UUID.randomUUID().toString(), idUser, "#e06666", PhaseCycle.MENSTRUAL, currentStart, currentStart.plusDays(menstruationLength - 1), "Sangrado y liberación del revestimiento uterino"));
        currentStart = currentStart.plusDays(menstruationLength);

        // Folicular
        phases.add(new PhaseDto(UUID.randomUUID().toString(), idUser, "#f6b26b", PhaseCycle.FOLICULAR, currentStart, currentStart.plusDays(follicularLength - 1), "Crecimiento de folículos en los ovarios"));
        currentStart = currentStart.plusDays(follicularLength);

        // Ovulación
        phases.add(new PhaseDto(UUID.randomUUID().toString(), idUser, "#93c47d", PhaseCycle.OVULAR, currentStart, currentStart.plusDays(ovulationLength - 1), "Liberación del óvulo maduro"));
        currentStart = currentStart.plusDays(ovulationLength);

        // Lútea
        phases.add(new PhaseDto(UUID.randomUUID().toString(), idUser, "#6fa8dc", PhaseCycle.LUTEA, currentStart, currentStart.plusDays(lutealLength - 1), "Preparación del útero para un posible embarazo"));

        return phases;
    }

    @Override
    public List<PhaseDto> generateAndSavePhases(LocalDate startDate, int cycleLength, int menstruationLength, String idUser) {
        List<PhaseDto> phases = generatePhases(startDate, cycleLength, menstruationLength, idUser);
        PhaseDto lutealPhase = phases.get(phases.size() - 1);

        LocalDate today = LocalDate.now();
        if (!today.isBefore(lutealPhase.getEndDay())) {
            lutealPhase.setEndDay(today);
        }

        PhaseVO lutealPhaseVO = PhaseMapper.phaseDtoToPhaseVO(lutealPhase);
        PhaseVO savedLutealPhase = phaseRepository.save(lutealPhaseVO);

        return phases.stream().map(p -> {
            if (p.getId().equals(savedLutealPhase.getId())) {
                return PhaseMapper.phaseVOToPhaseDto(savedLutealPhase);
            }
            return p;
        }).collect(Collectors.toList());
    }

    @Override
    public List<PhaseDto> generatePhasesFromPrediction(String userId) {
        Optional<PredictionDto> predictionOpt = predictionService.predictNextCycle(userId);

        if (predictionOpt.isEmpty()) {
            throw new IllegalArgumentException("No se encontró predicción para el usuario " + userId);
        }

        PredictionDto prediction = predictionOpt.get();
        return generateAndSavePhases(prediction.getNextPeriodDate(), prediction.getCycleLength(), prediction.getMenstruationDuration(), userId);
    }
}
