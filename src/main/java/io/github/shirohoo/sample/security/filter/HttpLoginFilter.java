package io.github.shirohoo.sample.security.filter;

import static org.springframework.http.MediaType.APPLICATION_JSON_VALUE;
import com.fasterxml.jackson.databind.ObjectMapper;
import io.github.shirohoo.sample.security.authentication.HttpAuthenticationToken;
import io.github.shirohoo.sample.security.web.LoginRequest;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.io.IOException;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.authentication.AbstractAuthenticationProcessingFilter;
import org.springframework.security.web.util.matcher.AntPathRequestMatcher;

/**
 * "/api/v1/login" 로 요청이 들어올 경우 이 필터가 동작합니다.
 */
public class HttpLoginFilter extends AbstractAuthenticationProcessingFilter {
    private final ObjectMapper objectMapper;

    public HttpLoginFilter(ObjectMapper objectMapper) {
        super(new AntPathRequestMatcher(
                /*default path to verify*/ "/api/v1/login"));
        this.objectMapper = objectMapper;
    }

    @Override
    public Authentication attemptAuthentication(HttpServletRequest request, HttpServletResponse response) throws AuthenticationException, IOException, ServletException {
        if (!isApplicationJson(request)) {
            throw new IllegalStateException("request content format is not application/json");
        }

        LoginRequest body = objectMapper.readValue(request.getReader(), LoginRequest.class);

        if (body.isEmptyContents()) {
            throw new IllegalStateException("no email or password entered.");
        }

        HttpAuthenticationToken token = HttpAuthenticationToken.unauthenticated(body.getEmail(), body.getPassword());
        return getAuthenticationManager().authenticate(token);
    }

    private boolean isApplicationJson(HttpServletRequest request) {
        return APPLICATION_JSON_VALUE.equals(request.getContentType());
    }
}
