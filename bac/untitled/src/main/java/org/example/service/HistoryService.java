package org.example.service;

import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;
import jakarta.transaction.Transactional;
import org.example.entity.HitEntity;
import org.example.repository.HitRepository;

import java.io.IOException;
import java.io.Writer;
import java.util.List;
import java.util.UUID;

@ApplicationScoped
public class HistoryService {

    private static final int BATCH_SIZE = 500;

    @Inject
    private HitRepository hitRepository;

    @Transactional
    public void exportHistory(UUID userId, Writer writer) throws IOException {

        writer.write("id,x,y,r,hit,created_at\n");

        int offset = 0;

        while (true) {
            List<HitEntity> hits = hitRepository.findByUser(
                    userId,
                    offset,
                    BATCH_SIZE
            );

            if (hits.isEmpty()) {
                break;
            }

            for (HitEntity hit : hits) {
                writer.write(toCsvRow(hit));
            }

            writer.flush();

            offset += hits.size();
        }
    }

    private String toCsvRow(HitEntity hit) {
        return String.format(
                "%s,%s,%s,%s,%s,%s%n",
                hit.getId(),
                hit.getX(),
                hit.getY(),
                hit.getR(),
                hit.isHit(),
                hit.getCreatedAt()
        );
    }
}