package com.example.BackEnd.model.prediction;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;
import org.springframework.data.annotation.Id;

import java.time.LocalDate;

@Getter
@Setter
@Builder
public class PredictionVO {
    @Id
    private String id;
    private String idUser;
    private LocalDate nextPeriodDate;
    private LocalDate nextOvulationDate;
    private Integer cycleLength;
    private Integer menstruationDuration;
    private Integer daysUntilPeriod;
}
