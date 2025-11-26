package com.lms.servlet;

import com.lms.dao.UserDAO;
import com.lms.model.User;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.*;
import java.io.IOException;
import java.util.List;

/**
 * User Management Servlet
 * File: src/com/lms/servlet/UserServlet.java
 * Handles CRUD operations for users (Admin functionality)
 */
@WebServlet("/user")
public class UserServlet extends HttpServlet {
    private UserDAO userDAO;
    
    @Override
    public void init() throws ServletException {
        userDAO = new UserDAO();
    }
    
    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        // Only allow admins to access user management
        if (!enforceAdmin(request, response)) {
            return;
        }
        
        String action = request.getParameter("action");
        
        if (action == null) {
            action = "list";
        }
        
        switch (action) {
            case "list":
                listUsers(request, response);
                break;
            case "add":
                showAddForm(request, response);
                break;
            case "edit":
                showEditForm(request, response);
                break;
            case "delete":
                deleteUser(request, response);
                break;
            default:
                listUsers(request, response);
        }
    }
    
    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        // Only allow admins to perform create/update actions
        if (!enforceAdmin(request, response)) {
            return;
        }
        
        String action = request.getParameter("action");
        
        if ("add".equals(action)) {
            addUser(request, response);
        } else if ("edit".equals(action)) {
            updateUser(request, response);
        }
    }
    
    // List all users
    private void listUsers(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        List<User> users = userDAO.getAllUsers();
        request.setAttribute("users", users);
        request.getRequestDispatcher("user-list.jsp").forward(request, response);
    }
    
    // Show add user form
    private void showAddForm(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        request.getRequestDispatcher("add-user.jsp").forward(request, response);
    }
    
    // Show edit user form
    private void showEditForm(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        int userId = Integer.parseInt(request.getParameter("id"));
        User user = userDAO.getUserById(userId);
        request.setAttribute("user", user);
        request.getRequestDispatcher("edit-user.jsp").forward(request, response);
    }
    
    // Add new user
    private void addUser(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        
        String name = request.getParameter("name");
        String email = request.getParameter("email");
        String password = request.getParameter("password");
        String role = request.getParameter("role");
        
        // Validate input
        if (name == null || name.trim().isEmpty() || 
            email == null || email.trim().isEmpty() ||
            password == null || password.trim().isEmpty() ||
            role == null || role.trim().isEmpty()) {
            request.setAttribute("error", "All fields are required");
            request.getRequestDispatcher("add-user.jsp").forward(request, response);
            return;
        }
        
        // Check if email already exists
        if (userDAO.emailExists(email)) {
            request.setAttribute("error", "Email already exists");
            request.getRequestDispatcher("add-user.jsp").forward(request, response);
            return;
        }
        
        User user = new User(name, email, password, role);
        boolean success = userDAO.addUser(user);
        
        if (success) {
            response.sendRedirect("user?action=list&success=User added successfully");
        } else {
            request.setAttribute("error", "Failed to add user");
            request.getRequestDispatcher("add-user.jsp").forward(request, response);
        }
    }
    
    // Update user
    private void updateUser(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        
        int userId = Integer.parseInt(request.getParameter("userId"));
        String name = request.getParameter("name");
        String email = request.getParameter("email");
        String role = request.getParameter("role");
        
        // Validate input
        if (name == null || name.trim().isEmpty() || 
            email == null || email.trim().isEmpty() ||
            role == null || role.trim().isEmpty()) {
            request.setAttribute("error", "All fields are required");
            User user = userDAO.getUserById(userId);
            request.setAttribute("user", user);
            request.getRequestDispatcher("edit-user.jsp").forward(request, response);
            return;
        }
        
        User user = new User(userId, name, email, role);
        boolean success = userDAO.updateUser(user);
        
        if (success) {
            response.sendRedirect("user?action=list&success=User updated successfully");
        } else {
            request.setAttribute("error", "Failed to update user");
            request.setAttribute("user", user);
            request.getRequestDispatcher("edit-user.jsp").forward(request, response);
        }
    }
    
    // Delete user
    private void deleteUser(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        
        int userId = Integer.parseInt(request.getParameter("id"));
        boolean success = userDAO.deleteUser(userId);
        
        if (success) {
            response.sendRedirect("user?action=list&success=User deleted successfully");
        } else {
            response.sendRedirect("user?action=list&error=Failed to delete user");
        }
    }

    // Helper to check if the current user is admin; returns true if admin
    private boolean isAdmin(HttpServletRequest request) {
        HttpSession session = request.getSession(false);
        if (session == null) return false;
        Object roleObj = session.getAttribute("userRole");
        if (roleObj == null) return false;
        return "admin".equalsIgnoreCase(roleObj.toString());
    }

    // Enforce admin and redirect to login if not
    private boolean enforceAdmin(HttpServletRequest request, HttpServletResponse response)
            throws IOException {
        if (!isAdmin(request)) {
            // Redirect to login with an error message
            response.sendRedirect("login.jsp?error=Access+denied");
            return false;
        }
        return true;
    }
}