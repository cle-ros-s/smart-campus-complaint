package com.campus.complaint.entity;

import com.campus.complaint.enums.ComplaintType;
import lombok.Data;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.index.Indexed;
import org.springframework.data.mongodb.core.mapping.DBRef;
import org.springframework.data.mongodb.core.mapping.Document;

@Data
@Document(collection = "complaint_categories")
public class ComplaintCategory {
    @Id
    private String id;

    @Indexed(unique = true)
    private String name;

    private ComplaintType type;

    @DBRef
    private Department defaultDepartment;
}
