package com.example.backMoom.service.impl;

import com.example.backMoom.model.symptom.SymptomDto;
import com.example.backMoom.model.symptom.SymptomVO;
import com.example.backMoom.repository.SymptomRepository;
import com.example.backMoom.service.SymptomService;
import com.example.backMoom.util.SymptomMapper;
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
        SymptomVO symptomVO = SymptomMapper.symptomDtoToSymptomVO(symptomDto);
        SymptomVO createSymptom = symptomRepository.save(symptomVO);
        return SymptomMapper.symptomVOToSymptomDto(createSymptom);
    }

    @Override
    public SymptomDto updateSymptom(SymptomDto symptomDto) {
        Optional<SymptomVO> symptomOptional = symptomRepository.findById(symptomDto.getId());

        if (symptomOptional.isPresent()) {
            SymptomVO symptomVO = symptomOptional.get();
            symptomVO.setDate(symptomDto.getDate());
            symptomVO.setTypePeriod(symptomDto.getTypePeriod());
            symptomVO.setTypePain(symptomDto.getTypePain());
            symptomVO.setTypeEmotion(symptomDto.getTypeEmotion());
            symptomVO.setNotes(symptomDto.getNotes());
            SymptomVO updateSymptom = symptomRepository.save(symptomVO);
            return SymptomMapper.symptomVOToSymptomDto(updateSymptom);
        } else {
            return null;
        }
    }

    @Override
    public ResponseEntity deleteSymptom(String id) {
        System.out.println("ID recibido: " + id);

        try {
            Optional<SymptomVO> symptomOptional = symptomRepository.findById(id);
            if (symptomOptional.isPresent()) {
                symptomRepository.deleteById(id);
                return ResponseEntity.ok("Síntomas eliminados exitosamente");
            } else {
                return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Síntomas no encontrados con el ID: " + id);
            }
        } catch (Exception e) {
            e.printStackTrace();
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Error al eliminar los síntomas");
        }
    }

    @Override
    public Optional<SymptomDto> getSymptomById(String id) {
        Optional<SymptomVO> symptomOptional = symptomRepository.findById(id);
        return symptomOptional.map(SymptomMapper::symptomVOToSymptomDto);
    }

    @Override
    public List<SymptomDto> getAllSymptoms() {
        List<SymptomVO> symptomVO = symptomRepository.findAll();
        return SymptomMapper.symptomVOListToSymptomDto(symptomVO);
    }

    @Override
    public List<SymptomDto> getSymptomsByUserId(String userId) {
        List<SymptomVO> symptomVO = symptomRepository.findByUserId(userId);
        return SymptomMapper.symptomVOListToSymptomDto(symptomVO);
    }

    @Override
    public List<SymptomDto> getSymptomsByUserIdAndDate(String userId, LocalDate date) {
        List<SymptomVO> symptomVO = symptomRepository.findByUserIdAndDate(userId, date);
        return SymptomMapper.symptomVOListToSymptomDto(symptomVO);
    }

    @Override
    public List<SymptomDto> getSymptomsBetweenDates(String userId, LocalDate from, LocalDate to) {
        List<SymptomVO> symptomVO = symptomRepository.findByUserIdAndDateBetween(userId, from, to);
        return SymptomMapper.symptomVOListToSymptomDto(symptomVO);
    }
}
