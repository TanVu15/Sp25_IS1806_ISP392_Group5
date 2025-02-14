<%-- 
    Document   : Zone
    Created on : Feb 14, 2025, 3:33:36 PM
    Author     : DELL
--%>

<%@page contentType="text/html" pageEncoding="UTF-8"%>
<html lang="en">

<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title>HomePage Product</title>
    <link rel="stylesheet" href="https://cdnjs.cloudflare.com/ajax/libs/font-awesome/6.7.2/css/all.min.css"
        integrity="sha512-Evv84Mr4kqVGRNSgIGL/F/aIDqQb7xQ2vcrdIwxfjThSH8CSR7PBEakCr51Ck+w+/U6swU2Im1vVX0SVk9ABhg=="
        crossorigin="anonymous" referrerpolicy="no-referrer" />
    <<link rel="stylesheet" href="css/home2.css"/>
</head>

<body>
    <!-- Homepage Header -->
    <div class="header">
        <div class="container">
            <img src="Image/logo.png" alt="logo" class="home-logo">
        </div>
        <div class="header__navbar-item navbar__user">
            <img src="./assets/image/b1a7482d6791463fe55cd7483db00d27.jpg" alt="" class="navbar__user--img">
            <span class="navbar__user--name">Tan Vu</span>
            <!-- Drop user table -->
            <div class="navbar__user--info">
                <div class="navbar__info--wrapper">
                    <a href="" class="navbar__info--item">Tài khoản của tôi</a>
                </div>
                <div class="navbar__info--wrapper">
                    <a href="" class="navbar__info--item">Đăng xuất</a>
                </div>
            </div>
        </div>
    </div>
    
    
    
    <div class="body">
        <div class="body-container">
            <!-- Menu sidebar  -->
            <div class="mainmenu">
                <ul class="mainmenu-list row no-gutters">
                    <li class="mainmenu__list-item">
                        <a href="">
                            <i class="fa-solid fa-bowl-rice list-item-icon"></i>
                            Sản Phẩm
                        </a>
                    </li>
                    <li class="mainmenu__list-item">
                        <a href="">
                            <i class="fa-solid fa-box list-item-icon"></i>
                            Kho
                        </a>
                    </li>
                    <li class="mainmenu__list-item">
                        <a href="">
                            <i class="fa-solid fa-dollar-sign list-item-icon"></i>
                            Bán Hàng
                        </a>
                    </li>
                    <li class="mainmenu__list-item">
                        <a href="">
                            <i class="fa-solid fa-person list-item-icon"></i>
                            Khách Hàng
                        </a>
                    </li>
                    <li class="mainmenu__list-item">
                        <a href="">
                            <i class="fa-solid fa-wallet list-item-icon"></i>
                            Công Nợ
                        </a>
                    </li>
                    <li class="mainmenu__list-item">
                        <a href="">
                            <i class="fa-solid fa-user list-item-icon"></i>
                            Tài Khoản
                        </a>
                    </li>
                </ul>
            </div>

            <!-- HomePage Body -->
            <div class="homepage-body">
                <div class="body-head">
                    <h3 class="body__head-title">Thông tin Kho(Zone)</h3>
                    <div class="search-container">
                        <input type="text" placeholder="Tìm kiếm kho..." class="search-input">
                        <i class="fa-solid fa-magnifying-glass search-icon"></i>
                    </div>
                </div>
                <!-- Zone ListList -->
                <div class="table-container">
                    <table class="product-table">
                        <thead>
                            <tr class="table-header">
                                <th class="table-header-item">Mã Kho</th>
                                <th class="table-header-item">Tên Kho</th>
                            </tr>
                        </thead>
                        <tbody>
                            <tr class="table-row">
                                <td class="table-cell">K001</td>
                                <td class="table-cell">Gạo Nam Bình</td>
                            </tr>
                            <tr class="table-row">
                                <td class="table-cell">K002</td>
                                <td class="table-cell">Gạo Lộc Xuân</td>
                            </tr>
                            <tr class="table-row">
                                <td class="table-cell">K003</td>
                                <td class="table-cell">Gạo An Nguyên</td>
                            </tr>
                           <tr class="table-row">
                                <td class="table-cell">K004</td>
                                <td class="table-cell">Thế Giới Gạo Việt</td>
                            </tr>
                        </tbody>
                    </table>
                </div>
                <!-- Page -->
                <div class="pagination">
                    <button class="pagination-button" id="prev-button" onclick="prevPage()">Trước</button>
                    <span class="pagination-info">Trang <span class="current-page" id="current-page">1</span> / <span
                            class="total-pages" id="total-pages">5</span></span>
                    <button class="pagination-button" id="next-button" onclick="nextPage()">Sau</button>
                </div>

            </div>
        </div>
    </div>
</body>
<!-- Footer -->
<div class="footer">
    <div class="container">
        <p>&copy; 2025 Công ty TNHH G5. Tất cả quyền được bảo lưu.</p>
    </div>
</div>

</html>
