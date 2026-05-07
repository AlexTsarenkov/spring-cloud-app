package ru.yandex.practicum.gateway.security;

import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;

class JwtTokenRelayGatewayFilterFactoryUnitTest {

    @Test
    void shouldCreateFilterSupplierInstance() {
        JwtTokenRelayGatewayFilterFactory factory = new JwtTokenRelayGatewayFilterFactory();
        assertThat(factory).isNotNull();
    }
}
