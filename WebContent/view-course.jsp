<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ page import="com.lms.model.*, com.lms.dao.*" %>
<%
    if(session.getAttribute("user") == null) {
        response.sendRedirect("login.jsp");
        return;
    }
    
    Course course = (Course) request.getAttribute("course");
    String userRole = (String) session.getAttribute("userRole");
    
    // Get enrollment count
    CourseDAO courseDAO = new CourseDAO();
    int enrollmentCount = 0;
    if(course != null) {
        enrollmentCount = courseDAO.getCourseEnrollmentCount(course.getCourseId());
    }
%>
<!DOCTYPE html>
<html>
<head>
    <meta charset="UTF-8">
    <title>Course Details - LMS</title>
    <link rel="stylesheet" href="css/style.css">
</head>
<body>
    <div class="navbar">
        <h2>LMS - Course Details</h2>
        <div>
            <a href="course?action=list">Back to Courses</a>
            <a href="logout">Logout</a>
        </div>
    </div>
    
    <div class="container">
        <% if(course != null) { %>
            <h1><%= course.getTitle() %></h1>
            
            <div class="card">
                <h3>Course Information</h3>
                <p><strong>Course ID:</strong> <%= course.getCourseId() %></p>
                <p><strong>Instructor:</strong> <%= course.getInstructorName() != null ? course.getInstructorName() : "N/A" %></p>
                <p><strong>Enrolled Students:</strong> <%= enrollmentCount %></p>
                <p><strong>Created:</strong> <%= course.getCreatedAt() != null ? course.getCreatedAt() : "N/A" %></p>
            </div>
            
            <div class="card">
                <h3>Description</h3>
                <p><%= course.getDescription() != null ? course.getDescription() : "No description available" %></p>
            </div>
            
            <div class="card">
                <h3>Syllabus</h3>
                <div class="pre-formatted">
                    <%= course.getSyllabus() != null ? course.getSyllabus() : "No syllabus available" %>
                </div>
            </div>
            
            <div class="card">
                <% if("student".equals(userRole)) { %>
                    <a href="enrollment?action=enroll" class="btn btn-success">Enroll in This Course</a>
                <% } %>
                
                <% if("admin".equals(userRole) || "instructor".equals(userRole)) { %>
                    <a href="course?action=edit&id=<%= course.getCourseId() %>" class="btn btn-warning">Edit Course</a>
                    <a href="assignment?action=list" class="btn btn-primary">View Assignments</a>
                <% } %>
                
                <a href="course?action=list" class="btn btn-primary">Back to Courses</a>
            </div>
        <% } else { %>
            <div class="alert alert-error">Course not found!</div>
            <a href="course?action=list" class="btn btn-primary">Back to Courses</a>
        <% } %>
    </div>
</body>
</html>