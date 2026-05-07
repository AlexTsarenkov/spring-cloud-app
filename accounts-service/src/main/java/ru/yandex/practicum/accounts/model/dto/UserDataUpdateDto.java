package ru.yandex.practicum.accounts.model.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Data;
import ru.yandex.practicum.accounts.validation.Adult;

import java.time.LocalDate;

@Data
public class UserDataUpdateDto {
    @NotBlank(message = "message: Username must be specified")
    private String username;

    @NotBlank(message = "message: Name and surname must be specified")
    private String nameSurename;

    @NotNull(message = "message: Birthdate must be specified")
    @Adult
    private LocalDate birthdate;
}

