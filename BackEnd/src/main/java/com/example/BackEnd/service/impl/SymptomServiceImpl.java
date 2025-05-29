package com.example.BackEnd.service.impl;

import com.example.BackEnd.model.symptom.SymptomDto;
import com.example.BackEnd.model.symptom.SymptomVO;
import com.example.BackEnd.repository.SymptomRepository;
import com.example.BackEnd.service.SymptomService;
import com.example.BackEnd.util.SymptomMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

@Service
public class SymptomServiceImpl implements SymptomService {

    @Autowired
    private SymptomRepository symptomRepository;

    @Override
    public SymptomDto addSymptom(SymptomDto symptomDto) {
        Optional<SymptomVO> existing = symptomRepository.findByUserIdAndDate(
                symptomDto.getUserId(), symptomDto.getDate()
        );

        if (existing.isPresent()) {
            throw new IllegalArgumentException("Ya existe un registro para esa fecha y usuario.");
        }

        SymptomVO symptomVO = SymptomMapper.symptomDtoToSymptomVO(symptomDto);
        SymptomVO createdSymptom = symptomRepository.save(symptomVO);
        return SymptomMapper.symptomVOToSymptomDto(createdSymptom);
    }

    @Override
    public SymptomDto updateSymptom(SymptomDto symptomDto) {
        return symptomRepository.findById(symptomDto.getId())
                .map(symptomVO -> {
                    symptomVO.setCycleId(symptomDto.getCycleId());
                    symptomVO.setDate(symptomDto.getDate());
                    symptomVO.setTypePeriod(symptomDto.getTypePeriod());
                    symptomVO.setTypePain(symptomDto.getTypePain());
                    symptomVO.setTypeEmotion(symptomDto.getTypeEmotion());
                    symptomVO.setNotes(symptomDto.getNotes());
                    SymptomVO updatedSymptom = symptomRepository.save(symptomVO);
                    return SymptomMapper.symptomVOToSymptomDto(updatedSymptom);
                })
                .orElse(null);
    }

    @Override
    public ResponseEntity<String> deleteSymptom(String id) {
        try {
            if (symptomRepository.existsById(id)) {
                symptomRepository.deleteById(id);
                return ResponseEntity.ok("Síntomas eliminados exitosamente");
            } else {
                return ResponseEntity.status(HttpStatus.NOT_FOUND)
                        .body("Síntomas no encontrados con el ID: " + id);
            }
        } catch (Exception e) {
            e.printStackTrace();
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body("Error al eliminar los síntomas");
        }
    }

    @Override
    public Optional<SymptomDto> getSymptomById(String id) {
        return symptomRepository.findById(id)
                .map(SymptomMapper::symptomVOToSymptomDto);
    }

    @Override
    public List<SymptomDto> getAllSymptoms() {
        List<SymptomVO> symptomVOList = symptomRepository.findAll();
        return SymptomMapper.symptomVOListToSymptomDto(symptomVOList);
    }

    @Override
    public List<SymptomDto> getSymptomsByUserId(String userId) {
        List<SymptomVO> symptomVOList = symptomRepository.findByUserId(userId);
        return SymptomMapper.symptomVOListToSymptomDto(symptomVOList);
    }

    @Override
    public Optional<SymptomDto> getSymptomsByUserIdAndDate(String userId, LocalDate date) {
        return symptomRepository.findByUserIdAndDate(userId, date)
                .map(SymptomMapper::symptomVOToSymptomDto);
    }

    @Override
    public List<SymptomDto> getSymptomsBetweenDates(String userId, LocalDate from, LocalDate to) {
        List<SymptomVO> symptomVOList = symptomRepository.findByUserIdAndDateBetween(userId, from, to);
        return SymptomMapper.symptomVOListToSymptomDto(symptomVOList);
    }
}
