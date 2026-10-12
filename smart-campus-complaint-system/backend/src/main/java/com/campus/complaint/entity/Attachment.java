package com.campus.complaint.entity;

import lombok.Data;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.DBRef;
import org.springframework.data.mongodb.core.mapping.Document;

import java.time.LocalDateTime;

@Data
@Document(collection = "attachments")
public class Attachment {
    @Id
    private String id;

    @DBRef
    private Complaint complaint;

    private String fileName;

    private String storedFileName;

    private String fileType;

    private Long fileSize;

    private String filePath;

    @DBRef
    private User uploadedBy;

    private LocalDateTime uploadedAt = LocalDateTime.now();
}
