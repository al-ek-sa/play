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
    @Column(name = "id")
    private UUID id;

    @Column(name = "version", nullable = false, unique = true)
    private int version;

    @Column(
            name = "offset_x",
            nullable = false,
            precision = 10,
            scale = 3
    )
    private BigDecimal offsetX;

    @Column(
            name = "offset_y",
            nullable = false,
            precision = 10,
            scale = 3
    )
    private BigDecimal offsetY;

    @Column(
            name = "rotation",
            nullable = false,
            precision = 10,
            scale = 3
    )
    private BigDecimal rotation;

    @Column(name = "active", nullable = false)
    private boolean active;

    @Column(
            name = "created_at",
            nullable = false,
            updatable = false
    )
    private Instant createdAt;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "created_by")
    private UserEntity createdBy;

    public AreaVersionEntity(
            int version,
            BigDecimal offsetX,
            BigDecimal offsetY,
            BigDecimal rotation,
            UserEntity createdBy
    ) {
        this.version = version;
        this.offsetX = offsetX;
        this.offsetY = offsetY;
        this.rotation = rotation;
        this.createdBy = createdBy;
        this.active = true;
        this.createdAt = Instant.now();
    }
}