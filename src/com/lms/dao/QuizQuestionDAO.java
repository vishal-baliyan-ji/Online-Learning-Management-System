package com.lms.dao;

import com.lms.model.QuizQuestion;
import com.lms.util.DBConnection;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class QuizQuestionDAO {
    
    // Add a question to quiz
    public static int addQuestion(QuizQuestion question) {
        String query = "INSERT INTO quiz_questions (quiz_id, question_text, question_type, option_a, option_b, option_c, option_d, correct_answer, points, question_order) " +
                      "VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?)";
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(query, Statement.RETURN_GENERATED_KEYS)) {
            stmt.setInt(1, question.getQuizId());
            stmt.setString(2, question.getQuestionText());
            stmt.setString(3, question.getQuestionType());
            stmt.setString(4, question.getOptionA());
            stmt.setString(5, question.getOptionB());
            stmt.setString(6, question.getOptionC());
            stmt.setString(7, question.getOptionD());
            stmt.setString(8, question.getCorrectAnswer());
            stmt.setInt(9, question.getPoints());
            stmt.setInt(10, question.getQuestionOrder());
            
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
    
    // Get question by ID
    public static QuizQuestion getQuestionById(int questionId) {
        String query = "SELECT * FROM quiz_questions WHERE question_id = ?";
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(query)) {
            stmt.setInt(1, questionId);
            ResultSet rs = stmt.executeQuery();
            
            if (rs.next()) {
                return mapResultSetToQuestion(rs);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }
    
    // Get all questions for a quiz
    public static List<QuizQuestion> getQuestionsByQuiz(int quizId) {
        List<QuizQuestion> questions = new ArrayList<>();
        String query = "SELECT * FROM quiz_questions WHERE quiz_id = ? ORDER BY question_order ASC";
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(query)) {
            stmt.setInt(1, quizId);
            ResultSet rs = stmt.executeQuery();
            
            while (rs.next()) {
                questions.add(mapResultSetToQuestion(rs));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return questions;
    }
    
    // Update question
    public static boolean updateQuestion(QuizQuestion question) {
        String query = "UPDATE quiz_questions SET question_text = ?, question_type = ?, option_a = ?, option_b = ?, " +
                      "option_c = ?, option_d = ?, correct_answer = ?, points = ?, question_order = ? WHERE question_id = ?";
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(query)) {
            stmt.setString(1, question.getQuestionText());
            stmt.setString(2, question.getQuestionType());
            stmt.setString(3, question.getOptionA());
            stmt.setString(4, question.getOptionB());
            stmt.setString(5, question.getOptionC());
            stmt.setString(6, question.getOptionD());
            stmt.setString(7, question.getCorrectAnswer());
            stmt.setInt(8, question.getPoints());
            stmt.setInt(9, question.getQuestionOrder());
            stmt.setInt(10, question.getQuestionId());
            
            return stmt.executeUpdate() > 0;
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return false;
    }
    
    // Delete question
    public static boolean deleteQuestion(int questionId) {
        String query = "DELETE FROM quiz_questions WHERE question_id = ?";
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(query)) {
            stmt.setInt(1, questionId);
            return stmt.executeUpdate() > 0;
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return false;
    }
    
    // Delete all questions for a quiz
    public static boolean deleteQuestionsByQuiz(int quizId) {
        String query = "DELETE FROM quiz_questions WHERE quiz_id = ?";
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(query)) {
            stmt.setInt(1, quizId);
            return stmt.executeUpdate() > 0;
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return false;
    }
    
    // Count questions in quiz
    public static int countQuestions(int quizId) {
        String query = "SELECT COUNT(*) as count FROM quiz_questions WHERE quiz_id = ?";
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(query)) {
            stmt.setInt(1, quizId);
            ResultSet rs = stmt.executeQuery();
            
            if (rs.next()) {
                return rs.getInt("count");
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return 0;
    }
    
    // Helper method to map ResultSet to QuizQuestion
    private static QuizQuestion mapResultSetToQuestion(ResultSet rs) throws SQLException {
        QuizQuestion question = new QuizQuestion();
        question.setQuestionId(rs.getInt("question_id"));
        question.setQuizId(rs.getInt("quiz_id"));
        question.setQuestionText(rs.getString("question_text"));
        question.setQuestionType(rs.getString("question_type"));
        question.setOptionA(rs.getString("option_a"));
        question.setOptionB(rs.getString("option_b"));
        question.setOptionC(rs.getString("option_c"));
        question.setOptionD(rs.getString("option_d"));
        question.setCorrectAnswer(rs.getString("correct_answer"));
        question.setPoints(rs.getInt("points"));
        question.setQuestionOrder(rs.getInt("question_order"));
        return question;
    }
}
