<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>

<!DOCTYPE html>
<html>
<head>
    <meta charset="UTF-8">
    <title>${quiz.title}</title>
    <link rel="stylesheet" href="css/style.css">
    <style>
        .quiz-detail-container {
            max-width: 800px;
            margin: 30px auto;
            padding: 20px;
        }
        .quiz-card {
            background: white;
            border: 1px solid #e0e0e0;
            border-radius: 8px;
            padding: 30px;
            box-shadow: 0 2px 4px rgba(0,0,0,0.1);
        }
        .quiz-title {
            font-size: 28px;
            font-weight: bold;
            color: #27ae60;
            margin-bottom: 15px;
        }
        .quiz-description {
            color: #666;
            line-height: 1.6;
            margin-bottom: 20px;
            padding: 15px;
            background: #f5f5f5;
            border-left: 4px solid #27ae60;
            border-radius: 4px;
        }
        .quiz-info {
            display: grid;
            grid-template-columns: 1fr 1fr;
            gap: 20px;
            margin-bottom: 30px;
        }
        .info-box {
            background: #f9f9f9;
            padding: 15px;
            border-radius: 4px;
            border: 1px solid #e0e0e0;
        }
        .info-label {
            color: #7f8c8d;
            font-size: 12px;
            text-transform: uppercase;
            margin-bottom: 5px;
        }
        .info-value {
            font-size: 18px;
            font-weight: bold;
            color: #333;
        }
        .btn-start {
            background-color: #27ae60;
            color: white;
            padding: 15px 40px;
            border: none;
            border-radius: 5px;
            cursor: pointer;
            font-size: 16px;
            font-weight: bold;
            width: 100%;
            margin-bottom: 10px;
        }
        .btn-start:hover {
            background-color: #229954;
        }
        .btn-back {
            background-color: #95a5a6;
            color: white;
            padding: 12px 20px;
            border: none;
            border-radius: 5px;
            cursor: pointer;
            text-decoration: none;
            display: inline-block;
            width: 100%;
            text-align: center;
            box-sizing: border-box;
        }
        .btn-back:hover {
            background-color: #7f8c8d;
        }
        .warning {
            background: #fff3cd;
            border: 1px solid #ffc107;
            border-radius: 4px;
            padding: 15px;
            margin-bottom: 20px;
            color: #856404;
        }
        .instructions {
            background: #e7f3ff;
            border: 1px solid #3498db;
            border-radius: 4px;
            padding: 15px;
            margin-bottom: 20px;
            color: #0c5394;
        }
    </style>
</head>
<body>
    <div class="quiz-detail-container">
        <div class="quiz-card">
            <div class="quiz-title">${quiz.title}</div>
            
            <div class="instructions">
                <strong>📋 Instructions:</strong>
                <ul style="margin-bottom: 0;">
                    <li>You will have <strong>${quiz.durationMinutes} minutes</strong> to complete this quiz</li>
                    <li>Once you start, the timer will begin automatically</li>
                    <li>You must answer all questions before submitting</li>
                    <li>Your score must be at least <strong>${quiz.passingScore}</strong> to pass</li>
                    <li>After submission, you cannot change your answers</li>
                </ul>
            </div>
            
            <div class="quiz-description">
                ${quiz.description}
            </div>
            
            <div class="quiz-info">
                <div class="info-box">
                    <div class="info-label">⏱️ Duration</div>
                    <div class="info-value">${quiz.durationMinutes} minutes</div>
                </div>
                <div class="info-box">
                    <div class="info-label">🎯 Max Score</div>
                    <div class="info-value">${quiz.maxScore} points</div>
                </div>
                <div class="info-box">
                    <div class="info-label">✓ Passing Score</div>
                    <div class="info-value">${quiz.passingScore} points</div>
                </div>
                <div class="info-box">
                    <div class="info-label">📊 Pass Percentage</div>
                    <div class="info-value">${Math.round((quiz.passingScore / quiz.maxScore) * 100)}%</div>
                </div>
            </div>
            
            <form method="POST" action="quiz" id="startForm">
                <input type="hidden" name="action" value="start">
                <input type="hidden" name="quizId" value="${quiz.quizId}">
                <button type="submit" class="btn-start" onclick="return confirm('Ready to start the quiz? Make sure you have enough time to complete it.');">
                    🚀 Start Quiz
                </button>
            </form>
            
            <a href="quiz?action=list&courseId=${quiz.courseId}" class="btn-back">← Back to Quizzes</a>
        </div>
    </div>
</body>
</html>
