<%-- 
    Document   : UdateUser
    Created on : Feb 6, 2025, 4:52:02 PM
    Author     : Admin
--%>
<%@ page import="java.util.ArrayList" %>
<%@ page import="model.Users" %>
<%@ page import="dal.DAOUser" %>
<%@page contentType="text/html" pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html lang="en">
    <head>
        <meta charset="UTF-8">
        <meta name="viewport" content="width=device-width, initial-scale=1.0">
        <title>Cập nhật Tài Khoản</title>
        <link rel="stylesheet" href="css/update.css">
        <link rel="stylesheet" href="https://cdnjs.cloudflare.com/ajax/libs/font-awesome/6.7.2/css/all.min.css" integrity="sha512-Evv84Mr4kqVGRNSgIGL/F/aIDqQb7xQ2vcrdIwxfjThSH8CSR7PBEakCr51Ck+w+/U6swU2Im1vVX0SVk9ABhg==" crossorigin="anonymous" referrerpolicy="no-referrer" />
    
    </head>
    
    <body>
        
        <div class="container">
        <h2>Cập nhật Tài Khoản</h2>
        
        <%
            String errorMessage = (String) request.getAttribute("errorMessage");
            if (errorMessage != null) {
        %>
        <div class="error-message">
            <%= errorMessage %>
        </div>
        <%
            }
        %>

        <form method="post">
                            <%
                                Users user = (Users) request.getAttribute("user");
                            %>
            <div class="form-group">
                <label for="username">Tên tài khoản:</label>
                <input type="text" id="username" name="username" value="<%= user.getUsername() %>" readonly>
            </div>
            <div class="form-group">
                <label for="password">Password:</label>
                <input type="password" id="password" name="password" value="<%= user.getPasswordHash() %> "required>
            </div>
            <div class="form-group">
                <label for="password2">Nhập lại password:</label>
                <input type="password" id="password2" name="password2" required>
            </div>
            <div class="form-group">
                <label for="name">Họ và Tên:</label>
                <input type="text" id="fullname" name="fullname" value="<%= user.getFullName() %> " required>
            </div>
            <div class="button-container">
                <input type="submit" class="btn btn-primary" value="Cập nhật tài khoản">
                <a href="listusers" class="btn btn-secondary">Hủy</a>
            </div>
        </form>
    </div>
    </body>
</html>
