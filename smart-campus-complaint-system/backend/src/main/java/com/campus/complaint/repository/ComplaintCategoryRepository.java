package com.campus.complaint.repository;

import com.campus.complaint.entity.ComplaintCategory;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface ComplaintCategoryRepository extends MongoRepository<ComplaintCategory, String> {
    Optional<ComplaintCategory> findByName(String name);
}
