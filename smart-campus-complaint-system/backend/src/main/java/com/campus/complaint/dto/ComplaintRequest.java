package com.campus.complaint.dto;

import com.campus.complaint.enums.Priority;
import com.campus.complaint.enums.PrivacyMode;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

@Data
public class ComplaintRequest {
    @NotNull
    private String categoryId;

    @NotBlank
    private String description;

    @NotBlank
    private String location;

    @NotNull
    private Priority priority;

    @NotNull
    private PrivacyMode privacyMode;
}
