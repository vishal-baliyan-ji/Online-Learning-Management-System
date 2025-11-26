<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ page import="com.lms.dao.*, com.lms.model.*, java.util.*" %>
<%
    if(session.getAttribute("user") == null || !"instructor".equals(session.getAttribute("userRole"))) {
        response.sendRedirect("login.jsp");
        return;
    }
    
    Integer instructorId = (Integer) session.getAttribute("userId");
    CourseDAO courseDAO = new CourseDAO();
    AssignmentSubmissionDAO submissionDAO = new AssignmentSubmissionDAO();
    
    List<Course> myCourses = courseDAO.getCoursesByInstructor(instructorId);
    int pendingSubmissions = submissionDAO.getPendingSubmissionsCount(instructorId);
%>
<!DOCTYPE html>
<html>
<head>
    <meta charset="UTF-8">
    <title>Instructor Dashboard - LMS</title>
    <link rel="stylesheet" href="css/style.css">
</head>
<body>
    <div class="navbar">
        <h2>LMS - Instructor Dashboard</h2>
        <div>
            <span>Welcome, <%= session.getAttribute("userName") %></span>
            <a href="logout">Logout</a>
        </div>
    </div>
    
    <div class="container">
        <h1>Instructor Dashboard</h1>
        
        <div class="dashboard-stats">
            <div class="stat-card">
                <h3><%= myCourses.size() %></h3>
                <p>My Courses</p>
            </div>
            <div class="stat-card">
                <h3><%= pendingSubmissions %></h3>
                <p>Pending Submissions</p>
            </div>
        </div>
        
        <div class="card">
            <h2>Quick Actions</h2>
            <a href="course?action=list" class="btn btn-primary">My Courses</a>
            <a href="course?action=add" class="btn btn-success">Create Course</a>
            <a href="assignment?action=list" class="btn btn-primary">My Assignments</a>
            <a href="assignment?action=add" class="btn btn-success">Create Assignment</a>
        </div>
        
        <div class="card">
            <h2>My Courses</h2>
            <table class="table">
                <thead>
                    <tr>
                        <th>Course Title</th>
                        <th>Enrollments</th>
                        <th>Actions</th>
                    </tr>
                </thead>
                <tbody>
                    <% for(Course course : myCourses) { 
                        int enrollmentCount = courseDAO.getCourseEnrollmentCount(course.getCourseId());
                    %>
                    <tr>
                        <td><%= course.getTitle() %></td>
                        <td><%= enrollmentCount %></td>
                        <td>
                            <a href="course?action=view&id=<%= course.getCourseId() %>" class="btn btn-primary">View</a>
                            <a href="course?action=edit&id=<%= course.getCourseId() %>" class="btn btn-warning">Edit</a>
                        </td>
                    </tr>
                    <% } %>
                </tbody>
            </table>
        </div>
    </div>
</body>
</html>