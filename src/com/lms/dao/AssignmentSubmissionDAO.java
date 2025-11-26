package com.lms.dao;

import com.lms.model.AssignmentSubmission;
import com.lms.util.DBConnection;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;

/**
 * AssignmentSubmission Data Access Object
 * File: src/com/lms/dao/AssignmentSubmissionDAO.java
 * Handles all database operations for Assignment Submissions
 */
public class AssignmentSubmissionDAO {
    
    // Create new submission
    public boolean addSubmission(AssignmentSubmission submission) {
        String sql = "INSERT INTO assignment_submissions (assignment_id, student_id, submission_text, file_path, status) " +
                     "VALUES (?, ?, ?, ?, ?)";
        
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            
            pstmt.setInt(1, submission.getAssignmentId());
            pstmt.setInt(2, submission.getStudentId());
            pstmt.setString(3, submission.getSubmissionText());
            pstmt.setString(4, submission.getFilePath());
            pstmt.setString(5, submission.getStatus());
            
            int rowsAffected = pstmt.executeUpdate();
            return rowsAffected > 0;
            
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }
    
    // Get all submissions
    public List<AssignmentSubmission> getAllSubmissions() {
        List<AssignmentSubmission> submissions = new ArrayList<>();
        String sql = "SELECT s.submission_id, s.assignment_id, s.student_id, s.submission_text, " +
                     "s.file_path, s.submitted_at, s.grade, s.feedback, s.graded_at, s.status, " +
                     "u.name as student_name, a.title as assignment_title " +
                     "FROM assignment_submissions s " +
                     "JOIN users u ON s.student_id = u.user_id " +
                     "JOIN assignments a ON s.assignment_id = a.assignment_id " +
                     "ORDER BY s.submitted_at DESC";
        
        try (Connection conn = DBConnection.getConnection();
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {
            
            while (rs.next()) {
                AssignmentSubmission submission = mapResultSetToSubmission(rs);
                submissions.add(submission);
            }
            
        } catch (SQLException e) {
            e.printStackTrace();
        }
        
        return submissions;
    }
    
    // Get submission by ID
    public AssignmentSubmission getSubmissionById(int submissionId) {
        String sql = "SELECT s.submission_id, s.assignment_id, s.student_id, s.submission_text, " +
                     "s.file_path, s.submitted_at, s.grade, s.feedback, s.graded_at, s.status, " +
                     "u.name as student_name, a.title as assignment_title " +
                     "FROM assignment_submissions s " +
                     "JOIN users u ON s.student_id = u.user_id " +
                     "JOIN assignments a ON s.assignment_id = a.assignment_id " +
                     "WHERE s.submission_id = ?";
        
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            
            pstmt.setInt(1, submissionId);
            ResultSet rs = pstmt.executeQuery();
            
            if (rs.next()) {
                return mapResultSetToSubmission(rs);
            }
            
        } catch (SQLException e) {
            e.printStackTrace();
        }
        
        return null;
    }
    
    // Get submissions by assignment
    public List<AssignmentSubmission> getSubmissionsByAssignment(int assignmentId) {
        List<AssignmentSubmission> submissions = new ArrayList<>();
        String sql = "SELECT s.submission_id, s.assignment_id, s.student_id, s.submission_text, " +
                     "s.file_path, s.submitted_at, s.grade, s.feedback, s.graded_at, s.status, " +
                     "u.name as student_name, a.title as assignment_title " +
                     "FROM assignment_submissions s " +
                     "JOIN users u ON s.student_id = u.user_id " +
                     "JOIN assignments a ON s.assignment_id = a.assignment_id " +
                     "WHERE s.assignment_id = ? " +
                     "ORDER BY s.submitted_at DESC";
        
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            
            pstmt.setInt(1, assignmentId);
            ResultSet rs = pstmt.executeQuery();
            
            while (rs.next()) {
                AssignmentSubmission submission = mapResultSetToSubmission(rs);
                submissions.add(submission);
            }
            
        } catch (SQLException e) {
            e.printStackTrace();
        }
        
        return submissions;
    }
    
    // Get submissions by student
    public List<AssignmentSubmission> getSubmissionsByStudent(int studentId) {
        List<AssignmentSubmission> submissions = new ArrayList<>();
        String sql = "SELECT s.submission_id, s.assignment_id, s.student_id, s.submission_text, " +
                     "s.file_path, s.submitted_at, s.grade, s.feedback, s.graded_at, s.status, " +
                     "u.name as student_name, a.title as assignment_title " +
                     "FROM assignment_submissions s " +
                     "JOIN users u ON s.student_id = u.user_id " +
                     "JOIN assignments a ON s.assignment_id = a.assignment_id " +
                     "WHERE s.student_id = ? " +
                     "ORDER BY s.submitted_at DESC";
        
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            
            pstmt.setInt(1, studentId);
            ResultSet rs = pstmt.executeQuery();
            
            while (rs.next()) {
                AssignmentSubmission submission = mapResultSetToSubmission(rs);
                submissions.add(submission);
            }
            
        } catch (SQLException e) {
            e.printStackTrace();
        }
        
        return submissions;
    }
    
    // Grade submission
    public boolean gradeSubmission(int submissionId, double grade, String feedback) {
        String sql = "UPDATE assignment_submissions SET grade = ?, feedback = ?, " +
                     "graded_at = CURRENT_TIMESTAMP, status = 'graded' WHERE submission_id = ?";
        
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            
            pstmt.setDouble(1, grade);
            pstmt.setString(2, feedback);
            pstmt.setInt(3, submissionId);
            
            int rowsAffected = pstmt.executeUpdate();
            return rowsAffected > 0;
            
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }
    
    // Update submission
    public boolean updateSubmission(AssignmentSubmission submission) {
        String sql = "UPDATE assignment_submissions SET submission_text = ?, file_path = ? " +
                     "WHERE submission_id = ?";
        
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            
            pstmt.setString(1, submission.getSubmissionText());
            pstmt.setString(2, submission.getFilePath());
            pstmt.setInt(3, submission.getSubmissionId());
            
            int rowsAffected = pstmt.executeUpdate();
            return rowsAffected > 0;
            
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }
    
    // Delete submission
    public boolean deleteSubmission(int submissionId) {
        String sql = "DELETE FROM assignment_submissions WHERE submission_id = ?";
        
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            
            pstmt.setInt(1, submissionId);
            int rowsAffected = pstmt.executeUpdate();
            return rowsAffected > 0;
            
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }
    
    // Check if student has submitted assignment
    public boolean hasStudentSubmitted(int assignmentId, int studentId) {
        String sql = "SELECT COUNT(*) FROM assignment_submissions " +
                     "WHERE assignment_id = ? AND student_id = ?";
        
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            
            pstmt.setInt(1, assignmentId);
            pstmt.setInt(2, studentId);
            ResultSet rs = pstmt.executeQuery();
            
            if (rs.next()) {
                return rs.getInt(1) > 0;
            }
            
        } catch (SQLException e) {
            e.printStackTrace();
        }
        
        return false;
    }

    // Get submission by assignment and student
    public AssignmentSubmission getSubmissionByAssignmentAndStudent(int assignmentId, int studentId) {
        String sql = "SELECT s.submission_id, s.assignment_id, s.student_id, s.submission_text, " +
                     "s.file_path, s.submitted_at, s.grade, s.feedback, s.graded_at, s.status, " +
                     "u.name as student_name, a.title as assignment_title " +
                     "FROM assignment_submissions s " +
                     "JOIN users u ON s.student_id = u.user_id " +
                     "JOIN assignments a ON s.assignment_id = a.assignment_id " +
                     "WHERE s.assignment_id = ? AND s.student_id = ? LIMIT 1";

        try (Connection conn = DBConnection.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {

            pstmt.setInt(1, assignmentId);
            pstmt.setInt(2, studentId);
            ResultSet rs = pstmt.executeQuery();

            if (rs.next()) {
                return mapResultSetToSubmission(rs);
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }

        return null;
    }
    
    // Get pending submissions count for instructor
    public int getPendingSubmissionsCount(int instructorId) {
        String sql = "SELECT COUNT(*) FROM assignment_submissions s " +
                     "JOIN assignments a ON s.assignment_id = a.assignment_id " +
                     "JOIN courses c ON a.course_id = c.course_id " +
                     "WHERE c.instructor_id = ? AND s.status = 'submitted'";
        
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            
            pstmt.setInt(1, instructorId);
            ResultSet rs = pstmt.executeQuery();
            
            if (rs.next()) {
                return rs.getInt(1);
            }
            
        } catch (SQLException e) {
            e.printStackTrace();
        }
        
        return 0;
    }
    
    // Helper method to map ResultSet to AssignmentSubmission object
    private AssignmentSubmission mapResultSetToSubmission(ResultSet rs) throws SQLException {
        AssignmentSubmission submission = new AssignmentSubmission();
        submission.setSubmissionId(rs.getInt("submission_id"));
        submission.setAssignmentId(rs.getInt("assignment_id"));
        submission.setStudentId(rs.getInt("student_id"));
        submission.setSubmissionText(rs.getString("submission_text"));
        submission.setFilePath(rs.getString("file_path"));
        submission.setSubmittedAt(rs.getTimestamp("submitted_at"));
        submission.setGrade(rs.getDouble("grade"));
        submission.setFeedback(rs.getString("feedback"));
        submission.setGradedAt(rs.getTimestamp("graded_at"));
        submission.setStatus(rs.getString("status"));
        submission.setStudentName(rs.getString("student_name"));
        submission.setAssignmentTitle(rs.getString("assignment_title"));
        return submission;
    }
}