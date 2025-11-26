<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ page import="java.util.*, com.lms.model.*" %>
<%
    if(session.getAttribute("user") == null || !"student".equals(session.getAttribute("userRole"))) {
        response.sendRedirect("login.jsp");
        return;
    }
    
    List<Course> courses = (List<Course>) request.getAttribute("courses");
    String error = request.getParameter("error");
%>
<!DOCTYPE html>
<html>
<head>
    <meta charset="UTF-8">
    <title>Enroll in Course - LMS</title>
    <link rel="stylesheet" href="css/style.css">
</head>
<body>
    <div class="navbar">
        <h2>LMS - Enroll in Course</h2>
        <div>
            <a href="enrollment?action=my-courses">My Courses</a>
            <a href="student-dashboard.jsp">Dashboard</a>
            <a href="logout">Logout</a>
        </div>
    </div>
    
    <div class="container">
        <h1>Available Courses</h1>
        
        <% if(error != null) { %>
            <div class="alert alert-error"><%= error %></div>
        <% } %>
        
        <div class="card">
            <% if(courses != null && !courses.isEmpty()) { %>
                <div class="course-grid">
                    <% for(Course course : courses) { %>
                        <div class="card course-card">
                            <h3><%= course.getTitle() %></h3>
                            <p><strong>Instructor:</strong> <%= course.getInstructorName() != null ? course.getInstructorName() : "N/A" %></p>
                            <p><%= course.getDescription() != null ? 
                                   course.getDescription().substring(0, Math.min(100, course.getDescription().length())) + "..." 
                                   : "No description available" %></p>
                            
                            <form action="enrollment" method="post" class="form-inline">
                                <input type="hidden" name="action" value="enroll">
                                <input type="hidden" name="courseId" value="<%= course.getCourseId() %>">
                                <button type="submit" class="btn btn-success">Enroll Now</button>
                                <a href="course?action=view&id=<%= course.getCourseId() %>" class="btn btn-primary">View Details</a>
                            </form>
                        </div>
                    <% } %>
                </div>
            <% } else { %>
                <div class="alert alert-error">
                    No available courses found. You may already be enrolled in all courses.
                    <br><br>
                    <a href="enrollment?action=my-courses" class="btn btn-primary">View My Courses</a>
                </div>
            <% } %>
        </div>
    </div>
</body>
</html>