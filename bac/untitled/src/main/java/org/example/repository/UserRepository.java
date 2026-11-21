package org.example.repository;

import jakarta.enterprise.context.ApplicationScoped;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import org.example.entity.UserEntity;

import java.util.UUID;

@ApplicationScoped
public class UserRepository {

    @PersistenceContext
    private EntityManager entityManager;

    public void save(UserEntity user) {
        entityManager.persist(user);
    }

    public UserEntity findById(UUID idUser) {
        return entityManager.find(UserEntity.class, idUser);
    }

    public UserEntity findByUserName(String userName) {
        return entityManager.createQuery(
                        "SELECT u FROM UserEntity u WHERE u.username = :username",
                        UserEntity.class
                )
                .setParameter("username", userName)
                .getResultStream()
                .findFirst()
                .orElse(null);
    }

    public boolean existsByUserName(String userName) {
        return !entityManager.createQuery(
                        "SELECT u.id FROM UserEntity u WHERE u.username = :username",
                        UUID.class
                )
                .setParameter("username", userName)
                .getResultList()
                .isEmpty();
    }
}
