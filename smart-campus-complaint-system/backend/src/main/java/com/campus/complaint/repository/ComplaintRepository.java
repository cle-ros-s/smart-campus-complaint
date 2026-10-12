package com.campus.complaint.repository;

import com.campus.complaint.entity.Complaint;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface ComplaintRepository extends MongoRepository<Complaint, String> {
    Optional<Complaint> findByComplaintNumber(String complaintNumber);
    Optional<Complaint> findByComplaintNumberAndTrackingCode(String complaintNumber, String trackingCode);
    List<Complaint> findByStudentId(String studentId);
    List<Complaint> findByDepartmentId(String departmentId);
    List<Complaint> findByAssignedStaffId(String staffId);
}
