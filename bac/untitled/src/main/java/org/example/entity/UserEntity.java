package org.example.entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.example.status.UserStatus;

import java.time.Instant;
import java.util.UUID;

@Getter
@Setter
@NoArgsConstructor
@Entity
@Table(
        name = "users",
        uniqueConstraints = {
                @UniqueConstraint(name = "uk_users_username", columnNames = "username")
        }
)
public class UserEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID id;

    @Column(nullable = false, length = 50)
    private String username;

    @Column(nullable = false, length = 255)
    private String passwordHash;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false, length = 20)
    private UserStatus role;

    @Column(nullable = false)
    private boolean enabled = true;

    @Column(nullable = false, updatable = false)
    private Instant createdAt;

    public UserEntity(String username, String passwordHash, UserStatus role) {
        this.username = username;
        this.passwordHash = passwordHash;
        this.role = role;
        this.createdAt = Instant.now();
    }
}