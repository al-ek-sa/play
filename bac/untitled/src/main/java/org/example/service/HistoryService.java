package org.example.service;

import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;
import org.apache.commons.csv.CSVFormat;
import org.apache.commons.csv.CSVPrinter;
import org.example.entity.HitEntity;
import org.example.entity.UserEntity;
import org.example.repository.HitRepository;
import org.example.repository.UserRepository;
import org.example.status.UserStatus;

import java.io.IOException;
import java.io.Writer;
import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

@ApplicationScoped
public class HistoryService {

    @Inject
    private HitRepository hitRepository;

    @Inject
    private UserRepository userRepository;

    /**
     * Получение истории по версии.
     */
    public long findByVersion(int version, int offset, int limit, UUID userId, Writer writer) throws IOException {

        checkUser(userId);

        List<HitEntity> hitEntityList = hitRepository.findByVersion(userId, version, offset, limit);

        parseCsv(hitEntityList, writer);

        return hitRepository.countByVersion(userId, version);
    }

    /**
     * Получение истории по результату.
     */
    public long findByResult(boolean hit, int offset, int limit, UUID userId, Writer writer) throws IOException {

        checkUser(userId);

        List<HitEntity> hitEntityList = hitRepository.findByResult(userId, hit, offset, limit);

        parseCsv(hitEntityList, writer);

        return hitRepository.countByResult(userId, hit);
    }

    /**
     * Получение истории по времени
     */
    public long findByTime(LocalDateTime from, LocalDateTime to, int offset, int limit, UUID userId, Writer writer) throws IOException {

        checkUser(userId);

        List<HitEntity> hitEntityList = hitRepository.findByTime(userId, from, to, offset, limit);

        parseCsv(hitEntityList, writer);

        return hitRepository.countByTime(userId, from, to);
    }

    private void checkUser(UUID userId) {
        UserEntity user = userRepository.findById(userId);

        if (user == null) {
            throw new IllegalArgumentException("User not found");
        }

        if(user.getRole() == UserStatus.ADMIN) {
            throw new IllegalArgumentException("Invalid role");
        }
    }

    /**
     * csv
     */
    private void parseCsv(List<HitEntity> hitEntityList, Writer writer)
            throws IOException {

        try (CSVPrinter csvPrinter = new CSVPrinter(writer, CSVFormat.DEFAULT)) {

            csvPrinter.printRecord(
                    "X",
                    "Y",
                    "R",
                    "Result",
                    "Created At",
                    "Area Version"
            );

            for (HitEntity hit : hitEntityList) {
                csvPrinter.printRecord(
                        hit.getX(),
                        hit.getY(),
                        hit.getR(),
                        hit.isHit() ? "HIT" : "MISS",
                        hit.getCreatedAt(),
                        hit.getAreaVersion().getId()
                );
            }

            csvPrinter.flush();
        }
    }
}