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
<%@ page import="model.Shops" %>
<%@page contentType="text/html" pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html lang="en">

    <head>
        <meta charset="UTF-8">
        <meta name="viewport" content="width=device-width, initial-scale=1.0">
        <title>Quản Lý Cửa Hàng</title>
    </head>

    <body>

        <%
    Shops shop = (Shops) request.getAttribute("shop");
    Users user = (Users) request.getAttribute("user");
    if (shop == null) {
%>
        <h3 style="color: red;">Lỗi: Không có dữ liệu cửa hàng!</h3>
<%
    } else {
%>
        <div class="form-group">
            <h3>Tên cửa hàng:</h3>
            <%= shop.getShopName() %>
        </div>
        <div class="">
            <h3>Logo:</h3>
            <img src="<%= shop.getLogoShop() %>" alt="<%= shop.getShopName() %>" width="150">
        </div>
        <div class="form-group">
            <h3>Địa chỉ:</h3>
            <%= shop.getLocation() %>
        </div>
        <div class="form-group">
            <h3>Email:</h3>
            <%= shop.getEmail() %>
        </div>
<%
    }
%>
<%
    if(user.getRoleid()!=3){
%>
<a href="updateshop">updateshop</a>
<%
    }
%>
    </body>

</html>
