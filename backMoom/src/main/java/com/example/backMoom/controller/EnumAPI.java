package com.example.backMoom.controller;

import com.example.backMoom.model.enums.TypeEmotion;
import com.example.backMoom.model.enums.TypePain;
import com.example.backMoom.model.enums.TypePeriod;

public interface EnumAPI {
    TypePain[] getTypePainValues();
    TypeEmotion[] getTypeEmotionValues();
    TypePeriod[] getTypePeriodValues();
}
