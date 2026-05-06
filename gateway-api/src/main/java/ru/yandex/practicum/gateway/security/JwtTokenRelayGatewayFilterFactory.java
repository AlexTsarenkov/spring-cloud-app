package ru.yandex.practicum.gateway.security;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.cloud.gateway.server.mvc.filter.SimpleFilterSupplier;
import org.springframework.http.HttpHeaders;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.oauth2.server.resource.authentication.JwtAuthenticationToken;
import org.springframework.web.servlet.function.HandlerFilterFunction;
import org.springframework.web.servlet.function.ServerRequest;
import org.springframework.web.servlet.function.ServerResponse;

public class JwtTokenRelayGatewayFilterFactory extends SimpleFilterSupplier {
    private static final Logger log =
            LoggerFactory.getLogger(JwtTokenRelayGatewayFilterFactory.class);

    public JwtTokenRelayGatewayFilterFactory() {
        super(JwtTokenRelayGatewayFilterFactory.class);
    }

    public static HandlerFilterFunction<ServerResponse, ServerResponse> jwtTokenRelay() {
        return (request, next) -> {
            String token = extractToken(request);
            ServerRequest requestWithToken = addToken(request, token);
            return next.handle(requestWithToken);
        };
    }

    private static String extractToken(ServerRequest request) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if (authentication instanceof JwtAuthenticationToken jwtAuth && jwtAuth.getToken() != null) {
            return jwtAuth.getToken().getTokenValue();
        }

        String authorizationHeader = request.headers().firstHeader(HttpHeaders.AUTHORIZATION);
        if (authorizationHeader != null && authorizationHeader.startsWith("Bearer ")) {
            return authorizationHeader.substring(7);
        }

        throw new IllegalStateException("JWT token not found in SecurityContext or Authorization header");
    }

    private static ServerRequest addToken(ServerRequest request, String token) {
        ServerRequest updatedRequest = ServerRequest.from(request)
                .headers(headers -> headers.setBearerAuth(token))
                .build();

        log.debug("Token relayed for path {} (len={})", request.path(), token.length());
        return updatedRequest;
    }
}
