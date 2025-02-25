<%-- 
    Document   : ShopDetail
    Created on : Feb 21, 2025, 4:48:48 PM
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
        <title>Quản Lý Cửa Hàng</title>
        <link rel="stylesheet" href="css/product.css">
        <link rel="stylesheet" href="https://cdnjs.cloudflare.com/ajax/libs/font-awesome/6.7.2/css/all.min.css"
              integrity="sha512-Evv84Mr4kqVGRNSgIGL/F/aIDqQb7xQ2vcrdIwxfjThSH8CSR7PBEakCr51Ck+w+/U6swU2Im1vVX0SVk9ABhg=="
              crossorigin="anonymous" referrerpolicy="no-referrer" />
    </head>

    <body>

        <div class="container">
            <h2>Cửa Hàng</h2>

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


            <%
                DAOUser dao = new DAOUser();
                Users u = (Users) request.getAttribute("user");
                Shops shop = (Shops) request.getAttribute("shop");
            %>
            <div class="form-group">
                <h3>Tên cửa hàng:</h3>
                <%= shop.getShopName()%>
            </div>
            <div class="">
                <h3>Logo:</h3>
                <img src="<%= shop.getLogoShop()%>" alt="<%= shop.getShopName()%>">
            </div>
            <div class="form-group">
                <h3>Địa chỉ:</h3>
                <%= shop.getLocation()%>
            </div>
            <div class="form-group">
                <h3>Email:</h3>
                <%= shop.getEmail()%>
            </div>

        </div>
    </body>

</html>
