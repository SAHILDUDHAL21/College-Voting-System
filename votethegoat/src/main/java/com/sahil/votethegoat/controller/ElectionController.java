package com.sahil.votethegoat.controller;

import com.sahil.votethegoat.model.Election;
import com.sahil.votethegoat.service.ElectionService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.Map;

@RestController
@RequestMapping("/api/elections")
public class ElectionController {

    @Autowired
    private ElectionService electionService;

    @PostMapping
    public ResponseEntity<?> createElection(@RequestBody Election election) {
        return ResponseEntity.ok(electionService.createElection(election));
    }

    @GetMapping
    public ResponseEntity<?> getAllElections() {
        return ResponseEntity.ok(electionService.getAllElections());
    }

    @PutMapping("/{id}/status")
    public ResponseEntity<?> toggleStatus(@PathVariable String id) {
        try {
            return ResponseEntity.ok(electionService.toggleStatus(id));
        } catch (Exception e) {
            Map<String, String> response = new HashMap<>();
            response.put("message", e.getMessage());
            return ResponseEntity.badRequest().body(response);
        }
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<?> deleteElection(@PathVariable String id) {
        electionService.deleteElection(id);
        return ResponseEntity.ok().build();
    }
}
