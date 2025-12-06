<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>

<!DOCTYPE html>
<html>
<head>
    <meta charset="UTF-8">
    <title>Take Quiz - ${quiz.title}</title>
    <link rel="stylesheet" href="css/style.css">
    <style>
        .quiz-attempt-container {
            max-width: 900px;
            margin: 20px auto;
            padding: 20px;
        }
        .quiz-header {
            background: #27ae60;
            color: white;
            padding: 20px;
            border-radius: 8px;
            margin-bottom: 20px;
        }
        .quiz-title {
            font-size: 24px;
            font-weight: bold;
            margin-bottom: 10px;
        }
        .quiz-info {
            display: flex;
            gap: 30px;
            font-size: 14px;
        }
        .question-form {
            background: white;
            border: 1px solid #e0e0e0;
            border-radius: 8px;
            padding: 20px;
            margin-bottom: 20px;
        }
        .question-block {
            background: #f9f9f9;
            border: 1px solid #ddd;
            border-radius: 8px;
            padding: 20px;
            margin-bottom: 20px;
        }
        .question-number {
            font-weight: bold;
            color: #27ae60;
            margin-bottom: 10px;
        }
        .question-text {
            font-weight: bold;
            margin-bottom: 15px;
            font-size: 16px;
        }
        .options {
            margin-left: 20px;
        }
        .option-item {
            margin-bottom: 10px;
            display: flex;
            align-items: center;
        }
        .option-item input[type="radio"],
        .option-item input[type="checkbox"] {
            margin-right: 10px;
            cursor: pointer;
        }
        .option-item label {
            cursor: pointer;
            flex: 1;
        }
        .option-item input[type="text"] {
            width: 100%;
            padding: 8px;
            border: 1px solid #ddd;
            border-radius: 4px;
        }
        .quiz-actions {
            display: flex;
            gap: 10px;
            justify-content: center;
        }
        .btn-submit {
            background-color: #27ae60;
            color: white;
            padding: 12px 30px;
            border: none;
            border-radius: 5px;
            cursor: pointer;
            font-size: 16px;
        }
        .btn-submit:hover {
            background-color: #229954;
        }
        .btn-cancel {
            background-color: #e74c3c;
            color: white;
            padding: 12px 30px;
            border: none;
            border-radius: 5px;
            cursor: pointer;
            font-size: 16px;
        }
        .btn-cancel:hover {
            background-color: #c0392b;
        }
        .timer {
            font-size: 18px;
            font-weight: bold;
            color: white;
        }
        .progress-bar {
            background-color: rgba(255,255,255,0.3);
            border-radius: 4px;
            height: 6px;
            margin-top: 10px;
        }
        .progress-fill {
            background-color: white;
            height: 100%;
            border-radius: 4px;
            transition: width 0.3s;
        }
    </style>
</head>
<body>
    <div class="quiz-attempt-container">
        <div class="quiz-header">
            <div class="quiz-title">${quiz.title}</div>
            <div class="quiz-info">
                <div>⏱️ Duration: ${quiz.durationMinutes} minutes</div>
                <div>🎯 Max Score: ${quiz.maxScore}</div>
                <div class="timer">Time Remaining: <span id="timeLeft">${quiz.durationMinutes}:00</span></div>
            </div>
            <div class="progress-bar">
                <div class="progress-fill" id="progressFill" style="width: 0%"></div>
            </div>
        </div>
        
        <form method="POST" action="quiz" id="quizForm">
            <input type="hidden" name="action" value="submit">
            <input type="hidden" name="attemptId" value="${attemptId}">
            
            <c:forEach var="question" items="${questions}" varStatus="status">
                <div class="question-block">
                    <div class="question-number">Question ${status.count} of ${questions.size()} (${question.points} points)</div>
                    <div class="question-text">${question.questionText}</div>
                    
                    <div class="options">
                        <c:choose>
                            <c:when test="${question.questionType == 'multiple_choice'}">
                                <div class="option-item">
                                    <input type="radio" name="answer_${question.questionId}" value="A" id="q${question.questionId}a" required>
                                    <label for="q${question.questionId}a"><strong>A)</strong> ${question.optionA}</label>
                                </div>
                                <div class="option-item">
                                    <input type="radio" name="answer_${question.questionId}" value="B" id="q${question.questionId}b" required>
                                    <label for="q${question.questionId}b"><strong>B)</strong> ${question.optionB}</label>
                                </div>
                                <c:if test="${not empty question.optionC}">
                                    <div class="option-item">
                                        <input type="radio" name="answer_${question.questionId}" value="C" id="q${question.questionId}c" required>
                                        <label for="q${question.questionId}c"><strong>C)</strong> ${question.optionC}</label>
                                    </div>
                                </c:if>
                                <c:if test="${not empty question.optionD}">
                                    <div class="option-item">
                                        <input type="radio" name="answer_${question.questionId}" value="D" id="q${question.questionId}d" required>
                                        <label for="q${question.questionId}d"><strong>D)</strong> ${question.optionD}</label>
                                    </div>
                                </c:if>
                            </c:when>
                            <c:when test="${question.questionType == 'true_false'}">
                                <div class="option-item">
                                    <input type="radio" name="answer_${question.questionId}" value="A" id="q${question.questionId}true" required>
                                    <label for="q${question.questionId}true"><strong>True</strong></label>
                                </div>
                                <div class="option-item">
                                    <input type="radio" name="answer_${question.questionId}" value="B" id="q${question.questionId}false" required>
                                    <label for="q${question.questionId}false"><strong>False</strong></label>
                                </div>
                            </c:when>
                            <c:when test="${question.questionType == 'short_answer'}">
                                <div class="option-item">
                                    <input type="text" name="answer_${question.questionId}" placeholder="Your answer" required>
                                </div>
                            </c:when>
                        </c:choose>
                    </div>
                </div>
            </c:forEach>
            
            <div class="quiz-actions">
                <button type="submit" class="btn-submit" onclick="return confirm('Are you sure you want to submit your quiz? You cannot change your answers after submission.');">Submit Quiz</button>
                <button type="button" class="btn-cancel" onclick="if(confirm('Are you sure you want to exit without submitting?')) window.history.back();">Cancel</button>
            </div>
        </form>
    </div>
    
    <script>
        // Timer functionality
        let totalSeconds = ${quiz.durationMinutes} * 60;
        let currentSeconds = totalSeconds;
        
        function updateTimer() {
            const minutes = Math.floor(currentSeconds / 60);
            const seconds = currentSeconds % 60;
            document.getElementById('timeLeft').textContent = 
                (minutes < 10 ? '0' : '') + minutes + ':' + (seconds < 10 ? '0' : '') + seconds;
            
            // Update progress bar
            const progressPercent = ((totalSeconds - currentSeconds) / totalSeconds) * 100;
            document.getElementById('progressFill').style.width = progressPercent + '%';
            
            if (currentSeconds > 0) {
                currentSeconds--;
                setTimeout(updateTimer, 1000);
            } else {
                alert('Time is up! Your quiz will be submitted automatically.');
                document.getElementById('quizForm').submit();
            }
        }
        
        // Start timer on page load
        updateTimer();
    </script>
</body>
</html>
