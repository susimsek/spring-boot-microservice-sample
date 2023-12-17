package io.github.susimsek.card.exception;

import graphql.GraphQLError;
import graphql.schema.DataFetchingEnvironment;
import lombok.extern.slf4j.Slf4j;
import org.springframework.graphql.data.method.annotation.GraphQlExceptionHandler;
import org.springframework.graphql.execution.ErrorType;
import org.springframework.web.bind.annotation.ControllerAdvice;

@Slf4j
@ControllerAdvice
public class GraphQLGlobalExceptionHandler  extends AbstractGraphQLExceptionHandler {

    @GraphQlExceptionHandler
    public GraphQLError handleEntityNotFound(
        EntityNotFoundException ex,
        DataFetchingEnvironment  env) {
        return handleExceptionInternal(ex, ErrorType.NOT_FOUND, env);
    }

    @GraphQlExceptionHandler
    public GraphQLError handleEntityNotFound(
        jakarta.persistence.EntityNotFoundException ex,
        DataFetchingEnvironment  env) {
        return handleExceptionInternal(ex, ErrorType.NOT_FOUND, env);
    }

    @GraphQlExceptionHandler
    public GraphQLError handleCardAlreadyExistsException(
        CardAlreadyExistsException ex,
        DataFetchingEnvironment  env) {
        return handleValidationException(env, ex);
    }

    @GraphQlExceptionHandler
    public GraphQLError handleAll(
        Exception ex,
        DataFetchingEnvironment  env) {
        return handleExceptionInternal(ex, ErrorType.INTERNAL_ERROR, env);
    }
}
