package com.example.backMoom.util;

import com.example.backMoom.model.prediction.PredictionDto;
import com.example.backMoom.model.prediction.PredictionVO;

import java.util.List;
import java.util.stream.Collectors;

public class PredictionMapper {

    public static PredictionVO predictionDtoToPredictionVO(PredictionDto dto) {
        return PredictionVO.builder()
                .id(dto.getId())
                .idUser(dto.getIdUser())
                .nextPeriodDate(dto.getNextPeriodDate())
                .nextOvulationDate(dto.getNextOvulationDate())
                .daysUntilPeriod(dto.getDaysUntilPeriod())
                .build();
    }

    public static PredictionDto predictionVOToPredictionDto(PredictionVO vo) {
        return PredictionDto.builder()
                .id(vo.getId())
                .idUser(vo.getIdUser())
                .nextPeriodDate(vo.getNextPeriodDate())
                .nextOvulationDate(vo.getNextOvulationDate())
                .daysUntilPeriod(vo.getDaysUntilPeriod())
                .build();
    }

    public static List<PredictionVO> predictionDtoListToPredictionVO(List<PredictionDto> dtoList) {
        return dtoList.stream()
                .map(PredictionMapper::predictionDtoToPredictionVO)
                .collect(Collectors.toList());
    }

    public static List<PredictionDto> predictionVOListToPredictionDto(List<PredictionVO> voList) {
        return voList.stream()
                .map(PredictionMapper::predictionVOToPredictionDto)
                .collect(Collectors.toList());
    }
}
