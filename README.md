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



## 📝 Copyright

# Made by Team Code Enforcers   
**Mehul Raj**
**Vishal Baliyan**
**Hritik Verma**

---


