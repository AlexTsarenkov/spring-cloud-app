package ru.yandex.practicum.transfer.dto;

import jakarta.validation.Validation;
import jakarta.validation.Validator;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;

class TransferActionDtoValidationTest {
    private Validator validator;

    @BeforeEach
    void setUp() {
        validator = Validation.buildDefaultValidatorFactory().getValidator();
    }

    @Test
    void shouldRejectNonPositiveAmount() {
        TransferActionDto dto = new TransferActionDto();
        dto.setFromAccountId(1L);
        dto.setToAccountId(2L);
        dto.setAmount(0.0);

        assertThat(validator.validate(dto)).isNotEmpty();
    }
}
