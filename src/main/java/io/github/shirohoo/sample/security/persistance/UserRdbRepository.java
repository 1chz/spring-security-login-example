package io.github.shirohoo.sample.security.persistance;

import io.github.shirohoo.sample.security.domain.User;
import io.github.shirohoo.sample.security.domain.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

@Repository
@RequiredArgsConstructor
public class UserRdbRepository implements UserRepository {
    private final UserJpaRepository repository;

    @Override
    public User save(User user) {
        return repository.save(user);
    }

    @Override
    public User findBy(String email) {
        return repository.findByEmail(email)
                .orElseThrow();
    }
}
