package ru.yandex.practicum.accounts.model.dto;

import jakarta.validation.constraints.NotBlank;
import lombok.Data;

import java.time.LocalDate;

@Data
public class UserDataUpdateDto {
    @NotBlank(message = "message: Username must be specified")
    private String username;

    @NotBlank(message = "message: Name and surname must be specified")
    private String nameSurename;


    private LocalDate birthdate;
}

