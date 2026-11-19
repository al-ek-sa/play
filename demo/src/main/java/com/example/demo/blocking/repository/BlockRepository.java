package com.example.demo.blocking.repository;

import com.example.demo.blocking.entity.Block;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;
import java.util.UUID;

@Repository
public interface BlockRepository extends JpaRepository<Block, UUID> {

    boolean existsByBlockerIdAndBlockedId(
            UUID blockerId,
            UUID blockedId
    );

    Optional<Block> findByBlockerIdAndBlockedId(
            UUID blockerId,
            UUID blockedId
    );

    void deleteByBlockerIdAndBlockedId(
            UUID blockerId,
            UUID blockedId
    );

    boolean existsByBlockerIdAndBlockedIdOrBlockedIdAndBlockerId(
            UUID first,
            UUID second,
            UUID secondReverse,
            UUID firstReverse
    );
}
