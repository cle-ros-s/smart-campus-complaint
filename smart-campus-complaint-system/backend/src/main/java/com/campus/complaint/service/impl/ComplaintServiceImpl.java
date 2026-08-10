package com.campus.complaint.service.impl;

import com.campus.complaint.dto.ComplaintFullResponse;
import com.campus.complaint.dto.ComplaintRequest;
import com.campus.complaint.dto.ComplaintResponse;
import com.campus.complaint.dto.ComplaintStatusUpdateRequest;
import com.campus.complaint.entity.*;
import com.campus.complaint.enums.ComplaintStatus;
import com.campus.complaint.repository.*;
import com.campus.complaint.service.ComplaintService;
import com.campus.complaint.storage.MediaStorageService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;
import java.util.Random;

@Service
public class ComplaintServiceImpl implements ComplaintService {

    @Autowired
    ComplaintRepository complaintRepository;

    @Autowired
    UserRepository userRepository;

    @Autowired
    ComplaintCategoryRepository categoryRepository;

    @Autowired
    ComplaintHistoryRepository historyRepository;

    @Autowired
    MediaStorageService mediaStorageService;

    @Override
    @Transactional
    public ComplaintResponse submitComplaint(ComplaintRequest request, String studentEmail) {
        User student = userRepository.findByEmail(studentEmail)
                .orElseThrow(() -> new RuntimeException("Student not found"));

        ComplaintCategory category = categoryRepository.findById(request.getCategoryId())
                .orElseThrow(() -> new RuntimeException("Category not found"));

        Complaint complaint = new Complaint();
        complaint.setComplaintNumber(generateComplaintNumber());
        complaint.setTrackingCode(generateTrackingCode());
        complaint.setStudent(student);
        complaint.setCategory(category);
        complaint.setDepartment(category.getDefaultDepartment());
        complaint.setDescription(request.getDescription());
        complaint.setLocation(request.getLocation());
        complaint.setPriority(request.getPriority());
        complaint.setStatus(ComplaintStatus.PENDING);
        complaint.setPrivacyMode(request.getPrivacyMode());
        
        complaint = complaintRepository.save(complaint);

        // Record history
        recordHistory(complaint, "COMPLAINT_SUBMITTED", student, null, ComplaintStatus.PENDING.name(), "Complaint submitted successfully.");

        return mapToResponse(complaint);
    }

    @Override
    public ComplaintFullResponse getComplaintById(Long id, String email) {
        Complaint complaint = complaintRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Complaint not found"));
        
        // Basic authorization - real app would use complex RBAC
        return mapToFullResponse(complaint);
    }

    @Override
    public ComplaintResponse getComplaintByTracking(String complaintNumber, String trackingCode) {
        Complaint complaint = complaintRepository.findByComplaintNumberAndTrackingCode(complaintNumber, trackingCode)
                .orElseThrow(() -> new RuntimeException("Invalid tracking details"));
        return mapToResponse(complaint);
    }

    @Override
    public List<ComplaintResponse> getMyComplaints(String email) {
        User student = userRepository.findByEmail(email).orElseThrow();
        return complaintRepository.findByStudentId(student.getId()).stream()
                .map(this::mapToResponse).collect(Collectors.toList());
    }

    @Override
    public List<ComplaintResponse> getStaffComplaints(String email) {
        User staff = userRepository.findByEmail(email).orElseThrow();
        // In a real app, logic to fetch by department or assigned ID
        return complaintRepository.findAll().stream()
                .map(this::mapToResponse).collect(Collectors.toList());
    }

    @Override
    @Transactional
    public ComplaintResponse updateStatus(Long id, ComplaintStatusUpdateRequest request, String email) {
        User user = userRepository.findByEmail(email).orElseThrow();
        Complaint complaint = complaintRepository.findById(id).orElseThrow();
        
        String oldStatus = complaint.getStatus().name();
        complaint.setStatus(request.getStatus());
        
        if (request.getStatus() == ComplaintStatus.RESOLVED) {
            complaint.setResolvedAt(LocalDateTime.now());
        } else if (request.getStatus() == ComplaintStatus.CLOSED) {
            complaint.setClosedAt(LocalDateTime.now());
        }

        complaint = complaintRepository.save(complaint);
        
        recordHistory(complaint, "STATUS_UPDATED", user, oldStatus, request.getStatus().name(), request.getComment());
        
        return mapToResponse(complaint);
    }

    @Override
    public void uploadEvidence(Long complaintId, MultipartFile[] files, String email) {
        // Implementation for multiple files
    }
    
    private void recordHistory(Complaint complaint, String action, User user, String oldStatus, String newStatus, String details) {
        ComplaintHistory history = new ComplaintHistory();
        history.setComplaint(complaint);
        history.setAction(action);
        history.setActionBy(user);
        history.setPreviousStatus(oldStatus);
        history.setNewStatus(newStatus);
        history.setDetails(details);
        historyRepository.save(history);
    }

    private String generateComplaintNumber() {
        return "CMP-" + java.time.Year.now().getValue() + "-" + (10000 + new Random().nextInt(90000));
    }
    
    private String generateTrackingCode() {
        return UUID.randomUUID().toString().substring(0, 6).toUpperCase();
    }

    private ComplaintResponse mapToResponse(Complaint complaint) {
        ComplaintResponse resp = new ComplaintResponse();
        resp.setId(complaint.getId());
        resp.setComplaintNumber(complaint.getComplaintNumber());
        resp.setCategoryName(complaint.getCategory().getName());
        resp.setDepartmentName(complaint.getDepartment() != null ? complaint.getDepartment().getName() : null);
        resp.setAssignedStaffName(complaint.getAssignedStaff() != null ? complaint.getAssignedStaff().getName() : null);
        resp.setDescription(complaint.getDescription());
        resp.setLocation(complaint.getLocation());
        resp.setPriority(complaint.getPriority());
        resp.setStatus(complaint.getStatus());
        resp.setCreatedAt(complaint.getCreatedAt());
        resp.setDueAt(complaint.getDueAt());
        return resp;
    }

    private ComplaintFullResponse mapToFullResponse(Complaint complaint) {
        ComplaintFullResponse resp = new ComplaintFullResponse();
        // copy properties
        resp.setId(complaint.getId());
        resp.setComplaintNumber(complaint.getComplaintNumber());
        resp.setCategoryName(complaint.getCategory().getName());
        resp.setDepartmentName(complaint.getDepartment() != null ? complaint.getDepartment().getName() : null);
        resp.setAssignedStaffName(complaint.getAssignedStaff() != null ? complaint.getAssignedStaff().getName() : null);
        resp.setDescription(complaint.getDescription());
        resp.setLocation(complaint.getLocation());
        resp.setPriority(complaint.getPriority());
        resp.setStatus(complaint.getStatus());
        resp.setCreatedAt(complaint.getCreatedAt());
        resp.setDueAt(complaint.getDueAt());
        
        if (complaint.getStudent() != null) {
            resp.setStudentName(complaint.getStudent().getName());
            resp.setStudentEmail(complaint.getStudent().getEmail());
            resp.setStudentCollegeId(complaint.getStudent().getCollegeId());
            resp.setStudentPhone(complaint.getStudent().getPhoneNumber());
        }
        return resp;
    }
}
