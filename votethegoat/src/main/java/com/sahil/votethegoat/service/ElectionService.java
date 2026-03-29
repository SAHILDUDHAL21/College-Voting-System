package com.sahil.votethegoat.service;

import com.sahil.votethegoat.model.Election;
import com.sahil.votethegoat.repository.ElectionRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class ElectionService {

    @Autowired
    private ElectionRepository electionRepository;

    public Election createElection(Election election) {
        election.setStatus("Active"); // Default status
        return electionRepository.save(election);
    }

    public List<Election> getAllElections() {
        return electionRepository.findAll();
    }

    public Election toggleStatus(String id) throws Exception {
        Optional<Election> opt = electionRepository.findById(id);
        if (opt.isPresent()) {
            Election election = opt.get();
            if ("Active".equalsIgnoreCase(election.getStatus())) {
                election.setStatus("Disabled");
            } else {
                election.setStatus("Active");
            }
            return electionRepository.save(election);
        }
        throw new Exception("Election not found");
    }

    public void deleteElection(String id) {
        electionRepository.deleteById(id);
    }
}
