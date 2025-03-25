<%--  
    Document   : UpdateZones  
    Created on : Feb 18, 2025, 5:14:07 PM  
    Author     : ASUS  
--%>  

<%@ page import="model.Zones" %>  
<%@ page import="dal.DAOZones" %>  
<%@ page import="model.Shops" %>
<%@ page import="dal.DAOShops" %>
<%@page contentType="text/html" pageEncoding="UTF-8"%>  

<!DOCTYPE html>  
<html lang="en">  
<head>  
    <meta charset="UTF-8">  
    <meta name="viewport" content="width=device-width, initial-scale=1.0">  
    <link rel="stylesheet" href="css/add.css">  
    <title>Cập Nhật Kho</title>  
    <link rel="stylesheet" href="css/product.css">  
</head>  

<body>  
    <div class="container">  
        <h2>Cập Nhật Kho</h2>  

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

        <form action="updatezone" method="post">  
            <%  
                Zones z = (Zones) request.getAttribute("z");  
                Shops shop = (Shops) session.getAttribute("shop");
            %>  
            <div class="form-group">  
                <input type="hidden" name="id" value="<%= z.getID() %>">  
                <label for="zone">Khu vực:</label>  
                <input type="text" id="zone" name="zone" value="<%= z.getZoneName() %>" required>
                
                <label for="zone">Shop:</label>  
                <input type="hidden" id="shop" name="shop" value="<%= z.getShopID() %>" required>
                <span class="input-info" ><%= shop.getShopName() %></span>
            </div>  
            <div class="button-container">  
                <input type="submit" class="btn add-button" value="Cập nhật">  
                <a href="listzones" class="btn cancel-button">Hủy</a>  
            </div>  
        </form>  
    </div>  
</body>  
</html>  
