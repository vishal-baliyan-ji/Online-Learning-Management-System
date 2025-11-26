<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ page import="com.lms.model.*" %>
<%
    if(session.getAttribute("user") == null || !"admin".equals(session.getAttribute("userRole"))) {
        response.sendRedirect("login.jsp");
        return;
    }
    
    User userToEdit = (User) request.getAttribute("user");
    String error = (String) request.getAttribute("error");
%>
<!DOCTYPE html>
<html>
<head>
    <meta charset="UTF-8">
    <title>Edit User - LMS</title>
    <link rel="stylesheet" href="css/style.css">
</head>
<body>
    <div class="navbar">
        <h2>LMS - Edit User</h2>
        <div>
            <a href="user?action=list">Back to Users</a>
            <a href="logout">Logout</a>
        </div>
    </div>
    
    <div class="container">
        <h1>Edit User</h1>
        
        <% if(error != null) { %>
            <div class="alert alert-error"><%= error %></div>
        <% } %>
        
        <% if(userToEdit != null) { %>
        <div class="card">
            <form action="user" method="post">
                <input type="hidden" name="action" value="edit">
                <input type="hidden" name="userId" value="<%= userToEdit.getUserId() %>">
                
                <div class="form-group">
                    <label for="name">Name:</label>
                    <input type="text" id="name" name="name" value="<%= userToEdit.getName() %>" required>
                </div>
                
                <div class="form-group">
                    <label for="email">Email:</label>
                    <input type="email" id="email" name="email" value="<%= userToEdit.getEmail() %>" required>
                </div>
                
                <div class="form-group">
                    <label for="role">Role:</label>
                    <select id="role" name="role" required>
                        <option value="admin" <%= "admin".equals(userToEdit.getRole()) ? "selected" : "" %>>Admin</option>
                        <option value="instructor" <%= "instructor".equals(userToEdit.getRole()) ? "selected" : "" %>>Instructor</option>
                        <option value="student" <%= "student".equals(userToEdit.getRole()) ? "selected" : "" %>>Student</option>
                    </select>
                </div>
                
                <button type="submit" class="btn btn-success">Update User</button>
                <a href="user?action=list" class="btn btn-danger">Cancel</a>
            </form>
        </div>
        <% } else { %>
            <div class="alert alert-error">User not found!</div>
        <% } %>
    </div>
</body>
</html>