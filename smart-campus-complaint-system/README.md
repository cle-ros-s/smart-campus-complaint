# Smart Campus Confidential Complaint & Resolution System

## Overview
A secure, confidential, and comprehensive full-stack web application designed for college campuses. It allows students to report various campus problems ranging from standard infrastructure issues (water, electricity) to sensitive concerns (ragging, harassment). 

## Technology Stack
- **Frontend**: HTML5, CSS3, Vanilla JavaScript (Fetch APIs, CSS Variables, Dark Mode)
- **Backend**: Java, Spring Boot, Spring Security (JWT, BCrypt), Spring Data JPA
- **Database**: MongoDB Atlas (Cloud NoSQL)

## Architecture
The application follows a standard layered architecture:
- **Presentation Layer (Frontend)**: Consumes REST APIs. Handles routing natively using distinct HTML pages.
- **Controller Layer (Backend)**: Exposes RESTful endpoints, handles parameter validation.
- **Service Layer**: Contains business logic (Complaint generation, SLA checking, Security handling).
- **Data Access Layer (Repository)**: Uses Spring Data MongoDB to interface with MongoDB Atlas.

## Database Design (MongoDB)
Data is stored as collections in MongoDB Atlas (`clement_db`):
- **users**: Central collection containing Students, Staff, Admins, and Safety Officers.
- **roles & permissions**: RBAC structure (`STUDENT`, `STAFF`, `DEPARTMENT_HEAD`, `ADMIN`, `SAFETY_OFFICER`).
- **departments**: Department records for ticket routing.
- **complaints**: Core ticket documents linked with references to student, category, department, and staff.
- **complaint_histories**: Tracks every state change (audit trail).
- **attachments**: File metadata documents.
- **escalations**: Overdue complaint escalation logs.
- **investigations**: Sensitive complaint investigation notes.

## Setup Instructions

### 1. MongoDB Setup
The application is connected directly to your MongoDB Atlas cluster:
`mongodb+srv://clement:clement@cluster0.urogep5.mongodb.net/clement_db`

**Sample Data is available in two ways:**
- **Automatic Seed (Recommended)**: On application startup, [`DataInitializer.java`](file:///c:/Users/Margrate%20Angel/Desktop/students%20complaint%20system/smart-campus-complaint-system/backend/src/main/java/com/campus/complaint/config/DataInitializer.java) checks MongoDB collections and auto-seeds all roles, departments, categories, test users, and sample complaints if they don't already exist.
- **Manual Script**: You can also view or run [`database/mongo-seed.js`](file:///c:/Users/Margrate%20Angel/Desktop/students%20complaint%20system/smart-campus-complaint-system/database/mongo-seed.js) directly in MongoDB Compass or `mongosh`.

### 2. Backend Setup
1. Navigate to the `backend/` directory.
2. Connection properties are configured in `src/main/resources/application.properties`.
3. Run the application: `.\mvnw.cmd spring-boot:run` (The server starts on port 8080 and auto-seeds data).

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
