package io.github.susimsek.card.security.websocket;

import static reactor.core.publisher.Mono.empty;

import java.util.Map;
import java.util.Optional;
import lombok.RequiredArgsConstructor;
import org.springframework.core.convert.converter.Converter;
import org.springframework.graphql.server.WebGraphQlInterceptor;
import org.springframework.graphql.server.WebGraphQlRequest;
import org.springframework.graphql.server.WebGraphQlResponse;
import org.springframework.graphql.server.WebSocketGraphQlInterceptor;
import org.springframework.graphql.server.WebSocketGraphQlRequest;
import org.springframework.graphql.server.WebSocketSessionInfo;
import org.springframework.security.authentication.AbstractAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.security.oauth2.jwt.JwtDecoder;
import org.springframework.security.oauth2.server.resource.authentication.BearerTokenAuthenticationToken;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;
import reactor.core.publisher.Mono;

@Component
@RequiredArgsConstructor
public class GraphQlWsAuthenticationInterceptor implements WebSocketGraphQlInterceptor {

    private static final String AUTHORIZATION_CONNECTION_INIT_PAYLOAD_VALUE_PREFIX = "Bearer ";

    private static final String AUTHORIZATION_CONNECTION_INIT_PAYLOAD_KEY_NAME = "Authorization";
    private static final String AUTHENTICATION_SESSION_ATTRIBUTE_KEY =
        GraphQlWsAuthenticationInterceptor.class.getCanonicalName() + ".authentication";

    private final JwtDecoder jwtDecoder;

    private final Converter<Jwt, AbstractAuthenticationToken> authenticationConverter;

    @Override
    public Mono<WebGraphQlResponse> intercept(WebGraphQlRequest request, WebGraphQlInterceptor.Chain chain) {
        if (!(request instanceof WebSocketGraphQlRequest wsRequest)) {
            return chain.next(request);
        }

        var optionalAuthentication = Optional.of(wsRequest)
            .map(webSocketGraphQlRequest -> getAuthentication(webSocketGraphQlRequest.getSessionInfo()))
            .map(this::authenticate);
        optionalAuthentication.ifPresent(authentication ->
            SecurityContextHolder.getContext().setAuthentication(authentication));
        return chain.next(request);
    }

    @Override
    public Mono<Void> handleCancelledSubscription(WebSocketSessionInfo sessionInfo, String subscriptionId) {
        clearAuthentication(sessionInfo);
        return empty();
    }

    @Override
    public void handleConnectionClosed(WebSocketSessionInfo sessionInfo, int statusCode,
                                       Map<String, Object> connectionInitPayload) {
        clearAuthentication(sessionInfo);
    }

    @Override
    public Mono<Object> handleConnectionInitialization(WebSocketSessionInfo sessionInfo,
                                                       Map<String, Object> connectionInitPayload) {
        String accessToken = resolveToken(connectionInitPayload);
        if (StringUtils.hasText(accessToken)) {
            setAuthentication(sessionInfo, new BearerTokenAuthenticationToken(accessToken));
        }
        return empty();
    }

    private String resolveToken(Map<String, Object> connectionInitPayload) {
        return resolveTokenFromPayload(connectionInitPayload);
    }

    private String resolveTokenFromPayload(Map<String, Object> connectionInitPayload) {
        String tokenFromPayload = (String) connectionInitPayload.get(AUTHORIZATION_CONNECTION_INIT_PAYLOAD_KEY_NAME);
        if (tokenFromPayload != null
            && tokenFromPayload.startsWith(AUTHORIZATION_CONNECTION_INIT_PAYLOAD_VALUE_PREFIX)) {
            return tokenFromPayload.substring(AUTHORIZATION_CONNECTION_INIT_PAYLOAD_VALUE_PREFIX.length());
        }
        return null;
    }


    private BearerTokenAuthenticationToken getAuthentication(WebSocketSessionInfo webSocketSessionInfo) {
        return (BearerTokenAuthenticationToken) webSocketSessionInfo.getAttributes()
            .get(AUTHENTICATION_SESSION_ATTRIBUTE_KEY);
    }

    private void setAuthentication(WebSocketSessionInfo webSocketSessionInfo,
                                   BearerTokenAuthenticationToken authentication) {
        webSocketSessionInfo.getAttributes().put(AUTHENTICATION_SESSION_ATTRIBUTE_KEY, authentication);
    }

    private void clearAuthentication(WebSocketSessionInfo webSocketSessionInfo) {
        webSocketSessionInfo.getAttributes().remove(AUTHENTICATION_SESSION_ATTRIBUTE_KEY);
    }

    private Authentication authenticate(Authentication authentication) {
        try {
            Optional<String> token = Optional.ofNullable(authentication)
                .filter(BearerTokenAuthenticationToken.class::isInstance)
                .map(BearerTokenAuthenticationToken.class::cast)
                . map(BearerTokenAuthenticationToken::getToken);
            var jwt = token.map(jwtDecoder::decode);
            return jwt.map(authenticationConverter::convert)
                .map(Authentication.class::cast)
                .orElse(null);
        } catch (Exception ex) {
           return null;
        }
    }
}


