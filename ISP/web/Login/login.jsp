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

            .container {
                background: url('<%= request.getContextPath() %>/Image/dai-ly-gao-1.jpg') no-repeat center center fixed;
                background-size: cover;
                height: 100vh;
                display: flex;
                justify-content: center;
                align-items: center;
            }

            .login-box {
                background: white;
                padding: 25px;
                border-radius: 10px;
                box-shadow: 0px 4px 8px rgba(0, 0, 0, 0.2);
                text-align: center;
                width: 350px;

                /* Căn giữa chính xác */
                position: absolute;
                left: 50%;
                top: 50%;
                transform: translate(-50%, -50%);
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

            .toast-message {
                position: fixed;
                top: 20px; /* Đưa thông báo lên góc trên */
                right: 20px; /* Căn lề phải */
                background: #dc3545; /* Màu xanh lá */
                color: white;
                padding: 10px 20px;
                border-radius: 5px;
                box-shadow: 0px 0px 10px rgba(0, 0, 0, 0.1);
                font-size: 14px;
                opacity: 1;
                transition: opacity 0.5s ease-out;
                z-index: 1000;
                display: none;
            }

            .search-button {
                background-color: var(--primary-color);
                background: #66c32c; /* Màu xanh lá */
                color: white;
                border: none;
                border-radius: 4px;
                padding: 8px 16px;
                cursor: pointer;
                transition: background-color 0.3s;
                font-size: 14px;
                margin-left: 10px;
            }

            .search-button:hover {
                background-color: #28a745; /* Màu xanh lá đậm hơn khi hover */
            }
        </style>
    </head>
    <body>
        <div class="container">
            <%  String name = (String) request.getAttribute("name"); 
                String password = (String) request.getAttribute("password"); 
                if (name == null || password == null) {
                    name = "";
                    password = "";
                }
                
            %>
            <div class="login-box">
                <h2>Đăng Nhập</h2>
                <form action="login" method="post">
                    <table border="0">
                        <tbody>
                            <tr>
                                <td>Tài Khoản:</td>
                                <td><input type="text" name="name" value= "<%= name %>" required></td>
                            </tr>
                            <tr>
                                <td>Mật Khẩu:</td>
                                <td><input type="password" name="password" value= "<%= password %>" required></td>
                            </tr>
                        </tbody>
                    </table>
                    <input type="submit" class="search-button" value="Đăng Nhập">
                </form>

                <div class="support">
                    Hỗ trợ: <a href="tel:0388258116">0388258116</a>
                </div>

            </div>
            <% String message = (String) request.getAttribute("message"); %>
            <% if (message != null && !message.isEmpty()) { %>
            <div id="toast-message" class="toast-message"><%= message %></div>
            <% } %>

            <script>
                window.onload = function () {
                    var toast = document.getElementById("toast-message");
                    if (toast) {
                        toast.style.display = "block"; // Hiển thị thông báo
                        setTimeout(function () {
                            toast.style.opacity = "0";
                            setTimeout(() => toast.style.display = "none", 500);
                        }, 3000);
                    }
                };
            </script>
        </div>


    </body>
</html>
