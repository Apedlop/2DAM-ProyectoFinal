package com.example.backMoom.util;

import com.example.backMoom.model.cycle.CycleDto;
import com.example.backMoom.model.cycle.CycleVO;

import java.util.List;
import java.util.stream.Collectors;

public class CycleMapper {

    public static CycleVO cycleDtoToCycleVO(CycleDto cycleDto) {
        return CycleVO.builder()
                .id(cycleDto.getId())
                .userId(cycleDto.getUserId())
                .startDate(cycleDto.getStartDate())
                .cycleLength(cycleDto.getCycleLength())
                .menstruationDuration(cycleDto.getMenstruationDuration())
                .build();
    }

    public static CycleDto cycleVOToCycleDto(CycleVO cycleVO) {
        return CycleDto.builder()
                .id(cycleVO.getId())
                .userId(cycleVO.getUserId())
                .startDate(cycleVO.getStartDate())
                .cycleLength(cycleVO.getCycleLength())
                .menstruationDuration(cycleVO.getMenstruationDuration())
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
