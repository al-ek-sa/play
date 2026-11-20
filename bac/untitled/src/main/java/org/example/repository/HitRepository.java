package org.example.repository;

import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import org.example.entity.HitEntity;
import org.example.entity.UserEntity;

import java.util.List;

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
            UserEntity user,
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
}