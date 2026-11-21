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
public class AreaVersionService {

    @Inject
    private AreaVersionRepository areaVersionRepository;

    @Inject
    private UserRepository userRepository;

    @Transactional
    public void save(AreaDTO areaDTO, UUID userId) {
        UserEntity user = userRepository.findById(userId);

        if (user == null) {
            throw new IllegalArgumentException("Пользователь не найден");
        }

        if (user.getRole() != UserStatus.ADMIN) {
            throw new SecurityException("Недостаточно прав для изменения области");
        }

        AreaVersionEntity activeArea = areaVersionRepository.findActive();

        int nextVersion = activeArea == null
                ? 1
                : activeArea.getVersion() + 1;

        AreaVersionEntity areaVersion = new AreaVersionEntity(
                nextVersion,
                areaDTO.offsetX(),
                areaDTO.offsetY(),
                areaDTO.rotation(),
                user
        );

        areaVersionRepository.save(areaVersion);
    }
}