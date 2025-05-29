package com.example.BackEnd.util;

import com.example.BackEnd.model.user.UserDto;
import com.example.BackEnd.model.user.UserVO;

import java.util.List;
import java.util.stream.Collectors;

public class UserMapper {

    public static UserVO userDtoToUserVO(UserDto userDto) {
        return UserVO.builder()
                .id(userDto.getId())
                .name(userDto.getName())
                .surname(userDto.getSurname())
                .email(userDto.getEmail())
                .password(userDto.getPassword())
                .birthdate(userDto.getBirthdate())
                .lastPeriod(userDto.getLastPeriod())
                .lastCycleLength(userDto.getLastCycleLength())
                .menstruationDuration(userDto.getMenstruationDuration())
                .build();
    }

    public static UserDto userVOToUserDto(UserVO userVO) {
        return UserDto.builder()
                .id(userVO.getId())
                .name(userVO.getName())
                .surname(userVO.getSurname())
                .email(userVO.getEmail())
                .password(userVO.getPassword())
                .birthdate(userVO.getBirthdate())
                .lastPeriod(userVO.getLastPeriod())
                .lastCycleLength(userVO.getLastCycleLength())
                .menstruationDuration(userVO.getMenstruationDuration())
                .build();
    }

    public static List<UserVO> userDtoListToUserVO(List<UserDto> userVOList) {
        return userVOList.stream()
                .map(UserMapper::userDtoToUserVO)
                .collect(Collectors.toList());
    }

    public static List<UserDto> userVOListToUserDto(List<UserVO> userDtoList) {
        return userDtoList.stream()
                .map(UserMapper::userVOToUserDto)
                .collect(Collectors.toList());
    }
}
