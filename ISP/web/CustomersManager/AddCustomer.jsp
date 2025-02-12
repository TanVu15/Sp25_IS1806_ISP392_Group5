<%-- 
    Document   : AddCustomer
    Created on : Feb 8, 2025, 7:54:20 PM
    Author     : ADMIN
--%>

<%@page contentType="text/html" pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html>
    <head>
        <meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
        <title>Add Customers</title>
    </head>
    <body>
        <h2>Add Customers</h2>
        <form action="addcustomer" method="post">
            <table border="0">
                <tbody>
                    <tr>
                        <td>Name</td>
                        <td> <input type="text" name="name" required></td>
                    </tr>
                    <tr>
                        <td>Phone:</td>
                        <td><input type="text" name="phone" required></td>
                    </tr>
                    <tr>
                        <td>Address:</td>
                        <td><input type="text" name="address" required></td>
                    </tr>
                </tbody>
            </table>
            <input type="submit" value="Add">
            <%
            String errorMessage = (String) request.getAttribute("errorMessage");
            if (errorMessage != null) {
            %>
            <p style="color:red;"><%= errorMessage %></p>
            <%
                }
            %>
            <button onclick="location.href = 'listcustomers'">Back to Customer List</button>
        </form>
    </body>
</html>
