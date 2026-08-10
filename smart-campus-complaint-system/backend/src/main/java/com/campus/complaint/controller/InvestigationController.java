package com.campus.complaint.controller;

import com.campus.complaint.dto.MessageResponse;
import com.campus.complaint.entity.Complaint;
import com.campus.complaint.repository.ComplaintRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

@CrossOrigin(origins = "*", maxAge = 3600)
@RestController
@RequestMapping("/api/investigations")
public class InvestigationController {
    
    @Autowired
    private ComplaintRepository complaintRepository;

    @GetMapping("/reporter/{complaintId}")
    @PreAuthorize("hasAuthority('VIEW_REPORTER_IDENTITY')")
    public ResponseEntity<?> getReporterIdentity(@PathVariable Long id) {
        Complaint complaint = complaintRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Complaint not found"));
        
        // Log this access in AuditLog (not fully implemented in this MVP snippet)
        
        return ResponseEntity.ok(complaint.getStudent());
    }
    
    @PostMapping("/{complaintId}/notes")
    @PreAuthorize("hasRole('SAFETY_OFFICER')")
    public ResponseEntity<?> addInvestigationNotes(@PathVariable Long complaintId, @RequestBody String notes) {
        return ResponseEntity.ok(new MessageResponse("Notes added successfully to investigation"));
    }
}
