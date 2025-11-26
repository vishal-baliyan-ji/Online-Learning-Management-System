package com.lms.model;

import java.sql.Timestamp;

/**
 * Enrollment Model Class
 * File: src/com/lms/model/Enrollment.java
 * Represents a student's enrollment in a course
 */
public class Enrollment {
    private int enrollmentId;
    private int studentId;
    private int courseId;
    private Timestamp enrollmentDate;
    private double progressPercentage;
    private String status; // active, completed, dropped
    private String courseName; // For display purposes
    private String studentName; // For display purposes
    private String instructorName; // For display purposes
    private String courseDescription;
    
    // Default Constructor
    public Enrollment() {}
    
    // Constructor for creating new enrollment
    public Enrollment(int studentId, int courseId) {
        this.studentId = studentId;
        this.courseId = courseId;
        this.progressPercentage = 0.0;
        this.status = "active";
    }
    
    // Full Constructor
    public Enrollment(int enrollmentId, int studentId, int courseId, 
                     double progressPercentage, String status) {
        this.enrollmentId = enrollmentId;
        this.studentId = studentId;
        this.courseId = courseId;
        this.progressPercentage = progressPercentage;
        this.status = status;
    }
    
    // Getters and Setters
    public int getEnrollmentId() {
        return enrollmentId;
    }
    
    public void setEnrollmentId(int enrollmentId) {
        this.enrollmentId = enrollmentId;
    }
    
    public int getStudentId() {
        return studentId;
    }
    
    public void setStudentId(int studentId) {
        this.studentId = studentId;
    }
    
    public int getCourseId() {
        return courseId;
    }
    
    public void setCourseId(int courseId) {
        this.courseId = courseId;
    }
    
    public Timestamp getEnrollmentDate() {
        return enrollmentDate;
    }
    
    public void setEnrollmentDate(Timestamp enrollmentDate) {
        this.enrollmentDate = enrollmentDate;
    }
    
    public double getProgressPercentage() {
        return progressPercentage;
    }
    
    public void setProgressPercentage(double progressPercentage) {
        this.progressPercentage = progressPercentage;
    }
    
    public String getStatus() {
        return status;
    }
    
    public void setStatus(String status) {
        this.status = status;
    }
    
    public String getCourseName() {
        return courseName;
    }
    
    public void setCourseName(String courseName) {
        this.courseName = courseName;
    }
    
    public String getStudentName() {
        return studentName;
    }
    
    public void setStudentName(String studentName) {
        this.studentName = studentName;
    }

    public String getInstructorName() {
        return instructorName;
    }

    public void setInstructorName(String instructorName) {
        this.instructorName = instructorName;
    }
    
    public String getCourseDescription() {
        return courseDescription;
    }
    
    public void setCourseDescription(String courseDescription) {
        this.courseDescription = courseDescription;
    }
    
    @Override
    public String toString() {
        return "Enrollment{" +
                "enrollmentId=" + enrollmentId +
                ", studentId=" + studentId +
                ", courseId=" + courseId +
                ", progressPercentage=" + progressPercentage +
                ", status='" + status + '\'' +
                '}';
    }
}