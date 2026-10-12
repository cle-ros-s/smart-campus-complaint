package com.campus.complaint.repository;

import com.campus.complaint.entity.ComplaintHistory;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ComplaintHistoryRepository extends MongoRepository<ComplaintHistory, String> {
    List<ComplaintHistory> findByComplaintIdOrderByCreatedAtDesc(String complaintId);
}
