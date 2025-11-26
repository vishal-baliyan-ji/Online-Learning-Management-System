<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ page import="com.lms.dao.*, com.lms.model.*" %>
<%
    if(session.getAttribute("user") == null || !"admin".equals(session.getAttribute("userRole"))) {
        response.sendRedirect("login.jsp");
        return;
    }
    
    UserDAO userDAO = new UserDAO();
    CourseDAO courseDAO = new CourseDAO();
    EnrollmentDAO enrollmentDAO = new EnrollmentDAO();
    
    int totalUsers = userDAO.getTotalUserCount();
    int totalCourses = courseDAO.getTotalCourseCount();
    int totalEnrollments = enrollmentDAO.getTotalEnrollmentCount();
%>
<!DOCTYPE html>
<html>
<head>
    <meta charset="UTF-8">
    <title>Admin Dashboard - LMS</title>
    <link rel="stylesheet" href="css/style.css">
</head>
<body>
    <div class="navbar">
        <h2>LMS - Admin Dashboard</h2>
        <div>
            <span>Welcome, <%= session.getAttribute("userName") %></span>
            <a href="logout">Logout</a>
        </div>
    </div>
    
    <div class="container">
        <h1>Admin Dashboard</h1>
        
        <div class="dashboard-stats">
            <div class="stat-card">
                <h3><%= totalUsers %></h3>
                <p>Total Users</p>
            </div>
            <div class="stat-card">
                <h3><%= totalCourses %></h3>
                <p>Total Courses</p>
            </div>
            <div class="stat-card">
                <h3><%= totalEnrollments %></h3>
                <p>Total Enrollments</p>
            </div>
        </div>
        
        <div class="card">
            <h2>Quick Actions</h2>
            <a href="user?action=list" class="btn btn-primary">Manage Users</a>
            <a href="course?action=list" class="btn btn-primary">Manage Courses</a>
            <a href="enrollment?action=list" class="btn btn-primary">View Enrollments</a>
            <a href="assignment?action=list" class="btn btn-primary">View Assignments</a>
        </div>
    </div>
</body>
</html>