package com.campus.complaint.entity;

import com.campus.complaint.enums.ComplaintType;
import jakarta.persistence.*;
import lombok.Data;

@Data
@Entity
@Table(name = "complaint_categories")
public class ComplaintCategory {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, unique = true)
    private String name;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private ComplaintType type;

    @ManyToOne
    @JoinColumn(name = "default_department_id")
    private Department defaultDepartment;
}
