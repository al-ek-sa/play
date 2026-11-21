package org.example.repository;

import jakarta.enterprise.context.ApplicationScoped;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import org.example.entity.AreaVersionEntity;

@ApplicationScoped
public class AreaVersionRepository {

    @PersistenceContext
    private EntityManager entityManager;

    public void save(AreaVersionEntity areaVersion) {
        entityManager.persist(areaVersion);
    }

    public int getLastVersion() {
        Integer version = entityManager.createQuery(
                "SELECT MAX(a.version) FROM AreaVersionEntity a",
                Integer.class
        ).getSingleResult();

        return version == null ? 0 : version;
    }

    public AreaVersionEntity findById(Long id) {
        return entityManager.find(AreaVersionEntity.class, id);
    }

    public AreaVersionEntity findActive() {
        return entityManager.createQuery(
                        "SELECT a FROM AreaVersionEntity a " +
                                "WHERE a.active = true " +
                                "ORDER BY a.version DESC",
                        AreaVersionEntity.class
                )
                .setMaxResults(1)
                .getResultStream()
                .findFirst()
                .orElse(null);
    }
}