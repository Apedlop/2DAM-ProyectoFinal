package com.example.backMoom.repository;

import com.example.backMoom.model.phase.PhaseVO;
import org.springframework.data.mongodb.repository.MongoRepository;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

public interface PhaseRepository extends MongoRepository<PhaseVO, String> {
    Optional<PhaseVO> findById(String id);
    List<PhaseVO> findAll();
    List<PhaseVO> findByStartDayBetween(LocalDate start, LocalDate end);
    List<PhaseVO> findByPhaseCycle(String phaseCycle);
}
