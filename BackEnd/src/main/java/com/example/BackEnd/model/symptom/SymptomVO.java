package com.example.BackEnd.model.symptom;

import com.example.BackEnd.model.enums.TypeEmotion;
import com.example.BackEnd.model.enums.TypePain;
import com.example.BackEnd.model.enums.TypePeriod;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;
import org.springframework.data.annotation.Id;

import java.time.LocalDate;
import java.util.ArrayList;

@Getter
@Setter
@Builder
public class SymptomVO {
    @Id
    private String id;
    private String userId;
    private String cycleId;
    private LocalDate date;
    private ArrayList<TypePeriod> typePeriod;
    private ArrayList<TypePain> typePain;
    private ArrayList<TypeEmotion> typeEmotion;
    private String notes;
}
