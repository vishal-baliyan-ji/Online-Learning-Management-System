<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<!DOCTYPE html>
<html>
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title>Create Assignment</title>
    <link rel="stylesheet" href="css/style.css">
</head>
<body>
    <nav class="navbar">
        <div class="navbar-container">
            <h1 class="navbar-brand">LMS</h1>
            <div class="navbar-menu">
                <a href="assignment?action=list" class="nav-link">All Assignments</a>
                <a href="course?action=list" class="nav-link">Courses</a>
                <a href="dashboard.jsp" class="nav-link">Dashboard</a>
                <a href="logout" class="nav-link">Logout</a>
            </div>
        </div>
    </nav>

    <div class="container">
        <div class="form-section">
            <h2>Create New Assignment</h2>
            
            <form action="assignment?action=add" method="POST" enctype="multipart/form-data" class="form">
                <div class="form-group">
                    <label for="course">Select Course:</label>
                    <select name="course_id" id="course" required class="form-control">
                        <option value="">-- Select a Course --</option>
                        <c:forEach var="course" items="${courses}">
                            <option value="${course.courseId}">${course.title}</option>
                        </c:forEach>
                    </select>
                </div>

                <div class="form-group">
                    <label for="title">Assignment Title:</label>
                    <input type="text" name="title" id="title" required placeholder="Enter assignment title" class="form-control">
                </div>

                <div class="form-group">
                    <label for="description">Description:</label>
                    <textarea name="description" id="description" rows="5" placeholder="Enter assignment description" class="form-control"></textarea>
                </div>

                <div class="form-group">
                    <label for="dueDate">Due Date:</label>
                    <input type="date" name="due_date" id="dueDate" required class="form-control">
                </div>

                <div class="form-group">
                    <label for="maxScore">Max Score:</label>
                    <input type="number" name="max_score" id="maxScore" value="100" min="0" max="999" required class="form-control">
                </div>

                <div class="form-group">
                    <label for="attachment">Attachment (Image/PDF):</label>
                    <input type="file" name="attachment" id="attachment" accept=".jpg,.jpeg,.png,.gif,.pdf,.doc,.docx" class="form-control">
                    <small class="form-text">Allowed formats: JPG, PNG, GIF, PDF, DOC, DOCX (Max 5MB)</small>
                </div>

                <div class="form-actions">
                    <button type="submit" class="btn btn-primary">Create Assignment</button>
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
    </style>
</body>
</html>
