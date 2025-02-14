<%@ page contentType="text/html" pageEncoding="UTF-8" %>
<%@ page import="model.Users" %>
<!DOCTYPE html>
<html lang="en">
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <link rel="stylesheet" href="css/add.css">
    <title>Thêm Sản Phẩm Mới</title>
    <link rel="stylesheet" href="css/product.css">
</head>
<body>
    <div class="container">
        <h2>Thêm Sản Phẩm Mới</h2>
        
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

        <form action="addproduct" method="post">
            <div class="form-group">
                <label for="productName">Tên sản phẩm:</label>
                <input type="text" id="productName" name="productName" required>
            </div>
            <div class="form-group">
                <label for="description">Mô tả:</label>
                <textarea id="description" name="description" required></textarea>
            </div>
            <div class="form-group">
                <label for="image">Hình ảnh:</label>
                <input type="text" id="image" name="image" placeholder="URL hình ảnh" required>
            </div>
            <div class="form-group">
                <label for="price">Giá tiền:</label>
                <input type="number" id="price" name="price" required>
            </div>
            <div class="form-group">
                <label for="quantity">Số lượng:</label>
                <input type="number" id="quantity" name="quantity" required>
            </div>
            <div class="form-group">
                <label for="location">Vị trí:</label>
                <input type="text" id="location" name="location" required>
            </div>
            <div class="form-group">
                <input type="submit" value="Thêm Sản Phẩm">
                <a href="listproducts" class="cancel-button">Hủy</a>
            </div>
        </form>
    </div>

</body>
</html>