package com.example.backMoom.model.user;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;
import org.springframework.data.annotation.Id;

import java.time.LocalDate;

@Getter
@Setter
@Builder
public class UserVO {
    @Id
    private String id;
    private String name;
    private String surname;
    private String email;
    private String password;
    private LocalDate birthdate;
    private LocalDate lastPeriod;
    private int lastCycleLength;
    private int menstruationDuration;
}
