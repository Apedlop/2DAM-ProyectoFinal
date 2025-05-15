package com.example.backMoom.model.cycle;

import lombok.*;
import org.springframework.data.mongodb.core.mapping.Document;

import java.time.LocalDate;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Document
@Builder
public class CycleDto {
    private String id;
    private String userId;
    private LocalDate startDate;
    private int cycleLength;
    private int menstruationDuration;
}
