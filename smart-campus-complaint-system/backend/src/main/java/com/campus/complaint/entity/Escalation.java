package com.campus.complaint.entity;

import lombok.Data;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.DBRef;
import org.springframework.data.mongodb.core.mapping.Document;

import java.time.LocalDateTime;

@Data
@Document(collection = "escalations")
public class Escalation {
    @Id
    private String id;

    @DBRef
    private Complaint complaint;

    @DBRef
    private User escalatedFrom;

    @DBRef
    private User escalatedTo;

    private String reason;

    private LocalDateTime createdAt = LocalDateTime.now();
}
