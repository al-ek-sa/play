package org.example.authentication.db;

import org.example.authentication.jpa.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.UUID;

@Repository
public interface UserRepository extends JpaRepository<User, UUID> {
    boolean existsByLogin(String login);
    boolean existsByEmail(String email);
}
