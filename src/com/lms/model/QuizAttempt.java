package com.lms.model;

import java.io.Serializable;
import java.util.Date;

public class QuizAttempt implements Serializable {
    private static final long serialVersionUID = 1L;
    
    private int attemptId;
    private int quizId;
    private int studentId;
    private int score;
    private String status; // in_progress, submitted, graded
    private Date startedAt;
    private Date submittedAt;
    private Date gradedAt;
    private String feedback;
    
    // Default constructor
    public QuizAttempt() {
    }
    
    // Constructor with parameters
    public QuizAttempt(int quizId, int studentId, String status, Date startedAt) {
        this.quizId = quizId;
        this.studentId = studentId;
        this.status = status;
        this.startedAt = startedAt;
    }
    
    // Getters and Setters
    public int getAttemptId() {
        return attemptId;
    }
    
    public void setAttemptId(int attemptId) {
        this.attemptId = attemptId;
    }
    
    public int getQuizId() {
        return quizId;
    }
    
    public void setQuizId(int quizId) {
        this.quizId = quizId;
    }
    
    public int getStudentId() {
        return studentId;
    }
    
    public void setStudentId(int studentId) {
        this.studentId = studentId;
    }
    
    public int getScore() {
        return score;
    }
    
    public void setScore(int score) {
        this.score = score;
    }
    
    public String getStatus() {
        return status;
    }
    
    public void setStatus(String status) {
        this.status = status;
    }
    
    public Date getStartedAt() {
        return startedAt;
    }
    
    public void setStartedAt(Date startedAt) {
        this.startedAt = startedAt;
    }
    
    public Date getSubmittedAt() {
        return submittedAt;
    }
    
    public void setSubmittedAt(Date submittedAt) {
        this.submittedAt = submittedAt;
    }
    
    public Date getGradedAt() {
        return gradedAt;
    }
    
    public void setGradedAt(Date gradedAt) {
        this.gradedAt = gradedAt;
    }
    
    public String getFeedback() {
        return feedback;
    }
    
    public void setFeedback(String feedback) {
        this.feedback = feedback;
    }
}
