package com.example.backMoom.repository;

import com.example.backMoom.model.user.UserVO;
import org.springframework.data.mongodb.repository.MongoRepository;

import java.util.List;
import java.util.Optional;

public interface UserRepository extends MongoRepository<UserVO, String> {
    Optional<UserVO> findById(String id);
    List<UserVO> findAll();
    Optional<UserVO> findByEmail(String email);
    List<UserVO> findByName(String name);
}
