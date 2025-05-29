package com.example.BackEnd.model.phase;

import com.example.BackEnd.model.enums.PhaseCycle;
import lombok.*;
import org.springframework.data.mongodb.core.mapping.Document;

import java.time.LocalDate;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Document(collection = "phases")
@Builder
public class PhaseDto {
    private String id;
    private String idUser;
    private String color;
    private PhaseCycle phaseCycle;
    private LocalDate startDay;
    private LocalDate endDay;
    private String description;
}
