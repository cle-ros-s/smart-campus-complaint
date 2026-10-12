package com.campus.complaint.controller;

import com.campus.complaint.entity.Role;
import com.campus.complaint.entity.User;
import com.campus.complaint.repository.ComplaintRepository;
import com.campus.complaint.repository.RoleRepository;
import com.campus.complaint.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.*;

import java.util.*;
import java.util.stream.Collectors;

@CrossOrigin(origins = "*", maxAge = 3600)
@RestController
@RequestMapping("/api/admin")
@PreAuthorize("hasRole('ADMIN')")
public class AdminController {

    @Autowired
    UserRepository userRepository;

    @Autowired
    RoleRepository roleRepository;

    @Autowired
    ComplaintRepository complaintRepository;

    @Autowired
    PasswordEncoder encoder;

    /** GET /api/admin/users — list all users with role & department info */
    @GetMapping("/users")
    public ResponseEntity<?> getAllUsers() {
        List<Map<String, Object>> result = userRepository.findAll().stream().map(u -> {
            Map<String, Object> m = new LinkedHashMap<>();
            m.put("id", u.getId());
            m.put("name", u.getName());
            m.put("email", u.getEmail());
            m.put("collegeId", u.getCollegeId());
            m.put("role", u.getRole() != null ? u.getRole().getName() : null);
            m.put("department", u.getDepartment() != null ? u.getDepartment().getName() : null);
            m.put("phoneNumber", u.getPhoneNumber());
            m.put("year", u.getYear());
            m.put("section", u.getSection());
            m.put("createdAt", u.getCreatedAt());
            return m;
        }).collect(Collectors.toList());
        return ResponseEntity.ok(result);
    }

    /** DELETE /api/admin/users/{id} — delete a user */
    @DeleteMapping("/users/{id}")
    public ResponseEntity<?> deleteUser(@PathVariable String id) {
        if (!userRepository.existsById(id)) {
            return ResponseEntity.notFound().build();
        }
        userRepository.deleteById(id);
        return ResponseEntity.ok(Map.of("message", "User deleted successfully."));
    }

    /** PATCH /api/admin/users/{id}/role — change user role */
    @PatchMapping("/users/{id}/role")
    public ResponseEntity<?> changeUserRole(@PathVariable String id, @RequestBody Map<String, String> body) {
        User user = userRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("User not found"));
        String roleName = body.get("role");
        Role role = roleRepository.findByName(roleName)
                .orElseThrow(() -> new RuntimeException("Role not found: " + roleName));
        user.setRole(role);
        userRepository.save(user);
        return ResponseEntity.ok(Map.of("message", "Role updated to " + roleName));
    }

    /** GET /api/admin/stats — aggregate complaint stats */
    @GetMapping("/stats")
    public ResponseEntity<?> getStats() {
        long total = complaintRepository.count();
        List<?> all = complaintRepository.findAll();
        long pending = all.stream()
                .filter(c -> "PENDING".equals(((com.campus.complaint.entity.Complaint) c).getStatus().name()))
                .count();
        long inProgress = all.stream()
                .filter(c -> {
                    String s = ((com.campus.complaint.entity.Complaint) c).getStatus().name();
                    return "IN_PROGRESS".equals(s) || "ASSIGNED".equals(s);
                })
                .count();
        long resolved = all.stream()
                .filter(c -> {
                    String s = ((com.campus.complaint.entity.Complaint) c).getStatus().name();
                    return "RESOLVED".equals(s) || "CLOSED".equals(s);
                })
                .count();
        long escalated = all.stream()
                .filter(c -> "ESCALATED".equals(((com.campus.complaint.entity.Complaint) c).getStatus().name()))
                .count();
        long totalUsers = userRepository.count();

        Map<String, Object> stats = new LinkedHashMap<>();
        stats.put("totalComplaints", total);
        stats.put("pending", pending);
        stats.put("inProgress", inProgress);
        stats.put("resolved", resolved);
        stats.put("escalated", escalated);
        stats.put("totalUsers", totalUsers);
        return ResponseEntity.ok(stats);
    }
}
