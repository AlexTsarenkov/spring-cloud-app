package ru.yandex.practicum.gateway;

import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.context.TestConfiguration;
import org.springframework.context.annotation.Bean;
import org.springframework.security.oauth2.jwt.JwtDecoder;

import static org.mockito.Mockito.mock;

@SpringBootTest(properties = {
        "spring.cloud.config.enabled=false",
        "spring.cloud.bootstrap.enabled=false",
        "spring.config.import=",
        "spring.security.oauth2.resourceserver.jwt.issuer-uri=http://localhost/issuer"
})
class GatewayApiApplicationIntegrationTest {

    @TestConfiguration
    static class TestSecurityConfig {
        @Bean
        JwtDecoder jwtDecoder() {
            return mock(JwtDecoder.class);
        }
    }

    @Test
    void contextLoads() {
    }
}
