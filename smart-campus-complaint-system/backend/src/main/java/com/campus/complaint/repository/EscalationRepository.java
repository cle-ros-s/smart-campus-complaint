package com.campus.complaint.repository;

import com.campus.complaint.entity.Escalation;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface EscalationRepository extends MongoRepository<Escalation, String> {
    List<Escalation> findByComplaintId(String complaintId);
}
