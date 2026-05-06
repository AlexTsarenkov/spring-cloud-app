package ru.yandex.practicum.mybankfront.dto;

import lombok.Data;

import java.time.LocalDate;

@Data
public class UserData {
    private Long id;
    private String username;
    private String nameSurename;
    private LocalDate birthdate;
}
