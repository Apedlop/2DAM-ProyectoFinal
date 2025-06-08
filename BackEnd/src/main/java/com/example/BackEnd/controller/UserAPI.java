package com.example.BackEnd.controller;

import com.example.BackEnd.model.user.UserDto;
import org.springframework.http.ResponseEntity;

import java.util.List;
import java.util.Optional;

public interface UserAPI {
    // Métodos CRUD
    UserDto addUser(UserDto userDto);
    UserDto updateUser(UserDto userDto, String id);
    ResponseEntity deleteUser(String id);
    ResponseEntity deleteAllUser();
    Optional<UserDto> getById(String id);
    List<UserDto> getAll();
    Optional<UserDto> getByEmail(String email);
    List<UserDto> getByName(String name);

    // Métodos para la logica del proyecto
    boolean existsByEmail(String email);
}
