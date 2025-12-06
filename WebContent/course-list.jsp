<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ page import="java.util.*, com.lms.model.*" %>
<%
    if(session.getAttribute("user") == null) {
        response.sendRedirect("login.jsp");
        return;
    }
    
    List<Course> courses = (List<Course>) request.getAttribute("courses");
    String success = request.getParameter("success");
    String error = request.getParameter("error");
    String userRole = (String) session.getAttribute("userRole");
%>
<!DOCTYPE html>
<html>
<head>
    <meta charset="UTF-8">
    <title>Course Management - LMS</title>
    <link rel="stylesheet" href="css/style.css">
</head>
<body>
    <div class="navbar">
        <h2>LMS - Course Management</h2>
        <div>
            <% if("admin".equals(userRole)) { %>
                <a href="admin-dashboard.jsp">Dashboard</a>
            <% } else if("instructor".equals(userRole)) { %>
                <a href="instructor-dashboard.jsp">Dashboard</a>
            <% } %>
            <a href="logout">Logout</a>
        </div>
    </div>
    
    <div class="container">
        <h1>Courses</h1>
        
        <% if(success != null) { %>
            <div class="alert alert-success"><%= success %></div>
        <% } %>
        
        <% if(error != null) { %>
            <div class="alert alert-error"><%= error %></div>
        <% } %>
        
        <div class="card">
            <% if("admin".equals(userRole) || "instructor".equals(userRole)) { %>
                <a href="course?action=add" class="btn btn-success">Add New Course</a>
            <% } %>
            
            <table class="table">
                <thead>
                    <tr>
                        <th>ID</th>
                        <th>Course Title</th>
                        <th>Instructor</th>
                        <th>Description</th>
                        <th>Actions</th>
                    </tr>
                </thead>
                <tbody>
                    <% if(courses != null && !courses.isEmpty()) {
                        for(Course course : courses) { %>
                    <tr>
                        <td><%= course.getCourseId() %></td>
                        <td><%= course.getTitle() %></td>
                        <td><%= course.getInstructorName() != null ? course.getInstructorName() : "N/A" %></td>
                        <td><%= course.getDescription() != null ? course.getDescription().substring(0, Math.min(50, course.getDescription().length())) + "..." : "N/A" %></td>
                        <td>
                            <a href="course?action=view&id=<%= course.getCourseId() %>" class="btn btn-primary">View</a>
                            <a href="quiz?action=list&courseId=<%= course.getCourseId() %>" class="btn btn-info">Quizzes</a>
                            <% if("admin".equals(userRole) || "instructor".equals(userRole)) { %>
                                <a href="course?action=edit&id=<%= course.getCourseId() %>" class="btn btn-warning">Edit</a>
                                <a href="course?action=delete&id=<%= course.getCourseId() %>" 
                                   class="btn btn-danger" 
                                   onclick="return confirm('Are you sure you want to delete this course?')">Delete</a>
                            <% } %>
                        </td>
                    </tr>
                    <% } 
                    } else { %>
                    <tr>
                        <td colspan="5" class="text-centered">No courses found</td>
                    </tr>
                    <% } %>
                </tbody>
            </table>
        </div>
    </div>
</body>
</html>