package com.sahil.votethegoat.model;

import lombok.Data;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

@Data
@Document(collection = "elections")
public class Election {
    @Id
    private String id;
    private String name;
    private String startDate;
    private String endDate;
    private String description;
    private String status; // "Active", "Disabled"
}
