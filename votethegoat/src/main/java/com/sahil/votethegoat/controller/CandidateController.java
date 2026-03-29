package com.sahil.votethegoat.controller;

import com.sahil.votethegoat.model.Candidate;
import com.sahil.votethegoat.service.CandidateService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/candidates")
public class CandidateController {

    @Autowired
    private CandidateService candidateService;

    @PostMapping
    public ResponseEntity<?> addCandidate(@RequestBody Candidate candidate) {
        return ResponseEntity.ok(candidateService.addCandidate(candidate));
    }

    @GetMapping("/election/{electionId}")
    public ResponseEntity<?> getCandidatesByElection(@PathVariable String electionId) {
        return ResponseEntity.ok(candidateService.getCandidatesByElection(electionId));
    }
}
