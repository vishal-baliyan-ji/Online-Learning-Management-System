# Learning Management System (LMS)

A simple Learning Management System implemented using Java Servlets, JSP (JSTL), and MySQL. This web application provides features for three roles: Admin, Instructor, and Student. It includes user management (admin), course creation and management (instructors), assignment creation and submission (instructors & students), enrollments, and grading.

---

## 🔧 Features

- Admin user management (CRUD users)
- Instructor: create/edit courses, assignments, add attachment files
- Student: enroll/drop/re-enroll courses, view "My Courses", view/submit assignments
# Learning Management System (LMS)

Welcome — this repository contains a small Learning Management System (LMS) built with Java Servlets, JSP (JSTL), and MySQL. It supports three roles: Admin, Instructor, and Student, with features for course management, assignments, enrollments, and a quiz system.

This README is written for developers who want to run or extend the project locally.

---

## Highlights

- Instructor and student workflows (courses, assignments, enrollments)
- Assignment file upload and submission handling
- Quiz subsystem (create, publish, attempt, grade)
- Simple role-based checks (admin, instructor, student)

---

## Tech Stack

- Java 8
- Servlets + JSP (JSTL)
- MySQL 8 (or compatible)
- Maven (build)

---

## Prerequisites

- Java JDK 8 (set `JAVA_HOME`)
- Maven 3.x
- MySQL (8.0 recommended)
- Apache Tomcat (8/9/10) or run with the Tomcat Maven plugin

---

## Quick Setup

1. Clone the repository.
2. Import the database schema and seed data (see `db/database_schema.sql`). The schema includes tables for users, courses, assignments, quizzes and related data.

   Note: the seed users in `db/database_schema.sql` use BCrypt-hashed passwords (NOT plaintext). See the "Security & passwords" section below.

3. Update database credentials for local development:

   - Edit `src/com/lms/util/DBConnection.java` or set environment variables and adjust the helper to read them. For local testing, the app uses `jdbc:mysql://localhost:3306/lms_db`.

4. Build and run:

```powershell
mvn clean package
  - View individual student attempts
mvn tomcat7:run
```

The generated WAR is `build/LMS_Project.war`.

---

## Database & Passwords

- The project ships with `db/database_schema.sql` which creates the `lms_db` schema and inserts a set of seed users.
- The seed passwords are stored as BCrypt hashes. The app uses BCrypt to hash passwords on user creation and to verify logins (see `src/com/lms/util/PasswordUtil.java` and `src/com/lms/dao/UserDAO.java`).
- Do NOT convert these hashes back to plaintext or store real credentials here. The seeded accounts are for local development only.

If you need to regenerate a test bcrypt hash locally:

```powershell
  - See all submissions in one place
```

---

## Endpoints (overview)

- `GET /login` — Login page
- `POST /login` — Authenticate user
- `GET /logout` — Logout
- `GET/POST /user` — Admin user management (action=list|add|edit|delete)
- `GET/POST /course` — Course flows (action=list|view|add|edit|delete)
- `GET/POST /assignment` — Assignment flows (submit, list, grade)
- `GET/POST /enrollment` — Enroll/drop flows
- `GET/POST /quiz` — Quiz subsystem (list, create, start, submit, results, grade)

See the servlet mappings in the `src/com/lms/servlet` package for details.

---

## Directory layout

- `src/` — Java source (servlets, DAOs, models, utils)
- `WebContent/` — JSP views, static assets
- `db/database_schema.sql` — schema + seed data
- `pom.xml` — Maven build

---

## Security & passwords (clear statement)

- Seed and application passwords are handled using BCrypt. The project does not rely on plaintext passwords for login verification.
- For development the seed users are present in `db/database_schema.sql` as hashed values; for production, use a secure seeding/migration strategy and never commit real credentials.
- Replace any hard-coded credentials in `DBConnection.java` with environment variables before deploying to production.

Security recommendations:

- Enable HTTPS and secure cookies in production.
- Add CSRF protection (anti-CSRF tokens for forms).
- Centralize authorization with servlet filters.
- Validate and sanitize all user input.

---

## Quiz subsystem (summary)

- Instructors can create quizzes, add questions (multiple-choice, true/false, short answer), publish quizzes, and grade attempts.
- Students can view published quizzes, take them (timer + auto-submit), and review results.
- Relevant files: `src/com/lms/servlet/QuizServlet.java`, `src/com/lms/dao/*Quiz*.java`, and JSPs under `WebContent/` containing `quiz-*.jsp`.

---

## Development tips

- To test DB connection quickly, use `DBConnection.main()` in your IDE.
- If you change file upload limits, ensure Tomcat's connector properties allow larger requests.
- Use `mvn -DskipTests=true package` to speed up iterative builds.

---

## Known limitations

- No automated unit/integration tests included — adding JUnit and basic integration tests is recommended.
- File downloads are served via JSPs; consider a secure download servlet for production.
- CSRF protection is not implemented.

---



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
```
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
```
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
```
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


## Contributors

Team Code Enforcers
- Mehul Raj
- Vishal Baliyan
- Hritik Verma

---

