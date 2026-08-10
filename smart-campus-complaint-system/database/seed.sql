USE smart_campus;

-- Insert Roles
INSERT INTO roles (name) VALUES 
('STUDENT'), 
('STAFF'), 
('DEPARTMENT_HEAD'), 
('ADMIN'), 
('SAFETY_OFFICER');

-- Insert Permissions
INSERT INTO permissions (name) VALUES 
('VIEW_REPORTER_IDENTITY'),
('MANAGE_USERS'),
('MANAGE_CATEGORIES'),
('MANAGE_DEPARTMENTS');

-- Assign Permissions
INSERT INTO role_permissions (role_id, permission_id) 
SELECT r.id, p.id FROM roles r, permissions p 
WHERE r.name = 'SAFETY_OFFICER' AND p.name = 'VIEW_REPORTER_IDENTITY';

INSERT INTO role_permissions (role_id, permission_id) 
SELECT r.id, p.id FROM roles r, permissions p 
WHERE r.name = 'ADMIN' AND p.name IN ('MANAGE_USERS', 'MANAGE_CATEGORIES', 'MANAGE_DEPARTMENTS');

-- Insert Departments
INSERT INTO departments (name, description) VALUES
('Hostel Department', 'Handles hostel related issues'),
('IT Department', 'Handles internet and network issues'),
('Electrical Department', 'Handles electricity and wiring issues'),
('Maintenance Department', 'Handles water, cleaning and infrastructure'),
('Student Welfare', 'Handles student safety, anti-ragging and sensitive issues');

-- Insert SLA Rules
INSERT INTO sla_rules (priority, resolution_time_hours) VALUES
('LOW', 72),
('MEDIUM', 48),
('HIGH', 24),
('CRITICAL', 6);

-- Insert Complaint Categories
INSERT INTO complaint_categories (name, type, default_department_id) VALUES
('Hostel', 'NORMAL', 1),
('Internet/Wi-Fi', 'NORMAL', 2),
('Electricity', 'NORMAL', 3),
('Water', 'NORMAL', 4),
('Cleaning', 'NORMAL', 4),
('Infrastructure', 'NORMAL', 4),
('Ragging', 'SENSITIVE', 5),
('Harassment', 'SENSITIVE', 5),
('Suspected drug activity', 'SENSITIVE', 5);

-- Insert Development Users
-- Passwords should be hashed using BCrypt in the application. For seed data, these are plain text placeholders (should be replaced with BCrypt hashes in production).
-- Assuming '$2a$10$X...' is the bcrypt hash for 'password123' (will use a valid dummy hash)
SET @password_hash = '$2a$10$N9qo8uLOickgx2ZMRZoMyeIjZAgcfl7p92ldGxad68LJZdL17lhWy'; -- password123

INSERT INTO users (name, email, password, role_id, department_id) VALUES
('Admin User', 'admin@campus.edu', @password_hash, 4, NULL),
('Head of IT', 'hod_it@campus.edu', @password_hash, 3, 2),
('Hostel Staff 1', 'hostel_staff@campus.edu', @password_hash, 2, 1),
('IT Staff 1', 'it_staff@campus.edu', @password_hash, 2, 2),
('Safety Officer 1', 'safety@campus.edu', @password_hash, 5, 5);

INSERT INTO users (name, email, password, role_id, college_id, year, section) VALUES
('John Doe', 'john@student.campus.edu', @password_hash, 1, 'STU1001', 2, 'A'),
('Jane Smith', 'jane@student.campus.edu', @password_hash, 1, 'STU1002', 1, 'B'),
('Alice Brown', 'alice@student.campus.edu', @password_hash, 1, 'STU1003', 3, 'C'),
('Bob White', 'bob@student.campus.edu', @password_hash, 1, 'STU1004', 4, 'A'),
('Charlie Green', 'charlie@student.campus.edu', @password_hash, 1, 'STU1005', 2, 'B');

-- Insert Sample Complaints
INSERT INTO complaints (complaint_number, tracking_code, student_id, category_id, department_id, assigned_staff_id, description, location, priority, status, privacy_mode) VALUES
('CMP-2026-10001', 'TRACK01', 6, 1, 1, 3, 'Water leakage in Block B bathroom', 'Block B', 'HIGH', 'IN_PROGRESS', 'CONFIDENTIAL'),
('CMP-2026-10002', 'TRACK02', 7, 2, 2, 4, 'Internet not working in Library', 'Library', 'MEDIUM', 'RESOLVED', 'CONFIDENTIAL'),
('CMP-2026-10003', 'TRACK03', 8, 9, 5, NULL, 'Suspected drug activity near parking', 'Parking Lot A', 'CRITICAL', 'PENDING', 'ANONYMOUS');
