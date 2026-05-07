package ru.yandex.practicum.mybankfront.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * Элемент списка «Кому» в main.html: {@code th:value="${account.login}"}, {@code th:text="${account.name}"}.
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class UserToTransfer {
    private String login;
    private String name;
}
