-- ============================================================
-- DATABASE SCHEMA FOR LEARNING MANAGEMENT SYSTEM
-- File: database_schema.sql
-- ============================================================

CREATE DATABASE IF NOT EXISTS lms_db;
USE lms_db;

-- Users Table
CREATE TABLE users (
    user_id INT PRIMARY KEY AUTO_INCREMENT,
    name VARCHAR(100) NOT NULL,
    email VARCHAR(100) NOT NULL UNIQUE,
    password VARCHAR(255) NOT NULL,
    role ENUM('admin', 'instructor', 'student') NOT NULL,
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP
);

-- Courses Table
CREATE TABLE courses (
    course_id INT PRIMARY KEY AUTO_INCREMENT,
    title VARCHAR(200) NOT NULL,
    description TEXT,
    syllabus TEXT,
    instructor_id INT,
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
    FOREIGN KEY (instructor_id) REFERENCES users(user_id) ON DELETE SET NULL
);

-- Course Materials Table
CREATE TABLE course_materials (
    material_id INT PRIMARY KEY AUTO_INCREMENT,
    course_id INT NOT NULL,
    title VARCHAR(200) NOT NULL,
    content TEXT,
    file_path VARCHAR(500),
    material_type ENUM('document', 'video', 'pdf', 'link') DEFAULT 'document',
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    FOREIGN KEY (course_id) REFERENCES courses(course_id) ON DELETE CASCADE
);

-- Assignments Table
CREATE TABLE assignments (
    assignment_id INT PRIMARY KEY AUTO_INCREMENT,
    course_id INT NOT NULL,
    title VARCHAR(200) NOT NULL,
    description TEXT,
    due_date DATE,
    max_score INT DEFAULT 100,
    attachment_path VARCHAR(500),
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    FOREIGN KEY (course_id) REFERENCES courses(course_id) ON DELETE CASCADE
);

-- Quizzes Table
CREATE TABLE quizzes (
    quiz_id INT PRIMARY KEY AUTO_INCREMENT,
    course_id INT NOT NULL,
    title VARCHAR(200) NOT NULL,
    description TEXT,
    duration_minutes INT,
    max_score INT DEFAULT 100,
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    FOREIGN KEY (course_id) REFERENCES courses(course_id) ON DELETE CASCADE
);

-- Enrollments Table
CREATE TABLE enrollments (
    enrollment_id INT PRIMARY KEY AUTO_INCREMENT,
    student_id INT NOT NULL,
    course_id INT NOT NULL,
    enrollment_date TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    progress_percentage DECIMAL(5,2) DEFAULT 0.00,
    status ENUM('active', 'completed', 'dropped') DEFAULT 'active',
    FOREIGN KEY (student_id) REFERENCES users(user_id) ON DELETE CASCADE,
    FOREIGN KEY (course_id) REFERENCES courses(course_id) ON DELETE CASCADE,
    UNIQUE KEY unique_enrollment (student_id, course_id)
);

-- Assignment Submissions Table
CREATE TABLE assignment_submissions (
    submission_id INT PRIMARY KEY AUTO_INCREMENT,
    assignment_id INT NOT NULL,
    student_id INT NOT NULL,
    submission_text TEXT,
    file_path VARCHAR(500),
    submitted_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    grade DECIMAL(5,2),
    feedback TEXT,
    graded_at TIMESTAMP NULL,
    status ENUM('submitted', 'graded', 'pending') DEFAULT 'submitted',
    FOREIGN KEY (assignment_id) REFERENCES assignments(assignment_id) ON DELETE CASCADE,
    FOREIGN KEY (student_id) REFERENCES users(user_id) ON DELETE CASCADE
);

-- Quiz Attempts Table
CREATE TABLE quiz_attempts (
    attempt_id INT PRIMARY KEY AUTO_INCREMENT,
    quiz_id INT NOT NULL,
    student_id INT NOT NULL,
    score DECIMAL(5,2),
    attempted_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    FOREIGN KEY (quiz_id) REFERENCES quizzes(quiz_id) ON DELETE CASCADE,
    FOREIGN KEY (student_id) REFERENCES users(user_id) ON DELETE CASCADE
);

-- System Settings Table
CREATE TABLE system_settings (
    setting_id INT PRIMARY KEY AUTO_INCREMENT,
    setting_key VARCHAR(100) NOT NULL UNIQUE,
    setting_value TEXT,
    updated_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP
);

-- Sample Data
-- Sample Data (passwords MUST be stored as bcrypt hashes)
-- IMPORTANT: This project now uses BCrypt for password hashing.
-- Do NOT leave plaintext passwords here. Replace the placeholders below with
-- bcrypt hashes produced by a secure tool (e.g., `PasswordUtil` in src).
-- Example: generate a hash locally using the project's PasswordUtil helper:
--   mvn clean package
--   mvn exec:java -Dexec.mainClass=com.lms.util.PasswordUtil -Dexec.args="admin123"
-- Then replace <bcrypt-hash-for-admin123> with the generated hash string.

INSERT INTO users (name, email, password, role) VALUES
('Admin User', 'admin@lms.com', '$2a$10$2ye1mjik.De2f0CNAvsKmOOHl6QWWF0adGqHooL8EIiPVF48zW9A2', 'admin'),
('John Instructor', 'john@lms.com', '$2a$10$SF..Dub.2tY6ByAPkA9QCOYAsPB1V0DQuKj077o.tpoOLX3VKKreO', 'instructor'),
('Jane Student', 'jane@lms.com', '$2a$10$gN.DuHvwAXV074FdgqQQweZ6Ka.4.1q8Uf52.JXPv85pth0IvD0vy', 'student'),
('Bob Instructor', 'bob@lms.com', '$2a$10$SF..Dub.2tY6ByAPkA9QCOYAsPB1V0DQuKj077o.tpoOLX3VKKreO', 'instructor'),
('Alice Student', 'alice@lms.com', '$2a$10$gN.DuHvwAXV074FdgqQQweZ6Ka.4.1q8Uf52.JXPv85pth0IvD0vy', 'student');

INSERT INTO courses (title, description, syllabus, instructor_id) VALUES
('Java Programming', 'Learn Java from basics to advanced', 'Week 1: Basics, Week 2: OOP, Week 3: Collections', 2),
('Web Development', 'HTML, CSS, JavaScript, and more', 'Week 1: HTML/CSS, Week 2: JavaScript, Week 3: Backend', 4),
('Data Structures', 'Comprehensive DS course', 'Week 1: Arrays, Week 2: LinkedList, Week 3: Trees', 2);

INSERT INTO enrollments (student_id, course_id, progress_percentage) VALUES
(3, 1, 45.5),
(5, 1, 67.3),
(3, 2, 23.0),
(5, 3, 89.2);

INSERT INTO assignments (course_id, title, description, due_date, max_score) VALUES
(1, 'Java Basics Assignment', 'Complete the exercises', '2025-12-01', 100),
(1, 'OOP Project', 'Build a small OOP project', '2025-12-15', 150),
(2, 'HTML/CSS Portfolio', 'Create your portfolio', '2025-12-10', 100);

-- NOTE: Sample users have been seeded with bcrypt-hashed passwords:
-- admin@lms.com / admin123
-- john@lms.com / instructor123
-- jane@lms.com / student123
-- bob@lms.com / instructor123
-- alice@lms.com / student123
-- 
-- You can now import this schema directly into your DBMS and log in with these credentials.