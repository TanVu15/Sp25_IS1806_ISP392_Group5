<%-- 
    Document   : Home
    Created on : Feb 3, 2025, 9:35:31 PM
    Author     : ADMIN
--%>

<%@page contentType="text/html" pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html lang="en">

<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title>HomePage Product</title>
    <link rel="stylesheet" href="https://cdnjs.cloudflare.com/ajax/libs/font-awesome/6.7.2/css/all.min.css"
        integrity="sha512-Evv84Mr4kqVGRNSgIGL/F/aIDqQb7xQ2vcrdIwxfjThSH8CSR7PBEakCr51Ck+w+/U6swU2Im1vVX0SVk9ABhg=="
        crossorigin="anonymous" referrerpolicy="no-referrer" />
    <<link rel="stylesheet" href="css/home2.css"/>
    <script>
        function openModal(code, name, price, quantity, location, description, image) {
            document.getElementById('modal-product-code').innerText = code;
            document.getElementById('modal-product-name').innerText = name;
            document.getElementById('modal-product-price').innerText = price;
            document.getElementById('modal-product-quantity').innerText = quantity;
            document.getElementById('modal-product-location').innerText = location;
            document.getElementById('modal-product-description').innerText = description;
            document.getElementById('modal-product-image').src = image;

            document.getElementById('product-detail-modal').style.display = 'block';
        }

        function closeModal() {
            document.getElementById('product-detail-modal').style.display = 'none';
        }

        // Đóng modal khi nhấn ra ngoài modal
        window.onclick = function (event) {
            const modal = document.getElementById('product-detail-modal');
            if (event.target === modal) {
                closeModal();
            }
        }
    </script>
    
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
                    <h3 class="body__head-title">Thông tin sản phẩm</h3>
                    <div class="search-container">
                        <input type="text" placeholder="Tìm kiếm sản phẩm..." class="search-input">
                        <i class="fa-solid fa-magnifying-glass search-icon"></i>
                    </div>
                </div>
                <!-- Product ListList -->
                <div class="table-container">
                    <table class="product-table">
                        <thead>
                            <tr class="table-header">
                                <th class="table-header-item">Hình ảnh</th>
                                <th class="table-header-item">Mã sản phẩm</th>
                                <th class="table-header-item">Tên sản phẩm</th>
                                <th class="table-header-item">Giá tiền</th>
                                <th class="table-header-item">Số lượng</th>
                                <th class="table-header-item">Vị trí</th>
                                <th class="table-header-item">Mô tả</th>
                                <th class="table-header-item">Hành động</th>
                            </tr>
                        </thead>
                        <tbody>
                            <tr class="table-row">
                                <td class="table-cell"><img src="assets/image/gao-ST25-hut-chan-khong-5kg.jpg"
                                        alt="Gạo ST25" class="product-image"></td>
                                <td class="table-cell">G001</td>
                                <td class="table-cell">Gạo ST25</td>
                                <td class="table-cell">30.000đ</td>
                                <td class="table-cell">100</td>
                                <td class="table-cell">A,F</td>
                                <td class="table-cell description">Gạo ST25 chất lượng cao, thơm ngon.</td>
                                <td class="table-cell">
                                    <button class="action-button"
                                        onclick="openModal('G001', 'Gạo ST25', '30.000đ', '100', 'A,F', 'Gạo ST25 chất lượng cao, thơm ngon.', 'assets/image/gao-ST25-hut-chan-khong-5kg.jpg')">Xem</button>
                                </td>
                            </tr>
                            <tr class="table-row">
                                <td class="table-cell"><img src="assets/image/gao-ST25-hut-chan-khong-5kg.jpg"
                                        alt="Gạo Jasmine" class="product-image"></td>
                                <td class="table-cell">G002</td>
                                <td class="table-cell">Gạo Jasmine</td>
                                <td class="table-cell">35.000đ</td>
                                <td class="table-cell">80</td>
                                <td class="table-cell">B</td>
                                <td class="table-cell description">Gạo Jasmine hạt dài, thơm mát.</td>
                                <td class="table-cell"><button class="action-button">Xem</button></td>
                            </tr>
                            <tr class="table-row">
                                <td class="table-cell"><img src="assets/image/gao-bac-huong.jpg" alt="Gạo Bắc Hương"
                                        class="product-image"></td>
                                <td class="table-cell">G003</td>
                                <td class="table-cell">Gạo Bắc Hương</td>
                                <td class="table-cell">28.000đ</td>
                                <td class="table-cell">60</td>
                                <td class="table-cell">C</td>
                                <td class="table-cell description">Gạo Bắc Hương ngọt, dẻo.</td>
                                <td class="table-cell"><button class="action-button">Xem</button></td>
                            </tr>
                            <tr class="table-row">
                                <td class="table-cell"><img src="assets/image/gao-ST25-hut-chan-khong-5kg.jpg"
                                        alt="Gạo Nàng Hoa 9" class="product-image"></td>
                                <td class="table-cell">G004</td>
                                <td class="table-cell">Gạo Nàng Hoa 9</td>
                                <td class="table-cell">32.000đ</td>
                                <td class="table-cell">75</td>
                                <td class="table-cell">D</td>
                                <td class="table-cell description">Gạo Nàng Hoa 9 dẻo, thơm.</td>
                                <td class="table-cell"><button class="action-button">Xem</button></td>
                            </tr>
                            <tr class="table-row">
                                <td class="table-cell"><img src="assets/image/gao-ST25-hut-chan-khong-5kg.jpg"
                                        alt="Gạo Tám Xoan" class="product-image"></td>
                                <td class="table-cell">G005</td>
                                <td class="table-cell">Gạo Tám Xoan</td>
                                <td class="table-cell">40.000đ</td>
                                <td class="table-cell">50</td>
                                <td class="table-cell">E</td>
                                <td class="table-cell description">Gạo Tám Xoan thơm ngon, đặc biệt.</td>
                                <td class="table-cell"><button class="action-button">Xem</button></td>
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

    <!-- MOdal -->
    <!-- Modal for Product Details -->
    <div id="product-detail-modal" class="modal">
        <div class="modal-content">
            <span class="close" onclick="closeModal()">&times;</span>
            <h2>Chi tiết sản phẩm</h2>
            <div class="modal-body">
                <img src="" alt="Product Image" class="modal-product-image" id="modal-product-image">
                <p><strong>Mã sản phẩm:</strong> <span id="modal-product-code"></span></p>
                <p><strong>Tên sản phẩm:</strong> <span id="modal-product-name"></span></p>
                <p><strong>Giá tiền:</strong> <span id="modal-product-price"></span></p>
                <p><strong>Số lượng:</strong> <span id="modal-product-quantity"></span></p>
                <p><strong>Vị trí:</strong> <span id="modal-product-location"></span></p>
                <p><strong>Mô tả:</strong> <span id="modal-product-description"></span></p>
                <div class="modal-actions">
                    <button class="action-button">Sửa</button>
                    <button class="action-button" id="delete-button">Xóa</button>
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