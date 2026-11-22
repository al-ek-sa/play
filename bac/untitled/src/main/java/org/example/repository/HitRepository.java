package org.example.repository;

import jakarta.enterprise.context.ApplicationScoped;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import org.example.entity.HitEntity;
import org.example.entity.UserEntity;

import java.time.Instant;
import java.util.List;
import java.util.UUID;
import java.util.stream.Stream;

@ApplicationScoped
public class HitRepository {

    @PersistenceContext
    private EntityManager entityManager;

    public void save(HitEntity hit) {
        entityManager.persist(hit);
    }

    public HitEntity findById(UUID id) {
        return entityManager.find(HitEntity.class, id);
    }

    public List<HitEntity> findAllByUser(UserEntity user) {
        return entityManager.createQuery(
                        """
                        SELECT h
                        FROM HitEntity h
                        WHERE h.user = :user
                        ORDER BY h.createdAt DESC
                        """,
                        HitEntity.class
                )
                .setParameter("user", user)
                .getResultList();
    }

    public List<HitEntity> findAll() {
        return entityManager.createQuery(
                        """
                        SELECT h
                        FROM HitEntity h
                        ORDER BY h.createdAt DESC
                        """,
                        HitEntity.class
                )
                .getResultList();
    }

    public List<HitEntity> findByUser(
            UUID userId,
            int offset,
            int limit
    ) {
        return entityManager.createQuery(
                        """
                        SELECT h
                        FROM HitEntity h
                        WHERE h.user.id = :userId
                        ORDER BY h.createdAt DESC
                        """,
                        HitEntity.class
                )
                .setParameter("userId", userId)
                .setFirstResult(offset)
                .setMaxResults(limit)
                .getResultList();
    }

    public List<HitEntity> findAll(int offset, int limit) {
        return entityManager.createQuery(
                        """
                        SELECT h
                        FROM HitEntity h
                        ORDER BY h.createdAt DESC
                        """,
                        HitEntity.class
                )
                .setFirstResult(offset)
                .setMaxResults(limit)
                .getResultList();
    }

    public long countByUser(UserEntity user) {
        return entityManager.createQuery(
                        """
                        SELECT COUNT(h)
                        FROM HitEntity h
                        WHERE h.user = :user
                        """,
                        Long.class
                )
                .setParameter("user", user)
                .getSingleResult();
    }

    public long countAll() {
        return entityManager.createQuery(
                        """
                        SELECT COUNT(h)
                        FROM HitEntity h
                        """,
                        Long.class
                )
                .getSingleResult();
    }

    /**
     * для истории методы и пагинации (по тз время, версия фигуры и результат)
     */
    public Stream<HitEntity> findByVersion(
            UUID userId,
            int version,
            int offset,
            int limit
    ) {
        return entityManager.createQuery(
                        """
                        SELECT h
                        FROM HitEntity h
                        WHERE h.user.id = :userId
                          AND h.areaVersion.version = :version
                        ORDER BY h.createdAt DESC
                        """,
                        HitEntity.class
                )
                .setParameter("userId", userId)
                .setParameter("version", version)
                .setFirstResult(offset)
                .setMaxResults(limit)
                .getResultStream();
    }

    public Stream<HitEntity> findByTime(
            UUID userId,
            Instant from,
            Instant to,
            int offset,
            int limit
    ) {
        return entityManager.createQuery(
                        """
                        SELECT h
                        FROM HitEntity h
                        WHERE h.user.id = :userId
                          AND h.createdAt >= :from
                          AND h.createdAt <= :to
                        ORDER BY h.createdAt DESC
                        """,
                        HitEntity.class
                )
                .setParameter("userId", userId)
                .setParameter("from", from)
                .setParameter("to", to)
                .setFirstResult(offset)
                .setMaxResults(limit)
                .getResultStream();
    }

    public Stream<HitEntity> findByResult(
            UUID userId,
            boolean hit,
            int offset,
            int limit
    ) {
        return entityManager.createQuery(
                        """
                        SELECT h
                        FROM HitEntity h
                        WHERE h.user.id = :userId
                          AND h.hit = :hit
                        ORDER BY h.createdAt DESC
                        """,
                        HitEntity.class
                )
                .setParameter("userId", userId)
                .setParameter("hit", hit)
                .setFirstResult(offset)
                .setMaxResults(limit)
                .getResultStream();
    }

    public long countByVersion(UUID userId, int version) {
        return entityManager.createQuery(
                        """
                        SELECT COUNT(h)
                        FROM HitEntity h
                        WHERE h.user.id = :userId
                          AND h.areaVersion.version = :version
                        """,
                        Long.class
                )
                .setParameter("userId", userId)
                .setParameter("version", version)
                .getSingleResult();
    }

    public long countByResult(UUID userId, boolean hit) {
        return entityManager.createQuery(
                        """
                        SELECT COUNT(h)
                        FROM HitEntity h
                        WHERE h.user.id = :userId
                          AND h.hit = :hit
                        """,
                        Long.class
                )
                .setParameter("userId", userId)
                .setParameter("hit", hit)
                .getSingleResult();
    }

    public long countByTime(
            UUID userId,
            Instant from,
            Instant to
    ) {
        return entityManager.createQuery(
                        """
                        SELECT COUNT(h)
                        FROM HitEntity h
                        WHERE h.user.id = :userId
                          AND h.createdAt >= :from
                          AND h.createdAt <= :to
                        """,
                        Long.class
                )
                .setParameter("userId", userId)
                .setParameter("from", from)
                .setParameter("to", to)
                .getSingleResult();
    }
}