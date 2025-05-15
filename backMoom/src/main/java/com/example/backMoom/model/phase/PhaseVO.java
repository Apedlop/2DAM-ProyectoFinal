package com.example.backMoom.model.phase;

import com.example.backMoom.model.enums.PhaseCycle;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;
import org.springframework.data.annotation.Id;

import java.time.LocalDate;

@Getter
@Setter
@Builder
public class PhaseVO {
    @Id
    private String id;
    private String idUser;
    private PhaseCycle phaseCycle;
    private LocalDate startDay;
    private LocalDate endDay;
    private String description;
}
