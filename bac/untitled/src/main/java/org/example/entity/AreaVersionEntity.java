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
        name = "area_versions",
        uniqueConstraints = {
                @UniqueConstraint(
                        name = "uk_area_versions_version",
                        columnNames = "version"
                )
        }
)
public class AreaVersionEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID id;

    @Column(nullable = false, unique = true)
    private int version;

    @Column(nullable = false, precision = 10, scale = 3)
    private BigDecimal offsetX;

    @Column(nullable = false, precision = 10, scale = 3)
    private BigDecimal offsetY;

    @Column(nullable = false)
    private boolean active;

    @Column(nullable = false, updatable = false)
    private Instant createdAt;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "created_by")
    private UserEntity createdBy;

    public AreaVersionEntity(
            int version,
            BigDecimal offsetX,
            BigDecimal offsetY,
            UserEntity createdBy
    ) {
        this.version = version;
        this.offsetX = offsetX;
        this.offsetY = offsetY;
        this.createdBy = createdBy;
        this.active = false;
        this.createdAt = Instant.now();
    }
}