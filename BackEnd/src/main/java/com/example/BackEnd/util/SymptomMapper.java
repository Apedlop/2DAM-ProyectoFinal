package com.example.BackEnd.util;

import com.example.BackEnd.model.symptom.SymptomDto;
import com.example.BackEnd.model.symptom.SymptomVO;

import java.util.List;
import java.util.stream.Collectors;

public class SymptomMapper {

    public static SymptomVO symptomDtoToSymptomVO(SymptomDto dto) {
        return SymptomVO.builder()
                .id(dto.getId())
                .userId(dto.getUserId())
                .cycleId(dto.getCycleId())
                .date(dto.getDate())
                .typePeriod(dto.getTypePeriod())
                .typePain(dto.getTypePain())
                .typeEmotion(dto.getTypeEmotion())
                .notes(dto.getNotes())
                .build();
    }

    public static SymptomDto symptomVOToSymptomDto(SymptomVO vo) {
        return SymptomDto.builder()
                .id(vo.getId())
                .userId(vo.getUserId())
                .cycleId(vo.getCycleId())
                .date(vo.getDate())
                .typePeriod(vo.getTypePeriod())
                .typePain(vo.getTypePain())
                .typeEmotion(vo.getTypeEmotion())
                .notes(vo.getNotes())
                .build();
    }

    public static List<SymptomVO> symptomDtoListToSymptomVO(List<SymptomDto> dtoList) {
        return dtoList.stream()
                .map(SymptomMapper::symptomDtoToSymptomVO)
                .collect(Collectors.toList());
    }

    public static List<SymptomDto> symptomVOListToSymptomDto(List<SymptomVO> voList) {
        return voList.stream()
                .map(SymptomMapper::symptomVOToSymptomDto)
                .collect(Collectors.toList());
    }
}
