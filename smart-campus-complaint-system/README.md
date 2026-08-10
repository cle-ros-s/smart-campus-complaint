# Smart Campus Confidential Complaint & Resolution System

## Overview
A secure, confidential, and comprehensive full-stack web application designed for college campuses. It allows students to report various campus problems ranging from standard infrastructure issues (water, electricity) to sensitive concerns (ragging, harassment). 

## Technology Stack
- **Frontend**: HTML5, CSS3, Vanilla JavaScript (Fetch APIs, CSS Variables, Dark Mode)
- **Backend**: Java, Spring Boot, Spring Security (JWT, BCrypt), Spring Data JPA
- **Database**: MySQL

## Architecture
The application follows a standard layered architecture:
- **Presentation Layer (Frontend)**: Consumes REST APIs. Handles routing natively using distinct HTML pages.
- **Controller Layer (Backend)**: Exposes RESTful endpoints, handles parameter validation.
- **Service Layer**: Contains business logic (Complaint generation, SLA checking, Security handling).
- **Data Access Layer (Repository)**: Uses Spring Data JPA to interface with MySQL.

## Database Design & ER Diagram Explanation
The database is fully normalized.
- **Users**: Central entity containing Students, Staff, Admins, and Investigators.
- **Roles & Permissions**: RBAC structure. Role handles broad access (e.g., `STAFF`), Permission handles fine-grained access (e.g., `VIEW_REPORTER_IDENTITY`).
- **Departments**: Mapped to Users (Staff) and Complaints (for routing).
- **Complaints**: The core entity. Linked to Student, Category, Department, and Assigned Staff. Contains status, priority, and privacy mode.
- **Complaint History**: Tracks every state change (audit trail).
- **Attachments**: Links file metadata to a Complaint.
- **SLA Rules & Escalations**: Tracks overdue complaints.
- **Investigations**: Specialized entity for handling sensitive `SENSITIVE` type complaints securely.

## Setup Instructions

### 1. MySQL Setup
1. Create database: `CREATE DATABASE smart_campus;`
2. Run `database/schema.sql` to generate tables.
3. Run `database/seed.sql` to populate initial data (Roles, Departments, Categories, Admin User).

### 2. Backend Setup
1. Navigate to the `backend/` directory.
2. Update `src/main/resources/application.properties` with your MySQL `username` and `password`.
3. Run the application: `mvn spring-boot:run`. (The server starts on port 8080).

### 3. Frontend Setup
1. The frontend requires no build steps. 
2. Serve the `frontend/` directory using any local web server (e.g., VS Code Live Server, or Python's `python -m http.server`).
3. Open `index.html`.

## Test Credentials (from Seed)
- **Admin**: admin@campus.edu / password123
- **Student**: john@student.campus.edu / password123
- **IT Staff**: it_staff@campus.edu / password123
- **Safety Officer**: safety@campus.edu / password123

*(Note: In the seed file, BCrypt hashes are used for "password123")*

## Security & Privacy Implementation
- **Authentication**: Stateless JWT tokens passed via Authorization header.
- **Authorization**: `@PreAuthorize` used extensively on controllers.
- **Privacy Mode**: Two modes: `CONFIDENTIAL` (identity known to system, hidden from staff) and `ANONYMOUS` (tracked via `Tracking Code`).
- **Identity Access**: Only roles with `VIEW_REPORTER_IDENTITY` can call the `/api/investigations/reporter/{id}` endpoint.
- **Audit Logging**: Every identity access and state transition is logged in `AuditLog` and `ComplaintHistory`.

## Future Enhancements
- Email/SMS Notifications
- Mobile Application
- Cloud Object Storage Integration (AWS S3) for attachments.
