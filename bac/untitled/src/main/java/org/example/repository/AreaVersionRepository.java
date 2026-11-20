package org.example.repository;

import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import org.example.entity.AreaVersionEntity;

public class AreaVersionRepository {

    @PersistenceContext
    private EntityManager entityManager;

    public void save(AreaVersionEntity areaVersion) {
        entityManager.persist(areaVersion);
    }

    public AreaVersionEntity findById(Long id) {
        return entityManager.find(AreaVersionEntity.class, id);
    }

    public AreaVersionEntity findActive() {
        return entityManager.createQuery(
                        "SELECT a FROM AreaVersionEntity a WHERE a.active = true",
                        AreaVersionEntity.class
                )
                .getResultStream()
                .findFirst()
                .orElse(null);
    }
}