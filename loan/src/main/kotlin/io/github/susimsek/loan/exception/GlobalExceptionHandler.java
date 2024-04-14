package io.github.susimsek.loan.exception;

import static io.github.susimsek.loan.exception.ErrorConstants.ERR_INTERNAL_SERVER;
import static io.github.susimsek.loan.exception.ErrorConstants.ERR_VALIDATION;
import static io.github.susimsek.loan.exception.ErrorConstants.PROBLEM_VIOLATION_KEY;
import static java.util.stream.Collectors.toList;

import io.github.susimsek.loan.dto.Violation;
import jakarta.validation.ConstraintViolationException;
import java.util.stream.Stream;
import lombok.extern.slf4j.Slf4j;
import lombok.val;
import org.springframework.core.Ordered;
import org.springframework.core.annotation.Order;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.HttpStatusCode;
import org.springframework.http.ProblemDetail;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

@Order(Ordered.HIGHEST_PRECEDENCE)
@Slf4j
@RestControllerAdvice
public class GlobalExceptionHandler extends ResponseEntityExceptionHandler {

    @Override
    protected ResponseEntity<Object> handleMethodArgumentNotValid(
        MethodArgumentNotValidException ex, HttpHeaders headers, HttpStatusCode status, WebRequest request) {
        var problem = ProblemDetail.forStatusAndDetail(
            HttpStatus.BAD_REQUEST, ERR_VALIDATION);
        var fieldErrors = ex.getFieldErrors().stream().map(Violation::new);
        var globalErrors = ex.getGlobalErrors().stream().map(Violation::new);
        var violations = Stream.concat(fieldErrors, globalErrors).collect(toList());
        problem.setProperty(PROBLEM_VIOLATION_KEY, violations);
        return handleExceptionInternal(ex, problem, headers,
            HttpStatusCode.valueOf(problem.getStatus()), request);
    }


    @ExceptionHandler(ConstraintViolationException.class)
    protected ResponseEntity<Object> handleConstraintViolationException(ConstraintViolationException ex,
                                                                        WebRequest request) {
        var problem = ProblemDetail.forStatusAndDetail(
            HttpStatus.BAD_REQUEST, ERR_VALIDATION);
        val violations = ex.getConstraintViolations().stream().map(Violation::new);
        problem.setProperty(PROBLEM_VIOLATION_KEY, violations);

        return handleExceptionInternal(ex, problem, null,
            HttpStatusCode.valueOf(problem.getStatus()), request);
    }


    @ExceptionHandler(EntityNotFoundException.class)
    public ResponseEntity<Object> handleEntityNotFound(
        EntityNotFoundException ex,
        WebRequest webRequest) {
        var problem = ProblemDetail.forStatusAndDetail(
            HttpStatus.NOT_FOUND, ex.getMessage());
        return handleExceptionInternal(ex, problem, null,
            HttpStatusCode.valueOf(problem.getStatus()), webRequest);
    }

    @ExceptionHandler(jakarta.persistence.EntityNotFoundException.class)
    protected ResponseEntity<Object> handleEntityNotFound(
        jakarta.persistence.EntityNotFoundException ex,
        WebRequest webRequest) {
        var problem = ProblemDetail.forStatusAndDetail(
            HttpStatus.NOT_FOUND, ex.getMessage());
        return handleExceptionInternal(ex, problem, null,
            HttpStatusCode.valueOf(problem.getStatus()), webRequest);
    }

    @ExceptionHandler(LoanAlreadyExistsException.class)
    public ResponseEntity<Object> handleLoanAlreadyExistsException(
        LoanAlreadyExistsException ex,
        WebRequest webRequest) {
        var problem = ProblemDetail.forStatusAndDetail(
            HttpStatus.BAD_REQUEST, ex.getMessage());
        return handleExceptionInternal(ex, problem, null,
            HttpStatusCode.valueOf(problem.getStatus()), webRequest);
    }

    @ExceptionHandler(Exception.class)
    public ResponseEntity<Object> handleAll(
        Exception ex,
        WebRequest webRequest) {
        var problem = ProblemDetail.forStatusAndDetail(
            HttpStatus.INTERNAL_SERVER_ERROR, ex.getMessage());
        return handleExceptionInternal(ex, problem, null,
            HttpStatusCode.valueOf(problem.getStatus()), webRequest);
    }

    @Override
    protected ResponseEntity<Object> handleExceptionInternal(Exception ex,
                                                             Object body, HttpHeaders headers,
                                                             HttpStatusCode status, WebRequest request) {
        if (status.is5xxServerError()) {
            log.error("An exception occured, which will cause a {} response", status, ex);
            var problem = ProblemDetail.forStatusAndDetail(status, ERR_INTERNAL_SERVER);
            return super.handleExceptionInternal(ex, problem, headers, status, request);
        } else if (status.is4xxClientError()) {
            log.warn("An exception occured, which will cause a {} response", status, ex);
        } else {
            log.debug("An exception occured, which will cause a {} response", status, ex);
        }
        return super.handleExceptionInternal(ex, body, headers, status, request);
    }
}