package com.example.demo.blocking.entity;

import jakarta.persistence.Entity;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import jakarta.persistence.*;

import java.time.Instant;
import java.util.Objects;
import java.util.UUID;

@Entity
@Table(
        name = "blocks",
        uniqueConstraints = {
                @UniqueConstraint(
                        name = "uk_block_pair",
                        columnNames = {"blocker_id", "blocked_id"}
                )
        },
        indexes = {
                @Index(name = "idx_blocker_id", columnList = "blocker_id"),
                @Index(name = "idx_blocked_id", columnList = "blocked_id")
        }
)
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Block {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID id;

    @Column(name = "blocker_id", nullable = false, updatable = false)
    private UUID blockerId;

    @Column(name = "blocked_id", nullable = false, updatable = false)
    private UUID blockedId;

    @Column(name = "created_at", nullable = false, updatable = false)
    private Instant createdAt;

    public Block(UUID blockerId, UUID blockedId) {
        this.blockerId = Objects.requireNonNull(blockerId);
        this.blockedId = Objects.requireNonNull(blockedId);

        if (blockerId.equals(blockedId)) {
            throw new IllegalArgumentException("Cannot block yourself");
        }
    }

    @PrePersist
    protected void onCreate() {
        this.createdAt = Instant.now();
    }
}