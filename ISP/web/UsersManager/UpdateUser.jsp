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
        <link rel="stylesheet" href="css/product.css">
        <link rel="stylesheet" href="css/update.css">
        <link rel="stylesheet" href="https://cdnjs.cloudflare.com/ajax/libs/font-awesome/6.7.2/css/all.min.css" integrity="sha512-Evv84Mr4kqVGRNSgIGL/F/aIDqQb7xQ2vcrdIwxfjThSH8CSR7PBEakCr51Ck+w+/U6swU2Im1vVX0SVk9ABhg==" crossorigin="anonymous" referrerpolicy="no-referrer" />
    
    </head>
    
    <body>
        
        <div class="container">
        <h2>Cập nhật Tài Khoản</h2>
        
        <% String message = (String) request.getAttribute("message"); %>
            <% if (message != null && !message.isEmpty()) { %>
            <div id="toast-message" class="toast-message"><%= message %></div>
            <% } %>

            <script>
                window.onload = function () {
                    var toast = document.getElementById("toast-message");
                    if (toast) {
                        toast.style.display = "block"; // Hiển thị thông báo
                        setTimeout(function () {
                            toast.style.opacity = "0";
                            setTimeout(() => toast.style.display = "none", 500);
                        }, 3000);
                    }
                };
            </script>

        <form method="post">
                            <%
                                Users user = (Users) request.getAttribute("user");
                                String username = (String) request.getAttribute("username"); 
                                String password = (String) request.getAttribute("password"); 
                                String password2 = (String) request.getAttribute("password2"); 
                                String fullname = (String) request.getAttribute("fullname"); 
                                if (username == null || password == null || fullname == null){
                                    username = "";
                                    password = "";
                                    password2 = "";
                                    fullname = "";
                                }
                            %>
            <div class="form-group">
                <label for="username">Tên tài khoản:</label>
                <input type="text" id="username" name="username" value="<%= user.getUsername() %>" readonly>
            </div>
            <div class="form-group">
                <label for="password">New Password:</label>
                <input type="password" id="password" name="password" value="<%=password %>"required>
            </div>
            <div class="form-group">
                <label for="password2">Nhập lại password:</label>
                <input type="password" id="password2" name="password2" value="<%=password2 %>"required>
            </div>
            <div class="form-group">
                <label for="name">Họ và Tên:</label>
                <input type="text" id="fullname" name="fullname" value="<%= fullname %>" required>
            </div>
            <div class="button-container">
                <input type="submit" class="btn btn-primary" value="Cập nhật tài khoản">
                <a href="listusers" class="btn btn-secondary">Hủy</a>
            </div>
        </form>
    </div>
    </body>
</html>
