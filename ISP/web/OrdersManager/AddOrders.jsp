<%@page contentType="text/html" pageEncoding="UTF-8"%>
<%@ page import="model.Orders" %>
<!DOCTYPE html>
<html lang="en">
    <head>
        <meta charset="UTF-8">
        <meta name="viewport" content="width=device-width, initial-scale=1.0">
        <link rel="stylesheet" href="css/add.css">
        <title>Thêm Đơn hàng Mới</title>
        <link rel="stylesheet" href="css/product.css">
        <script>
            function validateForm() {
                var amount = document.getElementById("amount").value;
                var errorMessage = "";

                if (amount < 0) {
                    errorMessage = "Giá trị không thể âm.";
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
            <h2>Thêm Đơn hàng Mới</h2>

            <div id="error-message" class="error-message">
                <%= request.getAttribute("errorMessage") != null ? request.getAttribute("errorMessage") : "" %>
            </div>

            <form action="addorder" method="post" enctype="multipart/form-data" onsubmit="return validateForm();">
                <div class="form-group">
                    <label for="CustomerID">Mã khách hàng: </label>
                    <input type="text" id="CustomerID" name="CustomerID" required>
                </div>
                <div class="form-group">
                    <label for="OrderItemID">Mã Sản phẩm: </label>
                    <input type="text" id="OrderItemID" name="OrderItemID" required>
                </div>
                <div class="form-group">
                    <label for="ShopID">Mã cửa hàng: </label>
                    <input type="text" id="ShopID" name="ShopID" required>
                </div>
                <div class="form-group">
                    <label for="amount">Giá trị đơn hàng: </label>
                    <input type="number" id="amount" name="amount" required>
                </div>
                <div class="form-group">
                    <label for="status">Tình trạng đơn hàng: </label><br>
                    <input type="radio" id="active" name="status" value="-1" required>
                    <label for="active">Bán</label><br>
                    <input type="radio" id="inactive" name="status" value="1" required>
                    <label for="inactive">nhập</label>
                </div>
                <div class="button-container">
                    <input type="submit" class="btn add-button" value="Thêm Đơn hàng">
                    <a href="listorders" class="btn cancel-button">Hủy</a>
                </div>
            </form>
        </div>
    </body>
</html>
