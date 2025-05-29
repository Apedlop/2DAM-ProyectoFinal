package com.example.BackEnd.repository;

import com.example.BackEnd.model.cycle.CycleDto;
import com.example.BackEnd.model.cycle.CycleVO;
import org.springframework.data.mongodb.repository.MongoRepository;

import java.util.List;
import java.util.Optional;

public interface CycleRepository extends MongoRepository<CycleVO, String> {
    Optional<CycleVO> findById(String id);
    Optional<CycleVO> findLastCycleByUserId(String userId);
    List<CycleVO> findAll();
    List<CycleVO> findByUserId(String userId);
    void deleteByUserId(String userId);
}
