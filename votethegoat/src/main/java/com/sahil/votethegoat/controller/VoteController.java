package com.sahil.votethegoat.controller;

import com.sahil.votethegoat.model.Candidate;
import com.sahil.votethegoat.model.Vote;
import com.sahil.votethegoat.service.CandidateService;
import com.sahil.votethegoat.service.VoteService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/votes")
public class VoteController {

    @Autowired
    private VoteService voteService;

    @Autowired
    private CandidateService candidateService;

    @PostMapping
    public ResponseEntity<?> castVote(@RequestBody Vote vote) {
        try {
            Vote savedVote = voteService.castVote(vote);
            return ResponseEntity.ok(savedVote);
        } catch (Exception e) {
            Map<String, String> response = new HashMap<>();
            response.put("message", e.getMessage());
            return ResponseEntity.badRequest().body(response);
        }
    }

    @GetMapping("/results/{electionId}")
    public ResponseEntity<?> getResults(@PathVariable String electionId) {
        List<Candidate> candidates = candidateService.getCandidatesByElection(electionId);
        long totalVotes = voteService.getTotalVotesForElection(electionId);

        List<Map<String, Object>> results = new ArrayList<>();

        for (Candidate c : candidates) {
            long votes = voteService.getVoteCountForCandidate(electionId, c.getId());
            double percentage = totalVotes == 0 ? 0 : Math.round((votes * 100.0) / totalVotes * 10.0) / 10.0;

            Map<String, Object> candidateResult = new HashMap<>();
            candidateResult.put("candidateName", c.getName());
            candidateResult.put("className", c.getClassName());
            candidateResult.put("department", c.getDepartment());
            candidateResult.put("voteCount", votes);
            candidateResult.put("percentage", percentage);
            results.add(candidateResult);
        }

        return ResponseEntity.ok(results);
    }
    
    @GetMapping("/check/{electionId}/{voterId}")
    public ResponseEntity<Boolean> hasVoted(@PathVariable String electionId, @PathVariable String voterId) {
        return ResponseEntity.ok(voteService.hasUserVoted(electionId, voterId));
    }

    @GetMapping("/total")
    public ResponseEntity<Long> getTotalVotes() {
        return ResponseEntity.ok(voteService.getTotalGlobalVotes());
    }
}
