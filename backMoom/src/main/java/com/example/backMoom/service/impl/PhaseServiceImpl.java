package com.example.backMoom.service.impl;

import com.example.backMoom.model.phase.PhaseDto;
import com.example.backMoom.model.phase.PhaseVO;
import com.example.backMoom.repository.PhaseRepository;
import com.example.backMoom.service.PhaseService;
import com.example.backMoom.util.PhaseMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class PhaseServiceImpl implements PhaseService {

    @Autowired
    private PhaseRepository phaseRepository;

    @Override
    public PhaseDto addPhase(PhaseDto phaseDto) {
        PhaseVO phaseVO = PhaseMapper.phaseDtoToPhaseVO(phaseDto);
        PhaseVO createPhase = phaseRepository.save(phaseVO);
        return PhaseMapper.phaseVOToPhaseDto(createPhase);
    }

    @Override
    public PhaseDto updatePhase(PhaseDto phaseDto) {
        Optional<PhaseVO> phaseOptiona = phaseRepository.findById(phaseDto.getId());

        if (phaseOptiona.isPresent()) {
            PhaseVO phaseVO = phaseOptiona.get();
            phaseVO.setColor(phaseVO.getColor());
            phaseVO.setPhaseCycle(phaseDto.getPhaseCycle());
            phaseVO.setStartDay(phaseDto.getStartDay());
            phaseVO.setEndDay(phaseDto.getEndDay());
            phaseVO.setDescription(phaseDto.getDescription());
            return PhaseMapper.phaseVOToPhaseDto(phaseVO);
        } else {
            return null;
        }
    }

    @Override
    public ResponseEntity deletePhase(String id) {
        System.out.println("ID recibido: " + id);

        try {
            Optional<PhaseVO> phaseOptional = phaseRepository.findById(id);
            if (phaseOptional.isPresent()) {
                phaseRepository.deleteById(id);
                return ResponseEntity.ok("Fase eliminada exitosamente");
            } else {
                return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Fase no encontrada con el ID: " + id);
            }
        } catch (Exception e) {
            e.printStackTrace();
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Error al eliminar la fase");
        }
    }

    @Override
    public Optional<PhaseDto> getPhaseById(String id) {
        Optional<PhaseVO> phaseOptional = phaseRepository.findById(id);
        return phaseOptional.map(PhaseMapper::phaseVOToPhaseDto);
    }

    @Override
    public List<PhaseDto> getAllPhase() {
        List<PhaseVO> phaseVO = phaseRepository.findAll();
        return phaseVO.stream().map(PhaseMapper::phaseVOToPhaseDto).collect(Collectors.toList());
    }

    @Override
    public List<PhaseDto> getPhaseByDateRange(LocalDate start, LocalDate end) {
        List<PhaseVO> phaseVO = phaseRepository.findByStartDayBetween(start, end);
        return phaseVO.stream().map(PhaseMapper::phaseVOToPhaseDto).collect(Collectors.toList());
    }

    @Override
    public List<PhaseDto> getPhaseByCycle(String phaseCycle) {
        List<PhaseVO> phaseVO = phaseRepository.findByPhaseCycle(phaseCycle);
        return phaseVO.stream().map(PhaseMapper::phaseVOToPhaseDto).collect(Collectors.toList());
    }
}
