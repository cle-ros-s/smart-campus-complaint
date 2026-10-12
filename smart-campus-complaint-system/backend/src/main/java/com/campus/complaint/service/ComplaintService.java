package com.campus.complaint.service;

import com.campus.complaint.dto.ComplaintFullResponse;
import com.campus.complaint.dto.ComplaintRequest;
import com.campus.complaint.dto.ComplaintResponse;
import com.campus.complaint.dto.ComplaintStatusUpdateRequest;
import org.springframework.web.multipart.MultipartFile;
import java.util.List;

public interface ComplaintService {
    ComplaintResponse submitComplaint(ComplaintRequest request, String studentEmail);
    
    ComplaintFullResponse getComplaintById(String id, String email);
    
    ComplaintResponse getComplaintByTracking(String complaintNumber, String trackingCode);
    
    List<ComplaintResponse> getMyComplaints(String email);
    
    List<ComplaintResponse> getStaffComplaints(String email);
    
    ComplaintResponse updateStatus(String id, ComplaintStatusUpdateRequest request, String email);
    
    void uploadEvidence(String complaintId, MultipartFile[] files, String email);
}
