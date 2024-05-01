package io.github.susimsek.card.exception.security;

import graphql.GraphQLError;
import graphql.GraphqlErrorBuilder;
import graphql.schema.DataFetchingEnvironment;
import java.util.Optional;
import org.springframework.graphql.execution.ErrorType;
import org.springframework.http.HttpStatus;
import org.springframework.security.authentication.AuthenticationTrustResolver;
import org.springframework.security.authentication.AuthenticationTrustResolverImpl;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;

@Component
public class GraphQLSecurityExceptionResolver {

    private final AuthenticationTrustResolver trustResolver = new AuthenticationTrustResolverImpl();

    public GraphQLError resolveException(
        Throwable ex,
        DataFetchingEnvironment env) {
        if (ex instanceof AuthenticationException) {
            return unauthorized(ex, env);
        } else {
            SecurityContext securityContext = SecurityContextHolder.getContext();
            return Optional.ofNullable(securityContext)
                .map(context -> accessDenied(ex, env, context))
                .orElse(unauthorized(ex, env));
        }
    }

    private GraphQLError unauthorized(Throwable ex, DataFetchingEnvironment env) {
        return GraphqlErrorBuilder.newError(env)
            .errorType(ErrorType.UNAUTHORIZED)
            .message(HttpStatus.UNAUTHORIZED.getReasonPhrase())
            .build();
    }

    private GraphQLError accessDenied(
        Throwable ex,
        DataFetchingEnvironment env,
        SecurityContext securityContext) {
        if (trustResolver.isAnonymous(securityContext.getAuthentication())) {
            return unauthorized(ex, env);
        } else {
            return GraphqlErrorBuilder.newError(env)
                .errorType(ErrorType.FORBIDDEN)
                .message(HttpStatus.FORBIDDEN.getReasonPhrase())
                .build();
        }
    }
}
