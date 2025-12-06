<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>

<!DOCTYPE html>
<html>
<head>
    <meta charset="UTF-8">
    <title>Create New Quiz</title>
    <link rel="stylesheet" href="css/style.css">
    <style>
        .create-quiz-container {
            max-width: 700px;
            margin: 30px auto;
            padding: 20px;
        }
        .form-card {
            background: white;
            border: 1px solid #e0e0e0;
            border-radius: 8px;
            padding: 30px;
            box-shadow: 0 2px 4px rgba(0,0,0,0.1);
        }
        .form-title {
            font-size: 24px;
            font-weight: bold;
            color: #27ae60;
            margin-bottom: 20px;
        }
        .form-group {
            margin-bottom: 20px;
        }
        .form-group label {
            display: block;
            margin-bottom: 8px;
            font-weight: bold;
            color: #333;
        }
        .form-group input,
        .form-group textarea {
            width: 100%;
            padding: 10px;
            border: 1px solid #ddd;
            border-radius: 4px;
            font-size: 14px;
            font-family: Arial, sans-serif;
            box-sizing: border-box;
        }
        .form-group textarea {
            resize: vertical;
            min-height: 80px;
        }
        .form-row {
            display: grid;
            grid-template-columns: 1fr 1fr;
            gap: 15px;
        }
        .btn-create {
            background-color: #27ae60;
            color: white;
            padding: 12px 30px;
            border: none;
            border-radius: 5px;
            cursor: pointer;
            font-size: 16px;
            font-weight: bold;
            width: 100%;
            margin-bottom: 10px;
        }
        .btn-create:hover {
            background-color: #229954;
        }
        .btn-cancel {
            background-color: #95a5a6;
            color: white;
            padding: 12px 30px;
            border: none;
            border-radius: 5px;
            cursor: pointer;
            text-decoration: none;
            display: block;
            text-align: center;
            width: 100%;
        }
        .btn-cancel:hover {
            background-color: #7f8c8d;
        }
        .error {
            color: #e74c3c;
            margin-bottom: 20px;
            padding: 10px;
            background-color: #fadbd8;
            border-radius: 4px;
        }
    </style>
</head>
<body>
    <div class="create-quiz-container">
        <div class="form-card">
            <div class="form-title">Create New Quiz</div>
            
            <c:if test="${not empty error}">
                <div class="error">${error}</div>
            </c:if>
            
            <form method="POST" action="quiz">
                <input type="hidden" name="action" value="create">
                <input type="hidden" name="courseId" value="${courseId}">
                
                <div class="form-group">
                    <label>Quiz Title *</label>
                    <input type="text" name="title" placeholder="e.g., Chapter 1 Quiz" required>
                </div>
                
                <div class="form-group">
                    <label>Description</label>
                    <textarea name="description" placeholder="Describe what this quiz covers..."></textarea>
                </div>
                
                <div class="form-row">
                    <div class="form-group">
                        <label>Duration (minutes) *</label>
                        <input type="number" name="durationMinutes" value="30" min="1" required>
                    </div>
                    <div class="form-group">
                        <label>Max Score *</label>
                        <input type="number" name="maxScore" value="100" min="1" required>
                    </div>
                </div>
                
                <div class="form-group">
                    <label>Passing Score *</label>
                    <input type="number" name="passingScore" value="60" min="0" required>
                </div>
                
                <button type="submit" class="btn-create">Create Quiz</button>
                <a href="quiz?action=list&courseId=${courseId}" class="btn-cancel">Cancel</a>
            </form>
        </div>
    </div>
</body>
</html>
