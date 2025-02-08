<%-- 
    Document   : UdateUser
    Created on : Feb 6, 2025, 4:52:02 PM
    Author     : Admin
--%>
<%@ page import="java.util.ArrayList" %>
<%@ page import="Model.Users" %>
<%@ page import="DAL.DAOUser" %>
<%@page contentType="text/html" pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html>
    <h1>Edit User</h1>
    <form method="post">
        <h1>Update User Information</h1>

        <%
            Users user = (Users) request.getAttribute("user");
        %>

        
        <input type="hidden" name="id" value="<%= user.getID() %>">

        <label for="username">Username:</label>
        <input type="text" id="username" name="username" value="<%= user.getUsername() %>" readonly><br><br>

        <label for="password">Password:</label>
        <input type="password" id="password" name="password" value="<%= user.getPasswordHash() %> "required><br><br>
        
        <label for="password">Recheck Password:</label>
        <input type="password" id="password2" name="password2" required><br><br>
        
        <label for="fullname">Full Name:</label>
        <input type="text" id="fullname" name="fullname" value="<%= user.getFullName() %> " required><br><br>

            <input type="submit" value="Update User">
        </form>
        <%
            String errorMessage = (String) request.getAttribute("errorMessage");
            if (errorMessage != null) {
        %>
        <p style="color:red;"><%= errorMessage %></p>
        <%
            }
        %>
        
        <button onclick="window.location.href='listusers'">Back to User List</button>
        <button onclick="location.href = '${pageContext.request.contextPath}/logout'">Logout</button>
    </form>
</html>
