package com.campus.complaint.entity;

import com.campus.complaint.enums.Priority;
import lombok.Data;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.index.Indexed;
import org.springframework.data.mongodb.core.mapping.Document;

@Data
@Document(collection = "sla_rules")
public class SlaRule {
    @Id
    private String id;

    @Indexed(unique = true)
    private Priority priority;

    private Integer resolutionTimeHours;
}
