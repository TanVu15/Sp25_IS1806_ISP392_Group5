<%@ page import="java.util.ArrayList" %>
<%@ page import="Model.Customers" %>
<%@ page import="Model.DebtRecords" %>
<%@ page import="Model.Users" %>
<%@ page import="DAL.DAOCustomers" %>
<%@ page import="DAL.DAOUser" %>
<%@page contentType="text/html" pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html>
    <head>
        <meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
        <title>Add Debt Records</title>
    </head>
    <body>
        <%  
                DAOCustomers dao1 = new DAOCustomers();
                Customers customer = (Customers) request.getAttribute("customer");
            %>
        <h2>Add Debt Records</h2>
        <form action="adddebtrecords" method="post">
            <table border="0">
                <tbody>
                    <tr>
                        <td>Name:</td>
                        <td> <%= customer.getName() %></td>
                    </tr>
                     <input type="hidden" name="customerid" value="<%= customer.getID() %>">
                    <tr>
                        <td>Amount Owed</td>
                        <td><input type="number" name="amountowed" required></td>
                    </tr>
                    <tr>
                        <td>Payment status:</td>
                        <td>
                            <select name="paymentstatus" required>
                                <option value="1">Trả Nợ</option>
                                <option value="-1">Vay Nợ</option>
                            </select>
                        </td>
                    </tr>
                    <tr>
                        <td>Note:</td>
                        <td><input type="text" name="note" required></td>
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
            <button onclick="location.href = 'listdebtrecords'">Back to Debt records List</button>
        </form>
    </body>
</html>
