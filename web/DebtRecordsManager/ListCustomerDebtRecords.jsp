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
        <%  
                DAOUser dao = new DAOUser();
                DAOCustomers dao1 = new DAOCustomers();
                Users u = (Users) request.getAttribute("user");
                Customers customer = (Customers) request.getAttribute("customer");
            %>
            
            <h3><%= customer.getName() %></h3>
            <button onclick="location.href = 'adddebtrecords?customerid=<%= customer.getID() %>'">
                Thêm Công Nợ
                </button>
            
        <table border="1">
            <tr>
                <th>ID</th>
                <th>Customer </th>
                <th>Amount Owed </th>
                <th>Payment Status</th>
                <th>create at</th>
                <th>create by</th>
            </tr>
            <%  
                ArrayList<DebtRecords> debtrecords = (ArrayList<DebtRecords>) request.getAttribute("debtrecords");
                for (DebtRecords debt : debtrecords) {
            %>
            <tr>
                <td><%= debt.getID() %></td>
                <td><%= dao1.getCustomersByID(debt.getCustomerID()).getName() %></td>
                <td><%= debt.getAmountOwed() %></td>
                <td>
                    <% if (debt.getPaymentStatus() == 1) { %>
                    Trả Nợ
                    <% } else { %>
                    Vay Nợ
                    <% } %>
                </td>
                <td><%= debt.getCreateAt() %></td>
                <td><%= dao.getUserByID(debt.getCreateBy()).getFullName() %></td>
                
            </tr>
            <%
                }
            %>
        </table>
        <br>
        <div>
            
            <button onclick="location.href = 'Admin/Admin.jsp'">Back to Admin</button>
        </div>
    </body>
</html>
