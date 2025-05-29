package com.example.BackEnd.model.cycle;

import com.example.BackEnd.model.phase.PhaseVO;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.data.mongodb.core.mapping.Field;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

@Getter
@Setter
@Builder
@Document(collection = "cycles")
public class CycleVO {
    @Id
    private String id;
    private String userId;
    private LocalDate startDate;
    private int cycleLength;
    private int menstruationDuration;
    @Field("phases")
    private List<PhaseVO> phases;
}
