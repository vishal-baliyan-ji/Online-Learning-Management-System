<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ page import="java.util.*, com.lms.model.*" %>
<%
    if(session.getAttribute("user") == null || !"admin".equals(session.getAttribute("userRole"))) {
        response.sendRedirect("login.jsp");
        return;
    }

    List<User> users = (List<User>) request.getAttribute("users");
    String success = request.getParameter("success");
    String error = request.getParameter("error");
%>
<!DOCTYPE html>
<html>
<head>
    <meta charset="UTF-8">
    <title>User Management - LMS</title>
    <link rel="stylesheet" href="css/style.css">
</head>
<body>
    <div class="navbar">
        <h2>LMS - User Management</h2>
        <div>
            <a href="admin-dashboard.jsp">Dashboard</a>
            <a href="logout">Logout</a>
        </div>
    </div>

    <div class="container">
        <h1>Manage Users</h1>

        <% if(success != null) { %>
            <div class="alert alert-success"><%= success %></div>
        <% } %>
        <% if(error != null) { %>
            <div class="alert alert-error"><%= error %></div>
        <% } %>

        <div class="card">
            <a href="user?action=add" class="btn btn-success">Create New User</a>

            <table class="table">
                <thead>
                    <tr>
                        <th>ID</th>
                        <th>Name</th>
                        <th>Email</th>
                        <th>Role</th>
                        <th>Actions</th>
                    </tr>
                </thead>
                <tbody>
                    <% if(users != null && !users.isEmpty()) {
                        for(User u : users) { %>
                    <tr>
                        <td><%= u.getUserId() %></td>
                        <td><%= u.getName() %></td>
                        <td><%= u.getEmail() %></td>
                        <td><%= u.getRole() %></td>
                        <td>
                            <a href="user?action=edit&id=<%= u.getUserId() %>" class="btn btn-warning">Edit</a>
                            <a href="user?action=delete&id=<%= u.getUserId() %>" class="btn btn-danger" onclick="return confirm('Are you sure you want to delete this user?')">Delete</a>
                        </td>
                    </tr>
                    <% } %>
                    <% } else { %>
                    <tr>
                        <td colspan="5" class="text-centered">No users found</td>
                    </tr>
                    <% } %>
                </tbody>
            </table>
        </div>
    </div>
</body>
</html>