package com.example.BackEnd.model.cycle;

import com.example.BackEnd.model.phase.PhaseDto;
import lombok.*;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.data.mongodb.core.mapping.Field;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Document(collection = "cycles")
@Builder
public class CycleDto {
    private String id;
    private String userId;
    private LocalDate startDate;
    private int cycleLength;
    private int menstruationDuration;
    @Field("phases")
    private List<PhaseDto> phases;
}
