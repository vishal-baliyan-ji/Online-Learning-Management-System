package com.lms.dao;

import com.lms.model.Assignment;
import com.lms.util.DBConnection;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;

/**
 * Assignment Data Access Object
 * File: src/com/lms/dao/AssignmentDAO.java
 * Handles all database operations for Assignment
 */
public class AssignmentDAO {
    
    // Create new assignment
    public boolean addAssignment(Assignment assignment) {
        String sql = "INSERT INTO assignments (course_id, title, description, due_date, max_score, attachment_path) " +
                     "VALUES (?, ?, ?, ?, ?, ?)";
        
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            
            pstmt.setInt(1, assignment.getCourseId());
            pstmt.setString(2, assignment.getTitle());
            pstmt.setString(3, assignment.getDescription());
            pstmt.setDate(4, assignment.getDueDate());
            pstmt.setInt(5, assignment.getMaxScore());
            pstmt.setString(6, assignment.getAttachmentPath());
            
            int rowsAffected = pstmt.executeUpdate();
            return rowsAffected > 0;
            
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }
    
    // Get all assignments
    public List<Assignment> getAllAssignments() {
        List<Assignment> assignments = new ArrayList<>();
        String sql = "SELECT a.assignment_id, a.course_id, a.title, a.description, a.due_date, " +
                     "a.max_score, a.attachment_path, a.created_at, c.title as course_name " +
                     "FROM assignments a " +
                     "JOIN courses c ON a.course_id = c.course_id " +
                     "ORDER BY a.due_date DESC";
        
        try (Connection conn = DBConnection.getConnection();
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {
            
            while (rs.next()) {
                Assignment assignment = new Assignment();
                assignment.setAssignmentId(rs.getInt("assignment_id"));
                assignment.setCourseId(rs.getInt("course_id"));
                assignment.setTitle(rs.getString("title"));
                assignment.setDescription(rs.getString("description"));
                assignment.setDueDate(rs.getDate("due_date"));
                assignment.setMaxScore(rs.getInt("max_score"));
                assignment.setAttachmentPath(rs.getString("attachment_path"));
                assignment.setCreatedAt(rs.getTimestamp("created_at"));
                assignment.setCourseName(rs.getString("course_name"));
                assignments.add(assignment);
            }
            
        } catch (SQLException e) {
            e.printStackTrace();
        }
        
        return assignments;
    }
    
    // Get assignment by ID
    public Assignment getAssignmentById(int assignmentId) {
        String sql = "SELECT a.assignment_id, a.course_id, a.title, a.description, a.due_date, " +
                     "a.max_score, a.attachment_path, a.created_at, c.title as course_name " +
                     "FROM assignments a " +
                     "JOIN courses c ON a.course_id = c.course_id " +
                     "WHERE a.assignment_id = ?";
        
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            
            pstmt.setInt(1, assignmentId);
            ResultSet rs = pstmt.executeQuery();
            
            if (rs.next()) {
                Assignment assignment = new Assignment();
                assignment.setAssignmentId(rs.getInt("assignment_id"));
                assignment.setCourseId(rs.getInt("course_id"));
                assignment.setTitle(rs.getString("title"));
                assignment.setDescription(rs.getString("description"));
                assignment.setDueDate(rs.getDate("due_date"));
                assignment.setMaxScore(rs.getInt("max_score"));
                assignment.setAttachmentPath(rs.getString("attachment_path"));
                assignment.setCreatedAt(rs.getTimestamp("created_at"));
                assignment.setCourseName(rs.getString("course_name"));
                return assignment;
            }
            
        } catch (SQLException e) {
            e.printStackTrace();
        }
        
        return null;
    }
    
    // Get assignments by course
    public List<Assignment> getAssignmentsByCourse(int courseId) {
        List<Assignment> assignments = new ArrayList<>();
        String sql = "SELECT a.assignment_id, a.course_id, a.title, a.description, a.due_date, " +
                     "a.max_score, a.attachment_path, a.created_at, c.title as course_name " +
                     "FROM assignments a " +
                     "JOIN courses c ON a.course_id = c.course_id " +
                     "WHERE a.course_id = ? " +
                     "ORDER BY a.due_date DESC";
        
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            
            pstmt.setInt(1, courseId);
            ResultSet rs = pstmt.executeQuery();
            
            while (rs.next()) {
                Assignment assignment = new Assignment();
                assignment.setAssignmentId(rs.getInt("assignment_id"));
                assignment.setCourseId(rs.getInt("course_id"));
                assignment.setTitle(rs.getString("title"));
                assignment.setDescription(rs.getString("description"));
                assignment.setDueDate(rs.getDate("due_date"));
                assignment.setMaxScore(rs.getInt("max_score"));
                assignment.setAttachmentPath(rs.getString("attachment_path"));
                assignment.setCreatedAt(rs.getTimestamp("created_at"));
                assignment.setCourseName(rs.getString("course_name"));
                assignments.add(assignment);
            }
            
        } catch (SQLException e) {
            e.printStackTrace();
        }
        
        return assignments;
    }
    
    // Update assignment
    public boolean updateAssignment(Assignment assignment) {
        String sql = "UPDATE assignments SET title = ?, description = ?, due_date = ?, max_score = ?, attachment_path = ? " +
                     "WHERE assignment_id = ?";
        
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            
            pstmt.setString(1, assignment.getTitle());
            pstmt.setString(2, assignment.getDescription());
            pstmt.setDate(3, assignment.getDueDate());
            pstmt.setInt(4, assignment.getMaxScore());
            pstmt.setString(5, assignment.getAttachmentPath());
            pstmt.setInt(6, assignment.getAssignmentId());
            
            int rowsAffected = pstmt.executeUpdate();
            return rowsAffected > 0;
            
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }
    
    // Delete assignment
    public boolean deleteAssignment(int assignmentId) {
        String sql = "DELETE FROM assignments WHERE assignment_id = ?";
        
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            
            pstmt.setInt(1, assignmentId);
            int rowsAffected = pstmt.executeUpdate();
            return rowsAffected > 0;
            
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }
    
    // Get total assignment count
    public int getTotalAssignmentCount() {
        String sql = "SELECT COUNT(*) FROM assignments";
        
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
    
    // Get assignments for a student's enrolled courses
    public List<Assignment> getAssignmentsForStudent(int studentId) {
        List<Assignment> assignments = new ArrayList<>();
        String sql = "SELECT DISTINCT a.assignment_id, a.course_id, a.title, a.description, " +
                     "a.due_date, a.max_score, a.attachment_path, c.title as course_name " +
                     "FROM assignments a " +
                     "JOIN courses c ON a.course_id = c.course_id " +
                     "JOIN enrollments e ON c.course_id = e.course_id " +
                     "WHERE e.student_id = ? AND e.status = 'active' " +
                     "ORDER BY a.due_date DESC";
        
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            
            pstmt.setInt(1, studentId);
            ResultSet rs = pstmt.executeQuery();
            
            while (rs.next()) {
                Assignment assignment = new Assignment();
                assignment.setAssignmentId(rs.getInt("assignment_id"));
                assignment.setCourseId(rs.getInt("course_id"));
                assignment.setTitle(rs.getString("title"));
                assignment.setDescription(rs.getString("description"));
                assignment.setDueDate(rs.getDate("due_date"));
                assignment.setMaxScore(rs.getInt("max_score"));
                assignment.setAttachmentPath(rs.getString("attachment_path"));
                assignment.setCourseName(rs.getString("course_name"));
                assignments.add(assignment);
            }
            
        } catch (SQLException e) {
            e.printStackTrace();
        }
        
        return assignments;
    }
}