<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ page import="com.lms.model.*" %>
<%
    if(session.getAttribute("user") == null || !"student".equals(session.getAttribute("userRole"))) {
        response.sendRedirect("login.jsp");
        return;
    }
    
    Assignment assignment = (Assignment) request.getAttribute("assignment");
    String error = request.getParameter("error");
%>
<!DOCTYPE html>
<html>
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title>Submit Assignment - LMS</title>
    <link rel="stylesheet" href="css/style.css">
</head>
<body>
    <nav class="navbar">
        <div class="navbar-container">
            <h1 class="navbar-brand">LMS</h1>
            <div class="navbar-menu">
                <a href="student-dashboard.jsp" class="nav-link">Dashboard</a>
                <a href="assignment?action=my-assignments" class="nav-link">My Assignments</a>
                <a href="logout" class="nav-link">Logout</a>
            </div>
        </div>
    </nav>

    <div class="container">
        <% if(assignment != null) { %>
            <div class="submission-section">
                <h1>Submit: <%= assignment.getTitle() %></h1>
                
                <div class="assignment-info">
                    <div class="info-item">
                        <span class="label">Course:</span>
                        <span class="value"><%= assignment.getCourseName() %></span>
                    </div>
                    <div class="info-item">
                        <span class="label">Due Date:</span>
                        <span class="value"><%= assignment.getDueDate() %></span>
                    </div>
                    <div class="info-item">
                        <span class="label">Max Score:</span>
                        <span class="value"><%= assignment.getMaxScore() %></span>
                    </div>
                </div>

                <div class="assignment-requirements">
                    <h3>Assignment Details</h3>
                    <p><%= assignment.getDescription() != null ? assignment.getDescription() : "No description provided" %></p>
                </div>

                <% if(assignment.getAttachmentPath() != null && !assignment.getAttachmentPath().isEmpty()) { %>
                    <div class="assignment-attachment">
                        <h3>📎 Course Materials</h3>
                        <p>Your instructor has provided materials for this assignment.</p>
                        <a href="<%= assignment.getAttachmentPath() %>" target="_blank" class="btn btn-info">Download Materials</a>
                    </div>
                <% } %>

                <% if(error != null) { %>
                    <div class="alert alert-error"><%= error %></div>
                <% } %>
                
                <form action="assignment?action=submit" method="POST" enctype="multipart/form-data" class="submission-form">
                    <input type="hidden" name="assignmentId" value="<%= assignment.getAssignmentId() %>">
                    
                    <div class="form-group">
                        <label for="submissionText">Your Response (Optional):</label>
                        <textarea name="submissionText" id="submissionText" rows="6" 
                                  placeholder="Write your response or explanation here..." 
                                  class="form-control"></textarea>
                    </div>

                    <div class="form-group">
                        <label for="submissionFile">Upload Your Work:</label>
                        <input type="file" name="submissionFile" id="submissionFile" 
                               accept=".pdf,.doc,.docx,.txt,.jpg,.jpeg,.png,.zip,.rar" 
                               class="form-control" required>
                        <small class="form-text">Allowed formats: PDF, DOC, DOCX, TXT, JPG, PNG, ZIP, RAR (Max 10MB)</small>
                    </div>

                    <div class="form-actions">
                        <button type="submit" class="btn btn-primary">Submit Assignment</button>
                        <a href="assignment?action=my-assignments" class="btn btn-secondary">Cancel</a>
                    </div>
                </form>
            </div>
        <% } else { %>
            <div class="card">
                <div class="alert alert-error">Assignment not found</div>
                <a href="assignment?action=my-assignments" class="btn btn-secondary">Back to My Assignments</a>
            </div>
        <% } %>
    </div>

    <style>
        .submission-section {
            max-width: 800px;
            margin: 30px auto;
            background: white;
            border-radius: 4px;
        }

        .submission-section h1 {
            color: #1b7f3f;
            margin-bottom: 30px;
            border-bottom: 2px solid #2ecc71;
            padding-bottom: 15px;
        }

        .assignment-info {
            background: #f0f7f0;
            padding: 20px;
            border-radius: 4px;
            margin: 20px 0;
        }

        .info-item {
            display: flex;
            justify-content: space-between;
            padding: 10px 0;
            border-bottom: 1px solid #e0e0e0;
        }

        .info-item:last-child {
            border-bottom: none;
        }

        .info-item .label {
            font-weight: 600;
            color: #1b7f3f;
            min-width: 150px;
        }

        .info-item .value {
            color: #333;
        }

        .assignment-requirements {
            background: #ffffff;
            padding: 20px;
            border: 1px solid #ddd;
            border-radius: 4px;
            margin: 20px 0;
        }

        .assignment-requirements h3 {
            color: #1b7f3f;
            margin-top: 0;
        }

        .assignment-requirements p {
            line-height: 1.8;
            color: #555;
            white-space: pre-wrap;
        }

        .assignment-attachment {
            background: #e3f2fd;
            padding: 20px;
            border-radius: 4px;
            border-left: 5px solid #3498db;
            margin: 20px 0;
        }

        .assignment-attachment h3 {
            color: #1976d2;
            margin-top: 0;
        }

        .assignment-attachment p {
            color: #1565c0;
            margin: 10px 0;
        }

        .submission-form {
            background: #fafafa;
            padding: 25px;
            border-radius: 4px;
            margin: 25px 0;
        }

        .form-group {
            margin-bottom: 20px;
        }

        .form-group label {
            display: block;
            font-weight: 600;
            color: #1b7f3f;
            margin-bottom: 8px;
        }

        .form-control {
            width: 100%;
            padding: 12px;
            border: 1px solid #ddd;
            border-radius: 4px;
            font-family: inherit;
            font-size: 1rem;
        }

        .form-control:focus {
            outline: none;
            border-color: #2ecc71;
            box-shadow: 0 0 5px rgba(46, 204, 113, 0.3);
        }

        textarea.form-control {
            resize: vertical;
            min-height: 120px;
        }

        .form-text {
            display: block;
            margin-top: 5px;
            font-size: 0.85rem;
            color: #666;
        }

        .form-actions {
            display: flex;
            gap: 15px;
            margin-top: 25px;
        }

        .btn {
            padding: 12px 24px;
            border: none;
            border-radius: 4px;
            cursor: pointer;
            text-decoration: none;
            display: inline-block;
            text-align: center;
            font-size: 1rem;
            transition: all 0.3s ease;
        }

        .btn-primary {
            background: #2ecc71;
            color: white;
            flex: 1;
        }

        .btn-primary:hover {
            background: #1b7f3f;
        }

        .btn-secondary {
            background: #ccc;
            color: #333;
            flex: 1;
        }

        .btn-secondary:hover {
            background: #999;
        }

        .btn-info {
            background: #3498db;
            color: white;
            flex: none;
        }

        .btn-info:hover {
            background: #2980b9;
        }

        .alert {
            padding: 15px;
            border-radius: 4px;
            margin: 20px 0;
        }

        .alert-error {
            background: #f8d7da;
            color: #721c24;
            border: 1px solid #f5c6cb;
        }

        .card {
            background: white;
            border-left: 5px solid #2ecc71;
            padding: 30px;
            border-radius: 4px;
            text-align: center;
        }
    </style>
</body>
</html>
