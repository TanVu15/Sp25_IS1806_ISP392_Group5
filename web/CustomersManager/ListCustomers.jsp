<%-- 
    Document   : ListCustomers
    Created on : Feb 7, 2025, 5:09:42 PM
    Author     : ADMIN
--%>

<%@ page import="java.util.ArrayList" %>
<%@ page import="Model.Customers" %>
<%@ page import="Model.Users" %>
<%@ page import="DAL.DAOCustomers" %>
<%@ page import="DAL.DAOUser" %>
<%@page contentType="text/html" pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html>
    <head>
        <meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
        <title>Manage Customers</title>
    </head>
    <body>

        <form action="listcustomers" method="post">
            <h1>Search</h1>
            <label for="information">Information:</label>
            <input type="text" id="information" name="information" >

            <input type="submit" value="Search">
        </form>
        <% 
            String message = (String) request.getAttribute("message");
            if (message != null && !message.isEmpty()) { 
        %>
        <p style="color: red;"><%= message %></p>
        <% } %>

        <h1>List of Customers</h1>
        <table border="1">
            <tr>
                <th>ID</th>
                <th>Wallet</th>
                <th>Name customer </th>
                <th>Phone</th>
                <th>Address</th>
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
                Users u = (Users) request.getAttribute("user");
                ArrayList<Customers> customers = (ArrayList<Customers>) request.getAttribute("customers");
                for (Customers cus : customers) {
            %>
            <tr>
                <td><%= cus.getID() %></td>
                <td><%= cus.getWallet() %></td>
                <td><%= cus.getName() %></td>
                <td><%= cus.getPhone() %></td>
                <td><%= cus.getAddress() %></td>
                <td><%= cus.getCreateAt() %></td>
                <td><%= cus.getUpdateAt() %></td>
                <td><%= dao.getUserByID(cus.getCreateBy()).getFullName() %></td>
                <td>
                    <% if (cus.getIsDelete() == 0) { %>
                    Active
                    <% } else { %>
                    Ban
                    <% } %>
                </td>

                <td><%= cus.getDeletedAt() %></td>
                <td>
                    <% if (cus.getIsDelete() == 0) { %>
                    Null
                    <% } else { %>
                    <%= dao.getUserByID(cus.getDeleteBy()).getFullName() %>
                    <% } %>
                </td>
                <td>
                    <button onclick="window.location.href = 'updatecustomer?id=<%= cus.getID() %>'">Edit</button>
                    <button onclick="window.location.href = 'deletecustomer?deleteid=<%= cus.getID() %>&userid=<%= u.getID() %>'">
                        Ban
                    </button>
                    <button onclick="window.location.href = 'listcustomerdebtrecords?customerid=<%= cus.getID() %>'">
                        Công nợ
                    </button>   
                </td>
            </tr>
            <%
                }
            %>
        </table>
        <br>
        <div>
            <button onclick="location.href = 'addcustomer'">Insert Customer</button>
            <button onclick="location.href = 'Admin/Admin.jsp'">Back to Admin</button>
        </div>
    </body>
</html>

