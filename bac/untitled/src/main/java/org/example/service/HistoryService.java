package org.example.service;

import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;
import jakarta.transaction.Transactional;
import org.apache.commons.csv.CSVFormat;
import org.apache.commons.csv.CSVPrinter;
import org.example.entity.HitEntity;
import org.example.entity.UserEntity;
import org.example.repository.HitRepository;
import org.example.repository.UserRepository;
import org.example.status.UserStatus;

import java.io.IOException;
import java.io.UncheckedIOException;
import java.io.Writer;
import java.time.LocalDateTime;
import java.util.UUID;
import java.util.stream.Stream;

@ApplicationScoped
public class HistoryService {

    @Inject
    private HitRepository hitRepository;

    @Inject
    private UserRepository userRepository;

    /**
     * Получение истории по версии области
     */
    @Transactional
    public long findByVersion(int version, int offset, int limit, UUID userId, Writer writer) throws IOException {

        checkUser(userId);

        try (Stream<HitEntity> hits = hitRepository.findByVersion(userId, version, offset, limit)) {
            parseCsv(hits, writer);
        }

        return hitRepository.countByVersion(userId, version);
    }

    /**
     * Получение истории по результату с пагинацией.
     */
    @Transactional
    public long findByResult(boolean hit, int offset, int limit, UUID userId, Writer writer) throws IOException {

        checkUser(userId);

        try (Stream<HitEntity> hits = hitRepository.findByResult(userId, hit, offset, limit)) {
            parseCsv(hits, writer);
        }

        return hitRepository.countByResult(userId, hit);
    }

    /**
     * Получение истории за временной интервал с пагинацией.
     */
    @Transactional
    public long findByTime(LocalDateTime from, LocalDateTime to, int offset, int limit, UUID userId, Writer writer) throws IOException {

        checkUser(userId);

        try (Stream<HitEntity> hits = hitRepository.findByTime(userId, from, to, offset, limit)) {
            parseCsv(hits, writer);
        }

        return hitRepository.countByTime(userId, from, to);
    }

    /**
     * Проверка пользователя и его роли.
     */
    private void checkUser(UUID userId) {

        UserEntity user = userRepository.findById(userId);

        if (user == null) {
            throw new IllegalArgumentException("User not found");
        }

        if (user.getRole() == UserStatus.ADMIN) {
            throw new IllegalArgumentException("Invalid role");
        }
    }

    /**
     * Формирование CSV из потока записей.
     */
    private void parseCsv(
            Stream<HitEntity> hits,
            Writer writer
    ) throws IOException {

        CSVPrinter csvPrinter = new CSVPrinter(writer, CSVFormat.DEFAULT);

        csvPrinter.printRecord(
                "X",
                "Y",
                "R",
                "Result",
                "Created At",
                "Area Version"
        );

        try {
            hits.forEach(hit -> {
                try {
                    csvPrinter.printRecord(
                            hit.getX(),
                            hit.getY(),
                            hit.getR(),
                            hit.isHit() ? "HIT" : "MISS",
                            hit.getCreatedAt(),
                            hit.getAreaVersion().getId()
                    );
                } catch (IOException e) {
                    throw new UncheckedIOException(e);
                }
            });
        } catch (UncheckedIOException e) {
            throw e.getCause();
        }

        csvPrinter.flush();
    }
}