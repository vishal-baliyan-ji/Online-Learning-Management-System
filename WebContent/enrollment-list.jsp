<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ page import="java.util.*, com.lms.model.*" %>
<%
    if(session.getAttribute("user") == null || !"admin".equals(session.getAttribute("userRole"))) {
        response.sendRedirect("login.jsp");
        return;
    }
    
    List<Enrollment> enrollments = (List<Enrollment>) request.getAttribute("enrollments");
    String success = request.getParameter("success");
    String error = request.getParameter("error");
%>
<!DOCTYPE html>
<html>
<head>
    <meta charset="UTF-8">
    <title>Enrollment Management - LMS</title>
    <link rel="stylesheet" href="css/style.css">
</head>
<body>
    <div class="navbar">
        <h2>LMS - Enrollment Management</h2>
        <div>
            <a href="admin-dashboard.jsp">Dashboard</a>
            <a href="logout">Logout</a>
        </div>
    </div>
    
    <div class="container">
        <h1>All Enrollments</h1>
        
        <% if(success != null) { %>
            <div class="alert alert-success"><%= success %></div>
        <% } %>
        
        <% if(error != null) { %>
            <div class="alert alert-error"><%= error %></div>
        <% } %>
        
        <div class="card">
            <table class="table">
                <thead>
                    <tr>
                        <th>ID</th>
                        <th>Student Name</th>
                        <th>Course Name</th>
                        <th>Progress</th>
                        <th>Status</th>
                        <th>Enrollment Date</th>
                    </tr>
                </thead>
                <tbody>
                    <% if(enrollments != null && !enrollments.isEmpty()) {
                        for(Enrollment enrollment : enrollments) { %>
                    <tr>
                        <td><%= enrollment.getEnrollmentId() %></td>
                        <td><%= enrollment.getStudentName() %></td>
                        <td><%= enrollment.getCourseName() %></td>
                        <td><%= String.format("%.1f", enrollment.getProgressPercentage()) %>%</td>
                        <td><span class="<%= "active".equals(enrollment.getStatus()) ? "status-active" : "status-inactive" %>"><%= enrollment.getStatus() %></span></td>
                        <td><%= enrollment.getEnrollmentDate() != null ? enrollment.getEnrollmentDate() : "N/A" %></td>
                    </tr>
                    <% } 
                    } else { %>
                    <tr>
                        <td colspan="6" class="text-centered">No enrollments found</td>
                    </tr>
                    <% } %>
                </tbody>
            </table>
        </div>
    </div>
</body>
</html>