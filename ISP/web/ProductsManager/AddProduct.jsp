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
    <script>
        function validateForm() {
            var price = document.getElementById("price").value;
            var errorMessage = "";

            if (price < 0) {
                errorMessage = "Giá không thể âm.";
            }

            if (errorMessage !== "") {
                document.getElementById("error-message").innerText = errorMessage;
                return false; // Prevent form submission
            }
            return true; // Allow form submission
        }
    </script>
</head>
<body>
    <div class="container">
        <h2>Thêm Sản Phẩm Mới</h2>
        
        <div id="error-message" class="error-message">
            <%= request.getAttribute("errorMessage") != null ? request.getAttribute("errorMessage") : "" %>
        </div>

        <form action="addproduct" method="post" enctype="multipart/form-data" onsubmit="return validateForm();">
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
                <input type="file" id="image" name="image" accept="image/*" required>
            </div>
            <div class="form-group">
                <label for="price">Giá tiền:</label>
                <input type="number" id="price" name="price" required>
            </div>
            <div class="form-group">
                <label for="quantity">Số lượng:</label>
                <input type="number" id="quantity" name="quantity" value="0" required readonly>
            </div>
            <div class="form-group">
                <label for="location">Vị trí:</label>
                <input type="text" id="location" name="location" required>
            </div>
            <div class="button-container">
                <input type="submit" class="btn add-button" value="Thêm Sản Phẩm">
                <a href="listproducts" class="btn cancel-button">Hủy</a>
            </div>
        </form>
    </div>
</body>
</html>