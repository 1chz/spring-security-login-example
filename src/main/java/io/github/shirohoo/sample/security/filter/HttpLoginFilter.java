package io.github.shirohoo.sample.security.filter;

import com.fasterxml.jackson.databind.ObjectMapper;
import io.github.shirohoo.sample.security.authentication.HttpAuthenticationToken;
import io.github.shirohoo.sample.security.request.LoginRequest;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.io.IOException;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.authentication.AbstractAuthenticationProcessingFilter;
import org.springframework.security.web.util.matcher.AntPathRequestMatcher;
import org.springframework.util.StringUtils;

/**
 * "/api/v1/login" 로 요청이 들어올 경우 이 필터가 동작합니다.
 */
public class HttpLoginFilter extends AbstractAuthenticationProcessingFilter {
    private final ObjectMapper objectMapper;

    public HttpLoginFilter(ObjectMapper objectMapper) {
        super(new AntPathRequestMatcher("/api/v1/login"));
        this.objectMapper = objectMapper;
    }

    @Override
    public Authentication attemptAuthentication(HttpServletRequest request, HttpServletResponse response) throws AuthenticationException, IOException, ServletException {
        if (!isJson(request)) {
            throw new IllegalStateException("request content format is not application/json");
        }

        LoginRequest body = objectMapper.readValue(request.getReader(), LoginRequest.class);

        if (!StringUtils.hasText(body.getEmail()) || !StringUtils.hasText(body.getPassword())) {
            throw new IllegalStateException("no email or password entered.");
        }

        HttpAuthenticationToken token = HttpAuthenticationToken.unauthenticated(body.getEmail(), body.getPassword());
        return getAuthenticationManager().authenticate(token);
    }

    private boolean isJson(HttpServletRequest request) {
        return "application/json".equals(request.getHeader("Content-Type"));
    }
}
