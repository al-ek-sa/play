package org.example.service;

import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;
import jakarta.transaction.Transactional;

import org.example.dto.HitRequestDto;
import org.example.entity.AreaVersionEntity;
import org.example.entity.HitEntity;
import org.example.entity.UserEntity;
import org.example.figure.Area;
import org.example.figure.Point;
import org.example.repository.AreaVersionRepository;
import org.example.repository.HitRepository;
import org.example.repository.UserRepository;
import org.example.websocket.HistoryNotificationService;

import java.math.BigDecimal;
import java.util.UUID;

@ApplicationScoped
public class HitService {

    @Inject
    private HistoryNotificationService historyNotificationService;

    @Inject
    private HitRepository hitRepository;

    @Inject
    private AreaVersionRepository areaVersionRepository;

    @Inject
    private UserRepository userRepository;

    @Transactional
    public HitResult hit(
            HitRequestDto hitDto,
            UUID userId,
            UUID idempotencyKey,
            int batchIndex
    ) {

        UserEntity user = userRepository.findById(userId);

        if (user == null || !user.isEnabled()) {
            throw new IllegalStateException(
                    "Пользователь не найден или отключён"
            );
        }

        AreaVersionEntity areaVersion = areaVersionRepository.findActive();

        if (areaVersion == null) {
            throw new IllegalStateException(
                    "Активная версия области не найдена"
            );
        }

        Area area = Area.variant(
                hitDto.r(),
                areaVersion.getOffsetX(),
                areaVersion.getOffsetY(),
                areaVersion.getRotation()
        );

        Point point = new Point(
                hitDto.x(),
                hitDto.y()
        );

        boolean hitResult = area.contains(point);

        HitEntity hit = new HitEntity(
                hitDto.x(),
                hitDto.y(),
                hitDto.r(),
                hitResult,
                user,
                areaVersion,
                idempotencyKey,
                batchIndex
        );

        hitRepository.save(hit);

        historyNotificationService.notifyHistoryUpdated(userId);

        return new HitResult(
                hitDto.x(),
                hitDto.y(),
                hitDto.r(),
                hitResult,
                areaVersion.getOffsetX(),
                areaVersion.getOffsetY(),
                areaVersion.getRotation(),
                areaVersion.getVersion(),
                areaVersion.getId()
        );
    }

    public record HitResult(
            BigDecimal x,
            BigDecimal y,
            BigDecimal r,
            boolean hit,
            BigDecimal offsetX,
            BigDecimal offsetY,
            BigDecimal rotation,
            int areaVersion,
            UUID areaVersionId
    ) {
    }
}