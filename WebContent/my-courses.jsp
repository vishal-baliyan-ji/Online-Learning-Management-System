<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ page import="java.util.*, com.lms.model.*" %>
<%
    if(session.getAttribute("user") == null || !"student".equals(session.getAttribute("userRole"))) {
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
    <title>My Courses - LMS</title>
    <link rel="stylesheet" href="css/style.css">
</head>
<body>
    <div class="navbar">
        <h2>LMS - My Courses</h2>
        <div>
            <a href="student-dashboard.jsp">Dashboard</a>
            <a href="enrollment?action=enroll">Enroll in Course</a>
            <a href="logout">Logout</a>
        </div>
    </div>

    <div class="container">
        <h1>My Enrolled Courses</h1>

        <% if(success != null) { %>
            <div class="alert alert-success"><%= success %></div>
        <% } %>
        <% if(error != null) { %>
            <div class="alert alert-error"><%= error %></div>
        <% } %>

        <div class="card">
            <% if(enrollments != null && !enrollments.isEmpty()) { %>
                <table class="table">
                    <thead>
                        <tr>
                            <th>Course</th>
                            <th>Instructor</th>
                            <th>Progress</th>
                            <th>Actions</th>
                        </tr>
                    </thead>
                    <tbody>
                        <% for(Enrollment e : enrollments) { %>
                        <tr>
                            <td><%= e.getCourseName() %></td>
                            <td><%= e.getInstructorName() %></td>
                            <td><%= String.format("%.1f", e.getProgressPercentage()) %> %</td>
                            <td>
                                <a href="course?action=view&id=<%= e.getCourseId() %>" class="btn btn-primary">View</a>
                                <a href="enrollment?action=drop&id=<%= e.getEnrollmentId() %>" class="btn btn-danger" onclick="return confirm('Are you sure you want to drop this course?')">Drop</a>
                            </td>
                        </tr>
                        <% } %>
                    </tbody>
                </table>
            <% } else { %>
                <div class="text-centered">
                    <p>You are not enrolled in any courses yet.</p>
                    <a href="enrollment?action=enroll" class="btn btn-primary">Enroll in Course</a>
                </div>
            <% } %>
        </div>
    </div>
</body>
</html>