package com.example.BackEnd.controller.impl;

import com.example.BackEnd.controller.UserAPI;
import com.example.BackEnd.model.user.UserDto;
import com.example.BackEnd.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@CrossOrigin
@RestController
@RequestMapping("/api/user")
public class UserController implements UserAPI {

    @Autowired
    private UserService userService;

    @Override
    @PostMapping
    public UserDto addUser(@RequestBody UserDto userDto) {
        return userService.addUser(userDto);
    }

    @Override
    @PutMapping("/{id}")
    public UserDto updateUser(@RequestBody UserDto userDto, @PathVariable String id) {
        return userService.updateUser(userDto);
    }

    @Override
    @DeleteMapping("/{id}")
    public ResponseEntity<?> deleteUser(@PathVariable String id) {
        return userService.deleteUser(id);
    }

    @Override
    @DeleteMapping("/all")
    public ResponseEntity<?> deleteAllUser() {
        return userService.deleteAllUser();
    }

    @Override
    @GetMapping("/{id}")
    public Optional<UserDto> getById(@PathVariable String id) {
        return userService.getById(id);
    }

    @Override
    @GetMapping("/all")
    public List<UserDto> getAll() {
        return userService.getAll();
    }

    @Override
    @GetMapping("/email/{email}")
    public Optional<UserDto> getByEmail(@PathVariable String email) {
        return userService.getByEmail(email);
    }

    @Override
    @GetMapping("/name/{name}")
    public List<UserDto> getByName(@PathVariable String name) {
        return userService.getByName(name);
    }

    @Override
    @GetMapping("/exists")
    public boolean existsByEmail(@RequestParam String email) {
        return userService.existsByEmail(email);
    }
}
