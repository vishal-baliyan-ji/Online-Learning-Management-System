package com.lms.dao;

import com.lms.model.QuizAttempt;
import com.lms.util.DBConnection;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class QuizAttemptDAO {
    
    // Start a quiz attempt
    public static int startAttempt(int quizId, int studentId) {
        String query = "INSERT INTO quiz_attempts (quiz_id, student_id, status, started_at) " +
                      "VALUES (?, ?, 'in_progress', NOW())";
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(query, Statement.RETURN_GENERATED_KEYS)) {
            stmt.setInt(1, quizId);
            stmt.setInt(2, studentId);
            
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
    
    // Get attempt by ID
    public static QuizAttempt getAttemptById(int attemptId) {
        String query = "SELECT * FROM quiz_attempts WHERE attempt_id = ?";
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(query)) {
            stmt.setInt(1, attemptId);
            ResultSet rs = stmt.executeQuery();
            
            if (rs.next()) {
                return mapResultSetToAttempt(rs);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }
    
    // Get student's attempt for a quiz (in progress or latest)
    public static QuizAttempt getStudentAttempt(int quizId, int studentId) {
        String query = "SELECT * FROM quiz_attempts WHERE quiz_id = ? AND student_id = ? ORDER BY started_at DESC LIMIT 1";
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(query)) {
            stmt.setInt(1, quizId);
            stmt.setInt(2, studentId);
            ResultSet rs = stmt.executeQuery();
            
            if (rs.next()) {
                return mapResultSetToAttempt(rs);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }
    
    // Get all attempts for a quiz (for instructor grading)
    public static List<QuizAttempt> getQuizAttempts(int quizId) {
        List<QuizAttempt> attempts = new ArrayList<>();
        String query = "SELECT * FROM quiz_attempts WHERE quiz_id = ? ORDER BY started_at DESC";
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(query)) {
            stmt.setInt(1, quizId);
            ResultSet rs = stmt.executeQuery();
            
            while (rs.next()) {
                attempts.add(mapResultSetToAttempt(rs));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return attempts;
    }
    
    // Submit quiz attempt
    public static boolean submitAttempt(int attemptId, int score) {
        String query = "UPDATE quiz_attempts SET status = 'submitted', submitted_at = NOW(), score = ? WHERE attempt_id = ?";
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(query)) {
            stmt.setInt(1, score);
            stmt.setInt(2, attemptId);
            
            return stmt.executeUpdate() > 0;
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return false;
    }
    
    // Grade attempt (instructor only)
    public static boolean gradeAttempt(int attemptId, int score, String feedback) {
        String query = "UPDATE quiz_attempts SET status = 'graded', graded_at = NOW(), score = ?, feedback = ? WHERE attempt_id = ?";
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(query)) {
            stmt.setInt(1, score);
            stmt.setString(2, feedback);
            stmt.setInt(3, attemptId);
            
            return stmt.executeUpdate() > 0;
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return false;
    }

    // Save student's answers for an attempt
    public static boolean saveAnswers(int attemptId, java.util.Map<Integer, String> answers) {
        String deleteQuery = "DELETE FROM quiz_attempt_answers WHERE attempt_id = ?";
        String insertQuery = "INSERT INTO quiz_attempt_answers (attempt_id, question_id, answer) VALUES (?, ?, ?)";
        try (Connection conn = DBConnection.getConnection()) {
            // delete any existing answers for this attempt (idempotent)
            try (PreparedStatement del = conn.prepareStatement(deleteQuery)) {
                del.setInt(1, attemptId);
                del.executeUpdate();
            }

            try (PreparedStatement ins = conn.prepareStatement(insertQuery)) {
                for (java.util.Map.Entry<Integer, String> e : answers.entrySet()) {
                    ins.setInt(1, attemptId);
                    ins.setInt(2, e.getKey());
                    ins.setString(3, e.getValue());
                    ins.addBatch();
                }
                ins.executeBatch();
            }
            return true;
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return false;
    }

    // Retrieve student's answers for an attempt
    public static java.util.Map<Integer, String> getAnswersForAttempt(int attemptId) {
        java.util.Map<Integer, String> answers = new java.util.HashMap<>();
        String query = "SELECT question_id, answer FROM quiz_attempt_answers WHERE attempt_id = ?";
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(query)) {
            stmt.setInt(1, attemptId);
            ResultSet rs = stmt.executeQuery();
            while (rs.next()) {
                answers.put(rs.getInt("question_id"), rs.getString("answer"));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return answers;
    }
    
    // Get attempts for a student
    public static List<QuizAttempt> getStudentAttempts(int studentId) {
        List<QuizAttempt> attempts = new ArrayList<>();
        String query = "SELECT * FROM quiz_attempts WHERE student_id = ? ORDER BY started_at DESC";
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(query)) {
            stmt.setInt(1, studentId);
            ResultSet rs = stmt.executeQuery();
            
            while (rs.next()) {
                attempts.add(mapResultSetToAttempt(rs));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return attempts;
    }
    
    // Check if student already attempted this quiz
    public static boolean hasAttempted(int quizId, int studentId) {
        String query = "SELECT COUNT(*) as count FROM quiz_attempts WHERE quiz_id = ? AND student_id = ?";
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(query)) {
            stmt.setInt(1, quizId);
            stmt.setInt(2, studentId);
            ResultSet rs = stmt.executeQuery();
            
            if (rs.next()) {
                return rs.getInt("count") > 0;
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return false;
    }
    
    // Helper method to map ResultSet to QuizAttempt
    private static QuizAttempt mapResultSetToAttempt(ResultSet rs) throws SQLException {
        QuizAttempt attempt = new QuizAttempt();
        attempt.setAttemptId(rs.getInt("attempt_id"));
        attempt.setQuizId(rs.getInt("quiz_id"));
        attempt.setStudentId(rs.getInt("student_id"));
        attempt.setScore(rs.getInt("score"));
        attempt.setStatus(rs.getString("status"));
        attempt.setStartedAt(rs.getTimestamp("started_at"));
        attempt.setSubmittedAt(rs.getTimestamp("submitted_at"));
        attempt.setGradedAt(rs.getTimestamp("graded_at"));
        attempt.setFeedback(rs.getString("feedback"));
        return attempt;
    }
}
