<%-- 
    Document   : UpdateZones
    Created on : Feb 18, 2025, 5:14:07 PM
    Author     : ASUS
--%>

<%@ page import="model.Zones" %>
<%@ page import="dal.DAOZones" %>
<%@page contentType="text/html" pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html>
    <head>
        <meta charset="UTF-8">
        <title>Edit Zone</title>
    </head>
    <body>
        <h1>Edit Zone</h1>
        <form method="post" action="updatezone">
            <h2>Update Zone Information</h2>

            <%
                Zones zone = (Zones) request.getAttribute("z");
            %>

            <input type="hidden" name="id" value="<%= zone.getID() %>"> <!-- Lấy ID của zone -->

            <label for="zone">Zone Name:</label>
            <input type="text" id="zone" name="zone" value="<%= zone.getZoneName() %>" required><br><br>



            <input type="submit" value="Update Zone">
        </form>

        <%
            String errorMessage = (String) request.getAttribute("errorMessage");
            if (errorMessage != null) {
        %>
        <p style="color:red;"><%= errorMessage %></p>
        <%
            }
        %>

        <button onclick="window.location.href = 'listzones'">Back to Zones List</button>
     
    </body>
</html>
