# Learning Management System (LMS)

A simple Learning Management System implemented using Java Servlets, JSP (JSTL), and MySQL. This web application provides features for three roles: Admin, Instructor, and Student. It includes user management (admin), course creation and management (instructors), assignment creation and submission (instructors & students), enrollments, and grading.

---

## 🔧 Features

- Admin user management (CRUD users)
- Instructor: create/edit courses, assignments, add attachment files
- Student: enroll/drop/re-enroll courses, view "My Courses", view/submit assignments
- File upload: assignment attachments and student submissions
- Enrollment status management and progress updates
- Simple role-based access checks (admin-only user management)

---

## 🧰 Tech stack

- Java 8 (source/target)
- Java Servlets and JSP with JSTL
- MySQL 8 (or compatible)
- Maven for build
- Apache Commons FileUpload & Commons IO for file uploads

---

## ⚙️ Prerequisites

- Java JDK 8 (install and set JAVA_HOME)
- Maven 3.x
- MySQL (8.0 recommended)
- Apache Tomcat (8, 9, or 10) / or run via the Maven tomcat plugin

---

## 🛠️ Setup and Configuration

1. Clone this repository into a workspace.

2. Database setup
   - Create the database and sample data using `db/database_schema.sql`:

```powershell
# Run in a MySQL client:
mysql -u root -p < db/database_schema.sql
```

   - Default sample accounts injected in `database_schema.sql`:
     - Admin: `admin@lms.com` / `admin123`
     - Instructors and students also seeded for dev/testing

3. Configure database credentials
   - Update `src/com/lms/util/DBConnection.java` with your MySQL username and password.
   - By default, `DBConnection` uses JDBC URL `jdbc:mysql://localhost:3306/lms_db`.
   - IMPORTANT: This project stores credentials in code for simplicity; for production, use environment variables or a secure store.

4. (Optional) If using the Tomcat manager plugin (`pom.xml`):
   - Configure your Tomcat Manager credentials in `~/.m2/settings.xml` with server id `TomcatServer` to allow `mvn tomcat7:deploy` or similar.

---

## 📦 Build and Run (local)

1. Build the application (creates a WAR file):

```powershell
mvn clean package
```

- The generated artifact: `build/LMS_Project.war` (as defined in `pom.xml`).

2. Run using the Tomcat Maven plugin (for development / quick local testing):

```powershell
mvn tomcat7:run
```

- This starts Tomcat (by default on port 8080) and deploys the app to `http://localhost:8080/LMS_Project` or `http://localhost:8080` depending on plugin config.

3. Deploy the WAR to Tomcat manually:
   - Copy the `build/LMS_Project.war` to your Tomcat's `webapps/` directory and start/restart the Tomcat server.

---

## 🧭 Application Endpoints / URLs

The application uses servlet mappings with query `action` parameters. Main servlet endpoints:

- `GET /login` — Login page
- `POST /login` — Authenticate user
- `GET /logout` — Logout

- `GET/POST /user` — User management (Admin)
  - Query parameters: `action=list|add|edit|delete`

- `GET/POST /course` — Course management
  - Query parameters: `action=list|add|edit|delete|view`

- `GET/POST /assignment` — Assignment management
  - Query parameters: `action=list|add|edit|delete|view|my-assignments|submit|submissions|view-submission|grade`

- `GET/POST /enrollment` — Enrollment management
  - Query parameters: `action=list|my-courses|enroll|drop`

Tip: Many flows use `action` and additional query parameters such as `id`, `assignmentId`, `courseId`, etc.

---

## 🗃️ Directory structure

Key folders & files:

- `src/` — Java source code (servlets, DAOs, models, util)
  - `src/com/lms/servlet/` — Servlets (User, Course, Enrollment, Assignment, Login, Logout)
  - `src/com/lms/dao/` — Data Access Objects
  - `src/com/lms/model/` — Model classes (User, Course, Assignment, Enrollment, etc.)
  - `src/com/lms/util/DBConnection.java` — Database connection helper

- `WebContent/` — web app files (JSPs, CSS, images)
  - `WebContent/css/style.css` — consolidated app styles (green/white theme)
  - `WebContent/uploads` — store assignment attachment files (created at runtime)
  - `WebContent/submissions` — store student submission files (created at runtime)

- `db/database_schema.sql` — SQL schema with seed data
- `pom.xml` — Maven build file

---

## 🔐 Security & Notes

- Passwords in SQL seed data and `DBConnection` are plain-text for development convenience. Never use plain-text passwords in production — use hashing (bcrypt) and secure storage.
- Uploaded files are stored inside `WebContent/uploads` and `WebContent/submissions` and served directly. For production, prefer:
  - Storing files outside the webroot and using a secure download servlet with authorization checks
  - Virus scanning & file type validation
  - Enforcing upload file size limits (currently 5 MB for assignment attachments, 10 MB for student submissions in code)
- Role checks: `UserServlet` now enforces admin-only actions; JSPs also have session checks. For robust enforcement, consider a Servlet Filter to centralize authorization.
- CSRF protection is not implemented. Add anti-CSRF tokens to forms and server-side validation.
- Session timeouts and login handling: Use HTTPS in production and secure cookie flags.

---

## ✅ Development Tips

- To test DB connection quickly, run: `DBConnection.main()` via IDE or execute after building.
- The application uses `commons-fileupload` with a size max configured in `AssignmentServlet`. If you need larger uploads, update the sizes and ensure Tomcat allows larger HTTP request sizes.
- Replace `DBConnection` credentials with environment variables or a config file for safety.
- Use `mvn -DskipTests=true package` if you add tests and want to quick-build.

---

## 📌 Known Limitations & To-Do

- No password hashing (bcrypt recommended)
- No centralized role filter — consider adding an `AdminFilter` and `AuthFilter`
- No automated unit/integration tests (consider adding JUnit + integration tests)
- File download served directly by JSP links — consider a download servlet to check access
- No CSRF protections and limited input sanitization/validation — must be hardened for production

---

## 🧪 Manual Acceptance Tests

- Build and deploy, login as `admin@lms.com` / `admin123` and check user CRUD
- Login as instructor and create a course and an assignment with an attachment
- Enroll a student in a course and submit an assignment (file)
- Grade a submission as instructor, and verify the student's `view-submission.jsp` shows a grade
- Drop a course and attempt re-enroll; verify re-enroll success

---



## Acknowledgements

Developed and maintained by Team Code Enforcers.

Contributors:
- Mehul Raj
- Vishal Baliyan
- Hritik Verma

---


## Additional documentation (merged)

For convenience, we've included two supplemental documents below: guidance about bcrypt password hashes used for seed data, and the quiz system documentation. These were merged here so you can find project details in one place.

---

### BCrypt: seed hashes and usage

The project uses BCrypt for secure password hashing. Seed users in `db/database_schema.sql` have bcrypt hashes; use the examples below only for local development.

## Database Schema Update

The file `db/database_schema.sql` has been updated with **verified bcrypt hashes** for the seed users.

### Seed User Credentials

| Email | Password | Role | Bcrypt Hash |
|-------|----------|------|-------------|
| admin@lms.com | admin123 | admin | `$2a$10$2ye1mjik.De2f0CNAvsKmOOHl6QWWF0adGqHooL8EIiPVF48zW9A2` |
| john@lms.com | instructor123 | instructor | `$2a$10$SF..Dub.2tY6ByAPkA9QCOYAsPB1V0DQuKj077o.tpoOLX3VKKreO` |
| jane@lms.com | student123 | student | `$2a$10$gN.DuHvwAXV074FdgqQQweZ6Ka.4.1q8Uf52.JXPv85pth0IvD0vy` |
| bob@lms.com | instructor123 | instructor | `$2a$10$SF..Dub.2tY6ByAPkA9QCOYAsPB1V0DQuKj077o.tpoOLX3VKKreO` |
| alice@lms.com | student123 | student | `$2a$10$gN.DuHvwAXV074FdgqQQweZ6Ka.4.1q8Uf52.JXPv85pth0IvD0vy` |

**These hashes have been verified and work correctly.**

## How to Use

### Step 1: Import Database Schema
```sql
-- In MySQL client or MySQL Workbench:
SOURCE db/database_schema.sql;
-- OR manually execute the contents of db/database_schema.sql
```

### Step 2: Build the Project
```bash
mvn clean package
```

### Step 3: Deploy and Login
- Deploy `build/LMS_Project.war` to your Tomcat server
- Navigate to the login page
- Use any of the seed credentials above, e.g.:
  - Email: `admin@lms.com`
  - Password: `admin123`

## Creating New Users

When you create new users through the application's admin panel:
1. The password you enter will be automatically hashed using bcrypt
2. The hash will be securely stored in the database
3. On login, the user's entered password is verified against the stored hash

## Technical Details

- **Library**: org.mindrot:jbcrypt:0.4
- **Hash Format**: BCrypt with 10 salt rounds (format: `$2a$10$...`)
- **Implementation**:
  - `src/com/lms/dao/UserDAO.java` - Hashing on user creation, verification on login
  - `src/com/lms/util/PasswordUtil.java` - Utility to generate hashes for testing/migration

## Troubleshooting

If you encounter "Invalid email or password" errors:

1. **Verify Database Import**: Confirm that `db/database_schema.sql` was successfully imported into your MySQL database
2. **Check Hash Format**: Query your database: `SELECT email, password FROM users;` - Hashes should start with `$2a$10$` and be 60 characters long
3. **Test with New User**: Create a new user through the admin panel and try logging in - this confirms bcrypt is working
4. **Connection String**: Ensure `src/com/lms/util/DBConnection.java` connects to the correct database and user

## Security Notes

- **Never store plaintext passwords** - all passwords must be hashed before database insertion
- **All login attempts** use bcrypt verification, not plaintext comparison
- The bcrypt library uses strong salt generation (automatically done via `BCrypt.gensalt()`)

---

### QUIZ_SYSTEM.md

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



