package org.example.service;

import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;
import jakarta.transaction.Transactional;
import jakarta.validation.ConstraintViolation;
import jakarta.validation.Validator;
import org.example.dto.HitRequestDto;
import org.example.entity.AreaVersionEntity;
import org.example.entity.HitEntity;
import org.example.entity.UserEntity;
import org.example.figure.Area;
import org.example.figure.Point;
import org.example.repository.AreaVersionRepository;
import org.example.repository.HitRepository;
import org.example.repository.UserRepository;
import org.example.status.UserStatus;

import java.math.BigDecimal;
import java.util.List;
import java.util.Set;
import java.util.UUID;

@ApplicationScoped
public class HitPackageService {

    private static final int MAX_BATCH_SIZE = 500;

    @Inject
    private UserRepository userRepository;

    @Inject
    private HitRepository hitRepository;

    @Inject
    private Validator validator;

    @Inject
    private AreaVersionRepository areaVersionRepository;

    @Transactional
    public void save(
            List<HitRequestDto> list,
            UUID userId,
            UUID idempotencyKey,
            int batchIndex
    ) {
        validateBatchSize(list);

        UserEntity user = userRepository.findById(userId);

        if (user == null || !user.isEnabled()) {
            throw new IllegalStateException("Пользователь не найден");
        }

        if (user.getRole() == UserStatus.ADMIN) {
            throw new IllegalStateException(
                    "Нет прав пользовательского аккаунта"
            );
        }

        AreaVersionEntity areaVersion = areaVersionRepository.findActive();

        if (areaVersion == null) {
            throw new IllegalStateException(
                    "Активная версия области не найдена"
            );
        }

        validateAll(list);

        for (int i = 0; i < list.size(); i++) {
            HitRequestDto hit = list.get(i);

            int currentBatchIndex = batchIndex + i;

            HitEntity entity = createHit(
                    hit,
                    user,
                    areaVersion,
                    idempotencyKey,
                    currentBatchIndex
            );

            hitRepository.save(entity);
        }
    }

    private void validateBatchSize(List<HitRequestDto> list) {
        if (list == null || list.isEmpty()) {
            throw new IllegalArgumentException(
                    "Batch не может быть пустым"
            );
        }

        if (list.size() > MAX_BATCH_SIZE) {
            throw new IllegalArgumentException(
                    "Batch не может содержать больше "
                            + MAX_BATCH_SIZE + " точек"
            );
        }
    }

    private void validateAll(List<HitRequestDto> list) {
        for (HitRequestDto hit : list) {
            if (hit == null) {
                throw new IllegalArgumentException(
                        "Batch содержит пустую точку"
                );
            }

            Set<ConstraintViolation<HitRequestDto>> violations =
                    validator.validate(hit);

            if (!violations.isEmpty()) {
                throw new IllegalArgumentException(
                        "Ошибка валидации точки. "
                                + "Транзакция отменена"
                );
            }
        }
    }

    private HitEntity createHit(
            HitRequestDto hit,
            UserEntity user,
            AreaVersionEntity areaVersion,
            UUID idempotencyKey,
            int batchIndex
    ) {
        BigDecimal r = hit.r();
        BigDecimal x = hit.x();
        BigDecimal y = hit.y();

        Area area = Area.variant(
                r,
                areaVersion.getOffsetX(),
                areaVersion.getOffsetY(),
                areaVersion.getRotation()
        );

        Point point = new Point(x, y);
        boolean result = area.contains(point);

        return new HitEntity(
                x,
                y,
                r,
                result,
                user,
                areaVersion,
                idempotencyKey,
                batchIndex
        );
    }
}