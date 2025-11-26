<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ page import="com.lms.dao.*, com.lms.model.*, java.util.*" %>
<%
    if(session.getAttribute("user") == null || !"student".equals(session.getAttribute("userRole"))) {
        response.sendRedirect("login.jsp");
        return;
    }
    
    Integer studentId = (Integer) session.getAttribute("userId");
    EnrollmentDAO enrollmentDAO = new EnrollmentDAO();
    AssignmentDAO assignmentDAO = new AssignmentDAO();
    
    List<Enrollment> myEnrollments = enrollmentDAO.getEnrollmentsByStudent(studentId);
    List<Assignment> myAssignments = assignmentDAO.getAssignmentsForStudent(studentId);
%>
<!DOCTYPE html>
<html>
<head>
    <meta charset="UTF-8">
    <title>Student Dashboard - LMS</title>
    <link rel="stylesheet" href="css/style.css">
</head>
<body>
    <div class="navbar">
        <h2>LMS - Student Dashboard</h2>
        <div>
            <span>Welcome, <%= session.getAttribute("userName") %></span>
            <a href="logout">Logout</a>
        </div>
    </div>
    
    <div class="container">
        <h1>Student Dashboard</h1>
        
        <div class="dashboard-stats">
            <div class="stat-card">
                <h3><%= myEnrollments.size() %></h3>
                <p>Enrolled Courses</p>
            </div>
            <div class="stat-card">
                <h3><%= myAssignments.size() %></h3>
                <p>Total Assignments</p>
            </div>
        </div>
        
        <div class="card">
            <h2>Quick Actions</h2>
            <a href="enrollment?action=my-courses" class="btn btn-primary">My Courses</a>
            <a href="enrollment?action=enroll" class="btn btn-success">Enroll in Course</a>
            <a href="assignment?action=my-assignments" class="btn btn-primary">My Assignments</a>
        </div>
        
        <div class="card">
            <h2>My Enrolled Courses</h2>
            <table class="table">
                <thead>
                    <tr>
                        <th>Course Name</th>
                        <th>Progress</th>
                        <th>Status</th>
                        <th>Actions</th>
                    </tr>
                </thead>
                <tbody>
                    <% for(Enrollment enrollment : myEnrollments) { %>
                    <tr>
                        <td><%= enrollment.getCourseName() %></td>
                        <td><%= String.format("%.1f", enrollment.getProgressPercentage()) %>%</td>
                        <td><%= enrollment.getStatus() %></td>
                        <td>
                            <a href="course?action=view&id=<%= enrollment.getCourseId() %>" class="btn btn-primary">View</a>
                        </td>
                    </tr>
                    <% } %>
                </tbody>
            </table>
        </div>
    </div>
</body>
</html>