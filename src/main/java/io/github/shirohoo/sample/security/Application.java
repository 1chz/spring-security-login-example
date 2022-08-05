package io.github.shirohoo.sample.security;

import io.github.shirohoo.sample.security.domain.User;
import io.github.shirohoo.sample.security.domain.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
@RequiredArgsConstructor
public class Application implements CommandLineRunner {
    private final UserRepository userRepository;

    public static void main(String[] args) {
        SpringApplication.run(Application.class, args);
    }

    @Override
    public void run(String... args) throws Exception {
        User user = new User("siro@gmail.com", "{bcrypt}$2a$10$1r7iIazckvHAq3CHGB4YDesJ5h/4gvv4n8ZHzmzr.ey6495c/AMKq");
        userRepository.save(user);
    }
}
