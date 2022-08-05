package io.github.shirohoo.sample.security.handler;

import com.fasterxml.jackson.databind.ObjectMapper;
import io.github.shirohoo.sample.security.web.HttpResponse;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.io.IOException;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.authentication.AuthenticationFailureHandler;

public class CustomAuthenticationFailureHandler implements AuthenticationFailureHandler {
    private final ObjectMapper objectMapper;

    public CustomAuthenticationFailureHandler(ObjectMapper objectMapper) {
        this.objectMapper = objectMapper;
    }

    @Override
    public void onAuthenticationFailure(HttpServletRequest request, HttpServletResponse response, AuthenticationException exception) throws IOException, ServletException {
        response.setCharacterEncoding("UTF-8");
        response.setStatus(HttpStatus.UNAUTHORIZED.value());
        response.setContentType(MediaType.APPLICATION_JSON_VALUE);

        String errMsg = "Email or password is invalid.";

        if (exception instanceof BadCredentialsException) {
            errMsg = "Email or password is invalid.";
        }

        HttpResponse<String> httpResponse = new HttpResponse<>(HttpStatus.UNAUTHORIZED, errMsg);
        objectMapper.writeValue(response.getWriter(), httpResponse);
    }
}
