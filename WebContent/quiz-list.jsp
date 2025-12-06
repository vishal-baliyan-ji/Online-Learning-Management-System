<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>

<!DOCTYPE html>
<html>
<head>
    <meta charset="UTF-8">
    <title>Available Quizzes</title>
    <link rel="stylesheet" href="css/style.css">
    <style>
        .quiz-container {
            max-width: 900px;
            margin: 30px auto;
            padding: 20px;
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
            font-size: 20px;
            font-weight: bold;
            color: #27ae60;
            margin-bottom: 10px;
        }
        .quiz-description {
            color: #666;
            margin-bottom: 15px;
            line-height: 1.6;
        }
        .quiz-meta {
            display: flex;
            gap: 20px;
            margin-bottom: 15px;
            font-size: 14px;
            color: #555;
        }
        .meta-item {
            display: flex;
            align-items: center;
            gap: 5px;
        }
        .quiz-actions {
            display: flex;
            gap: 10px;
        }
        .btn-primary {
            background-color: #27ae60;
            color: white;
            padding: 10px 20px;
            border: none;
            border-radius: 5px;
            cursor: pointer;
            font-size: 14px;
        }
        .btn-primary:hover {
            background-color: #229954;
        }
        .btn-secondary {
            background-color: #95a5a6;
            color: white;
            padding: 10px 20px;
            border: none;
            border-radius: 5px;
            cursor: pointer;
            font-size: 14px;
        }
        .btn-secondary:hover {
            background-color: #7f8c8d;
        }
        .status-badge {
            display: inline-block;
            padding: 5px 10px;
            border-radius: 4px;
            font-size: 12px;
            font-weight: bold;
        }
        .status-published {
            background-color: #d5f4e6;
            color: #27ae60;
        }
        .status-attempted {
            background-color: #ffeaa7;
            color: #f39c12;
        }
        .no-quizzes {
            text-align: center;
            color: #7f8c8d;
            padding: 40px;
        }
    </style>
</head>
<body>
    <div class="quiz-container">
        <h1>Available Quizzes</h1>
        
        <c:if test="${empty quizzes}">
            <div class="no-quizzes">
                <p>No quizzes available in this course yet.</p>
            </div>
        </c:if>
        
        <c:forEach var="quiz" items="${quizzes}">
            <c:if test="${quiz.published}">
                <div class="quiz-card">
                    <div class="quiz-title">${quiz.title}</div>
                    <div class="quiz-description">${quiz.description}</div>
                    
                    <div class="quiz-meta">
                        <div class="meta-item">
                            <span>⏱️</span>
                            <span>Duration: ${quiz.durationMinutes} minutes</span>
                        </div>
                        <div class="meta-item">
                            <span>🎯</span>
                            <span>Max Score: ${quiz.maxScore}</span>
                        </div>
                        <div class="meta-item">
                            <span>✓</span>
                            <span>Passing Score: ${quiz.passingScore}</span>
                        </div>
                    </div>
                    
                    <div class="quiz-actions">
                        <a href="quiz?action=start&quizId=${quiz.quizId}" class="btn-primary">Start Quiz</a>
                    </div>
                </div>
            </c:if>
        </c:forEach>
    </div>
    
    <script>
        function startQuiz(quizId) {
            if (confirm('Are you sure you want to start this quiz?')) {
                window.location.href = 'quiz?action=start&quizId=' + quizId;
            }
        }
    </script>
</body>
</html>
