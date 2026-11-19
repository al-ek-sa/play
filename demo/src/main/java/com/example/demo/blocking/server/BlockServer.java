package com.example.demo.blocking.server;


import com.example.demo.blocking.entity.Block;
import com.example.demo.blocking.repository.BlockRepository;
import com.example.demo.friendrequest.repository.FriendRequestRepository;
import com.example.demo.friendship.repository.FriendshipRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.UUID;

@Service
@RequiredArgsConstructor
@Transactional
public class BlockServer {

    private final BlockRepository blockRepository;
    private final FriendRequestRepository friendRequestRepository;
    private final FriendshipRepository friendshipRepository;

    @Transactional
    public void createdBlocking(UUID blocker, UUID blocking){
        if (blocker.equals(blocking)) {
            throw new IllegalArgumentException(
                    "User cannot block themselves"
            );
        }
        if (blockRepository.existsByBlockerIdAndBlockedId(blocker, blocking)) {
            throw new IllegalStateException(
                    "User is already blocked"
            );
        }
        Block block = new Block(blocker, blocking);
        blockRepository.save(block);
    }


}
