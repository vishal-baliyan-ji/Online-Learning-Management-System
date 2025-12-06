package com.lms.dao;

import com.lms.model.Quiz;
import com.lms.util.DBConnection;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class QuizDAO {
    
    // Create a new quiz
    public static int createQuiz(Quiz quiz) {
        String query = "INSERT INTO quizzes (course_id, title, description, duration_minutes, max_score, passing_score, is_published) " +
                      "VALUES (?, ?, ?, ?, ?, ?, ?)";
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(query, Statement.RETURN_GENERATED_KEYS)) {
            stmt.setInt(1, quiz.getCourseId());
            stmt.setString(2, quiz.getTitle());
            stmt.setString(3, quiz.getDescription());
            stmt.setInt(4, quiz.getDurationMinutes());
            stmt.setInt(5, quiz.getMaxScore());
            stmt.setInt(6, quiz.getPassingScore());
            stmt.setBoolean(7, quiz.isPublished());
            
            int result = stmt.executeUpdate();
            if (result > 0) {
                ResultSet rs = stmt.getGeneratedKeys();
                if (rs.next()) {
                    return rs.getInt(1);
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return -1;
    }
    
    // Get quiz by ID
    public static Quiz getQuizById(int quizId) {
        String query = "SELECT * FROM quizzes WHERE quiz_id = ?";
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(query)) {
            stmt.setInt(1, quizId);
            ResultSet rs = stmt.executeQuery();
            
            if (rs.next()) {
                Quiz quiz = new Quiz();
                quiz.setQuizId(rs.getInt("quiz_id"));
                quiz.setCourseId(rs.getInt("course_id"));
                quiz.setTitle(rs.getString("title"));
                quiz.setDescription(rs.getString("description"));
                quiz.setDurationMinutes(rs.getInt("duration_minutes"));
                quiz.setMaxScore(rs.getInt("max_score"));
                quiz.setPassingScore(rs.getInt("passing_score"));
                quiz.setPublished(rs.getBoolean("is_published"));
                quiz.setCreatedAt(rs.getTimestamp("created_at"));
                quiz.setUpdatedAt(rs.getTimestamp("updated_at"));
                quiz.setPublishedDate(rs.getTimestamp("published_date"));
                return quiz;
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }
    
    // Get all quizzes for a course
    public static List<Quiz> getQuizzesByCourse(int courseId) {
        List<Quiz> quizzes = new ArrayList<>();
        String query = "SELECT * FROM quizzes WHERE course_id = ? ORDER BY created_at DESC";
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(query)) {
            stmt.setInt(1, courseId);
            ResultSet rs = stmt.executeQuery();
            
            while (rs.next()) {
                Quiz quiz = new Quiz();
                quiz.setQuizId(rs.getInt("quiz_id"));
                quiz.setCourseId(rs.getInt("course_id"));
                quiz.setTitle(rs.getString("title"));
                quiz.setDescription(rs.getString("description"));
                quiz.setDurationMinutes(rs.getInt("duration_minutes"));
                quiz.setMaxScore(rs.getInt("max_score"));
                quiz.setPassingScore(rs.getInt("passing_score"));
                quiz.setPublished(rs.getBoolean("is_published"));
                quiz.setCreatedAt(rs.getTimestamp("created_at"));
                quiz.setUpdatedAt(rs.getTimestamp("updated_at"));
                quiz.setPublishedDate(rs.getTimestamp("published_date"));
                quizzes.add(quiz);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return quizzes;
    }
    
    // Update quiz
    public static boolean updateQuiz(Quiz quiz) {
        String query = "UPDATE quizzes SET title = ?, description = ?, duration_minutes = ?, " +
                      "max_score = ?, passing_score = ?, is_published = ? WHERE quiz_id = ?";
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(query)) {
            stmt.setString(1, quiz.getTitle());
            stmt.setString(2, quiz.getDescription());
            stmt.setInt(3, quiz.getDurationMinutes());
            stmt.setInt(4, quiz.getMaxScore());
            stmt.setInt(5, quiz.getPassingScore());
            stmt.setBoolean(6, quiz.isPublished());
            stmt.setInt(7, quiz.getQuizId());
            
            return stmt.executeUpdate() > 0;
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return false;
    }
    
    // Publish quiz
    public static boolean publishQuiz(int quizId) {
        String query = "UPDATE quizzes SET is_published = true, published_date = NOW() WHERE quiz_id = ?";
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(query)) {
            stmt.setInt(1, quizId);
            return stmt.executeUpdate() > 0;
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return false;
    }
    
    // Delete quiz
    public static boolean deleteQuiz(int quizId) {
        String query = "DELETE FROM quizzes WHERE quiz_id = ?";
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(query)) {
            stmt.setInt(1, quizId);
            return stmt.executeUpdate() > 0;
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return false;
    }
}
