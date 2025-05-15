package com.example.backMoom.model.cycle;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;
import org.springframework.data.annotation.Id;

import java.time.LocalDate;

@Getter
@Setter
@Builder
public class CycleVO {
    @Id
    private String id;
    private String userId;
    private LocalDate startDate;
    private int cycleLength;
    private int menstruationDuration;
}
