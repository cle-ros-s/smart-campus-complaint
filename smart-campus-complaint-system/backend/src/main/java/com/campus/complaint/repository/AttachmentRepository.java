package com.campus.complaint.repository;

import com.campus.complaint.entity.Attachment;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.List;

public interface AttachmentRepository extends JpaRepository<Attachment, Long> {
    List<Attachment> findByComplaintId(Long complaintId);
}
