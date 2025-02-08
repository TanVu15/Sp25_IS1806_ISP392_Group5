<%-- 
    Document   : ListUsers
    Created on : Feb 6, 2025, 2:22:21 PM
    Author     : ADMIN
--%>
<%@ page import="java.util.ArrayList" %>
<%@ page import="Model.Users" %>
<%@ page import="DAL.DAOUser" %>
<%@page contentType="text/html" pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html>
    <head>
        <meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
        <title>Manage Users</title>
    </head>
    <body>
        <form action="listusers" method="post">
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


        <h1>List of Users</h1>
        <table border="1">
            <tr>
                <th>ID</th>
                <th>User name</th>
                <th>User password </th>
                <th>Role</th>
                <th>Full name</th>
                <th>Create at</th>
                <th>Update at</th>
                <th>Create by</th>
                <th>Status</th>
                <th>Delete at </th>
                <th>Delete by</th>
                <th>Action</th>
            </tr>
            <%
                DAOUser dao = new DAOUser();
                Users u = (Users) request.getAttribute("user");
                ArrayList<Users> users = (ArrayList<Users>) request.getAttribute("users");
                for (Users user : users) {
                if(u.getRoleid() <= user.getRoleid()){
            %>
            <tr>
                <td><%= user.getID() %></td>
                <td><%= user.getUsername() %></td>
                <td><%= user.getPasswordHash() %></td>
                <td>
                    <% 
                        if (user.getRoleid() == 1) {
                            out.print("Admin");
                        } else if (user.getRoleid() == 2) {
                            out.print("Owner");
                        } else {
                            out.print("User");
                        }
                    %>
                </td>

                <td><%= user.getFullName() %></td>
                <td><%= user.getCreateAt() %></td>
                <td><%= user.getUpdateAt() %></td>
                <td><%= dao.getUserByID(user.getCreateBy()).getFullName() %></td>
                <td>
                    <% if (user.getIsDelete() == 0) { %>
                    Active
                    <% } else { %>
                    Ban
                    <% } %>
                </td>

                <td><%= user.getDeletedAt() %></td>
                <td>
                    <% if (user.getIsDelete() == 0) { %>
                    Null
                    <% } else { %>
                    <%= dao.getUserByID(user.getDeleteBy()).getFullName() %>
                    <% } %>

                <td>
                    <button onclick="window.location.href = 'updateuser?id=<%= user.getID() %>'">Edit</button>
                    <%
                    if(u.getRoleid() < user.getRoleid()){
                    %>
                    <button onclick="window.location.href = 'deleteuser?deleteid=<%= user.getID() %>&userid=<%= u.getID() %>'">
                        Ban
                    </button>

                </td>
            </tr>
            <%
                    }
                }
            }
            %>
        </table>
        <br>
        <div>
            <button onclick="location.href = 'register'">Insert new User</button>
            <button onclick="location.href = 'Admin/Admin.jsp'">Back to Admin</button>
        </div>
    </body>
</html>
