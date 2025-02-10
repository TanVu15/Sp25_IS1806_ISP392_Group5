<%-- 
    Document   : ListDebtRecords
    Created on : Feb 9, 2025, 10:43:08 PM
    Author     : ADMIN
--%>

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
        <title>Manage Debt Records</title>
    </head>
    <body>
        <h1>List of Debt Records</h1>
        <table border="1">
            <tr>
                <th>ID</th>
                <th>Customer </th>
                <th>Amount Owed </th>
                <th>Payment Status</th>
                <th>create at</th>
                <th>update at</th>
                <th>create by</th>
                <th>is delete </th>
                <th>delete at </th>
                <th>delete by</th>
                <th>action </th>
            </tr>
            <%  
                DAOUser dao = new DAOUser();
                DAOCustomers dao1 = new DAOCustomers();
                Users u = (Users) request.getAttribute("user");
                Customers customer = (Customers) request.getAttribute("customer");
                ArrayList<DebtRecords> debtrecords = (ArrayList<DebtRecords>) request.getAttribute("debtrecords");
                for (DebtRecords debt : debtrecords) {
            %>
            <tr>
                <td><%= debt.getID() %></td>
                <td><%= dao1.getCustomersByID(debt.getCustomerID()).getName() %></td>
                <td><%= debt.getAmountOwed() %></td>
                <td><%= debt.getPaymentStatus() %></td>
                <td><%= debt.getCreateAt() %></td>
                <td><%= debt.getUpdateAt() %></td>
                <td><%= dao.getUserByID(debt.getCreateBy()).getFullName() %></td>
                <td>
                    <% if (debt.getIsDelete() == 0) { %>
                    Active
                    <% } else { %>
                    Ban
                    <% } %>
                </td>

                <td><%= debt.getDeletedAt() %></td>
                <td>
                    <% if (debt.getIsDelete() == 0) { %>
                    Null
                    <% } else { %>
                    <%= dao.getUserByID(debt.getDeleteBy()).getFullName() %>
                    <% } %>
                </td>
                <td>
                    <button onclick="window.location.href = 'updatedebtrecords?id=<%= debt.getID() %>'">Edit</button>
                    <button onclick="window.location.href = 'deletedebtrecords?deleteid=<%= debt.getID() %>&userid=<%= u.getID() %>'">
                        Ban
                    </button>
                </td>
            </tr>
            <%
                }
            %>
        </table>
        <br>
        <div>
            <button onclick="location.href = 'adddebtrecords'">Insert debt records</button>
            <button onclick="location.href = 'Admin/Admin.jsp'">Back to Admin</button>
        </div>
    </body>
</html>
