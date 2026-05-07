package ru.yandex.practicum.mybankfront;

import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;

@SpringBootTest(properties = {
        "spring.cloud.config.enabled=false",
        "spring.cloud.bootstrap.enabled=false",
        "spring.config.import=",
        "spring.security.oauth2.client.provider.keycloak.authorization-uri=http://localhost/oauth2/authorize",
        "spring.security.oauth2.client.provider.keycloak.token-uri=http://localhost/oauth2/token",
        "spring.security.oauth2.client.provider.keycloak.jwk-set-uri=http://localhost/oauth2/jwks",
        "spring.security.oauth2.client.provider.keycloak.user-info-uri=http://localhost/oauth2/userinfo",
        "spring.security.oauth2.client.provider.keycloak.user-name-attribute=sub",
        "spring.security.oauth2.client.registration.keycloak.client-id=test-client",
        "spring.security.oauth2.client.registration.keycloak.client-secret=test-secret"
})
class MyBankFrontAppApplicationTests {

	@Test
	void contextLoads() {
	}

}
