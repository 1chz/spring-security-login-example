package io.github.shirohoo.sample.security.web;

import lombok.Getter;
import org.springframework.http.HttpStatus;

@Getter
public class HttpResponse<T> {
    private final int status;
    private final String reason;
    private final T body;

    public HttpResponse(HttpStatus httpStatus, T body) {
        this(httpStatus.value(), httpStatus.getReasonPhrase(), body);
    }

    private HttpResponse(int status, String reason, T body) {
        this.status = status;
        this.reason = reason;
        this.body = body;
    }
}
