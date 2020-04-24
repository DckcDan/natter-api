package com.example.natterapi.repository;

import com.example.natterapi.domain.Space;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface SpaceRepository extends MongoRepository<Space, Long> {
}
