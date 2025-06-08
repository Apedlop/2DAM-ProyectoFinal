package com.example.BackEnd.repository;

import com.example.BackEnd.model.prediction.PredictionDto;
import com.example.BackEnd.model.prediction.PredictionVO;
import org.springframework.data.mongodb.repository.MongoRepository;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

public interface PredictionRepository extends MongoRepository<PredictionVO, String> {
    Optional<PredictionVO> findById(String id);
    List<PredictionVO> findAll();
    List<PredictionVO> findByNextPeriodDateBetween(LocalDate start, LocalDate end);
    Optional<PredictionVO> findByNextPeriodDate(LocalDate date);
}
