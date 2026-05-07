package ru.yandex.practicum.gateway.configuration;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.context.TestConfiguration;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.Bean;
import org.springframework.security.oauth2.jwt.JwtDecoder;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.mock;

@SpringBootTest(properties = {
        "spring.cloud.config.enabled=false",
        "spring.cloud.bootstrap.enabled=false",
        "spring.config.import=",
        "spring.security.oauth2.resourceserver.jwt.issuer-uri=http://localhost/issuer"
})
class GatewaySecurityContractTest {
    @Autowired
    private ApplicationContext context;

    @TestConfiguration
    static class TestSecurityConfig {
        @Bean
        JwtDecoder jwtDecoder() {
            return mock(JwtDecoder.class);
        }
    }

    @Test
    void shouldRegisterJwtTokenRelayFilterBean() {
        assertThat(context.containsBean("jwtTokenRelayGatewayFilterFactory")).isTrue();
    }
}
