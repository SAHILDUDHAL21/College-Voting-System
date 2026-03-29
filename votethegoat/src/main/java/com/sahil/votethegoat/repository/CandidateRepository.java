package com.sahil.votethegoat.repository;

import com.sahil.votethegoat.model.Candidate;
import org.springframework.data.mongodb.repository.MongoRepository;
import java.util.List;

public interface CandidateRepository extends MongoRepository<Candidate, String> {
    List<Candidate> findByElectionId(String electionId);
}
