package com.campus.complaint.controller;

import com.campus.complaint.dto.ComplaintFullResponse;
import com.campus.complaint.dto.ComplaintRequest;
import com.campus.complaint.dto.ComplaintResponse;
import com.campus.complaint.dto.ComplaintStatusUpdateRequest;
import com.campus.complaint.service.ComplaintService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@CrossOrigin(origins = "*", maxAge = 3600)
@RestController
@RequestMapping("/api/complaints")
public class ComplaintController {

    @Autowired
    ComplaintService complaintService;

    @PostMapping
    @PreAuthorize("hasRole('STUDENT')")
    public ResponseEntity<?> submitComplaint(@Valid @RequestBody ComplaintRequest request, Authentication authentication) {
        ComplaintResponse response = complaintService.submitComplaint(request, authentication.getName());
        return ResponseEntity.ok(response);
    }

    @GetMapping
    @PreAuthorize("hasRole('STAFF') or hasRole('ADMIN') or hasRole('DEPARTMENT_HEAD') or hasRole('SAFETY_OFFICER')")
    public ResponseEntity<?> getAllComplaints(Authentication authentication) {
        return ResponseEntity.ok(complaintService.getStaffComplaints(authentication.getName()));
    }

    @GetMapping("/my")
    @PreAuthorize("hasRole('STUDENT')")
    public ResponseEntity<List<ComplaintResponse>> getMyComplaints(Authentication authentication) {
        return ResponseEntity.ok(complaintService.getMyComplaints(authentication.getName()));
    }

    @GetMapping("/track")
    public ResponseEntity<ComplaintResponse> trackComplaint(
            @RequestParam String complaintNumber, @RequestParam String trackingCode) {
        return ResponseEntity.ok(complaintService.getComplaintByTracking(complaintNumber, trackingCode));
    }

    @GetMapping("/{id}")
    public ResponseEntity<?> getComplaintById(@PathVariable Long id, Authentication authentication) {
        // Simple implementation, in a real app would check if user has rights to view identity
        return ResponseEntity.ok(complaintService.getComplaintById(id, authentication.getName()));
    }

    @PatchMapping("/{id}/status")
    @PreAuthorize("hasRole('STAFF') or hasRole('ADMIN') or hasRole('DEPARTMENT_HEAD')")
    public ResponseEntity<?> updateStatus(
            @PathVariable Long id,
            @Valid @RequestBody ComplaintStatusUpdateRequest request,
            Authentication authentication) {
        return ResponseEntity.ok(complaintService.updateStatus(id, request, authentication.getName()));
    }
}
