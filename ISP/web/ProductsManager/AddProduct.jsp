<%@ page contentType="text/html;charset=UTF-8" %>
<%@ page import="model.Zones" %>
<%@ page import="java.util.List" %>
<%@ page import="model.Users" %>
<%@ page import="dal.DAOUser" %>
<%@ page import="model.Shops" %>
<%@ page import="dal.DAOShops" %>
<!DOCTYPE html>
<html lang="en">
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <link rel="stylesheet" href="css/add.css">
    <title>Thêm Sản Phẩm Mới</title>
    <link rel="stylesheet" href="css/product.css">
    <link href="https://cdnjs.cloudflare.com/ajax/libs/select2/4.0.13/css/select2.min.css" rel="stylesheet" />
    <script src="https://cdnjs.cloudflare.com/ajax/libs/jquery/3.6.0/jquery.min.js"></script>
    <script src="https://cdnjs.cloudflare.com/ajax/libs/select2/4.0.13/js/select2.min.js"></script>
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

        $(document).ready(function() {
            $('#zoneIDs').select2({
                placeholder: "Chọn khu vực",
                allowClear: true,
                tags: true
            });
        });
    </script>
</head>
<body>
    
        <%      DAOShops daoShop = new DAOShops();
                Shops shop = (Shops) session.getAttribute("shop");
                DAOUser dao = new DAOUser();
                Users u = (Users) request.getAttribute("user");
        %>
        
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
                <label for="zoneIDs">Khu vực:</label>
                <select id="zoneIDs" name="zoneIDs" multiple="multiple" required>
                    <%
                        List<Zones> zones = (List<Zones>) request.getAttribute("zones"); // Lấy danh sách khu vực từ request
                        if (zones != null) {
                            for (Zones zone : zones) {
                            if(zone.getShopID() == shop.getID()){
                    %>
                        <option value="<%= zone.getID() %>"><%= zone.getZoneName() %></option>
                    <%
                            }
                            }
                        }
                    %>
                </select>
            </div>
            <div class="button-container">
                <input type="submit" class="btn add-button" value="Thêm Sản Phẩm">
                <a href="listproducts" class="btn cancel-button">Hủy</a>
            </div>
        </form>
    </div>
</body>
</html>