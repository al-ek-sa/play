package org.example.service;

import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;
import jakarta.transaction.Transactional;
import org.example.dto.HitRequestDto;
import org.example.entity.AreaVersionEntity;
import org.example.entity.HitEntity;
import org.example.entity.UserEntity;
import org.example.figure.Area;
import org.example.figure.Coordinate;
import org.example.figure.Point;
import org.example.figure.Valid;
import org.example.repository.AreaVersionRepository;
import org.example.repository.HitRepository;
import org.example.repository.UserRepository;
import org.example.validation.PointValidator;

import java.util.HashMap;
import java.util.Map;
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
    public Valid<Boolean> hit(
            HitRequestDto hitDto,
            UUID userId,
            UUID idempotencyKey,
            int batchIndex
    ) {

        UserEntity user = userRepository.findById(userId);

        if (user == null || !user.isEnabled()) {
            throw new IllegalStateException("User is not available");
        }

        Map<String, String> params = new HashMap<>();

        params.put("x", hitDto.x() == null ? "" : hitDto.x().toString());
        params.put("y", hitDto.y() == null ? "" : hitDto.y().toString());
        params.put("r", hitDto.r() == null ? "" : hitDto.r().toString());

        Valid<Coordinate> validation = PointValidator.validate(params);

        if (validation instanceof Valid.Failure<Coordinate>(java.util.List<org.example.figure.FieldError> errors)) {
            return new Valid.Failure<>(errors);
        }

        Coordinate coordinate =
                ((Valid.Success<Coordinate>) validation).value();

        AreaVersionEntity areaVersion = areaVersionRepository.findActive();

        if (areaVersion == null) {
            throw new IllegalStateException("Active area version not found");
        }

        Area area = Area.variant(
                coordinate.r(),
                areaVersion.getOffsetX(),
                areaVersion.getOffsetY(),
                areaVersion.getRotation()
        );

        Point point = new Point(
                coordinate.x(),
                coordinate.y()
        );

        boolean result = area.contains(point);

        HitEntity hit = new HitEntity(
                coordinate.x(),
                coordinate.y(),
                coordinate.r(),
                result,
                user,
                areaVersion,
                idempotencyKey,
                batchIndex
        );

        hitRepository.save(hit);

        return new Valid.Success<>(result);
    }
}