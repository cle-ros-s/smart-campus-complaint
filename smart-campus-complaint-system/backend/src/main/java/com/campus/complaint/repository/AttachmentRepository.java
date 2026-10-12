package com.campus.complaint.repository;

import com.campus.complaint.entity.Attachment;
import org.springframework.data.mongodb.repository.MongoRepository;

import java.util.List;

public interface AttachmentRepository extends MongoRepository<Attachment, String> {
    List<Attachment> findByComplaintId(String complaintId);
}
