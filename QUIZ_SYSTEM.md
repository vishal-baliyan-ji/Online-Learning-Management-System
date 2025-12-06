# Quiz System Documentation

## Overview
The LMS now includes a comprehensive quiz management system that allows instructors to create, manage, and grade quizzes, while students can take quizzes with automatic grading and timing.

## Features

### For Instructors
- **Create Quizzes**: Create new quizzes for any course with custom settings
  - Set quiz title and description
  - Configure time limit (in minutes)
  - Set maximum score and passing score
  - Define custom questions and answer options

- **Manage Questions**: Add multiple types of questions
  - Multiple Choice (A, B, C, D options)
  - True/False questions
  - Short Answer questions
  - Set points per question
  - Edit and delete questions

- **Publish Quizzes**: Control when quizzes become available to students
  - Save as draft before publishing
  - Publish when ready for students to take

- **Grade Submissions**: Review and grade student quiz attempts
  - View individual student attempts
  - See all submissions in one place
  - Add feedback for students
  - View score statistics

### For Students
- **Browse Quizzes**: See all published quizzes in their courses
  - View quiz details (duration, max score, passing score)
  - Read quiz description and instructions

- **Take Quizzes**: Complete quizzes with features
  - Automated timer with countdown (auto-submits when time is up)
  - Progress bar showing quiz completion
  - Clean interface for answering questions
  - Cannot submit without answering all questions

- **View Results**: See their quiz performance
  - Immediate score after submission
  - Pass/Fail status based on passing score
  - View correct/incorrect answers
  - Read instructor feedback if provided

## Database Schema

### Tables Created

#### 1. `quizzes`
Stores quiz information created by instructors
```sql
- quiz_id (INT, PRIMARY KEY)
- course_id (INT, FOREIGN KEY)
- title (VARCHAR 200)
- description (TEXT)
- duration_minutes (INT)
- max_score (INT)
- passing_score (INT)
- is_published (BOOLEAN)
- created_at (TIMESTAMP)
- updated_at (TIMESTAMP)
- published_date (TIMESTAMP)
```

#### 2. `quiz_questions`
Stores individual questions for each quiz
```sql
- question_id (INT, PRIMARY KEY)
- quiz_id (INT, FOREIGN KEY)
- question_text (TEXT)
- question_type (ENUM: multiple_choice, true_false, short_answer)
- option_a (VARCHAR 500)
- option_b (VARCHAR 500)
- option_c (VARCHAR 500)
- option_d (VARCHAR 500)
- correct_answer (VARCHAR)
- points (INT)
- question_order (INT)
- created_at (TIMESTAMP)
```

#### 3. `quiz_attempts`
Tracks student quiz attempts
```sql
- attempt_id (INT, PRIMARY KEY)
- quiz_id (INT, FOREIGN KEY)
- student_id (INT, FOREIGN KEY)
- score (INT)
- status (ENUM: in_progress, submitted, graded)
- started_at (TIMESTAMP)
- submitted_at (TIMESTAMP)
- graded_at (TIMESTAMP)
- feedback (TEXT)
```

## Code Structure

### Model Classes
- **Quiz.java**: Represents a quiz with metadata
- **QuizQuestion.java**: Represents individual questions in a quiz
- **QuizAttempt.java**: Represents a student's attempt at a quiz

### DAO Classes
- **QuizDAO.java**: Database operations for quizzes
  - `createQuiz()`, `getQuizById()`, `getQuizzesByCourse()`
  - `updateQuiz()`, `publishQuiz()`, `deleteQuiz()`

- **QuizQuestionDAO.java**: Database operations for questions
  - `addQuestion()`, `getQuestionsByQuiz()`, `updateQuestion()`
  - `deleteQuestion()`, `countQuestions()`

- **QuizAttemptDAO.java**: Database operations for attempts
  - `startAttempt()`, `getAttemptById()`, `submitAttempt()`
  - `gradeAttempt()`, `getStudentAttempts()`, `hasAttempted()`

### Servlet
- **QuizServlet.java**: Main servlet handling all quiz operations
  - URL mapping: `/quiz`
  - GET actions: list, view, create, edit, start, attempt, grade, results
  - POST actions: create, update, addQuestion, submit, publish

### JSP Pages

#### Instructor Pages
- **quiz-list-instructor.jsp**: List all quizzes for a course with management options
- **edit-quiz.jsp**: Add/edit quiz settings and manage questions
- **quiz-detail-instructor.jsp**: View all student attempts and grading options
- **create-quiz.jsp**: Create a new quiz form

#### Student Pages
- **quiz-list.jsp**: Browse published quizzes in a course
- **quiz-detail.jsp**: Quiz information and start button with instructions
- **quiz-attempt.jsp**: Interactive quiz taking interface with timer and progress bar
- **quiz-results.jsp**: Post-submission results with performance details

## How to Use

### For Instructors

#### Step 1: Create a Quiz
1. Navigate to a course
2. Go to "Quizzes" section
3. Click "Create New Quiz"
4. Fill in quiz details:
   - Title: e.g., "Chapter 1 Quiz"
   - Description: What the quiz covers
   - Duration: Time limit in minutes
   - Max Score: Total points possible
   - Passing Score: Minimum points to pass
5. Click "Create Quiz"

#### Step 2: Add Questions
1. After creating, you're taken to the edit page
2. Scroll down to "Add New Question" section
3. Select question type:
   - **Multiple Choice**: Provide 4 options (A, B, C, D)
   - **True/False**: Options are provided
   - **Short Answer**: Students type the answer
4. Enter question text and options
5. Specify the correct answer
6. Set points for the question
7. Click "Add Question"
8. Repeat for all questions

#### Step 3: Publish Quiz
1. Once all questions are added
2. Make sure settings are correct
3. Click "Publish" button
4. Quiz is now visible to students

#### Step 4: Grade Submissions
1. After students take the quiz
2. Go to quiz and click "View Attempts"
3. See all student submissions
4. Click "Grade" on a submitted attempt
5. Add feedback/comments
6. Submit grade

### For Students

#### Step 1: View Available Quizzes
1. Go to your course
2. Click on "Quizzes" section
3. Browse published quizzes
4. Click on a quiz to see details

#### Step 2: Take the Quiz
1. Read the instructions carefully
2. Click "Start Quiz"
3. Answer all questions before time runs out
4. Watch the timer countdown in the header
5. Make sure you've answered every question
6. Click "Submit Quiz" when ready
7. Cannot change answers after submission

#### Step 3: View Results
1. After submission, see your score immediately
2. View pass/fail status
3. Compare your answers with correct answers
4. Check instructor feedback (if provided)

## Features in Detail

### Timer System
- Countdown timer starts when quiz begins
- Shows minutes and seconds remaining
- Progress bar fills as time passes
- Automatically submits when time runs out (if student hasn't submitted yet)
- Students are alerted when time expires

### Automatic Grading
- Multiple choice and true/false are auto-graded immediately
- Scores calculated based on points per question
- Pass/fail determined by comparing score to passing score
- Short answer questions can be manually graded by instructor

### Question Types

1. **Multiple Choice**
   - 4 options (A, B, C, D)
   - Student selects one answer
   - Correct answer is one letter

2. **True/False**
   - Two options (True/False)
   - Student selects one
   - Answers as "A" or "B"

3. **Short Answer**
   - Students type their answer
   - Requires manual grading
   - Instructor compares with expected answer

## Security Features

- Only instructors can create/edit quizzes
- Only instructors can view all attempts
- Students can only see published quizzes
- Students cannot see other students' attempts
- Cannot retake quiz (instructor controls attempts)
- Timer prevents rushing or extending time

## API Endpoints

### Quiz Management
- `GET /quiz?action=list&courseId=X` - List quizzes
- `GET /quiz?action=create&courseId=X` - Create quiz form
- `POST /quiz` - Create quiz
- `GET /quiz?action=edit&quizId=X` - Edit quiz
- `POST /quiz?action=update` - Update quiz
- `POST /quiz?action=addQuestion` - Add question to quiz
- `POST /quiz?action=publish` - Publish quiz

### Student Actions
- `GET /quiz?action=start&quizId=X` - Start quiz attempt
- `POST /quiz?action=submit` - Submit quiz
- `GET /quiz?action=results&attemptId=X` - View results
- `GET /quiz?action=view&quizId=X` - View quiz details

### Grading
- `POST /quiz?action=grade` - Grade an attempt

## Configuration

No additional configuration needed. The quiz system works with existing database connection in `DBConnection.java`.

## Future Enhancements

Possible additions:
- Allow multiple attempts per student
- Question shuffling/randomization
- Custom feedback per question
- Quiz analytics and statistics dashboard
- Bulk question import
- Quiz templates
- Time tracking per question
- Negative marking for wrong answers
