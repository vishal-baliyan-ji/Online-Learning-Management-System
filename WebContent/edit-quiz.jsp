<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>

<!DOCTYPE html>
<html>
<head>
    <meta charset="UTF-8">
    <title>Edit Quiz</title>
    <link rel="stylesheet" href="css/style.css">
    <style>
        .edit-quiz-container {
            max-width: 1000px;
            margin: 30px auto;
            padding: 20px;
        }
        .quiz-form {
            background: white;
            border: 1px solid #e0e0e0;
            border-radius: 8px;
            padding: 20px;
            margin-bottom: 30px;
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
        }
        .form-group textarea {
            resize: vertical;
            min-height: 80px;
        }
        .form-row {
            display: grid;
            grid-template-columns: 1fr 1fr 1fr;
            gap: 15px;
        }
        .btn-update {
            background-color: #27ae60;
            color: white;
            padding: 10px 20px;
            border: none;
            border-radius: 5px;
            cursor: pointer;
            font-size: 14px;
        }
        .btn-update:hover {
            background-color: #229954;
        }
        .questions-section {
            background: white;
            border: 1px solid #e0e0e0;
            border-radius: 8px;
            padding: 20px;
            margin-bottom: 30px;
        }
        .add-question-form {
            background: #f9f9f9;
            border: 1px solid #ddd;
            border-radius: 8px;
            padding: 20px;
            margin-bottom: 20px;
        }
        .question-list {
            margin-top: 20px;
        }
        .question-item {
            background: #f5f5f5;
            border: 1px solid #ddd;
            border-radius: 4px;
            padding: 15px;
            margin-bottom: 10px;
        }
        .question-number {
            font-weight: bold;
            color: #27ae60;
            margin-bottom: 10px;
        }
        .question-text {
            margin-bottom: 10px;
        }
        .question-options {
            display: grid;
            grid-template-columns: 1fr 1fr;
            gap: 10px;
            font-size: 13px;
            margin-bottom: 10px;
        }
        .btn-delete-question {
            background-color: #e74c3c;
            color: white;
            padding: 5px 10px;
            border: none;
            border-radius: 4px;
            cursor: pointer;
            font-size: 12px;
        }
        .btn-delete-question:hover {
            background-color: #c0392b;
        }
        .btn-submit-question {
            background-color: #3498db;
            color: white;
            padding: 10px 20px;
            border: none;
            border-radius: 5px;
            cursor: pointer;
        }
        .btn-submit-question:hover {
            background-color: #2980b9;
        }
        .error {
            color: #e74c3c;
            margin-bottom: 20px;
            padding: 10px;
            background-color: #fadbd8;
            border-radius: 4px;
        }
        .success {
            color: #27ae60;
            margin-bottom: 20px;
            padding: 10px;
            background-color: #d5f4e6;
            border-radius: 4px;
        }
    </style>
</head>
<body>
    <div class="edit-quiz-container">
        <h1>Edit Quiz</h1>
        
        <c:if test="${not empty error}">
            <div class="error">${error}</div>
        </c:if>
        <c:if test="${not empty success}">
            <div class="success">${success}</div>
        </c:if>
        
        <div class="quiz-form">
            <h2>Quiz Settings</h2>
            <form method="POST" action="quiz">
                <input type="hidden" name="action" value="update">
                <input type="hidden" name="quizId" value="${quiz.quizId}">
                
                <div class="form-group">
                    <label>Quiz Title</label>
                    <input type="text" name="title" value="${quiz.title}" required>
                </div>
                
                <div class="form-group">
                    <label>Description</label>
                    <textarea name="description" required>${quiz.description}</textarea>
                </div>
                
                <div class="form-row">
                    <div class="form-group">
                        <label>Duration (minutes)</label>
                        <input type="number" name="durationMinutes" value="${quiz.durationMinutes}" min="1" required>
                    </div>
                    <div class="form-group">
                        <label>Max Score</label>
                        <input type="number" name="maxScore" value="${quiz.maxScore}" min="1" required>
                    </div>
                    <div class="form-group">
                        <label>Passing Score</label>
                        <input type="number" name="passingScore" value="${quiz.passingScore}" min="0" required>
                    </div>
                </div>
                
                <button type="submit" class="btn-update">Update Quiz Settings</button>
            </form>
        </div>
        
        <div class="questions-section">
            <h2>Questions</h2>
            
            <div class="add-question-form">
                <h3>Add New Question</h3>
                <form method="POST" action="quiz">
                    <input type="hidden" name="action" value="addQuestion">
                    <input type="hidden" name="quizId" value="${quiz.quizId}">
                    
                    <div class="form-group">
                        <label>Question Type</label>
                        <select name="questionType" required style="width: 100%; padding: 10px; border: 1px solid #ddd; border-radius: 4px;">
                            <option value="multiple_choice">Multiple Choice</option>
                            <option value="true_false">True/False</option>
                            <option value="short_answer">Short Answer</option>
                        </select>
                    </div>
                    
                    <div class="form-group">
                        <label>Question Text</label>
                        <textarea name="questionText" required></textarea>
                    </div>
                    
                    <div class="form-group">
                        <label>Option A</label>
                        <input type="text" name="optionA" required>
                    </div>
                    
                    <div class="form-group">
                        <label>Option B</label>
                        <input type="text" name="optionB" required>
                    </div>
                    
                    <div class="form-group">
                        <label>Option C</label>
                        <input type="text" name="optionC">
                    </div>
                    
                    <div class="form-group">
                        <label>Option D</label>
                        <input type="text" name="optionD">
                    </div>
                    
                    <div class="form-row">
                        <div class="form-group">
                            <label>Correct Answer (A, B, C, or D)</label>
                            <input type="text" name="correctAnswer" maxlength="1" required>
                        </div>
                        <div class="form-group">
                            <label>Points</label>
                            <input type="number" name="points" value="1" min="1" required>
                        </div>
                    </div>
                    
                    <button type="submit" class="btn-submit-question">Add Question</button>
                </form>
            </div>
            
            <div class="question-list">
                <h3>Current Questions (${questions.size()})</h3>
                <c:forEach var="question" items="${questions}" varStatus="status">
                    <div class="question-item">
                        <div class="question-number">Question ${status.count}</div>
                        <div class="question-text"><strong>${question.questionText}</strong></div>
                        <div class="question-options">
                            <div><strong>A:</strong> ${question.optionA}</div>
                            <div><strong>B:</strong> ${question.optionB}</div>
                            <c:if test="${not empty question.optionC}">
                                <div><strong>C:</strong> ${question.optionC}</div>
                            </c:if>
                            <c:if test="${not empty question.optionD}">
                                <div><strong>D:</strong> ${question.optionD}</div>
                            </c:if>
                        </div>
                        <div>
                            <strong>Correct Answer:</strong> ${question.correctAnswer} | 
                            <strong>Points:</strong> ${question.points}
                        </div>
                    </div>
                </c:forEach>
            </div>
        </div>
        
        <a href="quiz?action=list&courseId=${quiz.courseId}" style="background-color: #95a5a6; color: white; padding: 10px 20px; border-radius: 5px; text-decoration: none; display: inline-block;">Back to Quiz List</a>
    </div>
</body>
</html>
