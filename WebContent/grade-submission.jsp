<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ page import="com.lms.model.*, com.lms.dao.*" %>
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
    
    AssignmentSubmission submission = (AssignmentSubmission) request.getAttribute("submission");
    String error = request.getParameter("error");
    
    // Get assignment details
    Assignment assignment = null;
    if(submission != null) {
        AssignmentDAO assignmentDAO = new AssignmentDAO();
        assignment = assignmentDAO.getAssignmentById(submission.getAssignmentId());
    }
%>
<!DOCTYPE html>
<html>
<head>
    <meta charset="UTF-8">
    <title>Grade Submission - LMS</title>
    <link rel="stylesheet" href="css/style.css">
</head>
<body>
    <div class="navbar">
        <h2>LMS - Grade Submission</h2>
        <div>
            <a href="assignment?action=submissions&id=<%= submission != null ? submission.getAssignmentId() : "" %>">Back to Submissions</a>
            <a href="logout">Logout</a>
        </div>
    </div>
    
    <div class="container">
        <h1>Grade Student Submission</h1>
        
        <% if(error != null) { %>
            <div class="alert alert-error"><%= error %></div>
        <% } %>
        
        <% if(submission != null && assignment != null) { %>
            <div class="card">
                <h3>Assignment Information</h3>
                <p><strong>Assignment:</strong> <%= submission.getAssignmentTitle() %></p>
                <p><strong>Student:</strong> <%= submission.getStudentName() %></p>
                <p><strong>Submitted At:</strong> <%= submission.getSubmittedAt() %></p>
                <p><strong>Maximum Score:</strong> <%= assignment.getMaxScore() %></p>
                <p><strong>Current Status:</strong> 
                    <span class="<%= "graded".equals(submission.getStatus()) ? "status-graded" : "status-pending" %>">
                        <%= submission.getStatus() %>
                    </span>
                </p>
            </div>
            
            <div class="card">
                <h3>Student's Submission</h3>
                <div class="pre-formatted">
                    <%= submission.getSubmissionText() != null ? submission.getSubmissionText() : "No text submission" %>
                </div>
                
                <% if(submission.getFilePath() != null && !submission.getFilePath().trim().isEmpty()) { %>
                    <p><strong>File/Link:</strong> <a href="<%= submission.getFilePath() %>" target="_blank"><%= submission.getFilePath() %></a></p>
                <% } %>
            </div>
            
            <div class="card">
                <h3>Grade This Submission</h3>
                <form action="assignment" method="post">
                    <input type="hidden" name="action" value="grade">
                    <input type="hidden" name="submissionId" value="<%= submission.getSubmissionId() %>">
                    <input type="hidden" name="assignmentId" value="<%= submission.getAssignmentId() %>">
                    
                    <div class="form-group">
                        <label for="grade">Grade (out of <%= assignment.getMaxScore() %>):</label>
                        <input type="number" id="grade" name="grade" 
                               min="0" max="<%= assignment.getMaxScore() %>" step="0.5"
                               value="<%= submission.getGrade() > 0 ? submission.getGrade() : "" %>"
                               required>
                    </div>
                    
                    <div class="form-group">
                        <label for="feedback">Feedback:</label>
                        <textarea id="feedback" name="feedback" rows="6" 
                                  placeholder="Provide feedback to the student..." required><%= submission.getFeedback() != null ? submission.getFeedback() : "" %></textarea>
                    </div>
                    
                    <button type="submit" class="btn btn-success">
                        <%= "graded".equals(submission.getStatus()) ? "Update Grade" : "Submit Grade" %>
                    </button>
                    <a href="assignment?action=submissions&id=<%= submission.getAssignmentId() %>" class="btn btn-danger">Cancel</a>
                </form>
            </div>
            
            <% if("graded".equals(submission.getStatus()) && submission.getGradedAt() != null) { %>
                <div class="card">
                    <h3>Grading History</h3>
                    <p><strong>Graded At:</strong> <%= submission.getGradedAt() %></p>
                    <p><strong>Current Grade:</strong> <%= String.format("%.1f", submission.getGrade()) %> / <%= assignment.getMaxScore() %></p>
                    <p><strong>Current Feedback:</strong></p>
                <div class="pre-formatted">
                    <%= submission.getFeedback() != null ? submission.getFeedback() : "No feedback" %>
                </div>
                </div>
            <% } %>
            
        <% } else { %>
            <div class="alert alert-error">Submission not found!</div>
            <a href="assignment?action=list" class="btn btn-primary">Back to Assignments</a>
        <% } %>
    </div>
</body>
</html>