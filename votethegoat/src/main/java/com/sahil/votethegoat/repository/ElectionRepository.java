package com.sahil.votethegoat.repository;

import com.sahil.votethegoat.model.Election;
import org.springframework.data.mongodb.repository.MongoRepository;

public interface ElectionRepository extends MongoRepository<Election, String> {
}
