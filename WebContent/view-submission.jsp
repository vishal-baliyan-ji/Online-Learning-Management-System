<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ page import="java.util.*, com.lms.model.*" %>
<%
    if(session.getAttribute("user") == null || !"student".equals(session.getAttribute("userRole"))) {
        response.sendRedirect("login.jsp");
        return;
    }
    
    Integer studentId = (Integer) session.getAttribute("userId");
    int assignmentId = Integer.parseInt(request.getParameter("id"));

    // Prefer attributes set by servlet (if any)
    Assignment assignment = (Assignment) request.getAttribute("assignment");
    AssignmentSubmission studentSubmission = (AssignmentSubmission) request.getAttribute("submission");

    if (assignment == null || studentSubmission == null) {
        com.lms.dao.AssignmentDAO assignmentDAO = new com.lms.dao.AssignmentDAO();
        com.lms.dao.AssignmentSubmissionDAO submissionDAO = new com.lms.dao.AssignmentSubmissionDAO();

        assignment = assignmentDAO.getAssignmentById(assignmentId);
        List<AssignmentSubmission> submissions = submissionDAO.getSubmissionsByStudent(studentId);
        // Find the submission for this assignment
        if (studentSubmission == null && submissions != null) {
            for (AssignmentSubmission sub : submissions) {
                if (sub.getAssignmentId() == assignmentId) {
                    studentSubmission = sub;
                    break;
                }
            }
        }
    }
%>
<!DOCTYPE html>
<html>
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title>My Submission - LMS</title>
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
        <% if(assignment != null && studentSubmission != null) { %>
            <div class="submission-detail">
                <h1>Submission Details</h1>
                
                <div class="section">
                    <h2>Assignment Information</h2>
                    <div class="info-grid">
                        <div class="info-item">
                            <span class="label">Title:</span>
                            <span class="value"><%= assignment.getTitle() %></span>
                        </div>
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
                </div>

                <div class="section">
                    <h2>Your Submission</h2>
                    <div class="submission-info">
                        <div class="info-item">
                            <span class="label">Status:</span>
                            <span class="value">
                                <span class="status-badge <%= "graded".equals(studentSubmission.getStatus()) ? "status-graded" : "status-submitted" %>">
                                    <%= studentSubmission.getStatus().toUpperCase() %>
                                </span>
                            </span>
                        </div>
                        <div class="info-item">
                            <span class="label">Submitted At:</span>
                            <span class="value"><%= studentSubmission.getSubmittedAt() %></span>
                        </div>
                    </div>

                    <% if(studentSubmission.getSubmissionText() != null && !studentSubmission.getSubmissionText().isEmpty()) { %>
                        <div class="submission-text-box">
                            <h3>Your Response</h3>
                            <p><%= studentSubmission.getSubmissionText() %></p>
                        </div>
                    <% } %>

                    <% if(studentSubmission.getFilePath() != null && !studentSubmission.getFilePath().isEmpty()) { %>
                        <div class="submission-file-box">
                            <h3>📎 Your Submission File</h3>
                            <p>You have submitted the following file:</p>
                            <a href="<%= studentSubmission.getFilePath() %>" class="btn btn-info" target="_blank">Download Your Submission</a>
                        </div>
                    <% } %>
                </div>

                <% if("graded".equals(studentSubmission.getStatus())) { %>
                    <div class="section grading-section">
                        <h2>Your Grade</h2>
                        <div class="grade-display">
                            <div class="grade-box">
                                <span class="grade-label">Score:</span>
                                <span class="grade-value"><%= String.format("%.1f", studentSubmission.getGrade()) %> / <%= assignment.getMaxScore() %></span>
                            </div>
                            <div class="percentage-box">
                                <% double percentage = (studentSubmission.getGrade() / assignment.getMaxScore()) * 100; %>
                                <span class="percentage-label">Percentage:</span>
                                <span class="percentage-value <%= percentage >= 70 ? "excellent" : percentage >= 50 ? "good" : "poor" %>">
                                    <%= String.format("%.1f", percentage) %>%
                                </span>
                            </div>
                        </div>

                        <% if(studentSubmission.getFeedback() != null && !studentSubmission.getFeedback().isEmpty()) { %>
                            <div class="feedback-box">
                                <h3>Instructor Feedback</h3>
                                <p><%= studentSubmission.getFeedback() %></p>
                            </div>
                        <% } %>
                    </div>
                <% } else { %>
                    <div class="section">
                        <p class="info-message">⏳ Waiting for your instructor to grade your submission...</p>
                    </div>
                <% } %>

                <div class="action-buttons">
                    <a href="assignment?action=my-assignments" class="btn btn-secondary">Back to Assignments</a>
                </div>
            </div>
        <% } else { %>
            <div class="card">
                <div class="alert alert-error">Submission not found. You may not have submitted this assignment yet.</div>
                <a href="assignment?action=my-assignments" class="btn btn-secondary">Back to My Assignments</a>
            </div>
        <% } %>
    </div>

    <style>
        .submission-detail {
            max-width: 900px;
            margin: 30px auto;
            background: white;
            border-radius: 4px;
        }

        .submission-detail h1 {
            color: #1b7f3f;
            margin-bottom: 30px;
            border-bottom: 2px solid #2ecc71;
            padding-bottom: 15px;
        }

        .section {
            margin: 25px 0;
            padding: 20px;
            background: #f9f9f9;
            border-radius: 4px;
            border-left: 5px solid #2ecc71;
        }

        .section h2 {
            color: #1b7f3f;
            margin-top: 0;
            margin-bottom: 15px;
        }

        .info-grid {
            display: grid;
            grid-template-columns: repeat(auto-fit, minmax(200px, 1fr));
            gap: 15px;
        }

        .info-item {
            display: flex;
            flex-direction: column;
        }

        .info-item .label {
            font-weight: 600;
            color: #1b7f3f;
            margin-bottom: 5px;
            font-size: 0.9rem;
        }

        .info-item .value {
            color: #333;
            padding: 8px 12px;
            background: white;
            border-radius: 3px;
            border: 1px solid #ddd;
        }

        .submission-info {
            background: white;
            padding: 15px;
            border-radius: 3px;
            margin-bottom: 15px;
        }

        .submission-text-box {
            background: #f0f7f0;
            padding: 15px;
            border-radius: 3px;
            margin: 15px 0;
            border-left: 4px solid #2ecc71;
        }

        .submission-text-box h3 {
            color: #1b7f3f;
            margin-top: 0;
        }

        .submission-text-box p {
            color: #555;
            white-space: pre-wrap;
            line-height: 1.6;
        }

        .submission-file-box {
            background: #e3f2fd;
            padding: 15px;
            border-radius: 3px;
            margin: 15px 0;
            border-left: 4px solid #3498db;
        }

        .submission-file-box h3 {
            color: #1976d2;
            margin-top: 0;
        }

        .submission-file-box p {
            color: #1565c0;
            margin: 10px 0;
        }

        .status-badge {
            display: inline-block;
            padding: 6px 12px;
            border-radius: 3px;
            font-weight: 600;
            font-size: 0.85rem;
        }

        .status-submitted {
            background: #fff3cd;
            color: #856404;
        }

        .status-graded {
            background: #d4edda;
            color: #155724;
        }

        .grading-section {
            border-left: 5px solid #f39c12;
            background: #fef9e7;
        }

        .grade-display {
            display: grid;
            grid-template-columns: 1fr 1fr;
            gap: 15px;
            margin: 15px 0;
        }

        .grade-box, .percentage-box {
            background: white;
            padding: 20px;
            border-radius: 3px;
            text-align: center;
            border: 2px solid #2ecc71;
        }

        .grade-label, .percentage-label {
            display: block;
            font-weight: 600;
            color: #1b7f3f;
            margin-bottom: 10px;
        }

        .grade-value, .percentage-value {
            display: block;
            font-size: 24px;
            font-weight: bold;
        }

        .grade-value {
            color: #2ecc71;
        }

        .percentage-value.excellent {
            color: #27ae60;
        }

        .percentage-value.good {
            color: #f39c12;
        }

        .percentage-value.poor {
            color: #e74c3c;
        }

        .feedback-box {
            background: white;
            padding: 15px;
            border-radius: 3px;
            margin: 15px 0;
            border-left: 4px solid #3498db;
        }

        .feedback-box h3 {
            color: #1565c0;
            margin-top: 0;
        }

        .feedback-box p {
            color: #555;
            white-space: pre-wrap;
            line-height: 1.6;
        }

        .info-message {
            background: white;
            padding: 15px;
            border-radius: 3px;
            color: #f39c12;
            font-weight: 600;
            text-align: center;
        }

        .action-buttons {
            margin: 30px 0;
            text-align: center;
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
            margin-right: 10px;
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
