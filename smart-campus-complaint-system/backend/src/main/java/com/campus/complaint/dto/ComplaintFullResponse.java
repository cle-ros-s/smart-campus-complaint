package com.campus.complaint.dto;

import lombok.Data;
import lombok.EqualsAndHashCode;

@Data
@EqualsAndHashCode(callSuper = true)
public class ComplaintFullResponse extends ComplaintResponse {
    private String studentName;
    private String studentCollegeId;
    private String studentEmail;
    private String studentPhone;
}
