// ==============================================================================
// MongoDB Atlas Seed Script for Smart Campus Complaint System
// Database: clement_db
// Can be executed in MongoDB Compass or mongosh:
// mongosh "mongodb+srv://clement:clement@cluster0.urogep5.mongodb.net/clement_db" mongo-seed.js
// ==============================================================================

const db = db.getSiblingDB("clement_db");

// 1. ROLES
const studentRole = { _id: ObjectId("660000000000000000000001"), name: "STUDENT", permissions: [] };
const staffRole   = { _id: ObjectId("660000000000000000000002"), name: "STAFF", permissions: [] };
const hodRole     = { _id: ObjectId("660000000000000000000003"), name: "DEPARTMENT_HEAD", permissions: [] };
const adminRole   = { _id: ObjectId("660000000000000000000004"), name: "ADMIN", permissions: [] };
const safetyRole  = { _id: ObjectId("660000000000000000000005"), name: "SAFETY_OFFICER", permissions: [] };

db.roles.deleteMany({});
db.roles.insertMany([studentRole, staffRole, hodRole, adminRole, safetyRole]);

// 2. DEPARTMENTS
const hostelDept  = { _id: ObjectId("660000000000000000000011"), name: "Hostel Department", description: "Handles hostel related issues" };
const itDept      = { _id: ObjectId("660000000000000000000012"), name: "IT Department", description: "Handles internet and network issues" };
const elecDept    = { _id: ObjectId("660000000000000000000013"), name: "Electrical Department", description: "Handles electricity and wiring issues" };
const maintDept   = { _id: ObjectId("660000000000000000000014"), name: "Maintenance Department", description: "Handles water, cleaning and infrastructure" };
const welfareDept = { _id: ObjectId("660000000000000000000015"), name: "Student Welfare", description: "Handles student safety, anti-ragging and sensitive issues" };

db.departments.deleteMany({});
db.departments.insertMany([hostelDept, itDept, elecDept, maintDept, welfareDept]);

// 3. COMPLAINT CATEGORIES
const catHostel = { _id: ObjectId("660000000000000000000021"), name: "Hostel", type: "NORMAL", defaultDepartment: hostelDept._id };
const catWifi   = { _id: ObjectId("660000000000000000000022"), name: "Internet / Wi-Fi", type: "NORMAL", defaultDepartment: itDept._id };
const catElec   = { _id: ObjectId("660000000000000000000023"), name: "Electricity", type: "NORMAL", defaultDepartment: elecDept._id };
const catWater  = { _id: ObjectId("660000000000000000000024"), name: "Water", type: "NORMAL", defaultDepartment: maintDept._id };
const catClean  = { _id: ObjectId("660000000000000000000025"), name: "Cleaning", type: "NORMAL", defaultDepartment: maintDept._id };
const catInfra  = { _id: ObjectId("660000000000000000000026"), name: "Infrastructure", type: "NORMAL", defaultDepartment: maintDept._id };
const catRag    = { _id: ObjectId("660000000000000000000027"), name: "Ragging", type: "SENSITIVE", defaultDepartment: welfareDept._id };
const catHarass = { _id: ObjectId("660000000000000000000028"), name: "Harassment", type: "SENSITIVE", defaultDepartment: welfareDept._id };
const catDrugs  = { _id: ObjectId("660000000000000000000029"), name: "Suspected Drug Activity", type: "SENSITIVE", defaultDepartment: welfareDept._id };

db.complaint_categories.deleteMany({});
db.complaint_categories.insertMany([catHostel, catWifi, catElec, catWater, catClean, catInfra, catRag, catHarass, catDrugs]);

// 4. USERS (BCrypt hash for 'password123')
const passwordHash = "$2a$10$N9qo8uLOickgx2ZMRZoMyeIjZAgcfl7p92ldGxad68LJZdL17lhWy";

const adminUser   = { _id: ObjectId("660000000000000000000031"), name: "Admin User", email: "admin@campus.edu", password: passwordHash, role: adminRole._id, department: null };
const hodIt       = { _id: ObjectId("660000000000000000000032"), name: "Head of IT", email: "hod_it@campus.edu", password: passwordHash, role: hodRole._id, department: itDept._id };
const hostelStaff = { _id: ObjectId("660000000000000000000033"), name: "Hostel Staff 1", email: "hostel_staff@campus.edu", password: passwordHash, role: staffRole._id, department: hostelDept._id };
const itStaff     = { _id: ObjectId("660000000000000000000034"), name: "IT Staff 1", email: "it_staff@campus.edu", password: passwordHash, role: staffRole._id, department: itDept._id };
const safetyOff   = { _id: ObjectId("660000000000000000000035"), name: "Safety Officer", email: "safety@campus.edu", password: passwordHash, role: safetyRole._id, department: welfareDept._id };

const stuJohn    = { _id: ObjectId("660000000000000000000036"), name: "John Doe", email: "john@student.campus.edu", password: passwordHash, role: studentRole._id, collegeId: "STU1001", year: 2, section: "A" };
const stuJane    = { _id: ObjectId("660000000000000000000037"), name: "Jane Smith", email: "jane@student.campus.edu", password: passwordHash, role: studentRole._id, collegeId: "STU1002", year: 1, section: "B" };
const stuAlice   = { _id: ObjectId("660000000000000000000038"), name: "Alice Brown", email: "alice@student.campus.edu", password: passwordHash, role: studentRole._id, collegeId: "STU1003", year: 3, section: "C" };
const stuBob     = { _id: ObjectId("660000000000000000000039"), name: "Bob White", email: "bob@student.campus.edu", password: passwordHash, role: studentRole._id, collegeId: "STU1004", year: 4, section: "A" };
const stuCharlie = { _id: ObjectId("66000000000000000000003a"), name: "Charlie Green", email: "charlie@student.campus.edu", password: passwordHash, role: studentRole._id, collegeId: "STU1005", year: 2, section: "B" };

db.users.deleteMany({});
db.users.insertMany([adminUser, hodIt, hostelStaff, itStaff, safetyOff, stuJohn, stuJane, stuAlice, stuBob, stuCharlie]);

// 5. SAMPLE COMPLAINTS
db.complaints.deleteMany({});
db.complaints.insertMany([
  {
    complaintNumber: "CMP-2026-10001",
    trackingCode: "TRACK01",
    student: stuJohn._id,
    category: catHostel._id,
    department: hostelDept._id,
    assignedStaff: hostelStaff._id,
    description: "Water leakage in Block B bathroom causing flooding near Room 204.",
    location: "Block B, Room 204",
    priority: "HIGH",
    status: "IN_PROGRESS",
    privacyMode: "CONFIDENTIAL",
    createdAt: new Date(),
    dueAt: new Date(Date.now() + 24 * 60 * 60 * 1000)
  },
  {
    complaintNumber: "CMP-2026-10002",
    trackingCode: "TRACK02",
    student: stuJane._id,
    category: catWifi._id,
    department: itDept._id,
    assignedStaff: itStaff._id,
    description: "Internet not working in the Main Library — all Wi-Fi access points are offline.",
    location: "Main Library, 1st Floor",
    priority: "MEDIUM",
    status: "RESOLVED",
    privacyMode: "CONFIDENTIAL",
    createdAt: new Date(),
    resolvedAt: new Date(),
    dueAt: new Date(Date.now() - 5 * 60 * 60 * 1000)
  },
  {
    complaintNumber: "CMP-2026-10003",
    trackingCode: "TRACK03",
    student: stuAlice._id,
    category: catDrugs._id,
    department: welfareDept._id,
    assignedStaff: null,
    description: "Suspected drug activity observed near Parking Lot A after 10 PM on multiple occasions.",
    location: "Parking Lot A",
    priority: "CRITICAL",
    status: "PENDING",
    privacyMode: "ANONYMOUS",
    createdAt: new Date(),
    dueAt: new Date(Date.now() + 6 * 60 * 60 * 1000)
  },
  {
    complaintNumber: "CMP-2026-10004",
    trackingCode: "TRACK04",
    student: stuJohn._id,
    category: catWater._id,
    department: hostelDept._id,
    assignedStaff: null,
    description: "No water supply in Hostel Block C from 6 AM for the past 3 days.",
    location: "Hostel Block C",
    priority: "HIGH",
    status: "PENDING",
    privacyMode: "CONFIDENTIAL",
    createdAt: new Date(),
    dueAt: new Date(Date.now() + 24 * 60 * 60 * 1000)
  },
  {
    complaintNumber: "CMP-2026-10005",
    trackingCode: "TRACK05",
    student: stuJane._id,
    category: catRag._id,
    department: welfareDept._id,
    assignedStaff: null,
    description: "Senior students in Room 310 have been harassing juniors late at night.",
    location: "Hostel Block A, Room 310",
    priority: "CRITICAL",
    status: "ESCALATED",
    privacyMode: "ANONYMOUS",
    createdAt: new Date(),
    dueAt: new Date(Date.now() - 2 * 60 * 60 * 1000)
  }
]);

print("✅ MongoDB Atlas sample data successfully seeded into database: clement_db");
