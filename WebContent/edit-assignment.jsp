<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<!DOCTYPE html>
<html>
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title>Edit Assignment</title>
    <link rel="stylesheet" href="css/style.css">
</head>
<body>
    <nav class="navbar">
        <div class="navbar-container">
            <h1 class="navbar-brand">LMS</h1>
            <div class="navbar-menu">
                <a href="assignment?action=list" class="nav-link">All Assignments</a>
                <a href="course?action=list" class="nav-link">Courses</a>
                <a href="instructor-dashboard.jsp" class="nav-link">Dashboard</a>
                <a href="logout" class="nav-link">Logout</a>
            </div>
        </div>
    </nav>

    <div class="container">
        <div class="form-section">
            <h2>Edit Assignment</h2>
            
            <c:if test="${error != null}">
                <div class="alert alert-error">${error}</div>
            </c:if>
            
            <form action="assignment?action=edit" method="POST" enctype="multipart/form-data" class="form">
                <input type="hidden" name="assignmentId" value="${assignment.assignmentId}">
                
                <div class="form-group">
                    <label for="course">Select Course:</label>
                    <select name="course_id" id="course" required class="form-control" disabled>
                        <c:forEach var="course" items="${courses}">
                            <option value="${course.courseId}" 
                                <c:if test="${course.courseId == assignment.courseId}">selected</c:if>>
                                ${course.title}
                            </option>
                        </c:forEach>
                    </select>
                </div>

                <div class="form-group">
                    <label for="title">Assignment Title:</label>
                    <input type="text" name="title" id="title" required value="${assignment.title}" 
                           placeholder="Enter assignment title" class="form-control">
                </div>

                <div class="form-group">
                    <label for="description">Description:</label>
                    <textarea name="description" id="description" rows="5" 
                              placeholder="Enter assignment description" class="form-control">${assignment.description}</textarea>
                </div>

                <div class="form-group">
                    <label for="dueDate">Due Date:</label>
                    <input type="date" name="due_date" id="dueDate" required value="${assignment.dueDate}" class="form-control">
                </div>

                <div class="form-group">
                    <label for="maxScore">Max Score:</label>
                    <input type="number" name="max_score" id="maxScore" value="${assignment.maxScore}" 
                           min="0" max="999" required class="form-control">
                </div>

                <div class="form-group">
                    <label>Current Attachment:</label>
                    <c:if test="${not empty assignment.attachmentPath}">
                        <p>
                            <a href="${assignment.attachmentPath}" target="_blank" class="btn btn-small btn-info">Download Current File</a>
                        </p>
                    </c:if>
                    <c:if test="${empty assignment.attachmentPath}">
                        <p class="text-muted">No attachment</p>
                    </c:if>
                </div>

                <div class="form-group">
                    <label for="attachment">Replace Attachment (Optional):</label>
                    <input type="file" name="attachment" id="attachment" 
                           accept=".jpg,.jpeg,.png,.gif,.pdf,.doc,.docx" class="form-control">
                    <small class="form-text">Allowed formats: JPG, PNG, GIF, PDF, DOC, DOCX (Max 5MB)</small>
                </div>

                <div class="form-actions">
                    <button type="submit" class="btn btn-primary">Update Assignment</button>
                    <a href="assignment?action=list" class="btn btn-secondary">Cancel</a>
                </div>
            </form>
        </div>
    </div>

    <style>
        .form-section {
            max-width: 600px;
            margin: 30px auto;
            padding: 30px;
            background: white;
            border-left: 5px solid #2ecc71;
            border-radius: 4px;
            box-shadow: 0 2px 4px rgba(0, 0, 0, 0.1);
        }

        .form-section h2 {
            color: #1b7f3f;
            margin-bottom: 25px;
        }

        .form {
            display: flex;
            flex-direction: column;
        }

        .form-text {
            display: block;
            margin-top: 5px;
            font-size: 0.85rem;
            color: #666;
        }

        .form-actions {
            display: flex;
            gap: 10px;
            margin-top: 25px;
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

        .text-muted {
            color: #999;
            font-size: 0.9rem;
        }

        .btn-small {
            padding: 5px 10px;
            font-size: 0.85rem;
        }

        .btn-info {
            background: #3498db;
            color: white;
        }

        .btn-info:hover {
            background: #2980b9;
        }
    </style>
</body>
</html>
