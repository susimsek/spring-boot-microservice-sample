package io.github.susimsek.account.exception.security;

import com.fasterxml.jackson.databind.ObjectMapper;
import io.github.susimsek.account.exception.utils.AdviceUtils;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.io.IOException;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.HttpStatusCode;
import org.springframework.http.MediaType;
import org.springframework.http.ProblemDetail;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.AuthenticationEntryPoint;
import org.springframework.security.web.access.AccessDeniedHandler;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class SecurityProblemSupport implements AuthenticationEntryPoint, AccessDeniedHandler {

    private final ObjectMapper mapper;

    @Override
    public void commence(HttpServletRequest request, HttpServletResponse response,
                         AuthenticationException ex) throws IOException {
        var problem = ProblemDetail.forStatusAndDetail(
            HttpStatus.UNAUTHORIZED, ex.getMessage());
        var headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        AdviceUtils.setHttpResponse(
            new ResponseEntity<>(problem, headers, HttpStatusCode.valueOf(problem.getStatus())),
            response, mapper);
    }

    @Override
    public void handle(HttpServletRequest request, HttpServletResponse response,
                       AccessDeniedException ex) throws IOException {
        var problem = ProblemDetail.forStatusAndDetail(
            HttpStatus.FORBIDDEN, ex.getMessage());
        var headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        AdviceUtils.setHttpResponse(
            new ResponseEntity<>(problem, headers, HttpStatusCode.valueOf(problem.getStatus())),
            response, mapper);
    }
}