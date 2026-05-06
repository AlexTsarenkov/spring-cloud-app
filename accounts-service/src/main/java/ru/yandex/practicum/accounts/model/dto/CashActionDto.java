package ru.yandex.practicum.accounts.model.dto;

import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import lombok.Data;
import ru.yandex.practicum.accounts.enumeration.CashAction;

@Data
public class CashActionDto {
    @NotNull
    private CashAction action;

    @NotNull
    private Long accountNumber;

    @NotNull
    @Positive
    private Double balance;
}
