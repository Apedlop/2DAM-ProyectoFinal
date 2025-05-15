package com.example.backMoom.model.phase;

import com.example.backMoom.model.enums.PhaseCycle;
import lombok.*;
import org.springframework.data.mongodb.core.mapping.Document;

import java.time.LocalDate;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Document
@Builder
public class PhaseDto {
    private String id;
    private String idUser;
    private PhaseCycle phaseCycle;
    private LocalDate startDay;
    private LocalDate endDay;
    private String description;
}
