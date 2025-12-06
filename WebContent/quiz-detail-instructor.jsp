<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>

<!DOCTYPE html>
<html>
<head>
    <meta charset="UTF-8">
    <title>Quiz Attempts - ${quiz.title}</title>
    <link rel="stylesheet" href="css/style.css">
    <style>
        .attempts-container {
            max-width: 1000px;
            margin: 30px auto;
            padding: 20px;
        }
        .quiz-header {
            background: #27ae60;
            color: white;
            padding: 20px;
            border-radius: 8px;
            margin-bottom: 30px;
        }
        .quiz-title {
            font-size: 24px;
            font-weight: bold;
            margin-bottom: 10px;
        }
        .attempts-table {
            width: 100%;
            border-collapse: collapse;
            background: white;
            box-shadow: 0 2px 4px rgba(0,0,0,0.1);
            border-radius: 8px;
            overflow: hidden;
        }
        .attempts-table thead {
            background: #f5f5f5;
            border-bottom: 2px solid #27ae60;
        }
        .attempts-table th {
            padding: 15px;
            text-align: left;
            font-weight: bold;
            color: #333;
        }
        .attempts-table td {
            padding: 15px;
            border-bottom: 1px solid #ddd;
        }
        .attempts-table tbody tr:hover {
            background: #f9f9f9;
        }
        .student-name {
            font-weight: bold;
            color: #27ae60;
        }
        .status-badge {
            display: inline-block;
            padding: 5px 10px;
            border-radius: 4px;
            font-size: 12px;
            font-weight: bold;
        }
        .status-graded {
            background-color: #d5f4e6;
            color: #27ae60;
        }
        .status-submitted {
            background-color: #ffeaa7;
            color: #f39c12;
        }
        .status-inprogress {
            background-color: #dfe6e9;
            color: #2d3436;
        }
        .score-display {
            font-weight: bold;
            font-size: 16px;
            color: #27ae60;
        }
        .btn-grade {
            background-color: #3498db;
            color: white;
            padding: 8px 15px;
            border: none;
            border-radius: 4px;
            cursor: pointer;
            text-decoration: none;
            display: inline-block;
            font-size: 12px;
        }
        .btn-grade:hover {
            background-color: #2980b9;
        }
        .btn-view {
            background-color: #95a5a6;
            color: white;
            padding: 8px 15px;
            border: none;
            border-radius: 4px;
            cursor: pointer;
            text-decoration: none;
            display: inline-block;
            font-size: 12px;
            margin-right: 5px;
        }
        .btn-view:hover {
            background-color: #7f8c8d;
        }
        .no-attempts {
            text-align: center;
            color: #7f8c8d;
            padding: 40px;
            background: white;
            border-radius: 8px;
        }
        .stats-section {
            display: grid;
            grid-template-columns: 1fr 1fr 1fr 1fr;
            gap: 15px;
            margin-bottom: 30px;
        }
        .stat-card {
            background: white;
            border: 1px solid #e0e0e0;
            border-radius: 8px;
            padding: 20px;
            text-align: center;
        }
        .stat-value {
            font-size: 32px;
            font-weight: bold;
            color: #27ae60;
            margin-bottom: 5px;
        }
        .stat-label {
            color: #7f8c8d;
            font-size: 14px;
        }
    </style>
</head>
<body>
    <div class="attempts-container">
        <div class="quiz-header">
            <div class="quiz-title">${quiz.title}</div>
            <div>Instructor Dashboard - Student Attempts</div>
        </div>
        
        <c:if test="${not empty quiz}">
            <div class="stats-section">
                <div class="stat-card">
                    <div class="stat-value">-</div>
                    <div class="stat-label">Total Attempts</div>
                </div>
                <div class="stat-card">
                    <div class="stat-value">-</div>
                    <div class="stat-label">Average Score</div>
                </div>
                <div class="stat-card">
                    <div class="stat-value">${quiz.maxScore}</div>
                    <div class="stat-label">Max Score</div>
                </div>
                <div class="stat-card">
                    <div class="stat-value">${quiz.passingScore}</div>
                    <div class="stat-label">Passing Score</div>
                </div>
            </div>
        </c:if>
        
        <table class="attempts-table">
            <thead>
                <tr>
                    <th>Student Name</th>
                    <th>Email</th>
                    <th>Started</th>
                    <th>Score</th>
                    <th>Status</th>
                    <th>Actions</th>
                </tr>
            </thead>
            <tbody>
                <c:if test="${empty attempts}">
                    <tr>
                        <td colspan="6">
                            <div class="no-attempts">
                                <p>No attempts yet. Students can take this quiz once it's published.</p>
                            </div>
                        </td>
                    </tr>
                </c:if>
                
                <c:forEach var="attempt" items="${attempts}">
                    <tr>
                        <td><span class="student-name">${attempt.studentId}</span></td>
                        <td>-</td>
                        <td>
                            <fmt:formatDate value="${attempt.startedAt}" pattern="MMM dd, yyyy HH:mm"/>
                        </td>
                        <td>
                            <c:choose>
                                <c:when test="${attempt.score > 0 || attempt.status == 'graded' || attempt.status == 'submitted'}">
                                    <span class="score-display">${attempt.score}/${quiz.maxScore}</span>
                                </c:when>
                                <c:otherwise>
                                    <span>-</span>
                                </c:otherwise>
                            </c:choose>
                        </td>
                        <td>
                            <span class="status-badge status-${attempt.status}">
                                <c:choose>
                                    <c:when test="${attempt.status == 'in_progress'}">In Progress</c:when>
                                    <c:when test="${attempt.status == 'submitted'}">Submitted</c:when>
                                    <c:when test="${attempt.status == 'graded'}">Graded</c:when>
                                    <c:otherwise>${attempt.status}</c:otherwise>
                                </c:choose>
                            </span>
                        </td>
                        <td>
                            <a href="quiz?action=results&attemptId=${attempt.attemptId}" class="btn-view">View</a>
                            <c:if test="${attempt.status == 'submitted' || attempt.status == 'graded'}">
                                <a href="grade-attempt.jsp?attemptId=${attempt.attemptId}" class="btn-grade">Grade</a>
                            </c:if>
                        </td>
                    </tr>
                </c:forEach>
            </tbody>
        </table>
        
        <br><br>
        <a href="quiz?action=edit&quizId=${quiz.quizId}" style="background-color: #95a5a6; color: white; padding: 10px 20px; border-radius: 5px; text-decoration: none; display: inline-block;">← Back to Quiz Editor</a>
    </div>
</body>
</html>
