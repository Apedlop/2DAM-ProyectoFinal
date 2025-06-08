package com.example.BackEnd.service.impl;

import com.example.BackEnd.model.user.UserDto;
import com.example.BackEnd.model.user.UserVO;
import com.example.BackEnd.repository.CycleRepository;
import com.example.BackEnd.repository.UserRepository;
import com.example.BackEnd.service.UserService;
import com.example.BackEnd.util.UserMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

@Service
public class UserServiceImpl implements UserService {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private CycleRepository cycleRepository;

    // Métodos CRUD
    @Override
    public UserDto addUser(UserDto userDto) {
        UserVO userVO = UserMapper.userDtoToUserVO(userDto);
        UserVO createUser = userRepository.save(userVO);
        return UserMapper.userVOToUserDto(createUser);
    }

    @Override
    public UserDto updateUser(UserDto userDto) {
        Optional<UserVO> userOptional = userRepository.findById(userDto.getId());

        if (userOptional.isPresent()) {
            UserVO userVO = userOptional.get();
            userVO.setName(userDto.getName());
            userVO.setSurname(userDto.getSurname());
            userVO.setEmail(userDto.getEmail());
            userVO.setPassword(userDto.getPassword());
            userVO.setBirthdate(userDto.getBirthdate());
            userVO.setLastPeriod(userDto.getLastPeriod());
            userVO.setLastCycleLength(userDto.getLastCycleLength());
            userVO.setMenstruationDuration(userDto.getMenstruationDuration());
            UserVO updateUser = userRepository.save(userVO);
            return UserMapper.userVOToUserDto(updateUser);
        } else {
            return null;
        }
    }

    @Override
    public ResponseEntity deleteUser(String id) {
        System.out.println("ID recibido: " + id);

        try {
            Optional<UserVO> userOptional = userRepository.findById(id);
            if (userOptional.isPresent()) {
                // Eliminar ciclos del usuario
                cycleRepository.deleteByUserId(id);

                // Eliminar usuario
                userRepository.deleteById(id);

                return ResponseEntity.ok("Usuario y sus ciclos eliminados exitosamente");
            } else {
                return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Usuario no encontrado con el ID: " + id);
            }
        } catch (Exception e) {
            e.printStackTrace();
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Error al eliminar al usuario");
        }
    }

    @Override
    public ResponseEntity deleteAllUser() {
        userRepository.deleteAll();
        ResponseEntity.ok("Usuarios eliminados exitosamente");
        return ResponseEntity.ok().build();
    }

    @Override
    public Optional<UserDto> getById(String id) {
        Optional<UserVO> userOptional = userRepository.findById(id);
        return userOptional.map(UserMapper::userVOToUserDto);
    }

    @Override
    public List<UserDto> getAll() {
        List<UserVO> userVO = userRepository.findAll();
        return UserMapper.userVOListToUserDto(userVO);
    }

    @Override
    public Optional<UserDto> getByEmail(String email) {
        Optional<UserVO> userOptional = userRepository.findByEmail(email);
        return userOptional.map(UserMapper::userVOToUserDto);
    }

    @Override
    public List<UserDto> getByName(String name) {
        List<UserVO> userVO = userRepository.findByName(name);
        return UserMapper.userVOListToUserDto(userVO);
    }

    // Método para la lógica del proyecto
    @Override
    public boolean existsByEmail(String email) {
        return userRepository.findByEmail(email).isPresent();
    }
}
