package com.campus.complaint.entity;

import lombok.Data;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.DBRef;
import org.springframework.data.mongodb.core.mapping.Document;

import java.time.LocalDateTime;

@Data
@Document(collection = "investigations")
public class Investigation {
    @Id
    private String id;

    @DBRef
    private Complaint complaint;

    @DBRef
    private User investigator;

    private String status;

    private String outcome;

    private String notes;

    private LocalDateTime createdAt = LocalDateTime.now();

    private LocalDateTime updatedAt = LocalDateTime.now();
}
