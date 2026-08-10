package com.campus.complaint.repository;

import com.campus.complaint.entity.Complaint;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import java.util.List;
import java.util.Optional;

@Repository
public interface ComplaintRepository extends JpaRepository<Complaint, Long> {
    Optional<Complaint> findByComplaintNumber(String complaintNumber);
    Optional<Complaint> findByComplaintNumberAndTrackingCode(String complaintNumber, String trackingCode);
    List<Complaint> findByStudentId(Long studentId);
    List<Complaint> findByDepartmentId(Long departmentId);
    List<Complaint> findByAssignedStaffId(Long staffId);
}
