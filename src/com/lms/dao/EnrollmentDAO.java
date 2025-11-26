package com.lms.dao;

import com.lms.model.Enrollment;
import com.lms.util.DBConnection;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;

/**
 * Enrollment Data Access Object
 * File: src/com/lms/dao/EnrollmentDAO.java
 * Handles all database operations for Enrollment
 */
public class EnrollmentDAO {
    
    // Create new enrollment
    public boolean addEnrollment(Enrollment enrollment) {
        String sql = "INSERT INTO enrollments (student_id, course_id, progress_percentage, status) " +
                     "VALUES (?, ?, ?, ?)";
        
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            
            pstmt.setInt(1, enrollment.getStudentId());
            pstmt.setInt(2, enrollment.getCourseId());
            pstmt.setDouble(3, enrollment.getProgressPercentage());
            pstmt.setString(4, enrollment.getStatus());
            
            int rowsAffected = pstmt.executeUpdate();
            return rowsAffected > 0;
            
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }
    
    // Get all enrollments
    public List<Enrollment> getAllEnrollments() {
        List<Enrollment> enrollments = new ArrayList<>();
        String sql = "SELECT e.enrollment_id, e.student_id, e.course_id, e.enrollment_date, " +
                     "e.progress_percentage, e.status, u.name as student_name, c.title as course_name " +
                     "FROM enrollments e " +
                     "JOIN users u ON e.student_id = u.user_id " +
                     "JOIN courses c ON e.course_id = c.course_id " +
                     "ORDER BY e.enrollment_date DESC";
        
        try (Connection conn = DBConnection.getConnection();
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {
            
            while (rs.next()) {
                Enrollment enrollment = new Enrollment();
                enrollment.setEnrollmentId(rs.getInt("enrollment_id"));
                enrollment.setStudentId(rs.getInt("student_id"));
                enrollment.setCourseId(rs.getInt("course_id"));
                enrollment.setEnrollmentDate(rs.getTimestamp("enrollment_date"));
                enrollment.setProgressPercentage(rs.getDouble("progress_percentage"));
                enrollment.setStatus(rs.getString("status"));
                enrollment.setStudentName(rs.getString("student_name"));
                enrollment.setCourseName(rs.getString("course_name"));
                enrollments.add(enrollment);
            }
            
        } catch (SQLException e) {
            e.printStackTrace();
        }
        
        return enrollments;
    }
    
    // Get enrollments by student
    public List<Enrollment> getEnrollmentsByStudent(int studentId) {
        List<Enrollment> enrollments = new ArrayList<>();
        String sql = "SELECT e.enrollment_id, e.student_id, e.course_id, e.enrollment_date, " +
             "e.progress_percentage, e.status, c.title as course_name, c.description as course_description, " +
             "u.name as instructor_name " +
             "FROM enrollments e " +
             "JOIN courses c ON e.course_id = c.course_id " +
             "LEFT JOIN users u ON c.instructor_id = u.user_id " +
             "WHERE e.student_id = ? AND e.status = 'active' " +
             "ORDER BY e.enrollment_date DESC";
        
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            
            pstmt.setInt(1, studentId);
            ResultSet rs = pstmt.executeQuery();
            
            while (rs.next()) {
                Enrollment enrollment = new Enrollment();
                enrollment.setEnrollmentId(rs.getInt("enrollment_id"));
                enrollment.setStudentId(rs.getInt("student_id"));
                enrollment.setCourseId(rs.getInt("course_id"));
                enrollment.setEnrollmentDate(rs.getTimestamp("enrollment_date"));
                enrollment.setProgressPercentage(rs.getDouble("progress_percentage"));
                enrollment.setStatus(rs.getString("status"));
                enrollment.setCourseName(rs.getString("course_name"));
                enrollment.setCourseDescription(rs.getString("course_description"));
                enrollment.setInstructorName(rs.getString("instructor_name"));
                enrollments.add(enrollment);
            }
            
        } catch (SQLException e) {
            e.printStackTrace();
        }
        
        return enrollments;
    }
    
    // Get enrollments by course
    public List<Enrollment> getEnrollmentsByCourse(int courseId) {
        List<Enrollment> enrollments = new ArrayList<>();
        String sql = "SELECT e.enrollment_id, e.student_id, e.course_id, e.enrollment_date, " +
                     "e.progress_percentage, e.status, u.name as student_name, u.email " +
                     "FROM enrollments e " +
                     "JOIN users u ON e.student_id = u.user_id " +
                     "WHERE e.course_id = ? " +
                     "ORDER BY e.enrollment_date DESC";
        
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            
            pstmt.setInt(1, courseId);
            ResultSet rs = pstmt.executeQuery();
            
            while (rs.next()) {
                Enrollment enrollment = new Enrollment();
                enrollment.setEnrollmentId(rs.getInt("enrollment_id"));
                enrollment.setStudentId(rs.getInt("student_id"));
                enrollment.setCourseId(rs.getInt("course_id"));
                enrollment.setEnrollmentDate(rs.getTimestamp("enrollment_date"));
                enrollment.setProgressPercentage(rs.getDouble("progress_percentage"));
                enrollment.setStatus(rs.getString("status"));
                enrollment.setStudentName(rs.getString("student_name"));
                enrollments.add(enrollment);
            }
            
        } catch (SQLException e) {
            e.printStackTrace();
        }
        
        return enrollments;
    }
    
    // Get enrollment by ID
    public Enrollment getEnrollmentById(int enrollmentId) {
        String sql = "SELECT e.enrollment_id, e.student_id, e.course_id, e.enrollment_date, " +
                     "e.progress_percentage, e.status, u.name as student_name, c.title as course_name " +
                     "FROM enrollments e " +
                     "JOIN users u ON e.student_id = u.user_id " +
                     "JOIN courses c ON e.course_id = c.course_id " +
                     "WHERE e.enrollment_id = ?";
        
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            
            pstmt.setInt(1, enrollmentId);
            ResultSet rs = pstmt.executeQuery();
            
            if (rs.next()) {
                Enrollment enrollment = new Enrollment();
                enrollment.setEnrollmentId(rs.getInt("enrollment_id"));
                enrollment.setStudentId(rs.getInt("student_id"));
                enrollment.setCourseId(rs.getInt("course_id"));
                enrollment.setEnrollmentDate(rs.getTimestamp("enrollment_date"));
                enrollment.setProgressPercentage(rs.getDouble("progress_percentage"));
                enrollment.setStatus(rs.getString("status"));
                enrollment.setStudentName(rs.getString("student_name"));
                enrollment.setCourseName(rs.getString("course_name"));
                return enrollment;
            }
            
        } catch (SQLException e) {
            e.printStackTrace();
        }
        
        return null;
    }
    
    // Update enrollment progress
    public boolean updateEnrollmentProgress(int enrollmentId, double progress) {
        String sql = "UPDATE enrollments SET progress_percentage = ? WHERE enrollment_id = ?";
        
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            
            pstmt.setDouble(1, progress);
            pstmt.setInt(2, enrollmentId);
            
            int rowsAffected = pstmt.executeUpdate();
            return rowsAffected > 0;
            
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }
    
    // Update enrollment status
    public boolean updateEnrollmentStatus(int enrollmentId, String status) {
        String sql = "UPDATE enrollments SET status = ? WHERE enrollment_id = ?";
        
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            
            pstmt.setString(1, status);
            pstmt.setInt(2, enrollmentId);
            
            int rowsAffected = pstmt.executeUpdate();
            return rowsAffected > 0;
            
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }
    
    // Delete enrollment
    public boolean deleteEnrollment(int enrollmentId) {
        String sql = "DELETE FROM enrollments WHERE enrollment_id = ?";
        
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            
            pstmt.setInt(1, enrollmentId);
            int rowsAffected = pstmt.executeUpdate();
            return rowsAffected > 0;
            
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }
    
    // Check if student is enrolled in course
    public boolean isStudentEnrolled(int studentId, int courseId) {
        // Only consider active enrollments as 'enrolled'
        String sql = "SELECT COUNT(*) FROM enrollments WHERE student_id = ? AND course_id = ? AND status = 'active'";
        
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            
            pstmt.setInt(1, studentId);
            pstmt.setInt(2, courseId);
            ResultSet rs = pstmt.executeQuery();
            
            if (rs.next()) {
                return rs.getInt(1) > 0;
            }
            
        } catch (SQLException e) {
            e.printStackTrace();
        }
        
        return false;
    }

    // Get enrollment by student and course (any status)
    public Enrollment getEnrollmentByStudentCourse(int studentId, int courseId) {
        String sql = "SELECT e.enrollment_id, e.student_id, e.course_id, e.enrollment_date, " +
                     "e.progress_percentage, e.status, u.name as student_name, c.title as course_name " +
                     "FROM enrollments e " +
                     "JOIN users u ON e.student_id = u.user_id " +
                     "JOIN courses c ON e.course_id = c.course_id " +
                     "WHERE e.student_id = ? AND e.course_id = ? LIMIT 1";

        try (Connection conn = DBConnection.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setInt(1, studentId);
            pstmt.setInt(2, courseId);
            ResultSet rs = pstmt.executeQuery();
            if (rs.next()) {
                Enrollment enrollment = new Enrollment();
                enrollment.setEnrollmentId(rs.getInt("enrollment_id"));
                enrollment.setStudentId(rs.getInt("student_id"));
                enrollment.setCourseId(rs.getInt("course_id"));
                enrollment.setEnrollmentDate(rs.getTimestamp("enrollment_date"));
                enrollment.setProgressPercentage(rs.getDouble("progress_percentage"));
                enrollment.setStatus(rs.getString("status"));
                enrollment.setStudentName(rs.getString("student_name"));
                enrollment.setCourseName(rs.getString("course_name"));
                return enrollment;
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }

    // Re-activate a previously dropped enrollment
    public boolean reEnrollStudent(int enrollmentId) {
        String sql = "UPDATE enrollments SET status = 'active', progress_percentage = 0.0, enrollment_date = CURRENT_TIMESTAMP WHERE enrollment_id = ?";
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setInt(1, enrollmentId);
            int rowsAffected = pstmt.executeUpdate();
            return rowsAffected > 0;
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }
    
    // Get total enrollment count
    public int getTotalEnrollmentCount() {
        String sql = "SELECT COUNT(*) FROM enrollments";
        
        try (Connection conn = DBConnection.getConnection();
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {
            
            if (rs.next()) {
                return rs.getInt(1);
            }
            
        } catch (SQLException e) {
            e.printStackTrace();
        }
        
        return 0;
    }
}