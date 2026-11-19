package com.example.demo.blocking.controller;

import com.example.demo.blocking.server.BlockServer;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.UUID;

@RestController
@RequestMapping("/api/v1/blocking")
@RequiredArgsConstructor
public class BlockController {

    private final BlockServer blockServer;

    @PostMapping("/created")
    public void createdBlock(@AuthenticationPrincipal Jwt jwt, @PathVariable UUID userId) {
        blockServer.createdBlocking(userId(jwt), userId);
    }

    private UUID userId(Jwt jwt) {
        return UUID.fromString(jwt.getSubject());
    }
}
