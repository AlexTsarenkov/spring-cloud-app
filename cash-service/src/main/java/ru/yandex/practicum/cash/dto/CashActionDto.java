package ru.yandex.practicum.cash.dto;

import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import lombok.Builder;
import lombok.Data;
import ru.yandex.practicum.cash.enumeration.CashAction;

@Data
@Builder
public class CashActionDto {
    @NotNull
    private CashAction action;

    @NotNull
    private Long accountNumber;

    @NotNull
    @Positive
    private Double balance;
}
