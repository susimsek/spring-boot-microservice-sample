package io.github.susimsek.account.exception;

import io.github.susimsek.account.dto.ErrorResponseDTO;
import jakarta.validation.ConstraintViolationException;
import jakarta.validation.Path;
import java.time.Instant;
import java.util.HashMap;
import java.util.Map;
import java.util.stream.StreamSupport;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.HttpStatusCode;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

@RestControllerAdvice
public class GlobalExceptionHandler extends ResponseEntityExceptionHandler {

    @Override
    protected ResponseEntity<Object> handleMethodArgumentNotValid(
        MethodArgumentNotValidException ex, HttpHeaders headers, HttpStatusCode status, WebRequest request) {
        Map<String, String> validationErrors = new HashMap<>();
        var validationErrorList = ex.getBindingResult().getAllErrors();

        validationErrorList.forEach((error) -> {
            String fieldName = ((FieldError) error).getField();
            String validationMsg = error.getDefaultMessage();
            validationErrors.put(fieldName, validationMsg);
        });
        return new ResponseEntity<>(validationErrors, HttpStatus.BAD_REQUEST);
    }


    @ExceptionHandler(ConstraintViolationException.class)
    protected ResponseEntity<Object> handleConstraintViolationException(ConstraintViolationException ex,
                                                                        WebRequest webRequest) {
        Map<String, String> validationErrors = new HashMap<>();
        var validationErrorList = ex.getConstraintViolations();

        validationErrorList.forEach((error) -> {
            String fieldName = getField(error.getPropertyPath());
            String validationMsg = error.getMessage();
            validationErrors.put(fieldName, validationMsg);
        });

        return new ResponseEntity<>(validationErrors, HttpStatus.BAD_REQUEST);
    }


    @ExceptionHandler(Exception.class)
    public ResponseEntity<ErrorResponseDTO> handleGlobalException(Exception exception,
                                                                  WebRequest webRequest) {
        var errorResponseDTO = new ErrorResponseDTO(
            webRequest.getDescription(false),
            HttpStatus.INTERNAL_SERVER_ERROR,
            exception.getMessage(),
            Instant.now()
        );
        return new ResponseEntity<>(errorResponseDTO, HttpStatus.INTERNAL_SERVER_ERROR);
    }

    @ExceptionHandler(ResourceNotFoundException.class)
    public ResponseEntity<ErrorResponseDTO> handleResourceNotFoundException(ResourceNotFoundException exception,
                                                                            WebRequest webRequest) {
        var errorResponseDTO = new ErrorResponseDTO(
            webRequest.getDescription(false),
            HttpStatus.NOT_FOUND,
            exception.getMessage(),
            Instant.now()
        );
        return new ResponseEntity<>(errorResponseDTO, HttpStatus.NOT_FOUND);
    }


    @ExceptionHandler(CustomerAlreadyExistsException.class)
    public ResponseEntity<ErrorResponseDTO> handleCustomerAlreadyExistsException(
        CustomerAlreadyExistsException exception,
        WebRequest webRequest) {
        var errorResponseDTO = new ErrorResponseDTO(
            webRequest.getDescription(false),
            HttpStatus.BAD_REQUEST,
            exception.getMessage(),
            Instant.now()
        );
        return new ResponseEntity<>(errorResponseDTO, HttpStatus.BAD_REQUEST);
    }

    private String getField(Path propertyPath) {
        var node = StreamSupport.stream(
                propertyPath.spliterator(), false)
            .reduce((first, second) -> second)
            .orElse(null);
        return node != null ? node.toString() : "";
    }

}