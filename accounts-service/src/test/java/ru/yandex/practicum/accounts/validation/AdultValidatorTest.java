package ru.yandex.practicum.accounts.validation;

import jakarta.validation.Validation;
import jakarta.validation.Validator;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import ru.yandex.practicum.accounts.model.dto.UserDataUpdateDto;

import java.time.LocalDate;

import static org.assertj.core.api.Assertions.assertThat;

class AdultValidatorTest {
    private Validator validator;

    @BeforeEach
    void setUp() {
        validator = Validation.buildDefaultValidatorFactory().getValidator();
    }

    @Test
    void shouldRejectUnderageUser() {
        UserDataUpdateDto dto = new UserDataUpdateDto();
        dto.setUsername("user");
        dto.setNameSurename("User Test");
        dto.setBirthdate(LocalDate.now().minusYears(17));

        assertThat(validator.validate(dto)).isNotEmpty();
    }

    @Test
    void shouldAcceptAdultUser() {
        UserDataUpdateDto dto = new UserDataUpdateDto();
        dto.setUsername("user");
        dto.setNameSurename("User Test");
        dto.setBirthdate(LocalDate.now().minusYears(18));

        assertThat(validator.validate(dto)).isEmpty();
    }
}
