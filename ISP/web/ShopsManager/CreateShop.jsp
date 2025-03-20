<%-- 
    Document   : CreateShop
    Created on : Feb 25, 2025, 1:47:41 PM
    Author     : Admin
--%>
<%@ page import="java.util.ArrayList" %>
<%@ page import="model.Products" %>
<%@ page import="model.Users" %>
<%@ page import="dal.DAOProducts" %>
<%@ page import="dal.DAOCustomers" %>
<%@ page import="dal.DAOShops" %>
<%@ page import="dal.DAOUser" %>
<%@page contentType="text/html" pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html lang="en">
    <head>
        <meta charset="UTF-8">
        <meta name="viewport" content="width=device-width, initial-scale=1.0">
        <title>Tạo Cửa Hàng</title>
        <link rel="stylesheet" href="css/home2.css">
        <link rel="stylesheet" href="css/add.css">
        <link rel="stylesheet" href="css/product.css">
        <link rel="stylesheet" href="https://cdnjs.cloudflare.com/ajax/libs/font-awesome/6.7.2/css/all.min.css" integrity="sha512-Evv84Mr4kqVGRNSgIGL/F/aIDqQb7xQ2vcrdIwxfjThSH8CSR7PBEakCr51Ck+w+/U6swU2Im1vVX0SVk9ABhg==" crossorigin="anonymous" referrerpolicy="no-referrer" />
    </head>
    <body>

        <div class="container">
            <h2>Tạo Cửa Hàng</h2>


            <form action="createshop" method="post" enctype="multipart/form-data">
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
                <div class="form-group">
                    <label for="productName">Tên cửa hàng:</label>
                    <input type="text" id="shopname" name="shopname" required>
                </div>
                <div class="form-group">
                    <label for="image">Logo:</label>
                    <input type="file" id="logo" name="logo" accept="image/*">
                </div>
                <div class="form-group">
                    <label for="description">Số điện thoại:</label>
                    <input type="text" id="phone" name="phone" >
                </div>
                <div class="form-group">
                    <label for="description">Tài khoản ngân hàng:</label>
                    <input type="text" id="bankacc" name="bankacc" >
                </div>
                <div class="form-group">
                    <label for="description">Địa chỉ:</label>
                    <input type="text" id="location" name="location" required>
                </div>
                <div class="form-group">
                    <label for="price">Email:</label>
                    <input type="text" id="email" name="email" required>
                </div>
                <div class="button-container">
                    <input type="submit" class="btn add-button" value="Tạo Cửa Hàng">
                </div>
            </form>
        </div>
    </body>
</html>

