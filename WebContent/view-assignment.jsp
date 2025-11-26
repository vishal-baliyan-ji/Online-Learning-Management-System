<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ page import="java.util.*, com.lms.model.*" %>
<%
    if(session.getAttribute("user") == null) {
        response.sendRedirect("login.jsp");
        return;
    }
    
    Assignment assignment = (Assignment) request.getAttribute("assignment");
%>
<!DOCTYPE html>
<html>
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title>Assignment Details - LMS</title>
    <link rel="stylesheet" href="css/style.css">
</head>
<body>
    <nav class="navbar">
        <div class="navbar-container">
            <h1 class="navbar-brand">LMS</h1>
            <div class="navbar-menu">
                <a href="assignment?action=list" class="nav-link">Assignments</a>
                <a href="course?action=list" class="nav-link">Courses</a>
                <a href="dashboard.jsp" class="nav-link">Dashboard</a>
                <a href="logout" class="nav-link">Logout</a>
            </div>
        </div>
    </nav>

    <div class="container">
        <% if(assignment != null) { %>
            <div class="card">
                <h1><%= assignment.getTitle() %></h1>
                
                <div class="assignment-details">
                    <div class="detail-row">
                        <span class="label">Course:</span>
                        <span class="value"><%= assignment.getCourseName() != null ? assignment.getCourseName() : "N/A" %></span>
                    </div>
                    
                    <div class="detail-row">
                        <span class="label">Due Date:</span>
                        <span class="value"><%= assignment.getDueDate() %></span>
                    </div>
                    
                    <div class="detail-row">
                        <span class="label">Max Score:</span>
                        <span class="value"><%= assignment.getMaxScore() %></span>
                    </div>
                    
                    <% if(assignment.getAttachmentPath() != null && !assignment.getAttachmentPath().isEmpty()) { %>
                        <div class="detail-row">
                            <span class="label">Attachment:</span>
                            <span class="value">
                                <a href="<%= assignment.getAttachmentPath() %>" target="_blank" class="btn btn-info">
                                    Download Attachment
                                </a>
                            </span>
                        </div>
                    <% } %>
                </div>

                <div class="assignment-description">
                    <h3>Description</h3>
                    <p><%= assignment.getDescription() != null ? assignment.getDescription() : "No description provided" %></p>
                </div>

                <div class="action-buttons">
                    <a href="assignment?action=submit&id=<%= assignment.getAssignmentId() %>" class="btn btn-primary">Submit Assignment</a>
                    <a href="assignment?action=list" class="btn btn-secondary">Back to Assignments</a>
                </div>
            </div>
        <% } else { %>
            <div class="alert alert-error">Assignment not found</div>
            <a href="assignment?action=list" class="btn btn-secondary">Back to Assignments</a>
        <% } %>
    </div>

    <style>
        .card {
            background: white;
            border-left: 5px solid #2ecc71;
            border-radius: 4px;
            box-shadow: 0 2px 4px rgba(0, 0, 0, 0.1);
            padding: 30px;
            margin: 20px 0;
        }

        .assignment-details {
            background: #f9f9f9;
            padding: 20px;
            border-radius: 4px;
            margin: 20px 0;
        }

        .detail-row {
            display: flex;
            margin: 15px 0;
            align-items: center;
        }

        .detail-row .label {
            font-weight: 600;
            color: #1b7f3f;
            min-width: 120px;
            margin-right: 20px;
        }

        .detail-row .value {
            flex: 1;
            color: #333;
        }

        .assignment-description {
            margin: 30px 0;
            padding: 20px;
            background: #f0f7f0;
            border-radius: 4px;
        }

        .assignment-description h3 {
            color: #1b7f3f;
            margin-top: 0;
        }

        .assignment-description p {
            line-height: 1.8;
            color: #333;
            white-space: pre-wrap;
        }

        .action-buttons {
            display: flex;
            gap: 10px;
            margin-top: 30px;
        }

        .btn {
            padding: 10px 20px;
            border: none;
            border-radius: 4px;
            cursor: pointer;
            font-size: 1rem;
            text-decoration: none;
            display: inline-block;
            text-align: center;
            transition: all 0.3s ease;
        }

        .btn-primary {
            background: #2ecc71;
            color: white;
        }

        .btn-primary:hover {
            background: #1b7f3f;
        }

        .btn-secondary {
            background: #ccc;
            color: #333;
        }

        .btn-secondary:hover {
            background: #999;
        }

        .btn-info {
            background: #3498db;
            color: white;
            padding: 8px 16px;
            font-size: 0.95rem;
        }

        .btn-info:hover {
            background: #2980b9;
        }
    </style>
</body>
</html>
