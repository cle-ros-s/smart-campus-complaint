package com.campus.complaint.entity;

import com.campus.complaint.enums.Priority;
import jakarta.persistence.*;
import lombok.Data;

@Data
@Entity
@Table(name = "sla_rules")
public class SlaRule {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false, unique = true)
    private Priority priority;

    @Column(name = "resolution_time_hours", nullable = false)
    private Integer resolutionTimeHours;
}
