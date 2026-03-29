package com.sahil.votethegoat.model;

import lombok.Data;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.index.CompoundIndex;
import org.springframework.data.mongodb.core.mapping.Document;

@Data
@Document(collection = "votes")
@CompoundIndex(name = "unique_voter_per_election", def = "{'electionId': 1, 'voterId': 1}", unique = true)
public class Vote {
    @Id
    private String id;
    private String electionId;
    private String candidateId;
    private String voterId; // The user ID who voted
}
