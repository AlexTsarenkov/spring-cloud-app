package ru.yandex.practicum.cash.dto;

import jakarta.validation.Validation;
import jakarta.validation.Validator;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import ru.yandex.practicum.cash.enumeration.CashAction;

import static org.assertj.core.api.Assertions.assertThat;

class CashActionDtoValidationTest {
    private Validator validator;

    @BeforeEach
    void setUp() {
        validator = Validation.buildDefaultValidatorFactory().getValidator();
    }

    @Test
    void shouldRejectNonPositiveBalance() {
        CashActionDto dto = CashActionDto.builder()
                .action(CashAction.PUT)
                .accountNumber(1L)
                .balance(0.0)
                .build();

        assertThat(validator.validate(dto)).isNotEmpty();
    }
}
