<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ page import="model.Products" %>
<!DOCTYPE html>
<html lang="en">
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title>Cập Nhật Sản Phẩm</title>
    <link rel="stylesheet" href="css/update.css"> 
    <link rel="stylesheet" href="https://cdnjs.cloudflare.com/ajax/libs/font-awesome/6.7.2/css/all.min.css" 
          integrity="sha512-Evv84Mr4kqVGRNSgIGL/F/aIDqQb7xQ2vcrdIwxfjThSH8CSR7PBEakCr51Ck+w+/U6swU2Im1vVX0SVk9ABhg==" 
          crossorigin="anonymous" referrerpolicy="no-referrer" />
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
    <h2>Cập Nhập Sản Phẩm</h2>

    <div id="error-message" class="alert alert-danger">
        <%= request.getAttribute("errorMessage") != null ? request.getAttribute("errorMessage") : "" %>
    </div>

    <form action="updateproduct" method="post" enctype="multipart/form-data" onsubmit="return validateForm();">
        <input type="hidden" name="id" value="${product.ID}"/>
        <input type="hidden" name="currentImageLink" value="${product.imageLink}"/> <!-- Giữ ảnh cũ -->

        <div class="form-group">
            <label for="productName">Tên sản phẩm:</label>
            <input type="text" id="productName" name="productName" class="form-control" 
                   value="${product.productName}" required>
        </div>

        <div class="form-group">
            <label for="description">Mô tả:</label>
            <textarea id="description" name="description" class="form-control" required>${product.description}</textarea>
        </div>

        <div class="form-group">
            <label for="image">Hình ảnh:</label>
            <input type="file" id="image" name="image" class="form-control" accept="image/*">
            <p>Ảnh hiện tại: <img src="${product.imageLink}" alt="Product Image" style="max-width:100px;"/></p>
        </div>

        <div class="form-group">
            <label for="price">Giá tiền:</label>
            <input type="number" id="price" name="price" class="form-control" 
                   value="${product.price}" required>
        </div>

        <div class="form-group">
            <label for="quantity">Số lượng:</label>
            <input type="number" id="quantity" name="quantity" class="form-control" 
                   value="${product.quantity}" readonly>
        </div>

        <div class="form-group">
            <label for="location">Vị trí:</label>
            <input type="text" id="location" name="location" class="form-control" 
                   value="${product.location}" >
        </div>

        <button type="submit" class="btn btn-primary">Cập Nhập</button>
        <a href="listproducts" class="btn btn-secondary">Hủy</a>
    </form>
</div>

<script src="path/to/bootstrap.js"></script> <!-- Optional: Add your JS file path -->
</body>
</html>