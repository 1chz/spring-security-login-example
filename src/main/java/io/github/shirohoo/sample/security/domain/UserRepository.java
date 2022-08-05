package io.github.shirohoo.sample.security.domain;

public interface UserRepository {
    User save(User user);

    User findBy(String email);
}
