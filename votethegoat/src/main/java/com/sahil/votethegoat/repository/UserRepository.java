package com.sahil.votethegoat.repository;

import com.sahil.votethegoat.model.User;
import org.springframework.data.mongodb.repository.MongoRepository;
import java.util.Optional;
import java.util.List;

public interface UserRepository extends MongoRepository<User, String> {
    Optional<User> findByEmail(String email);
    Optional<User> findByCollegeId(String collegeId);
    List<User> findByRole(String role);
}
