package com.example.backMoom.repository;

import com.example.backMoom.model.cycle.CycleDto;
import com.example.backMoom.model.cycle.CycleVO;
import org.springframework.data.mongodb.repository.MongoRepository;

import java.util.List;
import java.util.Optional;

public interface CycleRepository extends MongoRepository<CycleVO, String> {
    Optional<CycleVO> findById(String id);
    Optional<CycleVO> findLastCycleByUserId(String userId);
    List<CycleVO> findAll();
    List<CycleVO> findByUserId(String userId);
}
