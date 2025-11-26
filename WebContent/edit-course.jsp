<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ page import="java.util.*, com.lms.model.*" %>
<%
    if(session.getAttribute("user") == null) {
        response.sendRedirect("login.jsp");
        return;
    }

    Course course = (Course) request.getAttribute("course");
    List<User> instructors = (List<User>) request.getAttribute("instructors");
    String error = request.getParameter("error");
%>
<!DOCTYPE html>
<html>
<head>
    <meta charset="UTF-8">
    <title>Edit Course - LMS</title>
    <link rel="stylesheet" href="css/style.css">
</head>
<body>
    <div class="navbar">
        <h2>LMS - Edit Course</h2>
        <div>
            <a href="course?action=list">Course List</a>
            <a href="logout">Logout</a>
        </div>
    </div>

    <div class="container">
        <h1>Edit Course</h1>

        <% if(error != null) { %>
            <div class="alert alert-error"><%= error %></div>
        <% } %>

        <div class="form-section">
            <form action="course" method="post" class="form">
                <input type="hidden" name="action" value="edit">
                <input type="hidden" name="courseId" value="<%= course.getCourseId() %>">

                <div class="form-group">
                    <label for="title">Course Title:</label>
                    <input type="text" name="title" id="title" class="form-control" value="<%= course.getTitle() %>" required>
                </div>

                <div class="form-group">
                    <label for="description">Description:</label>
                    <textarea name="description" id="description" rows="5" class="form-control"><%= course.getDescription() %></textarea>
                </div>

                <div class="form-group">
                    <label for="syllabus">Syllabus:</label>
                    <textarea name="syllabus" id="syllabus" rows="4" class="form-control"><%= course.getSyllabus() %></textarea>
                </div>

                <div class="form-group">
                    <label for="instructor">Instructor:</label>
                    <select name="instructorId" id="instructor" class="form-control">
                        <option value="">-- Select Instructor --</option>
                        <% for(User u : instructors) { %>
                            <option value="<%= u.getUserId() %>" <%= u.getUserId() == course.getInstructorId() ? "selected" : "" %>><%= u.getName() %></option>
                        <% } %>
                    </select>
                </div>

                <div class="form-actions">
                    <button type="submit" class="btn btn-primary">Save Changes</button>
                    <a href="course?action=list" class="btn btn-secondary">Cancel</a>
                </div>
            </form>
        </div>
    </div>
</body>
</html>