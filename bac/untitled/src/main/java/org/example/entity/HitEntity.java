package org.example.entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.math.BigDecimal;
import java.time.Instant;
import java.util.UUID;

@Getter
@Setter
@NoArgsConstructor
@Entity
@Table(
        name = "hits",
        indexes = {
                @Index(name = "idx_hits_user_created_at", columnList = "user_id, created_at"),
                @Index(name = "idx_hits_created_at", columnList = "created_at")
        },
        uniqueConstraints = {
                @UniqueConstraint(
                        name = "uk_hits_idempotency",
                        columnNames = {"user_id", "idempotency_key", "batch_index"}
                )
        }
)
public class HitEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private UUID id;

    @Column(nullable = false)
    private BigDecimal x;

    @Column(nullable = false)
    private BigDecimal y;

    @Column(nullable = false)
    private BigDecimal r;

    @Column(nullable = false)
    private boolean hit;

    @Column(nullable = false, updatable = false)
    private Instant createdAt;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "user_id", nullable = false)
    private UserEntity user;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "area_version_id", nullable = false)
    private AreaVersionEntity areaVersion;

    @Column(name = "idempotency_key", nullable = false, updatable = false)
    private UUID idempotencyKey;

    @Column(name = "batch_index", nullable = false, updatable = false)
    private int batchIndex;

    public HitEntity(
            BigDecimal x,
            BigDecimal y,
            BigDecimal r,
            boolean hit,
            UserEntity user,
            AreaVersionEntity areaVersion,
            UUID idempotencyKey,
            int batchIndex
    ) {
        this.x = x;
        this.y = y;
        this.r = r;
        this.hit = hit;
        this.user = user;
        this.areaVersion = areaVersion;
        this.idempotencyKey = idempotencyKey;
        this.batchIndex = batchIndex;
        this.createdAt = Instant.now();
    }
}