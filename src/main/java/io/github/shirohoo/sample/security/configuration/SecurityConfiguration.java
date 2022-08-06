package io.github.shirohoo.sample.security.configuration;

import static org.springframework.http.HttpMethod.POST;
import com.fasterxml.jackson.databind.ObjectMapper;
import io.github.shirohoo.sample.security.authentication.CustomAuthenticationProvider;
import io.github.shirohoo.sample.security.domain.UserRepository;
import io.github.shirohoo.sample.security.filter.HttpLoginFilter;
import io.github.shirohoo.sample.security.handler.CustomAuthenticationFailureHandler;
import io.github.shirohoo.sample.security.handler.CustomAuthenticationSuccessHandler;
import io.github.shirohoo.sample.security.userdetails.CustomUserDetailsService;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.factory.PasswordEncoderFactories;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

@Configuration
@RequiredArgsConstructor
public class SecurityConfiguration {
    @Bean
    public SecurityFilterChain filterChain(
            HttpSecurity http,
            UserDetailsService userDetailsService,
            AuthenticationProvider authenticationProvider,
            HttpLoginFilter httpLoginFilter
    ) throws Exception {
        http
                .httpBasic().disable()
                .csrf().disable()
                .authorizeHttpRequests(authorize -> authorize
                        .mvcMatchers(POST, "/api/v1/login").permitAll()
                        .anyRequest().authenticated())
                .authenticationProvider(authenticationProvider)
                .userDetailsService(userDetailsService)
                .addFilterBefore(httpLoginFilter, UsernamePasswordAuthenticationFilter.class);

        return http.build();
    }

    @Bean
    public PasswordEncoder passwordEncoder() {
        return PasswordEncoderFactories.createDelegatingPasswordEncoder();
    }

    @Bean
    public AuthenticationManager authenticationManager(AuthenticationConfiguration authenticationConfiguration) throws Exception {
        return authenticationConfiguration.getAuthenticationManager();
    }

    @Bean
    public AuthenticationProvider authenticationProvider(PasswordEncoder passwordEncoder, UserDetailsService userDetailsService) {
        return new CustomAuthenticationProvider(passwordEncoder, userDetailsService);
    }

    @Bean
    public UserDetailsService userDetailsService(UserRepository userRepository) {
        return new CustomUserDetailsService(userRepository);
    }

    @Bean
    public HttpLoginFilter httpLoginProcessingFilter(ObjectMapper objectMapper, AuthenticationManager authenticationManager) {
        HttpLoginFilter filter = new HttpLoginFilter(objectMapper);
        filter.setAuthenticationManager(authenticationManager);
        filter.setAuthenticationSuccessHandler(new CustomAuthenticationSuccessHandler(objectMapper));
        filter.setAuthenticationFailureHandler(new CustomAuthenticationFailureHandler(objectMapper));
        return filter;
    }
}
