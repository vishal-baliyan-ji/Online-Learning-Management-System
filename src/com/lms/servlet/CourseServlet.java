package com.lms.servlet;

import com.lms.dao.CourseDAO;
import com.lms.dao.UserDAO;
import com.lms.model.Course;
import com.lms.model.User;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.*;
import java.io.IOException;
import java.util.List;

/**
 * Course Management Servlet
 * File: src/com/lms/servlet/CourseServlet.java
 * Handles CRUD operations for courses
 */
@WebServlet("/course")
public class CourseServlet extends HttpServlet {
    private CourseDAO courseDAO;
    private UserDAO userDAO;
    
    @Override
    public void init() throws ServletException {
        courseDAO = new CourseDAO();
        userDAO = new UserDAO();
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
                listCourses(request, response);
                break;
            case "add":
                showAddForm(request, response);
                break;
            case "edit":
                showEditForm(request, response);
                break;
            case "delete":
                deleteCourse(request, response);
                break;
            case "view":
                viewCourse(request, response);
                break;
            default:
                listCourses(request, response);
        }
    }
    
    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        
        String action = request.getParameter("action");
        
        if ("add".equals(action)) {
            addCourse(request, response);
        } else if ("edit".equals(action)) {
            updateCourse(request, response);
        }
    }
    
    // List all courses
    private void listCourses(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        
        HttpSession session = request.getSession(false);
        String userRole = (String) session.getAttribute("userRole");
        Integer userId = (Integer) session.getAttribute("userId");
        
        List<Course> courses;
        
        // Instructors see only their courses
        if ("instructor".equals(userRole)) {
            courses = courseDAO.getCoursesByInstructor(userId);
        } else {
            courses = courseDAO.getAllCourses();
        }
        
        request.setAttribute("courses", courses);
        request.getRequestDispatcher("course-list.jsp").forward(request, response);
    }
    
    // Show add course form
    private void showAddForm(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        
        // Get list of instructors for dropdown
        List<User> instructors = userDAO.getUsersByRole("instructor");
        request.setAttribute("instructors", instructors);
        request.getRequestDispatcher("add-course.jsp").forward(request, response);
    }
    
    // Show edit course form
    private void showEditForm(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        
        int courseId = Integer.parseInt(request.getParameter("id"));
        Course course = courseDAO.getCourseById(courseId);
        
        // Get list of instructors for dropdown
        List<User> instructors = userDAO.getUsersByRole("instructor");
        
        request.setAttribute("course", course);
        request.setAttribute("instructors", instructors);
        request.getRequestDispatcher("edit-course.jsp").forward(request, response);
    }
    
    // View course details
    private void viewCourse(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        
        int courseId = Integer.parseInt(request.getParameter("id"));
        Course course = courseDAO.getCourseById(courseId);
        
        request.setAttribute("course", course);
        request.getRequestDispatcher("view-course.jsp").forward(request, response);
    }
    
    // Add new course
    private void addCourse(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        
        String title = request.getParameter("title");
        String description = request.getParameter("description");
        String syllabus = request.getParameter("syllabus");
        int instructorId = Integer.parseInt(request.getParameter("instructorId"));
        
        // Validate input
        if (title == null || title.trim().isEmpty()) {
            request.setAttribute("error", "Course title is required");
            List<User> instructors = userDAO.getUsersByRole("instructor");
            request.setAttribute("instructors", instructors);
            request.getRequestDispatcher("add-course.jsp").forward(request, response);
            return;
        }
        
        Course course = new Course(title, description, syllabus, instructorId);
        boolean success = courseDAO.addCourse(course);
        
        if (success) {
            response.sendRedirect("course?action=list&success=Course added successfully");
        } else {
            request.setAttribute("error", "Failed to add course");
            List<User> instructors = userDAO.getUsersByRole("instructor");
            request.setAttribute("instructors", instructors);
            request.getRequestDispatcher("add-course.jsp").forward(request, response);
        }
    }
    
    // Update course
    private void updateCourse(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        
        int courseId = Integer.parseInt(request.getParameter("courseId"));
        String title = request.getParameter("title");
        String description = request.getParameter("description");
        String syllabus = request.getParameter("syllabus");
        int instructorId = Integer.parseInt(request.getParameter("instructorId"));
        
        // Validate input
        if (title == null || title.trim().isEmpty()) {
            request.setAttribute("error", "Course title is required");
            Course course = courseDAO.getCourseById(courseId);
            List<User> instructors = userDAO.getUsersByRole("instructor");
            request.setAttribute("course", course);
            request.setAttribute("instructors", instructors);
            request.getRequestDispatcher("edit-course.jsp").forward(request, response);
            return;
        }
        
        Course course = new Course(courseId, title, description, syllabus, instructorId);
        boolean success = courseDAO.updateCourse(course);
        
        if (success) {
            response.sendRedirect("course?action=list&success=Course updated successfully");
        } else {
            request.setAttribute("error", "Failed to update course");
            List<User> instructors = userDAO.getUsersByRole("instructor");
            request.setAttribute("course", course);
            request.setAttribute("instructors", instructors);
            request.getRequestDispatcher("edit-course.jsp").forward(request, response);
        }
    }
    
    // Delete course
    private void deleteCourse(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        
        int courseId = Integer.parseInt(request.getParameter("id"));
        boolean success = courseDAO.deleteCourse(courseId);
        
        if (success) {
            response.sendRedirect("course?action=list&success=Course deleted successfully");
        } else {
            response.sendRedirect("course?action=list&error=Failed to delete course");
        }
    }
}