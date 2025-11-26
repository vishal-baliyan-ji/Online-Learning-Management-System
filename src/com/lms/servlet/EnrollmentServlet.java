package com.lms.servlet;

import com.lms.dao.CourseDAO;
import com.lms.dao.EnrollmentDAO;
import com.lms.model.Course;
import com.lms.model.Enrollment;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.*;
import java.io.IOException;
import java.util.List;

/**
 * Enrollment Management Servlet
 * File: src/com/lms/servlet/EnrollmentServlet.java
 * Handles student enrollments in courses
 */
@WebServlet("/enrollment")
public class EnrollmentServlet extends HttpServlet {
    private EnrollmentDAO enrollmentDAO;
    private CourseDAO courseDAO;
    
    @Override
    public void init() throws ServletException {
        enrollmentDAO = new EnrollmentDAO();
        courseDAO = new CourseDAO();
    }
    
    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        
        String action = request.getParameter("action");
        
        if (action == null) {
            action = "list";
        }
        
        switch (action) {
            case "list":
                listEnrollments(request, response);
                break;
            case "my-courses":
                listMyCourses(request, response);
                break;
            case "enroll":
                showEnrollForm(request, response);
                break;
            case "drop":
                dropCourse(request, response);
                break;
            default:
                listEnrollments(request, response);
        }
    }
    
    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        
        String action = request.getParameter("action");
        
        if ("enroll".equals(action)) {
            enrollStudent(request, response);
        } else if ("updateProgress".equals(action)) {
            updateProgress(request, response);
        }
    }
    
    // List all enrollments (Admin view)
    private void listEnrollments(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        
        List<Enrollment> enrollments = enrollmentDAO.getAllEnrollments();
        request.setAttribute("enrollments", enrollments);
        request.getRequestDispatcher("enrollment-list.jsp").forward(request, response);
    }
    
    // List student's enrolled courses
    private void listMyCourses(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        
        HttpSession session = request.getSession(false);
        Integer studentId = (Integer) session.getAttribute("userId");
        
        List<Enrollment> enrollments = enrollmentDAO.getEnrollmentsByStudent(studentId);
        request.setAttribute("enrollments", enrollments);
        request.getRequestDispatcher("my-courses.jsp").forward(request, response);
    }
    
    // Show enrollment form (available courses)
    private void showEnrollForm(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        
        HttpSession session = request.getSession(false);
        Integer studentId = (Integer) session.getAttribute("userId");
        
        // Get all available courses
        List<Course> allCourses = courseDAO.getAllCourses();
        
        // Filter out already enrolled courses
        List<Course> availableCourses = new java.util.ArrayList<>();
        for (Course course : allCourses) {
            if (!enrollmentDAO.isStudentEnrolled(studentId, course.getCourseId())) {
                availableCourses.add(course);
            }
        }
        
        request.setAttribute("courses", availableCourses);
        request.getRequestDispatcher("enroll-course.jsp").forward(request, response);
    }
    
    // Enroll student in course
    private void enrollStudent(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        
        HttpSession session = request.getSession(false);
        Integer studentId = (Integer) session.getAttribute("userId");
        int courseId = Integer.parseInt(request.getParameter("courseId"));
        
        // Check if already enrolled (active)
        if (enrollmentDAO.isStudentEnrolled(studentId, courseId)) {
            response.sendRedirect("enrollment?action=enroll&error=Already enrolled in this course");
            return;
        }

        // Check if there is a previous (dropped) enrollment to re-activate
        Enrollment existing = enrollmentDAO.getEnrollmentByStudentCourse(studentId, courseId);
        boolean success = false;
        String message = "";
        if (existing != null) {
            // Re-activate
            success = enrollmentDAO.reEnrollStudent(existing.getEnrollmentId());
            message = "Successfully re-enrolled in course";
        } else {
            Enrollment enrollment = new Enrollment(studentId, courseId);
            success = enrollmentDAO.addEnrollment(enrollment);
            message = "Successfully enrolled in course";
        }

        if (success) {
            response.sendRedirect("enrollment?action=my-courses&success=" + java.net.URLEncoder.encode(message, "UTF-8"));
        } else {
            response.sendRedirect("enrollment?action=enroll&error=Failed to enroll in course");
        }
    }
    
    // Drop/unenroll from course
    private void dropCourse(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        
        int enrollmentId = Integer.parseInt(request.getParameter("id"));
        
        // Update status to 'dropped' instead of deleting
        boolean success = enrollmentDAO.updateEnrollmentStatus(enrollmentId, "dropped");
        
        if (success) {
            response.sendRedirect("enrollment?action=my-courses&success=Successfully dropped course");
        } else {
            response.sendRedirect("enrollment?action=my-courses&error=Failed to drop course");
        }
    }
    
    // Update enrollment progress
    private void updateProgress(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        
        int enrollmentId = Integer.parseInt(request.getParameter("enrollmentId"));
        double progress = Double.parseDouble(request.getParameter("progress"));
        
        boolean success = enrollmentDAO.updateEnrollmentProgress(enrollmentId, progress);
        
        if (success) {
            response.sendRedirect("enrollment?action=my-courses&success=Progress updated successfully");
        } else {
            response.sendRedirect("enrollment?action=my-courses&error=Failed to update progress");
        }
    }
}