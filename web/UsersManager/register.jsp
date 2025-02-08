<%-- 
    Document   : register
    Created on : Feb 3, 2025, 9:34:47 PM
    Author     : ADMIN
--%>

<%@page contentType="text/html" pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html>
    <head>
        <meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
        <title>JSP Page</title>
    </head>
    <body>
        <h2>Register</h2>
        <form action="register" method="post">
            <table border="0">
                <tbody>
                    <tr>
                        <td>Username:</td>
                        <td> <input type="text" name="name" required></td>
                    </tr>
                    <tr>
                        <td>Password:</td>
                        <td><input type="password" name="password" required></td>
                    </tr>
                    <tr>
                        <td>Recheck Password:</td>
                        <td><input type="password" name="password2" required></td>
                    </tr>
                <input type="hidden" name="role" value=$>
                </tbody>
            </table>
            <input type="submit" value="Register">
            <%
            String errorMessage = (String) request.getAttribute("errorMessage");
            if (errorMessage != null) {
            %>
            <p style="color:red;"><%= errorMessage %></p>
            <%
                }
            %>
            <button onclick="location.href = 'listusers'">Back to User List</button>
        </form>
    </body>
</html>
