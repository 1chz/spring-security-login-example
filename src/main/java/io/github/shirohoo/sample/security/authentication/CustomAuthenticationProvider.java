package io.github.shirohoo.sample.security.authentication;

import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.password.PasswordEncoder;

@RequiredArgsConstructor
public final class CustomAuthenticationProvider implements AuthenticationProvider {
    private final PasswordEncoder passwordEncoder;
    private final UserDetailsService userDetailsService;

    @Override
    public Authentication authenticate(Authentication authentication) throws AuthenticationException {
        UserDetails user = userDetailsService.loadUserByUsername(authentication.getName());

        String dbPassword = user.getPassword();
        String enteredPassword = (String) authentication.getCredentials();

        if (!passwordEncoder.matches(enteredPassword, dbPassword)) {
            throw new BadCredentialsException("password not matched.");
        }

        if (isHttpAuthenticationToken(authentication.getClass())) {
            return HttpAuthenticationToken.authenticated(user.getUsername(), null, user.getAuthorities());
        }

        return new UsernamePasswordAuthenticationToken(user.getUsername(), null, user.getAuthorities());
    }

    @Override
    public boolean supports(Class<?> authentication) {
        return isFormLoginAuthenticationToken(authentication) || isHttpAuthenticationToken(authentication);
    }

    private boolean isFormLoginAuthenticationToken(Class<?> authentication) {
        return UsernamePasswordAuthenticationToken.class.isAssignableFrom(authentication);
    }

    private boolean isHttpAuthenticationToken(Class<?> authentication) {
        return HttpAuthenticationToken.class.isAssignableFrom(authentication);
    }
}
