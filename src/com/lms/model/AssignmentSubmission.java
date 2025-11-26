package com.lms.model;

import java.sql.Timestamp;

/**
 * AssignmentSubmission Model Class
 * File: src/com/lms/model/AssignmentSubmission.java
 * Represents a student's submission for an assignment
 */
public class AssignmentSubmission {
    private int submissionId;
    private int assignmentId;
    private int studentId;
    private String submissionText;
    private String filePath;
    private Timestamp submittedAt;
    private double grade;
    private String feedback;
    private Timestamp gradedAt;
    private String status; // submitted, graded, pending
    private String studentName; // For display purposes
    private String assignmentTitle; // For display purposes
    
    // Default Constructor
    public AssignmentSubmission() {}
    
    // Constructor for creating new submission
    public AssignmentSubmission(int assignmentId, int studentId, String submissionText, String filePath) {
        this.assignmentId = assignmentId;
        this.studentId = studentId;
        this.submissionText = submissionText;
        this.filePath = filePath;
        this.status = "submitted";
    }
    
    // Full Constructor
    public AssignmentSubmission(int submissionId, int assignmentId, int studentId, 
                               String submissionText, String filePath, double grade, 
                               String feedback, String status) {
        this.submissionId = submissionId;
        this.assignmentId = assignmentId;
        this.studentId = studentId;
        this.submissionText = submissionText;
        this.filePath = filePath;
        this.grade = grade;
        this.feedback = feedback;
        this.status = status;
    }
    
    // Getters and Setters
    public int getSubmissionId() {
        return submissionId;
    }
    
    public void setSubmissionId(int submissionId) {
        this.submissionId = submissionId;
    }
    
    public int getAssignmentId() {
        return assignmentId;
    }
    
    public void setAssignmentId(int assignmentId) {
        this.assignmentId = assignmentId;
    }
    
    public int getStudentId() {
        return studentId;
    }
    
    public void setStudentId(int studentId) {
        this.studentId = studentId;
    }
    
    public String getSubmissionText() {
        return submissionText;
    }
    
    public void setSubmissionText(String submissionText) {
        this.submissionText = submissionText;
    }
    
    public String getFilePath() {
        return filePath;
    }
    
    public void setFilePath(String filePath) {
        this.filePath = filePath;
    }
    
    public Timestamp getSubmittedAt() {
        return submittedAt;
    }
    
    public void setSubmittedAt(Timestamp submittedAt) {
        this.submittedAt = submittedAt;
    }
    
    public double getGrade() {
        return grade;
    }
    
    public void setGrade(double grade) {
        this.grade = grade;
    }
    
    public String getFeedback() {
        return feedback;
    }
    
    public void setFeedback(String feedback) {
        this.feedback = feedback;
    }
    
    public Timestamp getGradedAt() {
        return gradedAt;
    }
    
    public void setGradedAt(Timestamp gradedAt) {
        this.gradedAt = gradedAt;
    }
    
    public String getStatus() {
        return status;
    }
    
    public void setStatus(String status) {
        this.status = status;
    }
    
    public String getStudentName() {
        return studentName;
    }
    
    public void setStudentName(String studentName) {
        this.studentName = studentName;
    }
    
    public String getAssignmentTitle() {
        return assignmentTitle;
    }
    
    public void setAssignmentTitle(String assignmentTitle) {
        this.assignmentTitle = assignmentTitle;
    }
    
    @Override
    public String toString() {
        return "AssignmentSubmission{" +
                "submissionId=" + submissionId +
                ", assignmentId=" + assignmentId +
                ", studentId=" + studentId +
                ", status='" + status + '\'' +
                ", grade=" + grade +
                '}';
    }
}