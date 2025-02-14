<%-- 
    Document   : UpdateCustomer
    Created on : Feb 8, 2025, 1:04:52 AM
    Author     : ADMIN
--%>

<%@ page import="java.util.ArrayList" %>
<%@ page import="model.Customers" %>
<%@ page import="dal.DAOCustomers" %>
<%@page contentType="text/html" pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html>
    <h1>Edit Customer</h1>
    <form method="post">
        <h1>Update User Information</h1>

        <%
            Customers cus = (Customers) request.getAttribute("cus");
        %>


        <input type="hidden" name="id" value="<%= cus.getID() %>">

        <label for="name">Name:</label>
        <input type="text" id="name" name="name" value="<%= cus.getName() %>" readonly><br><br>

        <label for="phone">Phone:</label>
        <input type="number" id="phone" name="phone" value="<%= cus.getPhone() %> "required><br><br>

        <label for="address">Address:</label>
        <input type="text" id="address" name="address" value="<%= cus.getAddress() %> "required><br><br>

        <input type="submit" value="Update Customer">
    </form>
    <%
        String errorMessage = (String) request.getAttribute("errorMessage");
        if (errorMessage != null) {
    %>
    <p style="color:red;"><%= errorMessage %></p>
    <%
        }
    %>

    <button onclick="window.location.href = 'listcustomers'">Back to Customers List</button>
    <button onclick="location.href = '${pageContext.request.contextPath}/logout'">Logout</button>
</form>

</html>
