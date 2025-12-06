package com.lms.model;

import java.io.Serializable;

public class QuizQuestion implements Serializable {
    private static final long serialVersionUID = 1L;
    
    private int questionId;
    private int quizId;
    private String questionText;
    private String questionType; // multiple_choice, true_false, short_answer
    private String optionA;
    private String optionB;
    private String optionC;
    private String optionD;
    private String correctAnswer;
    private int points;
    private int questionOrder;
    
    // Default constructor
    public QuizQuestion() {
    }
    
    // Constructor with parameters
    public QuizQuestion(int quizId, String questionText, String questionType, 
                       String optionA, String optionB, String optionC, String optionD,
                       String correctAnswer, int points, int questionOrder) {
        this.quizId = quizId;
        this.questionText = questionText;
        this.questionType = questionType;
        this.optionA = optionA;
        this.optionB = optionB;
        this.optionC = optionC;
        this.optionD = optionD;
        this.correctAnswer = correctAnswer;
        this.points = points;
        this.questionOrder = questionOrder;
    }
    
    // Getters and Setters
    public int getQuestionId() {
        return questionId;
    }
    
    public void setQuestionId(int questionId) {
        this.questionId = questionId;
    }
    
    public int getQuizId() {
        return quizId;
    }
    
    public void setQuizId(int quizId) {
        this.quizId = quizId;
    }
    
    public String getQuestionText() {
        return questionText;
    }
    
    public void setQuestionText(String questionText) {
        this.questionText = questionText;
    }
    
    public String getQuestionType() {
        return questionType;
    }
    
    public void setQuestionType(String questionType) {
        this.questionType = questionType;
    }
    
    public String getOptionA() {
        return optionA;
    }
    
    public void setOptionA(String optionA) {
        this.optionA = optionA;
    }
    
    public String getOptionB() {
        return optionB;
    }
    
    public void setOptionB(String optionB) {
        this.optionB = optionB;
    }
    
    public String getOptionC() {
        return optionC;
    }
    
    public void setOptionC(String optionC) {
        this.optionC = optionC;
    }
    
    public String getOptionD() {
        return optionD;
    }
    
    public void setOptionD(String optionD) {
        this.optionD = optionD;
    }
    
    public String getCorrectAnswer() {
        return correctAnswer;
    }
    
    public void setCorrectAnswer(String correctAnswer) {
        this.correctAnswer = correctAnswer;
    }
    
    public int getPoints() {
        return points;
    }
    
    public void setPoints(int points) {
        this.points = points;
    }
    
    public int getQuestionOrder() {
        return questionOrder;
    }
    
    public void setQuestionOrder(int questionOrder) {
        this.questionOrder = questionOrder;
    }
}
