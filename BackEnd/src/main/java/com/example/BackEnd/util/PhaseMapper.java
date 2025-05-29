package com.example.BackEnd.util;

import com.example.BackEnd.model.phase.PhaseDto;
import com.example.BackEnd.model.phase.PhaseVO;

import java.util.List;
import java.util.stream.Collectors;

public class PhaseMapper {

    public static PhaseVO phaseDtoToPhaseVO(PhaseDto dto) {
        return PhaseVO.builder()
                .id(dto.getId())
                .idUser(dto.getIdUser())
                .color(dto.getColor())
                .phaseCycle(dto.getPhaseCycle())
                .startDay(dto.getStartDay())
                .endDay(dto.getEndDay())
                .description(dto.getDescription())
                .build();
    }

    public static PhaseDto phaseVOToPhaseDto(PhaseVO vo) {
        return PhaseDto.builder()
                .id(vo.getId())
                .idUser(vo.getIdUser())
                .color(vo.getColor())
                .phaseCycle(vo.getPhaseCycle())
                .startDay(vo.getStartDay())
                .endDay(vo.getEndDay())
                .description(vo.getDescription())
                .build();
    }

    public static List<PhaseVO> phaseDtoListToPhaseVO(List<PhaseDto> dtoList) {
        return dtoList.stream()
                .map(PhaseMapper::phaseDtoToPhaseVO)
                .collect(Collectors.toList());
    }

    public static List<PhaseDto> phaseVOListToPhaseDto(List<PhaseVO> voList) {
        return voList.stream()
                .map(PhaseMapper::phaseVOToPhaseDto)
                .collect(Collectors.toList());
    }
}
