package org.example.repository;

import jakarta.enterprise.context.ApplicationScoped;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import org.example.entity.HitEntity;
import org.example.entity.UserEntity;

import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

@ApplicationScoped
public class HitRepository {

    @PersistenceContext
    private EntityManager entityManager;

    public void save(HitEntity hit) {
        entityManager.persist(hit);
    }

    public HitEntity findById(Long id) {
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
            UUID user,
            int offset,
            int limit
    ) {
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

    public List<HitEntity> findByVersion(int version, int offset, int limit) {
        return entityManager.createQuery(
                        """
                                SELECT h
                                FROM HitEntity h
                                WHERE h.areaVersion.id = :version
                                ORDER BY h.createdAt DESC
                                """,
                        HitEntity.class
                )
                .setParameter("version", version)
                .setFirstResult(offset)
                .setMaxResults(limit)
                .getResultList();
    }

    public List<HitEntity> findByTime(
            LocalDateTime from,
            LocalDateTime to,
            int offset,
            int limit
    ) {
        return entityManager.createQuery(
                        """
                                SELECT h
                                FROM HitEntity h
                                WHERE h.createdAt BETWEEN :from AND :to
                                ORDER BY h.createdAt DESC
                                """,
                        HitEntity.class
                )
                .setParameter("from", from)
                .setParameter("to", to)
                .setFirstResult(offset)
                .setMaxResults(limit)
                .getResultList();
    }

    public List<HitEntity> findByResult(
            boolean hit,
            int offset,
            int limit
    ) {
        return entityManager.createQuery(
                        """
                                SELECT h
                                FROM HitEntity h
                                WHERE h.hit = :hit
                                ORDER BY h.createdAt DESC
                                """,
                        HitEntity.class
                )
                .setParameter("hit", hit)
                .setFirstResult(offset)
                .setMaxResults(limit)
                .getResultList();
    }
}