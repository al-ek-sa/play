package org.example.service;

import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;
import jakarta.transaction.Transactional;
import org.example.entity.UserEntity;
import org.example.repository.UserRepository;
import org.example.status.PasswordHasher;
import org.example.status.UserStatus;

import java.util.UUID;

/**
 * Сервис для регистрации пользователя и входа в систему.
 */
//todo удаление аккаунта
@ApplicationScoped
public class AuthService {

    @Inject
    private UserRepository userRepository;

    @Inject
    private PasswordHasher hasher;

    //todo сравнение по имени пользователя -
    public UserEntity login(String userName, String password){
        UserEntity user = userRepository.findByUserName(userName);

        if (user == null) {
            throw new IllegalArgumentException("Invalid username or password");
        }

        if (!user.isEnabled()) {
            throw new IllegalArgumentException("Account is disabled");
        }

        if (!hasher.matches(password, user.getPasswordHash())) {
            throw new IllegalArgumentException("Invalid username or password");
        }

        return user;
    }

    @Transactional
    public UserEntity register(String userName, String password) {
        if (userRepository.existsByUserName(userName)) {
            throw new IllegalArgumentException("Username already exists");
        }

        UserEntity user = new UserEntity(userName, hasher.hash(password), UserStatus.USER);

        userRepository.save(user);

        return user;
    }

    public UserEntity findById(UUID id) {
        return userRepository.findById(id);
    }
}
