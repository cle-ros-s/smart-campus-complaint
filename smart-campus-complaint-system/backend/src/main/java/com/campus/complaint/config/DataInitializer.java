package com.campus.complaint.config;

import com.campus.complaint.entity.*;
import com.campus.complaint.enums.*;
import com.campus.complaint.repository.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;
import java.util.List;

@Component
public class DataInitializer implements CommandLineRunner {

    private static final Logger log = LoggerFactory.getLogger(DataInitializer.class);

    @Autowired private RoleRepository roleRepository;
    @Autowired private DepartmentRepository departmentRepository;
    @Autowired private ComplaintCategoryRepository categoryRepository;
    @Autowired private UserRepository userRepository;
    @Autowired private ComplaintRepository complaintRepository;
    @Autowired private EscalationRepository escalationRepository;
    @Autowired private AttachmentRepository attachmentRepository;
    @Autowired private ComplaintHistoryRepository historyRepository;
    @Autowired private PasswordEncoder passwordEncoder;

    private static final String DEFAULT_PASSWORD = "password123";

    @Override
    public void run(String... args) {
        log.info("=== DataInitializer: Checking if seed data is needed... ===");

        // ── 1. ROLES ────────────────────────────────────────────────────────
        if (roleRepository.count() == 0) {
            log.info("Seeding roles...");
            roleRepository.saveAll(List.of(
                role("STUDENT"),
                role("STAFF"),
                role("DEPARTMENT_HEAD"),
                role("ADMIN"),
                role("SAFETY_OFFICER")
            ));
            log.info("  5 roles inserted.");
        } else {
            log.info("  Roles already seeded, skipping.");
        }

        // ── 2. DEPARTMENTS ───────────────────────────────────────────────────
        if (departmentRepository.count() == 0) {
            log.info("Seeding departments...");
            departmentRepository.saveAll(List.of(
                dept("Hostel Department",     "Handles hostel related issues"),
                dept("IT Department",         "Handles internet and network issues"),
                dept("Electrical Department", "Handles electricity and wiring issues"),
                dept("Maintenance Department","Handles water, cleaning and infrastructure"),
                dept("Student Welfare",       "Handles student safety, anti-ragging and sensitive issues")
            ));
            log.info("  5 departments inserted.");
        } else {
            log.info("  Departments already seeded, skipping.");
        }

        // ── 3. COMPLAINT CATEGORIES ──────────────────────────────────────────
        if (categoryRepository.count() == 0) {
            log.info("Seeding complaint categories...");
            Department hostel  = departmentRepository.findByName("Hostel Department").orElseThrow();
            Department it      = departmentRepository.findByName("IT Department").orElseThrow();
            Department elec    = departmentRepository.findByName("Electrical Department").orElseThrow();
            Department maint   = departmentRepository.findByName("Maintenance Department").orElseThrow();
            Department welfare = departmentRepository.findByName("Student Welfare").orElseThrow();

            categoryRepository.saveAll(List.of(
                category("Hostel",                  ComplaintType.NORMAL,    hostel),
                category("Internet / Wi-Fi",        ComplaintType.NORMAL,    it),
                category("Electricity",             ComplaintType.NORMAL,    elec),
                category("Water",                   ComplaintType.NORMAL,    maint),
                category("Cleaning",                ComplaintType.NORMAL,    maint),
                category("Infrastructure",          ComplaintType.NORMAL,    maint),
                category("Ragging",                 ComplaintType.SENSITIVE, welfare),
                category("Harassment",              ComplaintType.SENSITIVE, welfare),
                category("Suspected Drug Activity", ComplaintType.SENSITIVE, welfare)
            ));
            log.info("  9 complaint categories inserted.");
        } else {
            log.info("  Categories already seeded, skipping.");
        }

        // ── 4. USERS ─────────────────────────────────────────────────────────
        if (userRepository.count() == 0) {
            log.info("Seeding users...");
            String hash = passwordEncoder.encode(DEFAULT_PASSWORD);

            Role adminRole   = roleRepository.findByName("ADMIN").orElseThrow();
            Role headRole    = roleRepository.findByName("DEPARTMENT_HEAD").orElseThrow();
            Role staffRole   = roleRepository.findByName("STAFF").orElseThrow();
            Role safetyRole  = roleRepository.findByName("SAFETY_OFFICER").orElseThrow();
            Role studentRole = roleRepository.findByName("STUDENT").orElseThrow();

            Department hostel  = departmentRepository.findByName("Hostel Department").orElseThrow();
            Department it      = departmentRepository.findByName("IT Department").orElseThrow();
            Department welfare = departmentRepository.findByName("Student Welfare").orElseThrow();

            // Staff / Admin users
            userRepository.saveAll(List.of(
                user("Admin User",     "admin@campus.edu",           hash, adminRole,  null),
                user("Head of IT",     "hod_it@campus.edu",          hash, headRole,   it),
                user("Hostel Staff 1", "hostel_staff@campus.edu",    hash, staffRole,  hostel),
                user("IT Staff 1",     "it_staff@campus.edu",        hash, staffRole,  it),
                user("Safety Officer", "safety@campus.edu",          hash, safetyRole, welfare)
            ));

            // Student users
            userRepository.saveAll(List.of(
                student("John Doe",     "john@student.campus.edu",    hash, studentRole, "STU1001", 2, "A"),
                student("Jane Smith",   "jane@student.campus.edu",    hash, studentRole, "STU1002", 1, "B"),
                student("Alice Brown",  "alice@student.campus.edu",   hash, studentRole, "STU1003", 3, "C"),
                student("Bob White",    "bob@student.campus.edu",     hash, studentRole, "STU1004", 4, "A"),
                student("Charlie Green","charlie@student.campus.edu", hash, studentRole, "STU1005", 2, "B")
            ));

            log.info("  10 users inserted (5 staff + 5 students). Password: {}", DEFAULT_PASSWORD);
        } else {
            log.info("  Users already seeded, skipping.");
        }

        // ── 5. SAMPLE COMPLAINTS ─────────────────────────────────────────────
        if (complaintRepository.count() == 0) {
            log.info("Seeding sample complaints...");

            User john   = userRepository.findByEmail("john@student.campus.edu").orElseThrow();
            User jane   = userRepository.findByEmail("jane@student.campus.edu").orElseThrow();
            User alice  = userRepository.findByEmail("alice@student.campus.edu").orElseThrow();
            User hStaff = userRepository.findByEmail("hostel_staff@campus.edu").orElseThrow();
            User itStaff= userRepository.findByEmail("it_staff@campus.edu").orElseThrow();

            Department hostel  = departmentRepository.findByName("Hostel Department").orElseThrow();
            Department it      = departmentRepository.findByName("IT Department").orElseThrow();
            Department welfare = departmentRepository.findByName("Student Welfare").orElseThrow();

            ComplaintCategory hostelCat = categoryRepository.findByName("Hostel").orElseThrow();
            ComplaintCategory wifiCat   = categoryRepository.findByName("Internet / Wi-Fi").orElseThrow();
            ComplaintCategory drugCat   = categoryRepository.findByName("Suspected Drug Activity").orElseThrow();
            ComplaintCategory waterCat  = categoryRepository.findByName("Water").orElseThrow();
            ComplaintCategory ragCat    = categoryRepository.findByName("Ragging").orElseThrow();

            // Complaint 1 - IN_PROGRESS
            Complaint c1 = new Complaint();
            c1.setComplaintNumber("CMP-2026-10001");
            c1.setTrackingCode("TRACK01");
            c1.setStudent(john);
            c1.setCategory(hostelCat);
            c1.setDepartment(hostel);
            c1.setAssignedStaff(hStaff);
            c1.setDescription("Water leakage in Block B bathroom causing flooding near Room 204.");
            c1.setLocation("Block B, Room 204");
            c1.setPriority(Priority.HIGH);
            c1.setStatus(ComplaintStatus.IN_PROGRESS);
            c1.setPrivacyMode(PrivacyMode.CONFIDENTIAL);
            c1.setDueAt(LocalDateTime.now().plusHours(24));

            // Complaint 2 - RESOLVED
            Complaint c2 = new Complaint();
            c2.setComplaintNumber("CMP-2026-10002");
            c2.setTrackingCode("TRACK02");
            c2.setStudent(jane);
            c2.setCategory(wifiCat);
            c2.setDepartment(it);
            c2.setAssignedStaff(itStaff);
            c2.setDescription("Internet not working in the Main Library — all Wi-Fi access points are offline.");
            c2.setLocation("Main Library, 1st Floor");
            c2.setPriority(Priority.MEDIUM);
            c2.setStatus(ComplaintStatus.RESOLVED);
            c2.setPrivacyMode(PrivacyMode.CONFIDENTIAL);
            c2.setDueAt(LocalDateTime.now().minusHours(10));
            c2.setResolvedAt(LocalDateTime.now().minusHours(5));

            // Complaint 3 - PENDING ANONYMOUS sensitive
            Complaint c3 = new Complaint();
            c3.setComplaintNumber("CMP-2026-10003");
            c3.setTrackingCode("TRACK03");
            c3.setStudent(alice);
            c3.setCategory(drugCat);
            c3.setDepartment(welfare);
            c3.setDescription("Suspected drug activity observed near Parking Lot A after 10 PM on multiple occasions.");
            c3.setLocation("Parking Lot A");
            c3.setPriority(Priority.CRITICAL);
            c3.setStatus(ComplaintStatus.PENDING);
            c3.setPrivacyMode(PrivacyMode.ANONYMOUS);
            c3.setDueAt(LocalDateTime.now().plusHours(6));

            // Complaint 4 - PENDING water
            Complaint c4 = new Complaint();
            c4.setComplaintNumber("CMP-2026-10004");
            c4.setTrackingCode("TRACK04");
            c4.setStudent(john);
            c4.setCategory(waterCat);
            c4.setDepartment(hostel);
            c4.setDescription("No water supply in Hostel Block C from 6 AM for the past 3 days.");
            c4.setLocation("Hostel Block C");
            c4.setPriority(Priority.HIGH);
            c4.setStatus(ComplaintStatus.PENDING);
            c4.setPrivacyMode(PrivacyMode.CONFIDENTIAL);
            c4.setDueAt(LocalDateTime.now().plusHours(24));

            // Complaint 5 - ESCALATED ragging
            Complaint c5 = new Complaint();
            c5.setComplaintNumber("CMP-2026-10005");
            c5.setTrackingCode("TRACK05");
            c5.setStudent(jane);
            c5.setCategory(ragCat);
            c5.setDepartment(welfare);
            c5.setDescription("Senior students in Room 310 have been harassing juniors late at night.");
            c5.setLocation("Hostel Block A, Room 310");
            c5.setPriority(Priority.CRITICAL);
            c5.setStatus(ComplaintStatus.ESCALATED);
            c5.setPrivacyMode(PrivacyMode.ANONYMOUS);
            c5.setDueAt(LocalDateTime.now().minusHours(2));

            complaintRepository.saveAll(List.of(c1, c2, c3, c4, c5));
            log.info("  5 sample complaints inserted.");
        } else {
            log.info("  Complaints already seeded, skipping.");
        }

        log.info("=================================================================");
        log.info(" Login credentials (password: {})", DEFAULT_PASSWORD);
        log.info("  admin@campus.edu          -> ADMIN");
        log.info("  hod_it@campus.edu         -> DEPARTMENT_HEAD");
        log.info("  hostel_staff@campus.edu   -> STAFF");
        log.info("  it_staff@campus.edu       -> STAFF");
        log.info("  safety@campus.edu         -> SAFETY_OFFICER");
        log.info("  john@student.campus.edu   -> STUDENT");
        log.info("  jane@student.campus.edu   -> STUDENT");
        log.info("=================================================================");
    }

    // ── Helper builders ──────────────────────────────────────────────────────

    private Role role(String name) {
        Role r = new Role();
        r.setName(name);
        return r;
    }

    private Department dept(String name, String description) {
        Department d = new Department();
        d.setName(name);
        d.setDescription(description);
        return d;
    }

    private ComplaintCategory category(String name, ComplaintType type, Department dept) {
        ComplaintCategory c = new ComplaintCategory();
        c.setName(name);
        c.setType(type);
        c.setDefaultDepartment(dept);
        return c;
    }

    private User user(String name, String email, String password, Role role, Department department) {
        User u = new User();
        u.setName(name);
        u.setEmail(email);
        u.setPassword(password);
        u.setRole(role);
        u.setDepartment(department);
        return u;
    }

    private User student(String name, String email, String password, Role role,
                         String collegeId, int year, String section) {
        User u = user(name, email, password, role, null);
        u.setCollegeId(collegeId);
        u.setYear(year);
        u.setSection(section);
        return u;
    }
}
