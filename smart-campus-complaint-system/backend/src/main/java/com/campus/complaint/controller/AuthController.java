package com.campus.complaint.controller;

import com.campus.complaint.dto.JwtResponse;
import com.campus.complaint.dto.LoginRequest;
import com.campus.complaint.dto.MessageResponse;
import com.campus.complaint.dto.SignupRequest;
import com.campus.complaint.entity.Department;
import com.campus.complaint.entity.Role;
import com.campus.complaint.entity.User;
import com.campus.complaint.repository.DepartmentRepository;
import com.campus.complaint.repository.RoleRepository;
import com.campus.complaint.repository.UserRepository;
import com.campus.complaint.security.JwtUtils;
import com.campus.complaint.security.UserDetailsImpl;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;
import java.util.LinkedHashMap;
import java.util.stream.Collectors;

@CrossOrigin(origins = "*", maxAge = 3600)
@RestController
@RequestMapping("/api/auth")
public class AuthController {
    @Autowired
    AuthenticationManager authenticationManager;

    @Autowired
    UserRepository userRepository;

    @Autowired
    RoleRepository roleRepository;

    @Autowired
    DepartmentRepository departmentRepository;

    @Autowired
    PasswordEncoder encoder;

    @Autowired
    JwtUtils jwtUtils;

    @PostMapping("/login")
    public ResponseEntity<?> authenticateUser(@Valid @RequestBody LoginRequest loginRequest) {

        Authentication authentication = authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(loginRequest.getEmail(), loginRequest.getPassword()));

        SecurityContextHolder.getContext().setAuthentication(authentication);
        String jwt = jwtUtils.generateJwtToken(authentication);

        UserDetailsImpl userDetails = (UserDetailsImpl) authentication.getPrincipal();
        List<String> roles = userDetails.getAuthorities().stream()
                .map(GrantedAuthority::getAuthority)
                .collect(Collectors.toList());

        return ResponseEntity.ok(new JwtResponse(jwt,
                userDetails.getId(),
                userDetails.getUsername(),
                roles));
    }

    @PostMapping("/register")
    public ResponseEntity<?> registerUser(@Valid @RequestBody SignupRequest signUpRequest) {
        if (userRepository.existsByEmail(signUpRequest.getEmail())) {
            return ResponseEntity
                    .badRequest()
                    .body(new MessageResponse("Error: Email is already in use!"));
        }

        if (signUpRequest.getCollegeId() != null && !signUpRequest.getCollegeId().isEmpty() 
            && userRepository.existsByCollegeId(signUpRequest.getCollegeId())) {
            return ResponseEntity
                    .badRequest()
                    .body(new MessageResponse("Error: College ID is already in use!"));
        }

        User user = new User();
        user.setName(signUpRequest.getName());
        user.setEmail(signUpRequest.getEmail());
        user.setPassword(encoder.encode(signUpRequest.getPassword()));
        user.setPhoneNumber(signUpRequest.getPhoneNumber());
        user.setCollegeId(signUpRequest.getCollegeId());
        user.setYear(signUpRequest.getYear());
        user.setSection(signUpRequest.getSection());

        Role studentRole = roleRepository.findByName("STUDENT")
                .orElseThrow(() -> new RuntimeException("Error: Role is not found."));
        user.setRole(studentRole);

        if (signUpRequest.getDepartmentId() != null) {
            Department dept = departmentRepository.findById(signUpRequest.getDepartmentId())
                    .orElseThrow(() -> new RuntimeException("Error: Department is not found."));
            user.setDepartment(dept);
        }

        userRepository.save(user);

        return ResponseEntity.ok(new MessageResponse("User registered successfully!"));
    }

    @GetMapping("/me")
    public ResponseEntity<?> getCurrentUser(Authentication authentication) {
        if (authentication == null || !authentication.isAuthenticated()) {
            return ResponseEntity.status(401).body(new MessageResponse("Unauthorized"));
        }
        User user = userRepository.findByEmail(authentication.getName())
                .orElseThrow(() -> new RuntimeException("Error: User not found."));

        Map<String, Object> details = new LinkedHashMap<>();
        details.put("id", user.getId());
        details.put("name", user.getName());
        details.put("email", user.getEmail());
        details.put("phoneNumber", user.getPhoneNumber());
        details.put("collegeId", user.getCollegeId());
        details.put("year", user.getYear());
        details.put("section", user.getSection());
        details.put("role", user.getRole() != null ? user.getRole().getName() : null);
        details.put("department", user.getDepartment() != null ? user.getDepartment().getName() : null);

        return ResponseEntity.ok(details);
    }
}
