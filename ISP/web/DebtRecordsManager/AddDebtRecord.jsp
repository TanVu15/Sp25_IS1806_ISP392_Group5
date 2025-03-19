<%@ page import="java.util.ArrayList" %>
<%@ page import="model.Customers" %>
<%@ page import="model.DebtRecords" %>
<%@ page import="model.Users" %>
<%@ page import="dal.DAOCustomers" %>
<%@ page import="dal.DAOUser" %>
<%@page contentType="text/html" pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html lang="en">

    <head>
        <meta charset="UTF-8">
        <meta name="viewport" content="width=device-width, initial-scale=1.0">
        <title>Thêm Công Nợ</title>
        <link rel="stylesheet" href="css/add.css">
        <link rel="stylesheet" href="css/product.css">
        <link rel="stylesheet" href="https://cdnjs.cloudflare.com/ajax/libs/font-awesome/6.7.2/css/all.min.css" integrity="sha512-Evv84Mr4kqVGRNSgIGL/F/aIDqQb7xQ2vcrdIwxfjThSH8CSR7PBEakCr51Ck+w+/U6swU2Im1vVX0SVk9ABhg==" crossorigin="anonymous" referrerpolicy="no-referrer" />
    </head>

    <body>
        <%  
            DAOUser dao = new DAOUser();
            DAOCustomers dao1 = new DAOCustomers();
            Users u = (Users) request.getAttribute("user");
            Customers customer = (Customers) request.getAttribute("customer");
            ArrayList<DebtRecords> debtrecords = (ArrayList<DebtRecords>) request.getAttribute("debtrecords");
        %>
        <div class="container">
            <h2>Thêm Công Nợ</h2>

            <% String message = (String) request.getAttribute("message"); %>
            <% if (message != null && !message.isEmpty()) { %>
            <div id="toast-message" class="toast-message"><%= message %></div>
            <% } %>

            <script>
                window.onload = function () {
                    var toast = document.getElementById("toast-message");
                    if (toast) {
                        toast.style.display = "block"; // Hiển thị thông báo
                        setTimeout(function () {
                            toast.style.opacity = "0";
                            setTimeout(() => toast.style.display = "none", 500);
                        }, 3000);
                    }
                };
            </script>

            <form action="adddebtrecords" method="post" enctype="multipart/form-data">

                <div class="form-group">
                    <label for="productName">Tên:</label>
                    <%= customer.getName() %>
                </div>
                <input type="hidden" name="customerid" value="<%= customer.getID() %>">
                <div class="form-group">
                    <label for="description">Số Tiền:</label>
                    <input type="number" name="amountowed" required>
                </div>
                <div class="form-group">
                    <label for="paymentstatus">Trạng Thái:</label>
                    <select name="paymentstatus" required>
                        <option value="1">Khách trả</option>
                        <option value="-1">Khách Vay </option>
                        <option value="2">Cửa hàng vay</option>
                        <option value="-2">Cửa hàng trả</option>
                    </select>
                </div>
                <div class="form-group">
                    <label for="price">Ngày Lập Phiếu:</label>
                    <input type="date" name="invoicedate" required>
                </div>
                <div class="form-group">
                    <label for="image">Hình Ảnh:</label>
<!--                    <input type="text" name="image">-->
                     <input type="file" id="image" name="image" accept="image/*"> 
                </div>
                <div class="form-group">
                    <label for="location">Ghi Chú:</label>
                    <input type="text" name="note" >
                </div>
                <div class="button-container">
                    <input type="submit" class="btn add-button" value="Thêm Công Nợ">
                    <a class="btn cancel-button" href="listcustomerdebtrecords?customerid=<%= customer.getID() %>" class="btn cancel-button">Hủy</a>
                </div>
            </form>
        </div>
    </body>
</html>
