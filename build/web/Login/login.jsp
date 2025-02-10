<%-- 
    Document   : login
    Created on : Feb 3, 2025, 9:34:38 PM
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
        <h2>Login</h2>
        <form action="login" method="post">
            <table border="0">
                <tbody>
                    <tr>
                        <td>Username:</td>
                        <td><input type="text" name="name" required></td>
                    </tr>
                    <tr>
                        <td>Password:</td>
                        <td><input type="password" name="password" required></td>
                    </tr>
                </tbody>
            </table>
            <input type="submit" value="Login">
        </form>
        <%
            String errorMessage = (String) request.getAttribute("errorMessage");
            if (errorMessage != null) {
        %>
        <p style="color:red;"><%= errorMessage %></p>
        <%
            }
        %>
        <p style="color: red">Don't have an account? <a href="RegisterServlet">Register here</a></p>
    </body>
</html>
