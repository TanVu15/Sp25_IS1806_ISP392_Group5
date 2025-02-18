<%-- 
    Document   : AddCustomer
    Created on : Feb 8, 2025, 7:54:20 PM
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
        <title>Thêm Khách Hàng</title>
        <link rel="stylesheet" href="css/home2.css">
        <link rel="stylesheet" href="css/add.css">
        <link rel="stylesheet" href="css/product.css">
        <link rel="stylesheet" href="https://cdnjs.cloudflare.com/ajax/libs/font-awesome/6.7.2/css/all.min.css" integrity="sha512-Evv84Mr4kqVGRNSgIGL/F/aIDqQb7xQ2vcrdIwxfjThSH8CSR7PBEakCr51Ck+w+/U6swU2Im1vVX0SVk9ABhg==" crossorigin="anonymous" referrerpolicy="no-referrer" />
    </head>
    <body>
        
        <div class="container">
        <h2>Thêm khách hàng</h2>
        
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
                                DAOUser dao = new DAOUser();
                                Users u = (Users) request.getAttribute("user");
                                ArrayList<Customers> customers = (ArrayList<Customers>) request.getAttribute("customers");
                            %>
            <div class="form-group">
                <label for="name">Tên:</label>
                <input type="text" name="name" >
            </div>
            <div class="form-group">
                <label for="phone">Số điện thoại:</label>
                <input type="text" name="phone" >
            </div>
            <div class="form-group">
                <label for="address">Địa chỉ:</label>
                <input type="text" name="address" >
            </div>
            <div class="button-container">
                <input type="submit" class="btn add-button" value="Thêm Khách Hàng">
                <a href="listcustomers" class="btn cancel-button">Hủy</a>
            </div>
        </form>
    </div>
    </body>
</html>
