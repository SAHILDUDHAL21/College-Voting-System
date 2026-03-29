package com.sahil.votethegoat.service;

import com.sahil.votethegoat.model.Vote;
import com.sahil.votethegoat.repository.VoteRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DuplicateKeyException;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class VoteService {

    @Autowired
    private VoteRepository voteRepository;

    public boolean hasUserVoted(String electionId, String voterId) {
        return voteRepository.findByElectionIdAndVoterId(electionId, voterId).isPresent();
    }

    public Vote castVote(Vote vote) throws Exception {
        Optional<Vote> existingVote = voteRepository.findByElectionIdAndVoterId(vote.getElectionId(), vote.getVoterId());
        if (existingVote.isPresent()) {
            throw new Exception("You have already voted in this election!");
        }
        try {
            return voteRepository.save(vote);
        } catch (DuplicateKeyException e) {
            throw new Exception("You have already voted in this election!");
        }
    }

    public long getVoteCountForCandidate(String electionId, String candidateId) {
        return voteRepository.countByElectionIdAndCandidateId(electionId, candidateId);
    }
    
    public long getTotalVotesForElection(String electionId) {
        return voteRepository.countByElectionId(electionId);
    }
    public long getTotalGlobalVotes() {
        return voteRepository.count();
    }
}
