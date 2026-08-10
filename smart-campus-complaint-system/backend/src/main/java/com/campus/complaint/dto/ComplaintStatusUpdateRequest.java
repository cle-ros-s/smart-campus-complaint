package com.campus.complaint.dto;

import com.campus.complaint.enums.ComplaintStatus;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

@Data
public class ComplaintStatusUpdateRequest {
    @NotNull
    private ComplaintStatus status;
    
    private String comment;
}
