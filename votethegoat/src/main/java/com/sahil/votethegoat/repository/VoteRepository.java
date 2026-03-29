package com.sahil.votethegoat.repository;

import com.sahil.votethegoat.model.Vote;
import org.springframework.data.mongodb.repository.MongoRepository;
import java.util.Optional;
import java.util.List;

public interface VoteRepository extends MongoRepository<Vote, String> {
    Optional<Vote> findByElectionIdAndVoterId(String electionId, String voterId);
    List<Vote> findByElectionId(String electionId);
    long countByElectionIdAndCandidateId(String electionId, String candidateId);
    long countByElectionId(String electionId);
}
