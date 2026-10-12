package com.campus.complaint.entity;

import lombok.Data;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.index.Indexed;
import org.springframework.data.mongodb.core.mapping.Document;

@Data
@Document(collection = "permissions")
public class Permission {
    @Id
    private String id;

    @Indexed(unique = true)
    private String name;
}
