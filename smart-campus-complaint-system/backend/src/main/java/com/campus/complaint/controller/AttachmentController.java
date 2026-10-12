package com.campus.complaint.controller;

import com.campus.complaint.entity.Attachment;
import com.campus.complaint.entity.Complaint;
import com.campus.complaint.entity.User;
import com.campus.complaint.repository.AttachmentRepository;
import com.campus.complaint.repository.ComplaintRepository;
import com.campus.complaint.repository.UserRepository;
import com.campus.complaint.storage.MediaStorageService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.Resource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@CrossOrigin(origins = "*", maxAge = 3600)
@RestController
@RequestMapping("/api/attachments")
public class AttachmentController {

    @Autowired
    private MediaStorageService mediaStorageService;

    @Autowired
    private AttachmentRepository attachmentRepository;

    @Autowired
    private ComplaintRepository complaintRepository;

    @Autowired
    private UserRepository userRepository;

    /**
     * Upload an image/file for a complaint.
     * Accepts multipart/form-data with a "file" field and a "complaintId" field.
     */
    @PostMapping("/upload")
    @PreAuthorize("hasRole('STUDENT')")
    public ResponseEntity<?> uploadFile(
            @RequestParam("file") MultipartFile file,
            @RequestParam("complaintId") String complaintId,
            Authentication authentication) {

        // Validate file
        if (file.isEmpty()) {
            return ResponseEntity.badRequest().body(Map.of("message", "Please select a file to upload."));
        }

        String contentType = file.getContentType();
        if (contentType == null || (!contentType.startsWith("image/") && !contentType.equals("application/pdf"))) {
            return ResponseEntity.badRequest().body(Map.of("message", "Only image files (JPG, PNG, GIF, WebP) and PDF are allowed."));
        }

        if (file.getSize() > 10 * 1024 * 1024) { // 10 MB limit
            return ResponseEntity.badRequest().body(Map.of("message", "File size must not exceed 10 MB."));
        }

        // Validate complaint ownership
        Complaint complaint = complaintRepository.findById(complaintId)
                .orElseThrow(() -> new RuntimeException("Complaint not found"));

        User uploader = userRepository.findByEmail(authentication.getName())
                .orElseThrow(() -> new RuntimeException("User not found"));

        // Check that the logged-in user owns this complaint
        if (complaint.getStudent() == null || !complaint.getStudent().getEmail().equals(authentication.getName())) {
            return ResponseEntity.status(403).body(Map.of("message", "You do not have permission to attach files to this complaint."));
        }

        // Determine folder by type
        String typeFolder = contentType.startsWith("image/") ? "images" : "documents";

        // Store file
        String storedFileName = mediaStorageService.storeFile(file, typeFolder);

        // Save attachment record
        Attachment attachment = new Attachment();
        attachment.setComplaint(complaint);
        attachment.setFileName(file.getOriginalFilename());
        attachment.setStoredFileName(storedFileName);
        attachment.setFileType(contentType);
        attachment.setFileSize(file.getSize());
        attachment.setFilePath(typeFolder + "/" + storedFileName);
        attachment.setUploadedBy(uploader);
        attachmentRepository.save(attachment);

        Map<String, Object> response = new HashMap<>();
        response.put("id", attachment.getId());
        response.put("fileName", attachment.getFileName());
        response.put("fileType", attachment.getFileType());
        response.put("fileSize", attachment.getFileSize());
        response.put("filePath", attachment.getFilePath());
        response.put("message", "File uploaded successfully!");

        return ResponseEntity.ok(response);
    }

    /**
     * Get all attachments for a specific complaint.
     */
    @GetMapping("/complaint/{complaintId}")
    public ResponseEntity<List<Attachment>> getAttachmentsByComplaint(@PathVariable String complaintId) {
        List<Attachment> attachments = attachmentRepository.findByComplaintId(complaintId);
        return ResponseEntity.ok(attachments);
    }

    /**
     * Download / view a specific file.
     */
    @GetMapping("/view/{typeFolder}/{fileName:.+}")
    public ResponseEntity<Resource> downloadFile(
            @PathVariable String typeFolder,
            @PathVariable String fileName) {
        Resource resource = mediaStorageService.loadFileAsResource(fileName, typeFolder);
        String contentType = "application/octet-stream";
        try {
            contentType = resource.getFile().toURI().toURL().openConnection().getContentType();
        } catch (Exception ignored) {}

        return ResponseEntity.ok()
                .contentType(MediaType.parseMediaType(contentType))
                .header(HttpHeaders.CONTENT_DISPOSITION, "inline; filename=\"" + resource.getFilename() + "\"")
                .body(resource);
    }
}
