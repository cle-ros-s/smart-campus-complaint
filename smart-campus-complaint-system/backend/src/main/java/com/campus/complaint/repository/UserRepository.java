package com.campus.complaint.repository;

import com.campus.complaint.entity.User;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface UserRepository extends MongoRepository<User, String> {
    Optional<User> findByEmail(String email);
    Optional<User> findByCollegeId(String collegeId);
    boolean existsByEmail(String email);
    boolean existsByCollegeId(String collegeId);
}
