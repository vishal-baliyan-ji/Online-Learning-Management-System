<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ page import="com.lms.model.*, java.util.*" %>
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

    Assignment assignment = (Assignment) request.getAttribute("assignment");
    List<AssignmentSubmission> submissions = (List<AssignmentSubmission>) request.getAttribute("submissions");
    String success = request.getParameter("success");
    String error = request.getParameter("error");
%>
<!DOCTYPE html>
<html>
<head>
    <meta charset="UTF-8">
    <title>Submissions - <%= assignment != null ? assignment.getTitle() : "Assignments" %></title>
    <link rel="stylesheet" href="css/style.css">
</head>
<body>
    <div class="navbar">
        <h2>LMS - Submissions</h2>
        <div>
            <a href="assignment?action=list">Assignments</a>
            <a href="logout">Logout</a>
        </div>
    </div>

    <div class="container">
        <h1>Submissions for: <%= assignment != null ? assignment.getTitle() : "" %></h1>

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
                        <th>Student</th>
                        <th>Submitted At</th>
                        <th>File</th>
                        <th>Status</th>
                        <th>Grade</th>
                        <th>Actions</th>
                    </tr>
                </thead>
                <tbody>
                    <% if(submissions != null && !submissions.isEmpty()) {
                        for(AssignmentSubmission s : submissions) { %>
                    <tr>
                        <td><%= s.getStudentName() %></td>
                        <td><%= s.getSubmittedAt() %></td>
                        <td>
                            <% if(s.getFilePath() != null && !s.getFilePath().isEmpty()) { %>
                                <a href="<%= s.getFilePath() %>" target="_blank" class="btn btn-info btn-small">Download</a>
                            <% } else { %>
                                <span class="text-muted">No File</span>
                            <% } %>
                        </td>
                        <td><span class="<%= "graded".equals(s.getStatus()) ? "status-graded" : "status-pending" %>"><%= s.getStatus() %></span></td>
                        <td><%= s.getGrade() > 0 ? String.format("%.1f", s.getGrade()) : "-" %></td>
                        <td>
                            <a href="assignment?action=grade&id=<%= s.getSubmissionId() %>" class="btn btn-success">Grade</a>
                            <a href="assignment?action=view&id=<%= s.getAssignmentId() %>" class="btn btn-primary">View Assignment</a>
                        </td>
                    </tr>
                    <% } %>
                    <% } else { %>
                    <tr>
                        <td colspan="6" class="text-centered">No submissions yet for this assignment.</td>
                    </tr>
                    <% } %>
                </tbody>
            </table>
        </div>
    </div>
</body>
</html>
