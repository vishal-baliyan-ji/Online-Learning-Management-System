<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ page import="java.util.*, com.lms.model.*" %>
<%
    if(session.getAttribute("user") == null || !"student".equals(session.getAttribute("userRole"))) {
        response.sendRedirect("login.jsp");
        return;
    }
    
    List<Assignment> assignments = (List<Assignment>) request.getAttribute("assignments");
    List<AssignmentSubmission> submissions = (List<AssignmentSubmission>) request.getAttribute("submissions");
    String success = request.getParameter("success");
    String error = request.getParameter("error");
    
    // Create map of submitted assignment IDs for quick lookup
    java.util.Set<Integer> submittedIds = new java.util.HashSet<>();
    if(submissions != null) {
        for(AssignmentSubmission sub : submissions) {
            submittedIds.add(sub.getAssignmentId());
        }
    }
%>
<!DOCTYPE html>
<html>
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title>My Assignments - LMS</title>
    <link rel="stylesheet" href="css/style.css">
</head>
<body>
    <nav class="navbar">
        <div class="navbar-container">
            <h1 class="navbar-brand">LMS</h1>
            <div class="navbar-menu">
                <a href="student-dashboard.jsp" class="nav-link">Dashboard</a>
                <a href="enrollment?action=enroll" class="nav-link">Enroll in Course</a>
                <a href="assignment?action=my-assignments" class="nav-link">My Assignments</a>
                <a href="logout" class="nav-link">Logout</a>
            </div>
        </div>
    </nav>

    <div class="container">
        <h1>My Assignments</h1>
        
        <% if(success != null) { %>
            <div class="alert alert-success"><%= success %></div>
        <% } %>
        
        <% if(error != null) { %>
            <div class="alert alert-error"><%= error %></div>
        <% } %>
        
        <% if(assignments != null && !assignments.isEmpty()) { %>
            <div class="assignments-grid">
                <% for(Assignment assignment : assignments) { 
                    boolean isSubmitted = submittedIds.contains(assignment.getAssignmentId());
                %>
                <div class="assignment-card">
                    <div class="card-header">
                        <h3><%= assignment.getTitle() %></h3>
                        <span class="course-badge"><%= assignment.getCourseName() %></span>
                    </div>
                    
                    <div class="card-body">
                        <p class="assignment-description"><%= assignment.getDescription() != null ? assignment.getDescription().substring(0, Math.min(100, assignment.getDescription().length())) + "..." : "" %></p>
                        
                        <div class="assignment-meta">
                            <div class="meta-item">
                                <span class="label">Due Date:</span>
                                <span class="value"><%= assignment.getDueDate() %></span>
                            </div>
                            <div class="meta-item">
                                <span class="label">Max Score:</span>
                                <span class="value"><%= assignment.getMaxScore() %></span>
                            </div>
                        </div>
                        
                        <% if(assignment.getAttachmentPath() != null && !assignment.getAttachmentPath().isEmpty()) { %>
                            <div class="attachment-section">
                                <p class="attachment-label">📎 Attachment Available</p>
                                <a href="<%= assignment.getAttachmentPath() %>" target="_blank" class="btn btn-small btn-info">Download Materials</a>
                            </div>
                        <% } %>
                        
                        <div class="submission-status">
                            <% if(isSubmitted) { %>
                                <span class="status-badge status-submitted">✓ Submitted</span>
                            <% } else { %>
                                <span class="status-badge status-pending">⚠ Pending</span>
                            <% } %>
                        </div>
                    </div>
                    
                    <div class="card-footer">
                        <a href="assignment?action=view&id=<%= assignment.getAssignmentId() %>" class="btn btn-primary">View Details</a>
                        <% if(!isSubmitted) { %>
                            <a href="assignment?action=submit&id=<%= assignment.getAssignmentId() %>" class="btn btn-success">Submit Assignment</a>
                        <% } else { %>
                            <a href="assignment?action=view-submission&id=<%= assignment.getAssignmentId() %>" class="btn btn-info">View Submission</a>
                        <% } %>
                    </div>
                </div>
                <% } %>
            </div>
        <% } else { %>
            <div class="card">
                <p class="text-centered">You have no assignments in your enrolled courses.</p>
                <p class="text-centered"><a href="enrollment?action=enroll" class="btn btn-primary">Enroll in a course</a></p>
            </div>
        <% } %>
    </div>

    <style>
        .assignments-grid {
            display: grid;
            grid-template-columns: repeat(auto-fill, minmax(350px, 1fr));
            gap: 20px;
            margin: 20px 0;
        }

        .assignment-card {
            background: white;
            border: 1px solid #ddd;
            border-left: 5px solid #2ecc71;
            border-radius: 4px;
            overflow: hidden;
            display: flex;
            flex-direction: column;
            box-shadow: 0 2px 4px rgba(0, 0, 0, 0.1);
            transition: box-shadow 0.3s ease;
        }

        .assignment-card:hover {
            box-shadow: 0 4px 8px rgba(0, 0, 0, 0.15);
        }

        .card-header {
            background: #f9f9f9;
            padding: 15px;
            border-bottom: 1px solid #eee;
        }

        .card-header h3 {
            margin: 0 0 8px 0;
            color: #1b7f3f;
            font-size: 18px;
        }

        .course-badge {
            display: inline-block;
            background: #2ecc71;
            color: white;
            padding: 4px 8px;
            border-radius: 3px;
            font-size: 0.8rem;
            font-weight: 600;
        }

        .card-body {
            padding: 15px;
            flex: 1;
        }

        .assignment-description {
            color: #666;
            margin: 0 0 15px 0;
            line-height: 1.5;
        }

        .assignment-meta {
            background: #f0f7f0;
            padding: 12px;
            border-radius: 3px;
            margin: 12px 0;
        }

        .meta-item {
            display: flex;
            justify-content: space-between;
            margin: 5px 0;
            font-size: 0.9rem;
        }

        .meta-item .label {
            font-weight: 600;
            color: #1b7f3f;
        }

        .meta-item .value {
            color: #666;
        }

        .attachment-section {
            background: #e3f2fd;
            padding: 12px;
            border-radius: 3px;
            margin: 12px 0;
            border-left: 3px solid #3498db;
        }

        .attachment-label {
            margin: 0 0 8px 0;
            color: #1976d2;
            font-weight: 600;
            font-size: 0.9rem;
        }

        .submission-status {
            margin: 12px 0;
            text-align: center;
        }

        .status-badge {
            display: inline-block;
            padding: 6px 12px;
            border-radius: 3px;
            font-weight: 600;
            font-size: 0.85rem;
        }

        .status-submitted {
            background: #d4edda;
            color: #155724;
        }

        .status-pending {
            background: #fff3cd;
            color: #856404;
        }

        .card-footer {
            padding: 15px;
            background: #f9f9f9;
            border-top: 1px solid #eee;
            display: flex;
            gap: 10px;
        }

        .btn {
            padding: 8px 12px;
            border: none;
            border-radius: 3px;
            cursor: pointer;
            text-decoration: none;
            display: inline-block;
            text-align: center;
            font-size: 0.85rem;
            transition: all 0.3s ease;
            flex: 1;
        }

        .btn-primary {
            background: #2ecc71;
            color: white;
        }

        .btn-primary:hover {
            background: #1b7f3f;
        }

        .btn-success {
            background: #27ae60;
            color: white;
        }

        .btn-success:hover {
            background: #229954;
        }

        .btn-info {
            background: #3498db;
            color: white;
        }

        .btn-info:hover {
            background: #2980b9;
        }

        .btn-small {
            padding: 5px 10px;
            font-size: 0.8rem;
            flex: none;
        }

        .alert {
            padding: 15px;
            border-radius: 4px;
            margin: 20px 0;
        }

        .alert-success {
            background: #d4edda;
            color: #155724;
            border: 1px solid #c3e6cb;
        }

        .alert-error {
            background: #f8d7da;
            color: #721c24;
            border: 1px solid #f5c6cb;
        }

        .text-centered {
            text-align: center;
            color: #666;
            padding: 30px 20px;
        }
    </style>
</body>
</html>
