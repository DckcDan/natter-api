package com.example.natterapi.service;


import com.example.natterapi.domain.Space;
import com.example.natterapi.repository.SequenceGeneratorService;
import com.example.natterapi.repository.SpaceRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Service
public class SpaceService {
    @Autowired
    private SpaceRepository spaceRepository;
    @Autowired
    private SequenceGeneratorService sequenceGenerator;

    public Space createNewSpace(Space space) {
        space.setCreatedDate(LocalDate.now());
        space.setId(sequenceGenerator.generateSequence(Space.SEQUENCE_NAME));
        spaceRepository.save(space);
        return space;
    }

    public Optional<Space> findSpace(Long id) {
        return spaceRepository.findById(id);
    }

    public List<Space> findAll() {
        return spaceRepository.findAll();
    }
}
