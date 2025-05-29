package com.example.BackEnd.controller.impl;

import com.example.BackEnd.controller.EnumAPI;
import com.example.BackEnd.model.enums.TypeEmotion;
import com.example.BackEnd.model.enums.TypePain;
import com.example.BackEnd.model.enums.TypePeriod;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/enums")
public class EnumController implements EnumAPI {

    @GetMapping("/typePain")
    public TypePain[] getTypePainValues() {
        return TypePain.values();
    }

    @GetMapping("/typeEmotion")
    public TypeEmotion[] getTypeEmotionValues() {
        return TypeEmotion.values();
    }

    @GetMapping("/typePeriod")
    public TypePeriod[] getTypePeriodValues() {
        return TypePeriod.values();
    }
}
