<%-- 
    Document   : register
    Created on : Feb 3, 2025, 9:34:47 PM
    Author     : ADMIN
--%>
<%@ page import="java.util.ArrayList" %>
<%@ page import="model.Customers" %>
<%@ page import="model.DebtRecords" %>
<%@ page import="model.Users" %>
<%@ page import="dal.DAOCustomers" %>
<%@ page import="dal.DAOUser" %>
<%@page contentType="text/html" pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html lang="en">
    <head>
        <meta charset="UTF-8">
        <meta name="viewport" content="width=device-width, initial-scale=1.0">
        <title>Thêm Tài Khoản</title>
        <link rel="stylesheet" href="css/add.css">
        <link rel="stylesheet" href="css/product.css">
        <link rel="stylesheet" href="https://cdnjs.cloudflare.com/ajax/libs/font-awesome/6.7.2/css/all.min.css" integrity="sha512-Evv84Mr4kqVGRNSgIGL/F/aIDqQb7xQ2vcrdIwxfjThSH8CSR7PBEakCr51Ck+w+/U6swU2Im1vVX0SVk9ABhg==" crossorigin="anonymous" referrerpolicy="no-referrer" />
    </head>


    <body>
        
        <div class="container">
        <h2>Thêm tài khoản</h2>
        
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

        <form action="register" method="post">
            <div class="form-group">
                <label for="name">Tên tài khoản:</label>
                <input type="text" name="name" required>
            </div>
            <div class="form-group">
                <label for="password">Password:</label>
                <input type="password" name="password" required>
            </div>
            <div class="form-group">
                <label for="password2">Nhập lại password:</label>
                <input type="password" name="password2" required>
            </div>
            <div class="button-container">
                <input type="submit" class="btn add-button" value="Đăng Kí">
                <a href="listusers" class="btn cancel-button">Hủy</a>
            </div>
        </form>
    </div>
    </body>
</html>
