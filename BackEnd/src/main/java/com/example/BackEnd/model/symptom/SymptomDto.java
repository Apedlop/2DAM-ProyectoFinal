package com.example.BackEnd.model.symptom;

import com.example.BackEnd.model.enums.TypeEmotion;
import com.example.BackEnd.model.enums.TypePain;
import com.example.BackEnd.model.enums.TypePeriod;
import lombok.*;
import org.springframework.data.mongodb.core.mapping.Document;

import java.time.LocalDate;
import java.util.ArrayList;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Document
@Builder
public class SymptomDto {
    private String id;
    private String userId;
    private String cycleId;
    private LocalDate date;
    private ArrayList<TypePeriod> typePeriod;
    private ArrayList<TypePain> typePain;
    private ArrayList<TypeEmotion> typeEmotion;
    private String notes;
}
