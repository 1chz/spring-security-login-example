package io.github.shirohoo.sample.security.web;

import static lombok.AccessLevel.PRIVATE;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor(access = PRIVATE)
public class LoginRequest {
    @Email
    @NotNull(message = "please enter your email.")
    private String email;

    @NotNull(message = "please enter your password.")
    @Pattern(
            regexp = "^[a-zA-Z0-9!@#$%^&*]{12,32}$",
            message = "The password must be 12 to 32 characters, combining alphanumeric special characters without spaces."
    )
    private String password;

    public LoginRequest(String email, String password) {
        this.email = email;
        this.password = password;
    }

    public boolean isEmptyContents() {
        return email == null || email.isBlank() ||
                password == null || password.isBlank();
    }
}
