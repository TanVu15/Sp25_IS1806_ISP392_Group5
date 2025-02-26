<%@ page import="model.Shops" %>
<%@ page import="model.Users" %>
<%@page contentType="text/html" pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html lang="en">

<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title>Thông tin cửa hàng</title>
    <link rel="stylesheet" href="css/home2.css">
    <link rel="stylesheet" href="css/add.css">
    <link rel="stylesheet" href="css/product.css">
    <link rel="stylesheet" href="https://cdnjs.cloudflare.com/ajax/libs/font-awesome/6.7.2/css/all.min.css"
        integrity="sha512-Evv84Mr4kqVGRNSgIGL/F/aIDqQb7xQ2vcrdIwxfjThSH8CSR7PBEakCr51Ck+w+/U6swU2Im1vVX0SVk9ABhg=="
        crossorigin="anonymous" referrerpolicy="no-referrer" />
</head>

<body>

    <div class="container">
        <h2>Sửa cửa hàng</h2>

        <%
            Shops shop = (Shops) request.getAttribute("shop");
            Users user = (Users) request.getAttribute("user");
        %>

        <form action="updateshop" method="post" enctype="multipart/form-data">
            <div class="form-group">
                <label for="shopname">Tên cửa hàng:</label>
                <input type="text" id="shopname" name="shopname" value="<%= shop.getShopName() %>" required>
            </div>

            <div class="form-group">
                <label for="image">Logo hiện tại:</label>
                <img src="<%= shop.getLogoShop() %>" alt="<%= shop.getShopName() %>" width="150">
                <input type="file" id="logo" name="logo" accept="image/*">
            </div>

            <div class="form-group">
                <label for="location">Địa chỉ:</label>
                <input type="text" id="location" name="location" value="<%= shop.getLocation() %>" required>
            </div>

            <div class="form-group">
                <label for="email">Email:</label>
                <input type="text" id="email" name="email" value="<%= shop.getEmail() %>" required>
            </div>

            <div class="button-container">
                <input type="submit" class="btn add-button" value="Sửa">
                <a href="listproducts" class="btn cancel-button">Hủy</a>
            </div>
        </form>
    </div>

</body>

</html>
