package com.campus.complaint.entity;

import lombok.Data;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.DBRef;
import org.springframework.data.mongodb.core.mapping.Document;

import java.time.LocalDateTime;

@Data
@Document(collection = "complaint_history")
public class ComplaintHistory {
    @Id
    private String id;

    @DBRef
    private Complaint complaint;

    private String action;

    @DBRef
    private User actionBy;

    private String previousStatus;

    private String newStatus;

    private String details;

    private LocalDateTime createdAt = LocalDateTime.now();
}
