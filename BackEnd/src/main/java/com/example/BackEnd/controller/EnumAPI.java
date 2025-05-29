package com.example.BackEnd.controller;

import com.example.BackEnd.model.enums.TypeEmotion;
import com.example.BackEnd.model.enums.TypePain;
import com.example.BackEnd.model.enums.TypePeriod;

public interface EnumAPI {
    TypePain[] getTypePainValues();
    TypeEmotion[] getTypeEmotionValues();
    TypePeriod[] getTypePeriodValues();
}
