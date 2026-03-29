package com.sahil.votethegoat.service;

import com.sahil.votethegoat.model.Candidate;
import com.sahil.votethegoat.repository.CandidateRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class CandidateService {

    @Autowired
    private CandidateRepository candidateRepository;

    public Candidate addCandidate(Candidate candidate) {
        return candidateRepository.save(candidate);
    }

    public List<Candidate> getCandidatesByElection(String electionId) {
        return candidateRepository.findByElectionId(electionId);
    }
}
