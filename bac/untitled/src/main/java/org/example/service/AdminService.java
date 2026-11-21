package org.example.service;

import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;
import jakarta.transaction.Transactional;
import org.example.dto.AreaDTO;
import org.example.entity.AreaVersionEntity;
import org.example.entity.UserEntity;
import org.example.repository.AreaVersionRepository;
import org.example.repository.UserRepository;
import org.example.status.UserStatus;

import java.util.UUID;

@ApplicationScoped
public class AdminService {

    @Inject
    private AreaVersionRepository areaVersionRepository;

    @Inject
    private UserRepository userRepository;

    @Transactional
    public void saveArea(AreaDTO areaDTO, UUID userId) {

        UserEntity userEntity = userRepository.findById(userId);

        if (userEntity == null) {
            throw new IllegalArgumentException("Пользователь не найден");
        }

        if (userEntity.getRole() != UserStatus.ADMIN) {
            throw new SecurityException("Недостаточно прав");
        }

        int version = areaVersionRepository.getLastVersion() + 1;

        AreaVersionEntity areaVersion = new AreaVersionEntity(
                version,
                areaDTO.offsetX(),
                areaDTO.offsetY(),
                areaDTO.rotation(),
                userEntity
        );

        areaVersionRepository.save(areaVersion);
    }
}