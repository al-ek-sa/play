package org.example.service;

import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;
import jakarta.transaction.Transactional;
import org.example.repository.AreaVersionRepository;
import org.example.repository.HitRepository;
import org.example.repository.UserRepository;

@ApplicationScoped
public class HitService {

    @Inject
    private HitRepository hitRepository;

    @Inject
    private AreaVersionRepository areaVersionRepository;

    @Inject
    private UserRepository userRepository;

    @Transactional
    public boolean hit(){

        return true;
    }
}
