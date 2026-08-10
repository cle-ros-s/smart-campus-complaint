package com.campus.complaint.dto;

import com.campus.complaint.enums.ComplaintStatus;
import com.campus.complaint.enums.Priority;
import lombok.Data;

import java.time.LocalDateTime;

@Data
public class ComplaintResponse {
    private Long id;
    private String complaintNumber;
    private String categoryName;
    private String departmentName;
    private String assignedStaffName;
    private String description;
    private String location;
    private Priority priority;
    private ComplaintStatus status;
    private LocalDateTime createdAt;
    private LocalDateTime dueAt;
    
    // Identity fields intentionally omitted for privacy
}
