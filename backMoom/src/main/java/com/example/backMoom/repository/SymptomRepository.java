package com.example.backMoom.repository;

import com.example.backMoom.model.symptom.SymptomVO;
import org.springframework.data.domain.Example;
import org.springframework.data.mongodb.repository.MongoRepository;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

public interface SymptomRepository extends MongoRepository<SymptomVO, String> {
    Optional<SymptomVO> findById(String id);
    List<SymptomVO> findAll();
    List<SymptomVO> findByUserId(String userId);
    List<SymptomVO> findByUserIdAndDate(String userId, LocalDate date);
    List<SymptomVO> findByUserIdAndDateBetween(String userId, LocalDate from, LocalDate to);
}
