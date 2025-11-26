<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html>
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title>Login - LMS</title>
    <link rel="stylesheet" href="css/style.css">
</head>
<body class="login-page">
    <div class="login-container">
        <h2>Learning Management System</h2>
        <p class="subtitle">Enter your credentials to login</p>
        
        <% if(request.getAttribute("error") != null) { %>
            <div class="error">
                <%= request.getAttribute("error") %>
            </div>
        <% } %>
        
        <form action="login" method="post">
            <div class="form-group">
                <label for="email">Email Address</label>
                <input type="email" id="email" name="email" placeholder="Enter your email" required>
            </div>
            
            <div class="form-group">
                <label for="password">Password</label>
                <input type="password" id="password" name="password" placeholder="Enter your password" required>
            </div>
            
            <button type="submit" class="btn">Login</button>
        </form>
        
        <div class="info">
            <h4>📋 Demo Credentials:</h4>
            <p><strong>Admin:</strong> admin@lms.com / admin123</p>
            <p><strong>Instructor:</strong> john@lms.com / instructor123</p>
            <p><strong>Student:</strong> jane@lms.com / student123</p>
        </div>
    </div>
</body>
</html>