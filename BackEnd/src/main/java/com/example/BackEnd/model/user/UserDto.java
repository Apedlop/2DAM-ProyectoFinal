package com.example.BackEnd.model.user;

import lombok.*;
import org.springframework.data.mongodb.core.mapping.Document;

import java.time.LocalDate;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Document
@Builder
public class UserDto {
    private String id;
    private String name;
    private String surname;
    private String email;
    private String password;
    private LocalDate birthdate;
    private LocalDate lastPeriod;
    private Integer lastCycleLength;
    private Integer menstruationDuration;
}
