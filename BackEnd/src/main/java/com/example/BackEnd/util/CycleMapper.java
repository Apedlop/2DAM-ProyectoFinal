package com.example.BackEnd.util;

import com.example.BackEnd.model.cycle.CycleDto;
import com.example.BackEnd.model.cycle.CycleVO;
import com.example.BackEnd.model.phase.PhaseVO;

import java.util.List;
import java.util.stream.Collectors;

public class CycleMapper {

    public static CycleVO cycleDtoToCycleVO(CycleDto dto) {
        List<PhaseVO> phases = null;
        if (dto.getPhases() != null) {
            phases = dto.getPhases().stream()
                    .map(PhaseMapper::phaseDtoToPhaseVO)
                    .collect(Collectors.toList());
        }

        return CycleVO.builder()
                .id(dto.getId())
                .userId(dto.getUserId())
                .startDate(dto.getStartDate())
                .cycleLength(dto.getCycleLength())
                .menstruationDuration(dto.getMenstruationDuration())
                .phases(phases)
                .build();
    }

    public static CycleDto cycleVOToCycleDto(CycleVO vo) {
        return CycleDto.builder()
                .id(vo.getId())
                .userId(vo.getUserId())
                .startDate(vo.getStartDate())
                .cycleLength(vo.getCycleLength())
                .menstruationDuration(vo.getMenstruationDuration())
                .phases(vo.getPhases() != null ?
                        vo.getPhases().stream()
                                .map(PhaseMapper::phaseVOToPhaseDto)
                                .collect(Collectors.toList()) : null)
                .build();
    }

    public static List<CycleVO> cycleDtoListToCycleVO(List<CycleDto> dtoList) {
        return dtoList.stream()
                .map(CycleMapper::cycleDtoToCycleVO)
                .collect(Collectors.toList());
    }

    public static List<CycleDto> cycleVOListToCycleDto(List<CycleVO> voList) {
        return voList.stream()
                .map(CycleMapper::cycleVOToCycleDto)
                .collect(Collectors.toList());
    }

}
