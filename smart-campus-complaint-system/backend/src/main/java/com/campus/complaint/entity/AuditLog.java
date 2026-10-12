package com.campus.complaint.entity;

import lombok.Data;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.DBRef;
import org.springframework.data.mongodb.core.mapping.Document;

import java.time.LocalDateTime;

@Data
@Document(collection = "audit_logs")
public class AuditLog {
    @Id
    private String id;

    @DBRef
    private User user;

    private String action;

    private String entityType;

    private String entityId;

    private String ipAddress;

    private String details;

    private LocalDateTime timestamp = LocalDateTime.now();
}
