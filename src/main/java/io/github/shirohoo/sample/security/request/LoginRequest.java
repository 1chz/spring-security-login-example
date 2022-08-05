package io.github.shirohoo.sample.security.request;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;
import lombok.Getter;
import org.springframework.security.crypto.password.PasswordEncoder;

public class LoginRequest {
    @Email
    @Getter
    @NotNull(message = "please enter your email.")
    private String email;

    @Getter
    @NotNull(message = "please enter your password.")
    @Pattern(
            regexp = "^[a-zA-Z0-9!@#$%^&*]{12,32}$",
            message = "The password must be 12 to 32 characters, combining alphanumeric special characters without spaces."
    )
    private String password;

    private boolean isEncoded;

    public LoginRequest() {
    }

    public LoginRequest(String email, String password) {
        this.email = email;
        this.password = password;
        this.isEncoded = false;
    }

    public LoginRequest passwordEncode(PasswordEncoder passwordEncoder) {
        if (isEncoded) {
            throw new IllegalStateException("already encoded password");
        }
        this.password = passwordEncoder.encode(this.password);
        this.isEncoded = true;
        return this;
    }
}
