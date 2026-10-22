package org.example.auth.bd;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface BD extends JpaRepository<User, Long> {
    Optional<User> findByLoginAndPassword(String login, String password);

    boolean existsByLoginAndPassword(String login, String password);
}