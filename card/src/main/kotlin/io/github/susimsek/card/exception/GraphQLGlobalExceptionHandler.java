package io.github.susimsek.card.exception;

import static io.github.susimsek.card.exception.ErrorConstants.ERR_INTERNAL_SERVER;

import graphql.GraphQLError;
import graphql.GraphqlErrorBuilder;
import graphql.schema.DataFetchingEnvironment;
import lombok.extern.slf4j.Slf4j;
import org.springframework.graphql.data.method.annotation.GraphQlExceptionHandler;
import org.springframework.graphql.execution.ErrorType;
import org.springframework.web.bind.annotation.ControllerAdvice;

@Slf4j
@ControllerAdvice
public class GraphQLGlobalExceptionHandler {

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

    protected  GraphQLError handleValidationException(DataFetchingEnvironment env, Exception ex) {
        return handleExceptionInternal(ex, ErrorType.BAD_REQUEST, env );
    }

    protected GraphQLError handleExceptionInternal(Exception ex,
                                                             ErrorType errorType,
                                                             DataFetchingEnvironment env) {
        if (errorType == ErrorType.INTERNAL_ERROR) {
            log.error("An exception occured, which will cause response", ex);
            return buildGraphQLError(env, ERR_INTERNAL_SERVER,  errorType);
        } else if (errorType == ErrorType.BAD_REQUEST) {
            log.warn("An exception occured, which will cause response", ex);
        } else {
            log.debug("An exception occured, which will cause response", ex);
        }
        return buildGraphQLError(env, ex.getMessage(),  errorType);
    }

    private GraphQLError buildGraphQLError(
        DataFetchingEnvironment env,
        String errorMessage,
        ErrorType errorType) {
        return GraphqlErrorBuilder.newError(env)
            .message(errorMessage)
            .errorType(errorType).build();
    }
}
