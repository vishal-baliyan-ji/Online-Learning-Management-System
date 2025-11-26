package com.lms.servlet;

import com.lms.dao.AssignmentDAO;
import com.lms.dao.AssignmentSubmissionDAO;
import com.lms.dao.CourseDAO;
import com.lms.model.Assignment;
import com.lms.model.AssignmentSubmission;
import com.lms.model.Course;
import org.apache.commons.fileupload.FileItem;
import org.apache.commons.fileupload.FileUploadException;
import org.apache.commons.fileupload.disk.DiskFileItemFactory;
import org.apache.commons.fileupload.servlet.ServletFileUpload;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.*;
import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.sql.Date;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;

/**
 * Assignment Management Servlet
 * File: src/com/lms/servlet/AssignmentServlet.java
 * Handles CRUD operations for assignments
 */
@WebServlet("/assignment")
public class AssignmentServlet extends HttpServlet {
    private AssignmentDAO assignmentDAO;
    private AssignmentSubmissionDAO submissionDAO;
    private CourseDAO courseDAO;
    
    @Override
    public void init() throws ServletException {
        assignmentDAO = new AssignmentDAO();
        submissionDAO = new AssignmentSubmissionDAO();
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
                listAssignments(request, response);
                break;
            case "add":
                showAddForm(request, response);
                break;
            case "edit":
                showEditForm(request, response);
                break;
            case "delete":
                deleteAssignment(request, response);
                break;
            case "view":
                viewAssignment(request, response);
                break;
            case "my-assignments":
                listMyAssignments(request, response);
                break;
            case "submit":
                showSubmitForm(request, response);
                break;
            case "submissions":
                viewSubmissions(request, response);
                break;
            case "view-submission":
                showStudentSubmission(request, response);
                break;
            case "grade":
                showGradeForm(request, response);
                break;
            default:
                listAssignments(request, response);
        }
    }
    
    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        
        String action = request.getParameter("action");
        
        if ("add".equals(action)) {
            addAssignment(request, response);
        } else if ("edit".equals(action)) {
            updateAssignment(request, response);
        } else if ("submit".equals(action)) {
            submitAssignment(request, response);
        } else if ("grade".equals(action)) {
            gradeSubmission(request, response);
        }
    }
    
    // List all assignments
    private void listAssignments(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        
        HttpSession session = request.getSession(false);
        String userRole = (String) session.getAttribute("userRole");
        Integer userId = (Integer) session.getAttribute("userId");
        
        List<Assignment> assignments;
        
        if ("instructor".equals(userRole)) {
            // Get assignments for instructor's courses
            List<Course> courses = courseDAO.getCoursesByInstructor(userId);
            assignments = new java.util.ArrayList<>();
            for (Course course : courses) {
                assignments.addAll(assignmentDAO.getAssignmentsByCourse(course.getCourseId()));
            }
        } else {
            assignments = assignmentDAO.getAllAssignments();
        }
        
        request.setAttribute("assignments", assignments);
        request.getRequestDispatcher("assignment-list.jsp").forward(request, response);
    }
    
    // List student's assignments
    private void listMyAssignments(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        
        HttpSession session = request.getSession(false);
        Integer studentId = (Integer) session.getAttribute("userId");
        
        List<Assignment> assignments = assignmentDAO.getAssignmentsForStudent(studentId);
        List<AssignmentSubmission> submissions = submissionDAO.getSubmissionsByStudent(studentId);
        
        request.setAttribute("assignments", assignments);
        request.setAttribute("submissions", submissions);
        request.getRequestDispatcher("my-assignments.jsp").forward(request, response);
    }
    
    // Show add assignment form
    private void showAddForm(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        
        HttpSession session = request.getSession(false);
        Integer instructorId = (Integer) session.getAttribute("userId");
        
        // Get instructor's courses
        List<Course> courses = courseDAO.getCoursesByInstructor(instructorId);
        request.setAttribute("courses", courses);
        request.getRequestDispatcher("add-assignment.jsp").forward(request, response);
    }
    
    // Show edit assignment form
    private void showEditForm(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        
        int assignmentId = Integer.parseInt(request.getParameter("id"));
        Assignment assignment = assignmentDAO.getAssignmentById(assignmentId);
        
        HttpSession session = request.getSession(false);
        Integer instructorId = (Integer) session.getAttribute("userId");
        List<Course> courses = courseDAO.getCoursesByInstructor(instructorId);
        
        request.setAttribute("assignment", assignment);
        request.setAttribute("courses", courses);
        request.getRequestDispatcher("edit-assignment.jsp").forward(request, response);
    }
    
    // View assignment details
    private void viewAssignment(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        
        int assignmentId = Integer.parseInt(request.getParameter("id"));
        Assignment assignment = assignmentDAO.getAssignmentById(assignmentId);
        
        request.setAttribute("assignment", assignment);
        request.getRequestDispatcher("view-assignment.jsp").forward(request, response);
    }

    // Show a student's submission for an assignment
    private void showStudentSubmission(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        HttpSession session = request.getSession(false);
        Integer studentId = (Integer) session.getAttribute("userId");
        if (studentId == null) {
            response.sendRedirect("login.jsp");
            return;
        }

        int assignmentId = Integer.parseInt(request.getParameter("id"));
        Assignment assignment = assignmentDAO.getAssignmentById(assignmentId);
        AssignmentSubmission submission = submissionDAO.getSubmissionByAssignmentAndStudent(assignmentId, studentId);

        if (submission == null) {
            // If the student hasn't submitted yet, redirect to submit form
            response.sendRedirect("assignment?action=submit&id=" + assignmentId + "&error=You have not submitted this assignment yet");
            return;
        }

        request.setAttribute("assignment", assignment);
        request.setAttribute("submission", submission);
        request.getRequestDispatcher("view-submission.jsp").forward(request, response);
    }
    
    // Add new assignment
    private void addAssignment(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        
        String attachmentPath = null;
        
        try {
            // Check if this is a multipart request
            if (ServletFileUpload.isMultipartContent(request)) {
                ServletFileUpload upload = new ServletFileUpload(new DiskFileItemFactory());
                upload.setFileSizeMax(5 * 1024 * 1024); // 5MB max file size
                
                List<FileItem> items = upload.parseRequest(request);
                Map<String, String> params = new TreeMap<>();
                
                // Create uploads directory if it doesn't exist
                String uploadDir = getServletContext().getRealPath("/uploads");
                File uploadDirectory = new File(uploadDir);
                if (!uploadDirectory.exists()) {
                    uploadDirectory.mkdirs();
                }
                
                // Process file items
                for (FileItem item : items) {
                    if (item.isFormField()) {
                        // Regular form field
                        params.put(item.getFieldName(), item.getString());
                    } else if (item.getName() != null && !item.getName().isEmpty()) {
                        // File upload
                        String fileName = new File(item.getName()).getName();
                        String filePath = uploadDir + File.separator + System.currentTimeMillis() + "_" + fileName;
                        File uploadedFile = new File(filePath);
                        item.write(uploadedFile);
                        attachmentPath = "uploads/" + uploadedFile.getName();
                    }
                }
                
                // Create assignment with parameters
                int courseId = Integer.parseInt(params.get("course_id"));
                String title = params.get("title");
                String description = params.get("description");
                Date dueDate = Date.valueOf(params.get("due_date"));
                int maxScore = Integer.parseInt(params.get("max_score"));
                
                Assignment assignment = new Assignment(courseId, title, description, dueDate, maxScore);
                assignment.setAttachmentPath(attachmentPath);
                
                boolean success = assignmentDAO.addAssignment(assignment);
                
                if (success) {
                    response.sendRedirect("assignment?action=list&success=Assignment created successfully");
                } else {
                    request.setAttribute("error", "Failed to create assignment");
                    HttpSession session = request.getSession(false);
                    Integer instructorId = (Integer) session.getAttribute("userId");
                    List<Course> courses = courseDAO.getCoursesByInstructor(instructorId);
                    request.setAttribute("courses", courses);
                    request.getRequestDispatcher("add-assignment.jsp").forward(request, response);
                }
            } else {
                request.setAttribute("error", "Invalid request format");
                request.getRequestDispatcher("add-assignment.jsp").forward(request, response);
            }
        } catch (FileUploadException e) {
            request.setAttribute("error", "File upload error: " + e.getMessage());
            request.getRequestDispatcher("add-assignment.jsp").forward(request, response);
        } catch (Exception e) {
            request.setAttribute("error", "Error creating assignment: " + e.getMessage());
            request.getRequestDispatcher("add-assignment.jsp").forward(request, response);
        }
    }
    
    // Update assignment
    private void updateAssignment(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        
        try {
            Assignment existingAssignment = null;
            String attachmentPath = null;
            int assignmentId = 0;
            int courseId = 0;
            String title = "";
            String description = "";
            Date dueDate = null;
            int maxScore = 0;
            
            // Check if this is a multipart request
            if (ServletFileUpload.isMultipartContent(request)) {
                ServletFileUpload upload = new ServletFileUpload(new DiskFileItemFactory());
                upload.setFileSizeMax(5 * 1024 * 1024); // 5MB max file size
                
                List<FileItem> items = upload.parseRequest(request);
                Map<String, String> params = new TreeMap<>();
                
                // Get existing assignment first
                for (FileItem item : items) {
                    if (item.isFormField() && "assignmentId".equals(item.getFieldName())) {
                        assignmentId = Integer.parseInt(item.getString());
                        break;
                    }
                }
                
                existingAssignment = assignmentDAO.getAssignmentById(assignmentId);
                
                // Create uploads directory if it doesn't exist
                String uploadDir = getServletContext().getRealPath("/uploads");
                File uploadDirectory = new File(uploadDir);
                if (!uploadDirectory.exists()) {
                    uploadDirectory.mkdirs();
                }
                
                // Process file items
                for (FileItem item : items) {
                    if (item.isFormField()) {
                        // Regular form field
                        params.put(item.getFieldName(), item.getString());
                    } else if (item.getName() != null && !item.getName().isEmpty()) {
                        // File upload - only if a new file is provided
                        String fileName = new File(item.getName()).getName();
                        String filePath = uploadDir + File.separator + System.currentTimeMillis() + "_" + fileName;
                        File uploadedFile = new File(filePath);
                        item.write(uploadedFile);
                        attachmentPath = "uploads/" + uploadedFile.getName();
                    }
                }
                
                // Use existing attachment if no new one provided
                if (attachmentPath == null) {
                    attachmentPath = existingAssignment.getAttachmentPath();
                }
                
                // Get updated parameters
                assignmentId = Integer.parseInt(params.get("assignmentId"));
                courseId = Integer.parseInt(params.get("course_id"));
                title = params.get("title");
                description = params.get("description");
                dueDate = Date.valueOf(params.get("due_date"));
                maxScore = Integer.parseInt(params.get("max_score"));
                
                // Create updated assignment
                Assignment assignment = new Assignment(assignmentId, courseId, title, description, dueDate, maxScore);
                assignment.setAttachmentPath(attachmentPath);
                
                boolean success = assignmentDAO.updateAssignment(assignment);
                
                if (success) {
                    response.sendRedirect("assignment?action=list&success=Assignment updated successfully");
                } else {
                    request.setAttribute("error", "Failed to update assignment");
                    request.setAttribute("assignment", assignment);
                    
                    Integer instructorId = (Integer) request.getSession().getAttribute("userId");
                    List<Course> courses = courseDAO.getCoursesByInstructor(instructorId);
                    request.setAttribute("courses", courses);
                    
                    request.getRequestDispatcher("edit-assignment.jsp").forward(request, response);
                }
            } else {
                request.setAttribute("error", "Invalid request format");
                request.getRequestDispatcher("edit-assignment.jsp").forward(request, response);
            }
        } catch (FileUploadException e) {
            request.setAttribute("error", "File upload error: " + e.getMessage());
            request.getRequestDispatcher("edit-assignment.jsp").forward(request, response);
        } catch (Exception e) {
            request.setAttribute("error", "Error updating assignment: " + e.getMessage());
            request.getRequestDispatcher("edit-assignment.jsp").forward(request, response);
        }
    }
    
    // Delete assignment
    private void deleteAssignment(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        
        int assignmentId = Integer.parseInt(request.getParameter("id"));
        boolean success = assignmentDAO.deleteAssignment(assignmentId);
        
        if (success) {
            response.sendRedirect("assignment?action=list&success=Assignment deleted successfully");
        } else {
            response.sendRedirect("assignment?action=list&error=Failed to delete assignment");
        }
    }
    
    // Show assignment submission form
    private void showSubmitForm(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        
        int assignmentId = Integer.parseInt(request.getParameter("id"));
        Assignment assignment = assignmentDAO.getAssignmentById(assignmentId);
        
        request.setAttribute("assignment", assignment);
        request.getRequestDispatcher("submit-assignment.jsp").forward(request, response);
    }
    
    // Submit assignment
    private void submitAssignment(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        
        try {
            HttpSession session = request.getSession(false);
            Integer studentId = (Integer) session.getAttribute("userId");
            
            String submissionFilePath = null;
            String submissionText = "";
            int assignmentId = 0;
            
            // Check if this is a multipart request
            if (ServletFileUpload.isMultipartContent(request)) {
                ServletFileUpload upload = new ServletFileUpload(new DiskFileItemFactory());
                upload.setFileSizeMax(10 * 1024 * 1024); // 10MB max file size
                
                List<FileItem> items = upload.parseRequest(request);
                Map<String, String> params = new TreeMap<>();
                
                // Create submissions directory if it doesn't exist
                String uploadDir = getServletContext().getRealPath("/submissions");
                File uploadDirectory = new File(uploadDir);
                if (!uploadDirectory.exists()) {
                    uploadDirectory.mkdirs();
                }
                
                // Process file items
                for (FileItem item : items) {
                    if (item.isFormField()) {
                        params.put(item.getFieldName(), item.getString());
                    } else if (item.getName() != null && !item.getName().isEmpty()) {
                        // File upload
                        String fileName = new File(item.getName()).getName();
                        String filePath = uploadDir + File.separator + System.currentTimeMillis() + "_" + fileName;
                        File uploadedFile = new File(filePath);
                        item.write(uploadedFile);
                        submissionFilePath = "submissions/" + uploadedFile.getName();
                    }
                }
                
                assignmentId = Integer.parseInt(params.get("assignmentId"));
                submissionText = params.getOrDefault("submissionText", "");
                
                // Check if already submitted
                if (submissionDAO.hasStudentSubmitted(assignmentId, studentId)) {
                    response.sendRedirect("assignment?action=my-assignments&error=You have already submitted this assignment");
                    return;
                }
                
                // Check if file was uploaded
                if (submissionFilePath == null) {
                    Assignment assignment = assignmentDAO.getAssignmentById(assignmentId);
                    request.setAttribute("assignment", assignment);
                    request.setAttribute("error", "Please upload a file for your submission");
                    request.getRequestDispatcher("submit-assignment.jsp").forward(request, response);
                    return;
                }
                
                AssignmentSubmission submission = new AssignmentSubmission(assignmentId, studentId, submissionText, submissionFilePath);
                boolean success = submissionDAO.addSubmission(submission);
                
                if (success) {
                    response.sendRedirect("assignment?action=my-assignments&success=Assignment submitted successfully!");
                } else {
                    response.sendRedirect("assignment?action=submit&id=" + assignmentId + "&error=Failed to submit assignment");
                }
            } else {
                response.sendRedirect("assignment?action=submit&error=Invalid request format");
            }
        } catch (FileUploadException e) {
            response.sendRedirect("assignment?action=my-assignments&error=File upload error: " + e.getMessage());
        } catch (Exception e) {
            response.sendRedirect("assignment?action=my-assignments&error=Error submitting assignment: " + e.getMessage());
        }
    }
    
    // View submissions for an assignment
    private void viewSubmissions(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        
        int assignmentId = Integer.parseInt(request.getParameter("id"));
        Assignment assignment = assignmentDAO.getAssignmentById(assignmentId);
        List<AssignmentSubmission> submissions = submissionDAO.getSubmissionsByAssignment(assignmentId);
        
        request.setAttribute("assignment", assignment);
        request.setAttribute("submissions", submissions);
        request.getRequestDispatcher("assignment-submissions.jsp").forward(request, response);
    }
    
    // Show grade form
    private void showGradeForm(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        
        int submissionId = Integer.parseInt(request.getParameter("id"));
        AssignmentSubmission submission = submissionDAO.getSubmissionById(submissionId);
        
        request.setAttribute("submission", submission);
        request.getRequestDispatcher("grade-submission.jsp").forward(request, response);
    }
    
    // Grade submission
    private void gradeSubmission(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        
        int submissionId = Integer.parseInt(request.getParameter("submissionId"));
        int assignmentId = Integer.parseInt(request.getParameter("assignmentId"));
        double grade = Double.parseDouble(request.getParameter("grade"));
        String feedback = request.getParameter("feedback");
        
        boolean success = submissionDAO.gradeSubmission(submissionId, grade, feedback);
        
        if (success) {
            response.sendRedirect("assignment?action=submissions&id=" + assignmentId + "&success=Submission graded successfully");
        } else {
            response.sendRedirect("assignment?action=grade&id=" + submissionId + "&error=Failed to grade submission");
        }
    }
}