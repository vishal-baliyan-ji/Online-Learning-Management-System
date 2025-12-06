<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>

<!DOCTYPE html>
<html>
<head>
    <meta charset="UTF-8">
    <title>Quiz Results</title>
    <link rel="stylesheet" href="css/style.css">
    <style>
        .results-container {
            max-width: 900px;
            margin: 30px auto;
            padding: 20px;
        }
        .score-section {
            background: white;
            border: 1px solid #e0e0e0;
            border-radius: 8px;
            padding: 30px;
            text-align: center;
            margin-bottom: 30px;
        }
        .score-display {
            font-size: 48px;
            font-weight: bold;
            color: #27ae60;
            margin-bottom: 10px;
        }
        .score-text {
            font-size: 18px;
            color: #555;
            margin-bottom: 20px;
        }
        .status-badge {
            display: inline-block;
            padding: 10px 20px;
            border-radius: 5px;
            font-size: 16px;
            font-weight: bold;
            margin-bottom: 20px;
        }
        .status-passed {
            background-color: #d5f4e6;
            color: #27ae60;
        }
        .status-failed {
            background-color: #fadbd8;
            color: #e74c3c;
        }
        .quiz-info {
            display: grid;
            grid-template-columns: 1fr 1fr 1fr;
            gap: 15px;
            margin-top: 20px;
        }
        .info-box {
            background: #f5f5f5;
            padding: 15px;
            border-radius: 4px;
            text-align: center;
        }
        .info-label {
            color: #7f8c8d;
            font-size: 12px;
            margin-bottom: 5px;
        }
        .info-value {
            font-weight: bold;
            font-size: 18px;
            color: #333;
        }
        .details-section {
            background: white;
            border: 1px solid #e0e0e0;
            border-radius: 8px;
            padding: 20px;
            margin-bottom: 20px;
        }
        .question-review {
            background: #f9f9f9;
            border: 1px solid #ddd;
            border-radius: 4px;
            padding: 15px;
            margin-bottom: 15px;
        }
        .question-review.correct {
            border-left: 4px solid #27ae60;
        }
        .question-review.incorrect {
            border-left: 4px solid #e74c3c;
        }
        .review-question {
            font-weight: bold;
            margin-bottom: 10px;
        }
        .review-answer {
            margin: 5px 0;
            font-size: 14px;
        }
        .correct-answer {
            color: #27ae60;
        }
        .incorrect-answer {
            color: #e74c3c;
        }
        .btn-back {
            background-color: #3498db;
            color: white;
            padding: 10px 20px;
            border: none;
            border-radius: 5px;
            cursor: pointer;
            text-decoration: none;
            display: inline-block;
        }
        .btn-back:hover {
            background-color: #2980b9;
        }
        .feedback-box {
            background: #fffbea;
            border: 1px solid #f0e5c7;
            border-radius: 4px;
            padding: 15px;
            margin-top: 10px;
            color: #7f6a2f;
        }
    </style>
</head>
<body>
    <div class="results-container">
        <div class="score-section">
            <div class="score-display">${attempt.score}/${quiz.maxScore}</div>
            <div class="score-text">Your Score</div>
            
            <c:choose>
                <c:when test="${attempt.score >= quiz.passingScore}">
                    <div class="status-badge status-passed">✓ PASSED</div>
                </c:when>
                <c:otherwise>
                    <div class="status-badge status-failed">✗ FAILED</div>
                </c:otherwise>
            </c:choose>
            
            <div class="quiz-info">
                <div class="info-box">
                    <div class="info-label">Passing Score</div>
                    <div class="info-value">${quiz.passingScore}</div>
                </div>
                <div class="info-box">
                    <div class="info-label">Total Questions</div>
                    <div class="info-value">${questions.size()}</div>
                </div>
                <div class="info-box">
                    <div class="info-label">Percentage</div>
                    <div class="info-value">${Math.round((attempt.score / quiz.maxScore) * 100)}%</div>
                </div>
            </div>
        </div>
        
        <c:if test="${not empty attempt.feedback}">
            <div class="details-section">
                <h2>Instructor Feedback</h2>
                <div class="feedback-box">
                    ${attempt.feedback}
                </div>
            </div>
        </c:if>
        
        <div class="details-section">
            <h2>Question Review</h2>
            <c:forEach var="question" items="${questions}" varStatus="status">
                <c:set var="studentAnswerParam" value="answer_${question.questionId}"/>
                <c:set var="isCorrect" value="${param[studentAnswerParam] == question.correctAnswer}"/>
                
                <div class="question-review ${isCorrect ? 'correct' : 'incorrect'}">
                    <div class="review-question">
                        Question ${status.count}: ${question.questionText} (${question.points} points)
                    </div>
                    
                    <div class="review-answer">
                        <strong>Your Answer:</strong> 
                        <c:choose>
                            <c:when test="${isCorrect}">
                                <span class="correct-answer">✓ ${question.correctAnswer}</span>
                            </c:when>
                            <c:otherwise>
                                <span class="incorrect-answer">✗ ${param[studentAnswerParam] != null ? param[studentAnswerParam] : 'Not answered'}</span>
                            </c:otherwise>
                        </c:choose>
                    </div>
                    
                    <c:if test="${!isCorrect}">
                        <div class="review-answer">
                            <strong>Correct Answer:</strong> 
                            <span class="correct-answer">${question.correctAnswer}</span>
                        </div>
                    </c:if>
                </div>
            </c:forEach>
        </div>
        
        <a href="quiz?action=list&courseId=${quiz.courseId}" class="btn-back">← Back to Quizzes</a>
    </div>
</body>
</html>
