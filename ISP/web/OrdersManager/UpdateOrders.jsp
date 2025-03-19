<%@page contentType="text/html" pageEncoding="UTF-8"%>
<%@ page import="model.Orders" %>
<!DOCTYPE html>
<html>
    <head>
        <meta charset="UTF-8">
        <meta name="viewport" content="width=device-width, initial-scale=1.0">
        <title>Cập Nhật Đơn hàng</title>
        <link rel="stylesheet" href="css/update.css"> 
        <link rel="stylesheet" href="https://cdnjs.cloudflare.com/ajax/libs/font-awesome/6.7.2/css/all.min.css" 
              integrity="sha512-Evv84Mr4kqVGRNSgIGL/F/aIDqQb7xQ2vcrdIwxfjThSH8CSR7PBEakCr51Ck+w+/U6swU2Im1vVX0SVk9ABhg==" 
              crossorigin="anonymous" referrerpolicy="no-referrer" />
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
            <h2>Cập Nhập Đơn hàng</h2>

            <div id="error-message" class="alert alert-danger">
                <%= request.getAttribute("errorMessage") != null ? request.getAttribute("errorMessage") : "" %>
            </div>

            <form action="updateorder" method="post" enctype="multipart/form-data" onsubmit="return validateForm();">
                <%  
                    Orders order = (Orders) request.getAttribute("order");  
                %> 
                <div class="form-group">
                    <label for="CustomerID">Mã khách hàng: </label>
                    <input type="text" id="CustomerID" name="CustomerID" class="form-control" 
                           value="<%= order.getCustomerID() %>" required>
                </div>

                <div class="form-group">
                    <label for="UserID">Mã nhân viên: </label>
                    <input type="text" id="UserID" name="UserID" class="form-control" 
                           value="<%= order.getUserID() %>" required>
                </div>
                
                <div class="form-group">
                    <label for="ShopID">Mã cửa hàng: </label>
                    <input type="text" id="ShopID" name="ShopID" class="form-control" 
                           value="<%= order.getShopID() %>" required>
                </div>

                <div class="form-group">
                    <label for="amount">Giá trị đơn hàng: </label>
                    <input type="number" id="amount" name="amount" class="form-control" 
                           value="<%= order.getTotalAmount() %>" required>
                </div>

                <div class="form-group">
                    <label for="status">Tình trạng đơn hàng: </label><br>
                    <input type="radio" id="active" name="status" value="-1" required>
                    <label for="active">Bán</label><br>
                    <input type="radio" id="inactive" name="status" value="1" required>
                    <label for="inactive">nhập</label>
                </div>
                
                <input type="hidden" name="id" value="<%= order.getID() %>"> 
                <button type="submit" class="btn btn-primary">Cập Nhập</button>
                <a href="listorders" class="btn btn-secondary">Hủy</a>
            </form>
        </div>

        <script src="path/to/bootstrap.js"></script> 
    </body>
</html>
