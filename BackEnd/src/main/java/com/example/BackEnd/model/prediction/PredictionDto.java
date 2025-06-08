package com.example.BackEnd.model.prediction;

import lombok.*;
import org.springframework.data.mongodb.core.mapping.Document;

import java.time.LocalDate;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Document
@Builder
public class PredictionDto {
    private String id;
    private String idUser;
    private LocalDate nextPeriodDate;
    private LocalDate nextOvulationDate;
    private Integer cycleLength;
    private Integer menstruationDuration;
    private Integer daysUntilPeriod;
}
