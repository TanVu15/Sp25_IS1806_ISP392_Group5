<%-- 
    Document   : AddZones
    Created on : Feb 18, 2025, 5:13:30 PM
    Author     : ASUS
--%>

<%@ page import="java.util.ArrayList" %>
<%@ page import="model.Zones" %>
<%@ page import="model.Users" %>
<%@ page import="dal.DAOZones" %>
<%@ page import="dal.DAOUser" %>
<%@ page import="model.Shops" %>
<%@ page import="dal.DAOShops" %>
<%@page contentType="text/html" pageEncoding="UTF-8"%>

<!DOCTYPE html>
<html lang="en">
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <link rel="stylesheet" href="css/add.css">
    <title>Thêm kho</title>
    <link rel="stylesheet" href="css/product.css">
</head>
<body>
    <div class="container">
        <h2>Thêm khu vực mới</h2>
                            <% 
                            DAOUser dao = new DAOUser();
                            DAOShops daoShop = new DAOShops();
                            Shops shop = (Shops) session.getAttribute("shop");
                            Users u = (Users) request.getAttribute("user");
                            ArrayList<Zones> zones = (ArrayList<Zones>) request.getAttribute("zones");
                            %>
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

        <form action="addzone" method="post">
            <div class="form-group">
                <label for="zone">Kho:</label>
                <input type="text" id="zone" name="zone" required>
                
                <label for="zone">Shop Name:</label>
                <input type="text" id="shop" name="shop" value="<%= shop.getShopName() %>" required>
            </div>
            
            <div class="button-container">
                <input type="submit" class="btn add-button" value="Thêm">
                <a href="listzones" class="btn cancel-button">Hủy</a>
            </div>
        </form>
    </div>

</body>
</html>