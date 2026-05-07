package ru.yandex.practicum.mybankfront.dto;

import lombok.Builder;
import lombok.Data;

import java.time.LocalDate;

@Data
@Builder
public class UserDataUpdateDto {
    private String username;
    private String nameSurename;
    private LocalDate birthdate;
}

