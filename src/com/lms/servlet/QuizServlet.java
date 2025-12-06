package com.lms.servlet;

import com.lms.dao.QuizDAO;
import com.lms.dao.QuizQuestionDAO;
import com.lms.dao.QuizAttemptDAO;
import com.lms.model.Quiz;
import com.lms.model.QuizQuestion;
import com.lms.model.QuizAttempt;
import com.lms.model.User;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import java.io.IOException;
import java.util.List;

@WebServlet("/quiz")
public class QuizServlet extends HttpServlet {
    private static final long serialVersionUID = 1L;
    
    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) 
            throws ServletException, IOException {
        HttpSession session = request.getSession(false);
        if (session == null || session.getAttribute("user") == null) {
            response.sendRedirect("login.jsp");
            return;
        }
        
        User user = (User) session.getAttribute("user");
        String action = request.getParameter("action");
        
        if (action == null) {
            action = "list";
        }
        
        switch (action) {
            case "list":
                handleListQuizzes(request, response, user);
                break;
            case "view":
                handleViewQuiz(request, response, user);
                break;
            case "create":
                if (isInstructor(user)) {
                    request.getRequestDispatcher("create-quiz.jsp").forward(request, response);
                } else {
                    response.sendRedirect("login.jsp");
                }
                break;
            case "edit":
                if (isInstructor(user)) {
                    handleEditQuiz(request, response, user);
                } else {
                    response.sendRedirect("login.jsp");
                }
                break;
            case "start":
                handleStartQuiz(request, response, user);
                break;
            case "attempt":
                handleQuizAttempt(request, response, user);
                break;
            case "grade":
                if (isInstructor(user)) {
                    handleGradeAttempt(request, response, user);
                } else {
                    response.sendRedirect("login.jsp");
                }
                break;
            case "results":
                handleViewResults(request, response, user);
                break;
            default:
                handleListQuizzes(request, response, user);
        }
    }
    
    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) 
            throws ServletException, IOException {
        HttpSession session = request.getSession(false);
        if (session == null || session.getAttribute("user") == null) {
            response.sendRedirect("login.jsp");
            return;
        }
        
        User user = (User) session.getAttribute("user");
        String action = request.getParameter("action");
        
        if (action == null) {
            action = "create";
        }
        
        switch (action) {
            case "create":
                if (isInstructor(user)) {
                    handleCreateQuiz(request, response, user);
                } else {
                    response.sendRedirect("login.jsp");
                }
                break;
            case "update":
                if (isInstructor(user)) {
                    handleUpdateQuiz(request, response, user);
                } else {
                    response.sendRedirect("login.jsp");
                }
                break;
            case "addQuestion":
                if (isInstructor(user)) {
                    handleAddQuestion(request, response, user);
                } else {
                    response.sendRedirect("login.jsp");
                }
                break;
            case "submit":
                handleSubmitAttempt(request, response, user);
                break;
            case "publish":
                if (isInstructor(user)) {
                    handlePublishQuiz(request, response, user);
                } else {
                    response.sendRedirect("login.jsp");
                }
                break;
            default:
                response.sendRedirect("quiz?action=list");
        }
    }
    
    private void handleListQuizzes(HttpServletRequest request, HttpServletResponse response, User user) 
            throws ServletException, IOException {
        String courseIdParam = request.getParameter("courseId");
        
        // If courseId is not provided or invalid, show all courses
        if (courseIdParam == null || courseIdParam.trim().isEmpty()) {
            response.sendRedirect("course-list.jsp");
            return;
        }
        
        try {
            int courseId = Integer.parseInt(courseIdParam.trim());
            
            if (courseId > 0) {
                List<Quiz> quizzes = QuizDAO.getQuizzesByCourse(courseId);
                request.setAttribute("quizzes", quizzes);
                request.setAttribute("courseId", courseId);
                
                if (isInstructor(user)) {
                    request.getRequestDispatcher("quiz-list-instructor.jsp").forward(request, response);
                } else {
                    request.getRequestDispatcher("quiz-list.jsp").forward(request, response);
                }
            } else {
                response.sendRedirect("course-list.jsp");
            }
        } catch (NumberFormatException e) {
            response.sendRedirect("course-list.jsp");
        }
    }
    
    private void handleViewQuiz(HttpServletRequest request, HttpServletResponse response, User user) 
            throws ServletException, IOException {
        int quizId = Integer.parseInt(request.getParameter("quizId") != null ? 
                                     request.getParameter("quizId") : "0");
        
        if (quizId <= 0) {
            response.sendRedirect("quiz?action=list");
            return;
        }
        
        Quiz quiz = QuizDAO.getQuizById(quizId);
        if (quiz == null) {
            response.sendRedirect("quiz?action=list");
            return;
        }
        
        if (isInstructor(user)) {
            request.setAttribute("quiz", quiz);
            request.getRequestDispatcher("quiz-detail-instructor.jsp").forward(request, response);
        } else {
            request.setAttribute("quiz", quiz);
            request.getRequestDispatcher("quiz-detail.jsp").forward(request, response);
        }
    }
    
    private void handleCreateQuiz(HttpServletRequest request, HttpServletResponse response, User user) 
            throws ServletException, IOException {
        try {
            String courseIdParam = request.getParameter("courseId");
            int courseId = (courseIdParam != null && !courseIdParam.trim().isEmpty()) ? 
                          Integer.parseInt(courseIdParam) : 0;
            
            // Validate courseId
            if (courseId <= 0) {
                request.setAttribute("error", "Invalid Course. Please select a valid course.");
                request.getRequestDispatcher("create-quiz.jsp").forward(request, response);
                return;
            }
            
            String title = request.getParameter("title");
            String description = request.getParameter("description");
            
            // Validate title
            if (title == null || title.trim().isEmpty()) {
                request.setAttribute("error", "Quiz title is required.");
                request.setAttribute("courseId", courseId);
                request.getRequestDispatcher("create-quiz.jsp").forward(request, response);
                return;
            }
            
            String durationParam = request.getParameter("durationMinutes");
            int durationMinutes = (durationParam != null && !durationParam.trim().isEmpty()) ? 
                                 Integer.parseInt(durationParam) : 30;
            
            String maxScoreParam = request.getParameter("maxScore");
            int maxScore = (maxScoreParam != null && !maxScoreParam.trim().isEmpty()) ? 
                          Integer.parseInt(maxScoreParam) : 100;
            
            String passingScoreParam = request.getParameter("passingScore");
            int passingScore = (passingScoreParam != null && !passingScoreParam.trim().isEmpty()) ? 
                              Integer.parseInt(passingScoreParam) : 60;
            
            Quiz quiz = new Quiz(courseId, title, description, durationMinutes, maxScore, passingScore, false);
            int quizId = QuizDAO.createQuiz(quiz);
            
            if (quizId > 0) {
                response.sendRedirect("quiz?action=edit&quizId=" + quizId);
            } else {
                request.setAttribute("error", "Failed to create quiz. Please check the course ID and try again.");
                request.setAttribute("courseId", courseId);
                request.getRequestDispatcher("create-quiz.jsp").forward(request, response);
            }
        } catch (NumberFormatException e) {
            request.setAttribute("error", "Invalid input: Please check all numeric fields. " + e.getMessage());
            request.setAttribute("courseId", request.getParameter("courseId"));
            request.getRequestDispatcher("create-quiz.jsp").forward(request, response);
        } catch (Exception e) {
            request.setAttribute("error", "Error creating quiz: " + e.getMessage());
            request.setAttribute("courseId", request.getParameter("courseId"));
            request.getRequestDispatcher("create-quiz.jsp").forward(request, response);
            e.printStackTrace();
        }
    }
    
    private void handleEditQuiz(HttpServletRequest request, HttpServletResponse response, User user) 
            throws ServletException, IOException {
        int quizId = Integer.parseInt(request.getParameter("quizId") != null ? 
                                     request.getParameter("quizId") : "0");
        
        if (quizId <= 0) {
            response.sendRedirect("quiz?action=list");
            return;
        }
        
        Quiz quiz = QuizDAO.getQuizById(quizId);
        List<QuizQuestion> questions = QuizQuestionDAO.getQuestionsByQuiz(quizId);
        
        request.setAttribute("quiz", quiz);
        request.setAttribute("questions", questions);
        request.getRequestDispatcher("edit-quiz.jsp").forward(request, response);
    }
    
    private void handleUpdateQuiz(HttpServletRequest request, HttpServletResponse response, User user) 
            throws ServletException, IOException {
        try {
            String quizIdParam = request.getParameter("quizId");
            int quizId = (quizIdParam != null && !quizIdParam.trim().isEmpty()) ? 
                        Integer.parseInt(quizIdParam) : 0;
            
            String title = request.getParameter("title");
            String description = request.getParameter("description");
            
            String durationParam = request.getParameter("durationMinutes");
            int durationMinutes = (durationParam != null && !durationParam.trim().isEmpty()) ? 
                                 Integer.parseInt(durationParam) : 30;
            
            String maxScoreParam = request.getParameter("maxScore");
            int maxScore = (maxScoreParam != null && !maxScoreParam.trim().isEmpty()) ? 
                          Integer.parseInt(maxScoreParam) : 100;
            
            String passingScoreParam = request.getParameter("passingScore");
            int passingScore = (passingScoreParam != null && !passingScoreParam.trim().isEmpty()) ? 
                              Integer.parseInt(passingScoreParam) : 60;
            
            Quiz quiz = new Quiz();
            quiz.setQuizId(quizId);
            quiz.setTitle(title);
            quiz.setDescription(description);
            quiz.setDurationMinutes(durationMinutes);
            quiz.setMaxScore(maxScore);
            quiz.setPassingScore(passingScore);
            
            if (QuizDAO.updateQuiz(quiz)) {
                response.sendRedirect("quiz?action=edit&quizId=" + quizId);
            } else {
                request.setAttribute("error", "Failed to update quiz");
                handleEditQuiz(request, response, user);
            }
        } catch (NumberFormatException e) {
            request.setAttribute("error", "Invalid input: Please check all numeric fields");
            handleEditQuiz(request, response, user);
        }
    }
    
    private void handleAddQuestion(HttpServletRequest request, HttpServletResponse response, User user) 
            throws ServletException, IOException {
        try {
            String quizIdParam = request.getParameter("quizId");
            int quizId = (quizIdParam != null && !quizIdParam.trim().isEmpty()) ? 
                        Integer.parseInt(quizIdParam) : 0;
            
            String questionText = request.getParameter("questionText");
            String questionType = request.getParameter("questionType");
            String optionA = request.getParameter("optionA");
            String optionB = request.getParameter("optionB");
            String optionC = request.getParameter("optionC");
            String optionD = request.getParameter("optionD");
            String correctAnswer = request.getParameter("correctAnswer");
            
            String pointsParam = request.getParameter("points");
            int points = (pointsParam != null && !pointsParam.trim().isEmpty()) ? 
                        Integer.parseInt(pointsParam) : 1;
            
            int questionOrder = QuizQuestionDAO.countQuestions(quizId) + 1;
            
            QuizQuestion question = new QuizQuestion(quizId, questionText, questionType, 
                                                     optionA, optionB, optionC, optionD, 
                                                     correctAnswer, points, questionOrder);
            
            int questionId = QuizQuestionDAO.addQuestion(question);
            if (questionId > 0) {
                request.setAttribute("success", "Question added successfully");
            } else {
                request.setAttribute("error", "Failed to add question");
            }
            
            response.sendRedirect("quiz?action=edit&quizId=" + quizId);
        } catch (NumberFormatException e) {
            int quizId = 0;
            try {
                String quizIdParam = request.getParameter("quizId");
                if (quizIdParam != null && !quizIdParam.trim().isEmpty()) {
                    quizId = Integer.parseInt(quizIdParam);
                }
            } catch (Exception ignore) {}
            
            request.setAttribute("error", "Invalid input: Please check all numeric fields");
            if (quizId > 0) {
                response.sendRedirect("quiz?action=edit&quizId=" + quizId);
            } else {
                response.sendRedirect("quiz?action=list");
            }
        }
    }
    
    private void handlePublishQuiz(HttpServletRequest request, HttpServletResponse response, User user) 
            throws ServletException, IOException {
        int quizId = Integer.parseInt(request.getParameter("quizId") != null ? 
                                     request.getParameter("quizId") : "0");
        
        if (QuizDAO.publishQuiz(quizId)) {
            request.setAttribute("success", "Quiz published successfully");
        } else {
            request.setAttribute("error", "Failed to publish quiz");
        }
        
        response.sendRedirect("quiz?action=edit&quizId=" + quizId);
    }
    
    private void handleStartQuiz(HttpServletRequest request, HttpServletResponse response, User user) 
            throws ServletException, IOException {
        int quizId = Integer.parseInt(request.getParameter("quizId") != null ? 
                                     request.getParameter("quizId") : "0");
        
        Quiz quiz = QuizDAO.getQuizById(quizId);
        if (quiz == null || !quiz.isPublished()) {
            response.sendRedirect("login.jsp");
            return;
        }
        
        // Check if student already has an in-progress attempt
        QuizAttempt existingAttempt = QuizAttemptDAO.getStudentAttempt(quizId, user.getUserId());
        int attemptId;
        
        if (existingAttempt != null && "in_progress".equals(existingAttempt.getStatus())) {
            attemptId = existingAttempt.getAttemptId();
        } else {
            attemptId = QuizAttemptDAO.startAttempt(quizId, user.getUserId());
        }
        
        if (attemptId > 0) {
            request.setAttribute("quiz", quiz);
            request.setAttribute("attemptId", attemptId);
            request.getRequestDispatcher("quiz-attempt.jsp").forward(request, response);
        } else {
            response.sendRedirect("login.jsp");
        }
    }
    
    private void handleQuizAttempt(HttpServletRequest request, HttpServletResponse response, User user) 
            throws ServletException, IOException {
        int attemptId = Integer.parseInt(request.getParameter("attemptId") != null ? 
                                        request.getParameter("attemptId") : "0");
        
        QuizAttempt attempt = QuizAttemptDAO.getAttemptById(attemptId);
        Quiz quiz = QuizDAO.getQuizById(attempt.getQuizId());
        List<QuizQuestion> questions = QuizQuestionDAO.getQuestionsByQuiz(attempt.getQuizId());
        
        request.setAttribute("quiz", quiz);
        request.setAttribute("attempt", attempt);
        request.setAttribute("questions", questions);
        request.getRequestDispatcher("quiz-attempt.jsp").forward(request, response);
    }
    
    private void handleSubmitAttempt(HttpServletRequest request, HttpServletResponse response, User user) 
            throws ServletException, IOException {
        try {
            String attemptIdParam = request.getParameter("attemptId");
            int attemptId = (attemptIdParam != null && !attemptIdParam.trim().isEmpty()) ? 
                           Integer.parseInt(attemptIdParam) : 0;
            
            QuizAttempt attempt = QuizAttemptDAO.getAttemptById(attemptId);
            if (attempt == null) {
                response.sendRedirect("login.jsp");
                return;
            }
            
            // Calculate score based on answers
            List<QuizQuestion> questions = QuizQuestionDAO.getQuestionsByQuiz(attempt.getQuizId());
            int score = 0;
            
            for (QuizQuestion question : questions) {
                String studentAnswer = request.getParameter("answer_" + question.getQuestionId());
                if (studentAnswer != null && studentAnswer.equals(question.getCorrectAnswer())) {
                    score += question.getPoints();
                }
            }
            
            if (QuizAttemptDAO.submitAttempt(attemptId, score)) {
                response.sendRedirect("quiz?action=results&attemptId=" + attemptId);
            } else {
                response.sendRedirect("login.jsp");
            }
        } catch (NumberFormatException e) {
            response.sendRedirect("login.jsp");
        }
    }
    
    private void handleViewResults(HttpServletRequest request, HttpServletResponse response, User user) 
            throws ServletException, IOException {
        try {
            String attemptIdParam = request.getParameter("attemptId");
            int attemptId = (attemptIdParam != null && !attemptIdParam.trim().isEmpty()) ? 
                           Integer.parseInt(attemptIdParam) : 0;
            
            QuizAttempt attempt = QuizAttemptDAO.getAttemptById(attemptId);
            Quiz quiz = QuizDAO.getQuizById(attempt.getQuizId());
            List<QuizQuestion> questions = QuizQuestionDAO.getQuestionsByQuiz(attempt.getQuizId());
            
            request.setAttribute("quiz", quiz);
            request.setAttribute("attempt", attempt);
            request.setAttribute("questions", questions);
            request.getRequestDispatcher("quiz-results.jsp").forward(request, response);
        } catch (NumberFormatException e) {
            response.sendRedirect("login.jsp");
        }
    }
    
    private void handleGradeAttempt(HttpServletRequest request, HttpServletResponse response, User user) 
            throws ServletException, IOException {
        try {
            String attemptIdParam = request.getParameter("attemptId");
            int attemptId = (attemptIdParam != null && !attemptIdParam.trim().isEmpty()) ? 
                           Integer.parseInt(attemptIdParam) : 0;
            
            String scoreParam = request.getParameter("score");
            int score = (scoreParam != null && !scoreParam.trim().isEmpty()) ? 
                       Integer.parseInt(scoreParam) : 0;
            
            String feedback = request.getParameter("feedback") != null ? 
                             request.getParameter("feedback") : "";
            
            if (QuizAttemptDAO.gradeAttempt(attemptId, score, feedback)) {
                response.sendRedirect("quiz?action=results&attemptId=" + attemptId);
            } else {
                response.sendRedirect("login.jsp");
            }
        } catch (NumberFormatException e) {
            response.sendRedirect("login.jsp");
        }
    }
    
    private boolean isInstructor(User user) {
        return user != null && "instructor".equals(user.getRole());
    }
}
