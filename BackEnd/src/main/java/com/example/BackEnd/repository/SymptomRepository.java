package com.example.BackEnd.repository;

import com.example.BackEnd.model.symptom.SymptomVO;
import org.springframework.data.domain.Example;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.http.ResponseEntity;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

public interface SymptomRepository extends MongoRepository<SymptomVO, String> {
    Optional<SymptomVO> findById(String id);
    List<SymptomVO> findAll();
    List<SymptomVO> findByUserId(String userId);
    Optional<SymptomVO> findByUserIdAndDate(String userId, LocalDate date);
    List<SymptomVO> findByUserIdAndDateBetween(String userId, LocalDate from, LocalDate to);
    List<SymptomVO> findByUserIdAndDateBetweenOrderByDateAsc(String userId, LocalDate start, LocalDate end);
    List<SymptomVO> findByCycleId(String cycleId);
    void deleteByCycleId(String id);
    List<SymptomVO> findByUserIdAndCycleIdOrderByDateAsc(String userId, String cycleId);
}
