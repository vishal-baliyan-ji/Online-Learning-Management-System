<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ page import="java.util.*, com.lms.model.*" %>
<%
    if(session.getAttribute("user") == null) {
        response.sendRedirect("login.jsp");
        return;
    }
    
    String userRole = (String) session.getAttribute("userRole");
    if(!"admin".equals(userRole) && !"instructor".equals(userRole)) {
        response.sendRedirect("login.jsp");
        return;
    }
    
    List<Assignment> assignments = (List<Assignment>) request.getAttribute("assignments");
    String success = request.getParameter("success");
    String error = request.getParameter("error");
%>
<!DOCTYPE html>
<html>
<head>
    <meta charset="UTF-8">
    <title>Assignment Management - LMS</title>
    <link rel="stylesheet" href="css/style.css">
</head>
<body>
    <div class="navbar">
        <h2>LMS - Assignment Management</h2>
        <div>
            <% if("admin".equals(userRole)) { %>
                <a href="admin-dashboard.jsp">Dashboard</a>
            <% } else { %>
                <a href="instructor-dashboard.jsp">Dashboard</a>
            <% } %>
            <a href="logout">Logout</a>
        </div>
    </div>
    
    <div class="container">
        <h1>Assignments</h1>
        
        <% if(success != null) { %>
            <div class="alert alert-success"><%= success %></div>
        <% } %>
        
        <% if(error != null) { %>
            <div class="alert alert-error"><%= error %></div>
        <% } %>
        
        <div class="card">
            <% if("instructor".equals(userRole)) { %>
                <a href="assignment?action=add" class="btn btn-success">Create New Assignment</a>
            <% } %>
            
            <table class="table">
                <thead>
                    <tr>
                        <th>ID</th>
                        <th>Title</th>
                        <th>Course</th>
                        <th>Due Date</th>
                        <th>Max Score</th>
                        <th>Attachment</th>
                        <th>Actions</th>
                    </tr>
                </thead>
                <tbody>
                    <% if(assignments != null && !assignments.isEmpty()) {
                        for(Assignment assignment : assignments) { %>
                    <tr>
                        <td><%= assignment.getAssignmentId() %></td>
                        <td><%= assignment.getTitle() %></td>
                        <td><%= assignment.getCourseName() != null ? assignment.getCourseName() : "N/A" %></td>
                        <td><%= assignment.getDueDate() %></td>
                        <td><%= assignment.getMaxScore() %></td>
                        <td>
                            <% if(assignment.getAttachmentPath() != null && !assignment.getAttachmentPath().isEmpty()) { %>
                                <a href="<%= assignment.getAttachmentPath() %>" target="_blank" class="btn btn-small btn-info">Download</a>
                            <% } else { %>
                                <span class="text-muted">-</span>
                            <% } %>
                        </td>
                        <td>
                            <a href="assignment?action=view&id=<%= assignment.getAssignmentId() %>" class="btn btn-primary">View</a>
                            <a href="assignment?action=submissions&id=<%= assignment.getAssignmentId() %>" class="btn btn-primary">Submissions</a>
                            <% if("instructor".equals(userRole)) { %>
                                <a href="assignment?action=edit&id=<%= assignment.getAssignmentId() %>" class="btn btn-warning">Edit</a>
                                <a href="assignment?action=delete&id=<%= assignment.getAssignmentId() %>" 
                                   class="btn btn-danger" 
                                   onclick="return confirm('Are you sure you want to delete this assignment?')">Delete</a>
                            <% } %>
                        </td>
                    </tr>
                    <% } 
                    } else { %>
                    <tr>
                        <td colspan="7" class="text-centered">No assignments found</td>
                    </tr>
                    <% } %>
                </tbody>
            </table>
        </div>
    </div>
</body>
</html>