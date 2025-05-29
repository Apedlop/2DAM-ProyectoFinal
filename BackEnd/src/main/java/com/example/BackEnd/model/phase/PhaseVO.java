package com.example.BackEnd.model.phase;

import com.example.BackEnd.model.enums.PhaseCycle;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import java.time.LocalDate;

@Getter
@Setter
@Builder
@Document(collection = "phases")
public class PhaseVO {
    @Id
    private String id;
    private String idUser;
    private String color;
    private PhaseCycle phaseCycle;
    private LocalDate startDay;
    private LocalDate endDay;
    private String description;
}
