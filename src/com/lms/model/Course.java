package com.lms.model;

import java.sql.Timestamp;

/**
 * Course Model Class
 * File: src/com/lms/model/Course.java
 * Represents a course in the LMS
 */
public class Course {
    private int courseId;
    private String title;
    private String description;
    private String syllabus;
    private int instructorId;
    private String instructorName; // For display purposes
    private Timestamp createdAt;
    private Timestamp updatedAt;
    
    // Default Constructor
    public Course() {}
    
    // Constructor for creating new course
    public Course(String title, String description, String syllabus, int instructorId) {
        this.title = title;
        this.description = description;
        this.syllabus = syllabus;
        this.instructorId = instructorId;
    }
    
    // Full Constructor
    public Course(int courseId, String title, String description, String syllabus, int instructorId) {
        this.courseId = courseId;
        this.title = title;
        this.description = description;
        this.syllabus = syllabus;
        this.instructorId = instructorId;
    }
    
    // Getters and Setters
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
    
    public String getSyllabus() {
        return syllabus;
    }
    
    public void setSyllabus(String syllabus) {
        this.syllabus = syllabus;
    }
    
    public int getInstructorId() {
        return instructorId;
    }
    
    public void setInstructorId(int instructorId) {
        this.instructorId = instructorId;
    }
    
    public String getInstructorName() {
        return instructorName;
    }
    
    public void setInstructorName(String instructorName) {
        this.instructorName = instructorName;
    }
    
    public Timestamp getCreatedAt() {
        return createdAt;
    }
    
    public void setCreatedAt(Timestamp createdAt) {
        this.createdAt = createdAt;
    }
    
    public Timestamp getUpdatedAt() {
        return updatedAt;
    }
    
    public void setUpdatedAt(Timestamp updatedAt) {
        this.updatedAt = updatedAt;
    }
    
    @Override
    public String toString() {
        return "Course{" +
                "courseId=" + courseId +
                ", title='" + title + '\'' +
                ", instructorId=" + instructorId +
                ", instructorName='" + instructorName + '\'' +
                '}';
    }
}