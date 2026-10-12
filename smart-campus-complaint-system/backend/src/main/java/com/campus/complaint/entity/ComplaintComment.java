package com.campus.complaint.entity;

import lombok.Data;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.DBRef;
import org.springframework.data.mongodb.core.mapping.Document;

import java.time.LocalDateTime;

@Data
@Document(collection = "complaint_comments")
public class ComplaintComment {
    @Id
    private String id;

    @DBRef
    private Complaint complaint;

    @DBRef
    private User user;

    private String type; // 'PUBLIC_COMMENT', 'INTERNAL_COMMENT'

    private String content;

    private LocalDateTime createdAt = LocalDateTime.now();
}
