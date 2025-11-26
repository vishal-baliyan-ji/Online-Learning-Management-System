package com.lms.dao;

import com.lms.model.Course;
import com.lms.util.DBConnection;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;

/**
 * Course Data Access Object
 * File: src/com/lms/dao/CourseDAO.java
 * Handles all database operations for Course
 */
public class CourseDAO {
    
    // Create new course
    public boolean addCourse(Course course) {
        String sql = "INSERT INTO courses (title, description, syllabus, instructor_id) VALUES (?, ?, ?, ?)";
        
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            
            pstmt.setString(1, course.getTitle());
            pstmt.setString(2, course.getDescription());
            pstmt.setString(3, course.getSyllabus());
            pstmt.setInt(4, course.getInstructorId());
            
            int rowsAffected = pstmt.executeUpdate();
            return rowsAffected > 0;
            
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }
    
    // Get all courses
    public List<Course> getAllCourses() {
        List<Course> courses = new ArrayList<>();
        String sql = "SELECT c.course_id, c.title, c.description, c.syllabus, c.instructor_id, " +
                     "u.name as instructor_name, c.created_at " +
                     "FROM courses c " +
                     "LEFT JOIN users u ON c.instructor_id = u.user_id " +
                     "ORDER BY c.course_id DESC";
        
        try (Connection conn = DBConnection.getConnection();
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {
            
            while (rs.next()) {
                Course course = new Course();
                course.setCourseId(rs.getInt("course_id"));
                course.setTitle(rs.getString("title"));
                course.setDescription(rs.getString("description"));
                course.setSyllabus(rs.getString("syllabus"));
                course.setInstructorId(rs.getInt("instructor_id"));
                course.setInstructorName(rs.getString("instructor_name"));
                course.setCreatedAt(rs.getTimestamp("created_at"));
                courses.add(course);
            }
            
        } catch (SQLException e) {
            e.printStackTrace();
        }
        
        return courses;
    }
    
    // Get course by ID
    public Course getCourseById(int courseId) {
        String sql = "SELECT c.course_id, c.title, c.description, c.syllabus, c.instructor_id, " +
                     "u.name as instructor_name, c.created_at " +
                     "FROM courses c " +
                     "LEFT JOIN users u ON c.instructor_id = u.user_id " +
                     "WHERE c.course_id = ?";
        
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            
            pstmt.setInt(1, courseId);
            ResultSet rs = pstmt.executeQuery();
            
            if (rs.next()) {
                Course course = new Course();
                course.setCourseId(rs.getInt("course_id"));
                course.setTitle(rs.getString("title"));
                course.setDescription(rs.getString("description"));
                course.setSyllabus(rs.getString("syllabus"));
                course.setInstructorId(rs.getInt("instructor_id"));
                course.setInstructorName(rs.getString("instructor_name"));
                course.setCreatedAt(rs.getTimestamp("created_at"));
                return course;
            }
            
        } catch (SQLException e) {
            e.printStackTrace();
        }
        
        return null;
    }
    
    // Get courses by instructor
    public List<Course> getCoursesByInstructor(int instructorId) {
        List<Course> courses = new ArrayList<>();
        String sql = "SELECT c.course_id, c.title, c.description, c.syllabus, c.instructor_id, " +
                     "u.name as instructor_name, c.created_at " +
                     "FROM courses c " +
                     "LEFT JOIN users u ON c.instructor_id = u.user_id " +
                     "WHERE c.instructor_id = ? " +
                     "ORDER BY c.created_at DESC";
        
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            
            pstmt.setInt(1, instructorId);
            ResultSet rs = pstmt.executeQuery();
            
            while (rs.next()) {
                Course course = new Course();
                course.setCourseId(rs.getInt("course_id"));
                course.setTitle(rs.getString("title"));
                course.setDescription(rs.getString("description"));
                course.setSyllabus(rs.getString("syllabus"));
                course.setInstructorId(rs.getInt("instructor_id"));
                course.setInstructorName(rs.getString("instructor_name"));
                course.setCreatedAt(rs.getTimestamp("created_at"));
                courses.add(course);
            }
            
        } catch (SQLException e) {
            e.printStackTrace();
        }
        
        return courses;
    }
    
    // Update course
    public boolean updateCourse(Course course) {
        String sql = "UPDATE courses SET title = ?, description = ?, syllabus = ?, instructor_id = ? " +
                     "WHERE course_id = ?";
        
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            
            pstmt.setString(1, course.getTitle());
            pstmt.setString(2, course.getDescription());
            pstmt.setString(3, course.getSyllabus());
            pstmt.setInt(4, course.getInstructorId());
            pstmt.setInt(5, course.getCourseId());
            
            int rowsAffected = pstmt.executeUpdate();
            return rowsAffected > 0;
            
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }
    
    // Delete course
    public boolean deleteCourse(int courseId) {
        String sql = "DELETE FROM courses WHERE course_id = ?";
        
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            
            pstmt.setInt(1, courseId);
            int rowsAffected = pstmt.executeUpdate();
            return rowsAffected > 0;
            
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }
    
    // Get total course count
    public int getTotalCourseCount() {
        String sql = "SELECT COUNT(*) FROM courses";
        
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
    
    // Get enrollment count for a course
    public int getCourseEnrollmentCount(int courseId) {
        String sql = "SELECT COUNT(*) FROM enrollments WHERE course_id = ? AND status = 'active'";
        
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            
            pstmt.setInt(1, courseId);
            ResultSet rs = pstmt.executeQuery();
            
            if (rs.next()) {
                return rs.getInt(1);
            }
            
        } catch (SQLException e) {
            e.printStackTrace();
        }
        
        return 0;
    }
}