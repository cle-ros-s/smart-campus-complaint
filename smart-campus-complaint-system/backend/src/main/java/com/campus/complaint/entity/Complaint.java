package com.campus.complaint.entity;

import com.campus.complaint.enums.ComplaintStatus;
import com.campus.complaint.enums.Priority;
import com.campus.complaint.enums.PrivacyMode;
import lombok.Data;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.index.Indexed;
import org.springframework.data.mongodb.core.mapping.DBRef;
import org.springframework.data.mongodb.core.mapping.Document;

import java.time.LocalDateTime;

@Data
@Document(collection = "complaints")
public class Complaint {
    @Id
    private String id;

    @Indexed(unique = true)
    private String complaintNumber;

    private String trackingCode;

    @DBRef
    private User student;

    @DBRef
    private ComplaintCategory category;

    @DBRef
    private Department department;

    @DBRef
    private User assignedStaff;

    private String description;

    private String location;

    private Priority priority;

    private ComplaintStatus status;

    private PrivacyMode privacyMode;

    private LocalDateTime createdAt = LocalDateTime.now();

    private LocalDateTime updatedAt = LocalDateTime.now();

    private LocalDateTime dueAt;

    private LocalDateTime resolvedAt;

    private LocalDateTime closedAt;
}
