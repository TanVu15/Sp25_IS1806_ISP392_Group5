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
        <title>Login Page</title>
        <style>
        /* Reset một số kiểu mặc định */
        body, html {
            margin: 0;
            padding: 0;
            height: 100%;
            font-family: Arial, sans-serif;
        }

        /* Định dạng nền */
        .container {
            background: url('Image/cuahang.jpg') no-repeat center center fixed;
            background-size: cover;
            height: 100vh;
            display: flex;
            justify-content: center;
            align-items: center;
        }

        /* Khung đăng nhập */
        .login-box {
            background: white;
            padding: 25px;
            border-radius: 10px;
            box-shadow: 0px 4px 8px rgba(0, 0, 0, 0.2);
            text-align: center;
            width: 350px;
        }

        .login-box h2 {
            color: #003399;
            margin-bottom: 20px;
        }

        /* Ô nhập liệu */
        input[type="text"], input[type="password"] {
            width: 100%;
            padding: 10px;
            margin: 10px 0;
            border: 1px solid #ccc;
            border-radius: 5px;
        }

        /* Nút đăng nhập */
        button {
            width: 100%;
            padding: 10px;
            background-color: #007BFF;
            border: none;
            color: white;
            font-size: 16px;
            border-radius: 5px;
            cursor: pointer;
        }

        button:hover {
            background-color: #0056b3;
        }

        /* Hỗ trợ */
        .support {
            margin-top: 15px;
            font-size: 14px;
            color: #666;
        }
        </style>
    </head>
    <body>
        <div class="container">
        <div class="login-box">
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
            <div class="support">
                Hỗ trợ: <a href="tel:0388258116">0388258116</a>
            </div>
        </div>
    </div>
        
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
