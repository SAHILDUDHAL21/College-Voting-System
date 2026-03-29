package com.sahil.votethegoat.model;

import lombok.Data;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

@Data
@Document(collection = "users")
public class User {
    @Id
    private String id;
    private String fullname;
    private String collegeId;
    private String email;
    private String password;
    private String role; // "student", "teacher", "admin"
    private String className;
    private String department;
    private boolean verified; // true after email OTP verification
}
