package com.campus.complaint.entity;

import lombok.Data;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.DBRef;
import org.springframework.data.mongodb.core.mapping.Document;

import java.time.LocalDateTime;

@Data
@Document(collection = "feedback")
public class Feedback {
    @Id
    private String id;

    @DBRef
    private Complaint complaint;

    @DBRef
    private User student;

    private Integer rating; // 1 to 5

    private String comment;

    private LocalDateTime createdAt = LocalDateTime.now();
}
