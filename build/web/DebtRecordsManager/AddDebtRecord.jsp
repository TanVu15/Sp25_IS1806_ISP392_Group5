<%@ page import="java.util.ArrayList" %>
<%@ page import="model.Customers" %>
<%@ page import="model.DebtRecords" %>
<%@ page import="model.Users" %>
<%@ page import="dal.DAOCustomers" %>
<%@ page import="dal.DAOUser" %>
<%@page contentType="text/html" pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html lang="en">

    <head>
        <meta charset="UTF-8">
        <meta name="viewport" content="width=device-width, initial-scale=1.0">
        <title>Thêm công nợ</title>
        <link rel="stylesheet" href="css/home2.css">
        <link rel="stylesheet" href="https://cdnjs.cloudflare.com/ajax/libs/font-awesome/6.7.2/css/all.min.css" integrity="sha512-Evv84Mr4kqVGRNSgIGL/F/aIDqQb7xQ2vcrdIwxfjThSH8CSR7PBEakCr51Ck+w+/U6swU2Im1vVX0SVk9ABhg==" crossorigin="anonymous" referrerpolicy="no-referrer" />
    </head>

    <body>
        <%  
                DAOUser dao = new DAOUser();
                Users u = (Users) request.getAttribute("user");
                ArrayList<Customers> customers = (ArrayList<Customers>) request.getAttribute("customers");
                
                DAOCustomers dao1 = new DAOCustomers();
                Customers customer = (Customers) request.getAttribute("customer");
        
                
        %>
        <div class="header">
            <div class="container">
                <img src="Image/logo.png" alt="logo" class="home-logo">
            </div>
            <div class="header__navbar-item navbar__user">
                <span class="navbar__user--name"> <%= u.getFullName() %></span>
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

                <div class="mainmenu">
                    <ul class="mainmenu-list row no-gutters">
                        <li class="mainmenu__list-item"><a href=""><i class="fa-solid fa-bowl-rice list-item-icon"></i>Sản Phẩm</a></li>
                        <li class="mainmenu__list-item"><a href=""><i class="fa-solid fa-box list-item-icon"></i>Kho</a></li>
                        <li class="mainmenu__list-item"><a href=""><i class="fa-solid fa-dollar-sign list-item-icon"></i>Bán Hàng</a></li>
                        <li class="mainmenu__list-item"><a href="listcustomers"><i class="fa-solid fa-person list-item-icon"></i>Khách Hàng</a></li>
                        <li class="mainmenu__list-item"><a href="listdebtrecords"><i class="fa-solid fa-wallet list-item-icon"></i>Công Nợ</a></li>
                        <li class="mainmenu__list-item"><a href="listusers"><i class="fa-solid fa-user list-item-icon"></i>Tài Khoản</a></li>
                    </ul>
                </div>

                <div class="homepage-body">
                    <h3 class="body__head-title">Thông tin Công Nợ</h3>
                    <div class="table-container">
                        <form action="adddebtrecords" method="post">
                            
                            <table class="product-table">
                                <thead>
                                    <tr class="table-header">
                                        <th class="table-header-item">Tên</th>
                                        <th class="table-header-item">Số tiền</th>
                                        <th class="table-header-item">Trạng thái</th>
                                        <th class="table-header-item">Ngày lập phiếu</th>
                                        <th class="table-header-item">Hình ảnh</th>
                                        <th class="table-header-item">Ghi chú</th>
                                        <th class="table-header-item">Thao tác</th>
                                    </tr>
                                </thead>
                                <tbody>
                                    <tr class="table-row">
                                        <input type="hidden" name="customerid" value="<%= customer.getID() %>">
                                        <td class="table-cell"><%= customer.getName() %></td>
                                        <td class="table-cell"><input type="number" name="amountowed" required></td>
                                        <td class="table-cell">
                                            <select name="paymentstatus" required>
                                                <option value="1">Trả Nợ</option>
                                                <option value="-1">Vay Nợ</option>
                                            </select>
                                        </td>
                                        <td class="table-cell"><input type="date" name="invoicedate" required></td>
                                        <td class="table-cell"><input type="text" name="imagepath" required></td>
                                        <td class="table-cell"><input type="text" name="note" required></td>
                                        <td class="table-cell">
                                            <button onclick="submit" class="submit-button">Add</button>
                                        </td>
                                    </tr>
                                </tbody>
                            </table>
                        </form>
                    </div>
                    <div class="pagination">
                        <button class="pagination-button" id="prev-button" onclick="prevPage()">Trước</button>
                        <span class="pagination-info">Trang <span class="current-page" id="current-page">1</span> / <span class="total-pages" id="total-pages">5</span></span>
                        <button class="pagination-button" id="next-button" onclick="nextPage()">Sau</button>
                    </div>
                </div>
            </div>
        </div>
        <div class="footer">
            <div class="container">
                <p>&copy; 2025 Công ty TNHH G5. Tất cả quyền được bảo lưu.</p>
            </div>
        </div>
    </body>

</html>
