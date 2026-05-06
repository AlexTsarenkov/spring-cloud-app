package ru.yandex.practicum.mybankfront.configuration;

import org.junit.jupiter.api.Test;
import org.springframework.security.authentication.AnonymousAuthenticationToken;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.authority.AuthorityUtils;

import static org.assertj.core.api.Assertions.assertThat;

class CurrentUserModelAdviceTest {
    private final CurrentUserModelAdvice advice = new CurrentUserModelAdvice();

    @Test
    void shouldReturnNullWhenAuthenticationIsNull() {
        assertThat(advice.currentUsername(null)).isNull();
    }

    @Test
    void shouldReturnUsernameForAuthenticatedUser() {
        UsernamePasswordAuthenticationToken auth =
                new UsernamePasswordAuthenticationToken("alex", "pwd", AuthorityUtils.NO_AUTHORITIES);
        assertThat(advice.currentUsername(auth)).isEqualTo("alex");
    }

    @Test
    void shouldReturnNullForAnonymousAuthentication() {
        AnonymousAuthenticationToken auth = new AnonymousAuthenticationToken(
                "key",
                "anonymousUser",
                AuthorityUtils.createAuthorityList("ROLE_ANONYMOUS")
        );
        assertThat(advice.currentUsername(auth)).isEqualTo("anonymousUser");
    }
}
