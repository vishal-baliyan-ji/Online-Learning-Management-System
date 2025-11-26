package com.lms.model;

import java.sql.Date;
import java.sql.Timestamp;

/**
 * Assignment Model Class
 * File: src/com/lms/model/Assignment.java
 * Represents an assignment in a course
 */
public class Assignment {
    private int assignmentId;
    private int courseId;
    private String title;
    private String description;
    private Date dueDate;
    private int maxScore;
    private String attachmentPath;
    private Timestamp createdAt;
    private String courseName; // For display purposes
    
    // Default Constructor
    public Assignment() {}
    
    // Constructor for creating new assignment
    public Assignment(int courseId, String title, String description, Date dueDate, int maxScore) {
        this.courseId = courseId;
        this.title = title;
        this.description = description;
        this.dueDate = dueDate;
        this.maxScore = maxScore;
    }
    
    // Full Constructor
    public Assignment(int assignmentId, int courseId, String title, String description, 
                     Date dueDate, int maxScore) {
        this.assignmentId = assignmentId;
        this.courseId = courseId;
        this.title = title;
        this.description = description;
        this.dueDate = dueDate;
        this.maxScore = maxScore;
    }
    
    // Getters and Setters
    public int getAssignmentId() {
        return assignmentId;
    }
    
    public void setAssignmentId(int assignmentId) {
        this.assignmentId = assignmentId;
    }
    
    public int getCourseId() {
        return courseId;
    }
    
    public void setCourseId(int courseId) {
        this.courseId = courseId;
    }
    
    public String getTitle() {
        return title;
    }
    
    public void setTitle(String title) {
        this.title = title;
    }
    
    public String getDescription() {
        return description;
    }
    
    public void setDescription(String description) {
        this.description = description;
    }
    
    public Date getDueDate() {
        return dueDate;
    }
    
    public void setDueDate(Date dueDate) {
        this.dueDate = dueDate;
    }
    
    public int getMaxScore() {
        return maxScore;
    }
    
    public void setMaxScore(int maxScore) {
        this.maxScore = maxScore;
    }
    
    public Timestamp getCreatedAt() {
        return createdAt;
    }
    
    public void setCreatedAt(Timestamp createdAt) {
        this.createdAt = createdAt;
    }
    
    public String getCourseName() {
        return courseName;
    }
    
    public void setCourseName(String courseName) {
        this.courseName = courseName;
    }
    
    public String getAttachmentPath() {
        return attachmentPath;
    }
    
    public void setAttachmentPath(String attachmentPath) {
        this.attachmentPath = attachmentPath;
    }
    
    @Override
    public String toString() {
        return "Assignment{" +
                "assignmentId=" + assignmentId +
                ", courseId=" + courseId +
                ", title='" + title + '\'' +
                ", dueDate=" + dueDate +
                ", maxScore=" + maxScore +
                '}';
    }
}