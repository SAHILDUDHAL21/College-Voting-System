package com.sahil.votethegoat.model;

import lombok.Data;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

@Data
@Document(collection = "candidates")
public class Candidate {
    @Id
    private String id;
    private String name;
    private String profilePhoto; // Store as base64 string
    private String className;
    private String department;
    private String description;
    private String electionId;
}
