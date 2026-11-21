package org.example.service;

import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;
import jakarta.transaction.Transactional;
import org.example.dto.HitRequestDto;
import org.example.entity.AreaVersionEntity;
import org.example.entity.UserEntity;
import org.example.figure.Area;
import org.example.figure.Point;
import org.example.repository.AreaVersionRepository;
import org.example.repository.HitRepository;
import org.example.repository.UserRepository;

import java.math.BigDecimal;
import java.util.UUID;

@ApplicationScoped
public class HitService {

    @Inject
    private HitRepository hitRepository;

    @Inject
    private AreaVersionRepository areaVersionRepository;

    @Inject
    private UserRepository userRepository;

    @Transactional
    public boolean hit(HitRequestDto hitDto, UUID userId) {

        UserEntity user = userRepository.findById(userId);

        if (user == null || !user.isEnabled()) {
            throw new IllegalStateException("User is not available");
        }

        AreaVersionEntity areaVersion = areaVersionRepository.findActive();

        if (areaVersion == null) {
            throw new IllegalStateException("Active area version not found");
        }

        BigDecimal x = hitDto.x();
        BigDecimal y = hitDto.y();
        BigDecimal r = hitDto.r();

        Area area = Area.variant(
                r,
                areaVersion.getOffsetX(),
                areaVersion.getOffsetY()
        );

        Point point = new Point(x, y);

        return area.contains(point);
    }
}