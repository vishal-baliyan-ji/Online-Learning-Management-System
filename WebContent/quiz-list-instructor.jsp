<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>

<!DOCTYPE html>
<html>
<head>
    <meta charset="UTF-8">
    <title>Quiz Management</title>
    <link rel="stylesheet" href="css/style.css">
    <style>
        .quiz-management {
            max-width: 1000px;
            margin: 30px auto;
            padding: 20px;
        }
        .header-section {
            display: flex;
            justify-content: space-between;
            align-items: center;
            margin-bottom: 30px;
        }
        .btn-create {
            background-color: #27ae60;
            color: white;
            padding: 10px 20px;
            border: none;
            border-radius: 5px;
            cursor: pointer;
            font-size: 14px;
            text-decoration: none;
            display: inline-block;
        }
        .btn-create:hover {
            background-color: #229954;
        }
        .quiz-card {
            background: white;
            border: 1px solid #e0e0e0;
            border-radius: 8px;
            padding: 20px;
            margin-bottom: 20px;
            box-shadow: 0 2px 4px rgba(0,0,0,0.1);
        }
        .quiz-title {
            font-size: 18px;
            font-weight: bold;
            color: #27ae60;
            margin-bottom: 10px;
        }
        .quiz-info {
            display: grid;
            grid-template-columns: 1fr 1fr 1fr;
            gap: 15px;
            margin-bottom: 15px;
            font-size: 14px;
            color: #555;
        }
        .info-item {
            padding: 8px;
            background: #f5f5f5;
            border-radius: 4px;
        }
        .quiz-actions {
            display: flex;
            gap: 10px;
        }
        .btn-edit, .btn-delete, .btn-view, .btn-publish {
            padding: 8px 12px;
            border: none;
            border-radius: 4px;
            cursor: pointer;
            font-size: 12px;
            text-decoration: none;
            display: inline-block;
        }
        .btn-edit {
            background-color: #3498db;
            color: white;
        }
        .btn-edit:hover {
            background-color: #2980b9;
        }
        .btn-view {
            background-color: #95a5a6;
            color: white;
        }
        .btn-view:hover {
            background-color: #7f8c8d;
        }
        .btn-publish {
            background-color: #27ae60;
            color: white;
        }
        .btn-publish:hover {
            background-color: #229954;
        }
        .btn-delete {
            background-color: #e74c3c;
            color: white;
        }
        .btn-delete:hover {
            background-color: #c0392b;
        }
        .status-badge {
            display: inline-block;
            padding: 4px 8px;
            border-radius: 4px;
            font-size: 12px;
            font-weight: bold;
        }
        .status-published {
            background-color: #d5f4e6;
            color: #27ae60;
        }
        .status-draft {
            background-color: #f0f0f0;
            color: #7f8c8d;
        }
        .no-quizzes {
            text-align: center;
            color: #7f8c8d;
            padding: 40px;
        }
    </style>
</head>
<body>
    <div class="quiz-management">
        <div class="header-section">
            <h1>Quiz Management</h1>
            <a href="quiz?action=create&courseId=${courseId}" class="btn-create">+ Create New Quiz</a>
        </div>
        
        <c:if test="${empty quizzes}">
            <div class="no-quizzes">
                <p>No quizzes created yet. <a href="quiz?action=create&courseId=${courseId}">Create one now</a></p>
            </div>
        </c:if>
        
        <c:forEach var="quiz" items="${quizzes}">
            <div class="quiz-card">
                <div class="quiz-title">
                    ${quiz.title}
                    <span class="status-badge ${quiz.published ? 'status-published' : 'status-draft'}">
                        ${quiz.published ? 'Published' : 'Draft'}
                    </span>
                </div>
                
                <div class="quiz-info">
                    <div class="info-item">
                        <strong>Duration:</strong> ${quiz.durationMinutes} mins
                    </div>
                    <div class="info-item">
                        <strong>Max Score:</strong> ${quiz.maxScore}
                    </div>
                    <div class="info-item">
                        <strong>Passing Score:</strong> ${quiz.passingScore}
                    </div>
                </div>
                
                <div class="quiz-actions">
                    <a href="quiz?action=edit&quizId=${quiz.quizId}" class="btn-edit">Edit Questions</a>
                    <a href="quiz?action=view&quizId=${quiz.quizId}" class="btn-view">View Attempts</a>
                    <c:if test="${!quiz.published}">
                        <form method="POST" action="quiz" style="display: inline;">
                            <input type="hidden" name="action" value="publish">
                            <input type="hidden" name="quizId" value="${quiz.quizId}">
                            <button type="submit" class="btn-publish">Publish</button>
                        </form>
                    </c:if>
                </div>
            </div>
        </c:forEach>
    </div>
</body>
</html>
