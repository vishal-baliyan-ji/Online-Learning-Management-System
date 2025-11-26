<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ page import="java.util.*, com.lms.model.*" %>
<%
    if(session.getAttribute("user") == null) {
        response.sendRedirect("login.jsp");
        return;
    }

    String userRole = (String) session.getAttribute("userRole");
    Integer userId = (Integer) session.getAttribute("userId");
    List<User> instructors = (List<User>) request.getAttribute("instructors");
    String error = request.getParameter("error");
%>
<!DOCTYPE html>
<html>
<head>
    <meta charset="UTF-8">
    <title>Add Course - LMS</title>
    <link rel="stylesheet" href="css/style.css">
</head>
<body>
    <div class="navbar">
        <h2>LMS - Add Course</h2>
        <div>
            <% if("admin".equals(userRole)) { %>
                <a href="admin-dashboard.jsp">Dashboard</a>
            <% } else if("instructor".equals(userRole)) { %>
                <a href="instructor-dashboard.jsp">Dashboard</a>
            <% } %>
            <a href="course?action=list">Course List</a>
            <a href="logout">Logout</a>
        </div>
    </div>

    <div class="container">
        <h1>Create New Course</h1>

        <% if(error != null) { %>
            <div class="alert alert-error"><%= error %></div>
        <% } %>

        <div class="form-section">
            <form action="course" method="post" class="form">
                <input type="hidden" name="action" value="add">

                <div class="form-group">
                    <label for="title">Course Title:</label>
                    <input type="text" name="title" id="title" class="form-control" required>
                </div>

                <div class="form-group">
                    <label for="description">Description:</label>
                    <textarea name="description" id="description" rows="5" class="form-control"></textarea>
                </div>

                <div class="form-group">
                    <label for="syllabus">Syllabus:</label>
                    <textarea name="syllabus" id="syllabus" rows="4" class="form-control"></textarea>
                </div>

                <div class="form-group">
                    <label for="instructor">Instructor:</label>
                    <% if("instructor".equals(userRole)) { %>
                        <input type="hidden" name="instructorId" value="<%= userId %>">
                        <p class="text-muted">You will be set as the instructor for this course.</p>
                    <% } else { %>
                        <select name="instructorId" id="instructor" class="form-control" required>
                            <option value="">-- Select Instructor --</option>
                            <% for(User u : instructors) { %>
                                <option value="<%= u.getUserId() %>"><%= u.getName() %></option>
                            <% } %>
                        </select>
                    <% } %>
                </div>

                <div class="form-actions">
                    <button type="submit" class="btn btn-primary">Create Course</button>
                    <a href="course?action=list" class="btn btn-secondary">Cancel</a>
                </div>
            </form>
        </div>
    </div>
</body>
</html>