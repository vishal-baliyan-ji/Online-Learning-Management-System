package com.lms.model;

import java.io.Serializable;
import java.util.Date;

public class Quiz implements Serializable {
    private static final long serialVersionUID = 1L;
    
    private int quizId;
    private int courseId;
    private String title;
    private String description;
    private int durationMinutes;
    private int maxScore;
    private int passingScore;
    private boolean isPublished;
    private Date createdAt;
    private Date updatedAt;
    private Date publishedDate;
    
    // Default constructor
    public Quiz() {
    }
    
    // Constructor with parameters
    public Quiz(int courseId, String title, String description, int durationMinutes, 
                int maxScore, int passingScore, boolean isPublished) {
        this.courseId = courseId;
        this.title = title;
        this.description = description;
        this.durationMinutes = durationMinutes;
        this.maxScore = maxScore;
        this.passingScore = passingScore;
        this.isPublished = isPublished;
    }
    
    // Getters and Setters
    public int getQuizId() {
        return quizId;
    }
    
    public void setQuizId(int quizId) {
        this.quizId = quizId;
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
    
    public int getDurationMinutes() {
        return durationMinutes;
    }
    
    public void setDurationMinutes(int durationMinutes) {
        this.durationMinutes = durationMinutes;
    }
    
    public int getMaxScore() {
        return maxScore;
    }
    
    public void setMaxScore(int maxScore) {
        this.maxScore = maxScore;
    }
    
    public int getPassingScore() {
        return passingScore;
    }
    
    public void setPassingScore(int passingScore) {
        this.passingScore = passingScore;
    }
    
    public boolean isPublished() {
        return isPublished;
    }
    
    public void setPublished(boolean isPublished) {
        this.isPublished = isPublished;
    }
    
    public Date getCreatedAt() {
        return createdAt;
    }
    
    public void setCreatedAt(Date createdAt) {
        this.createdAt = createdAt;
    }
    
    public Date getUpdatedAt() {
        return updatedAt;
    }
    
    public void setUpdatedAt(Date updatedAt) {
        this.updatedAt = updatedAt;
    }
    
    public Date getPublishedDate() {
        return publishedDate;
    }
    
    public void setPublishedDate(Date publishedDate) {
        this.publishedDate = publishedDate;
    }
}
