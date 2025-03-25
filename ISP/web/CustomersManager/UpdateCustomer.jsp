<%-- 
    Document   : UpdateCustomer
    Created on : Feb 8, 2025, 1:04:52 AM
    Author     : ADMIN
--%>

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
        <title>Cập Nhật Khách Hàng</title>
        <link rel="stylesheet" href="css/update.css">
        <link rel="stylesheet" href="https://cdnjs.cloudflare.com/ajax/libs/font-awesome/6.7.2/css/all.min.css" integrity="sha512-Evv84Mr4kqVGRNSgIGL/F/aIDqQb7xQ2vcrdIwxfjThSH8CSR7PBEakCr51Ck+w+/U6swU2Im1vVX0SVk9ABhg==" crossorigin="anonymous" referrerpolicy="no-referrer" />
    </head>
    
    <body>
        
        <div class="container">
        <h2>Cập nhật khách hàng</h2>
        
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

        <form method="post">
                            <%
                                Customers cus = (Customers) request.getAttribute("cus");
                            %>
            <div class="form-group">
                <label for="name">Tên:</label>
                <input type="text" id="name" name="name" value="<%= cus.getName() %>">
            </div>
            <div class="form-group">
                <input type="hidden" name="id" value="<%= cus.getID() %>">
            </div>
            <div class="form-group">
                <label for="phone">Số điện thoại:</label>
                <input type="text" id="phone" name="phone" value="<%= cus.getPhone() %> ">
            </div>
            <div class="form-group">
                <label for="address">Địa chỉ:</label>
                <input type="text" id="address" name="address" value="<%= cus.getAddress() %> ">
            </div>
            <div class="form-group">
                <label for="bank">Ngân hàng:</label>
                <input type="text" id="bank" name="bank" value="<%= cus.getBankAcc() %> ">
            </div>
            <div class="button-container">
                <input type="submit" class="btn btn-primary" value="Cập nhật">
                <a href="listcustomers" class="btn btn-secondary">Hủy</a>
            </div>
        </form>
    </div>
    </body>
</html>
