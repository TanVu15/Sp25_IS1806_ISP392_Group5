<%-- 
    Document   : AddExportOrder
    Created on : Mar 9, 2025, 10:30:02 PM
    Author     : ADMIN
--%>

<%@page contentType="text/html" pageEncoding="UTF-8"%>
<%@ page import="model.Orders" %>
<%@ page import="java.util.ArrayList" %>
<%@ page import="model.Customers" %>
<%@ page import="model.DebtRecords" %>
<%@ page import="model.Users" %>
<%@ page import="model.Zones" %>
<%@ page import="model.Products" %>
<%@ page import="dal.DAOCustomers" %>
<%@ page import="dal.DAOUser" %>
<%@ page import="dal.DAOProducts" %>
<%@ page import="dal.DAOZones" %>
<%@ page import="model.Shops" %>
<%@ page import="dal.DAOShops" %>
<%@ page import="java.util.List" %>
<!DOCTYPE html>
<html lang="vi">
    <head>
        <meta charset="UTF-8">
        <meta name="viewport" content="width=device-width, initial-scale=1.0">
        <title>Hóa Đơn Nhập Kho</title>
        <link rel="stylesheet" href="css/addorder.css">
        <link href="https://cdn.jsdelivr.net/npm/select2@4.1.0-rc.0/dist/css/select2.min.css" rel="stylesheet" />
        <script src="https://code.jquery.com/jquery-3.6.0.min.js"></script>
        <script src="https://cdn.jsdelivr.net/npm/select2@4.1.0-rc.0/dist/js/select2.min.js"></script>
        
        <script>
    function openNewInvoiceTab() {
        window.open('addimportorder', '_blank'); // Opens the add order page in a new tab
    }
</script>

    </head>
    <body>
        <% DAOUser dao = new DAOUser(); 
        List<Zones> zones = (List<Zones>) request.getAttribute("zones");
        ArrayList<Products> products = (ArrayList<Products>) request.getAttribute("products");
        ArrayList<Customers> customers = (ArrayList<Customers>) request.getAttribute("customers");
        Users u = (Users) request.getAttribute("user"); 
        %>

        <form action="addexportorder" method="post" id="invoiceForm">
            <div class="invoice-details">
                <h2>Chi Tiết Hóa Đơn</h2>
                <div class="input-container">
                    <label>Loại Hóa Đơn:</label>
                    <input type="hidden" name="orderType" value="-1"> <!-- Gửi giá trị 1 -->
                    <span class="input-info">Bán Hàng</span> <!-- Hiển thị cho người dùng -->
                </div>


                <div class="invoice-info">
                    <div class="input-container">
                        <label for="customerName">Tên Khách Hàng:</label>
                        <input class="input-info" type="text" id="customerName" name="customerName" list="customersList" placeholder="Tìm kiếm khách hàng..." required>
                        <datalist id="customersList">
                            <%
                           if (customers != null && !customers.isEmpty()) {
                           for (Customers customer1 : customers) { 
                                if(customer1.getShopID() == u.getShopID()){
                            %>
                            <option value="<%= customer1.getName() %>"></option>
                            <% } } }%>
                        </datalist>
                    </div>
                    <div class="input-container">
                        <label for="customerPhone">Số Điện thoại:</label>
                        <input class="input-info" type="text" id="customerPhone" name="customerPhone">
                    </div>

                    <div class="input-container">
                        <label for="creater">Người tạo phiếu:</label>
                        <input type="hidden" name="creater" value="<%= u.getFullName() %>"> <!-- Gửi giá trị 1 -->
                        <span class="input-info"><%= u.getFullName() %></span>
                    </div>
                </div>

                <div class="product-list">
                    <table id="productTable">
                        <thead>
                            <tr>
                                <th>Tên Sản Phẩm</th>
                                <th>Số Lượng</th>
                                <th>Khu Vực</th>
                                <th>Quy Cách</th>
                                <th>Giá Gốc</th>
                                <th>Giảm Giá</th>
                                <th>Thành Tiền</th>
                                <th>Xóa</th>
                            </tr>
                        </thead>
                        <tbody id="productList">
                            <tr>
                                <td>
                                    <select class="area-select" name="productName" multiple="multiple" required style="width: 100%;">
                                        <% 
                                          for (Products product : products) { 
                                               if(product.getShopID() == u.getShopID()) {
                                        %>
                                        <option value="<%= product.getProductName() %>"><%= product.getProductName() %></option>
                                        <% } }%>
                                    </select>
                                </td>
                                <td><input type="number" name="quantity" min="1" required onchange="calculateTotal()"></td>
                                <td>
                                    <select class="area-select" name="area" multiple="multiple" required style="width: 100%;">
                                        <% 
                                           for (Zones zone : zones) { 
                                                if(zone.getShopID() == u.getShopID()) { 
                                        %>
                                        <option value="<%= zone.getID() %>"><%= zone.getZoneName() %></option>
                                        <% } } %>
                                    </select>
                                </td>
                                <td><input type="text" name="spec" placeholder="Kg/bao"></td>
                                <td><input type="number" name="price" min="0" value="0" required onchange="calculateTotal()"></td>
                                <td><input type="number" name="discount" min="0" value="0" onchange="calculateTotal()"></td>
                                <td><input type="text" name="total" readonly></td>
                                <td><button type="button" onclick="deleteProductRow(this)">Xóa</button></td>
                            </tr>
                        </tbody>
                    </table>
                    <button type="button" onclick="addProductRow()">Thêm Sản Phẩm</button>
                </div>
                <script>
                    $(document).ready(function () {
                        $('.area-select').select2({
                            placeholder: "Chọn...",
                            allowClear: true
                        });
                    });

                    const productList = [
                    <% if (products != null) { 
                        for (Products product : products) { 
                            if(product.getShopID() == u.getShopID()) { %>
                        {id: '<%= product.getProductName() %>', text: '<%= product.getProductName() %>'},
                    <%    } 
                      } 
                   } %>
                    ];

                    const zoneList = [
                    <%  if (zones != null) {
                        for (Zones zone : zones) { 
                        if(zone.getShopID() == u.getShopID()) { %>
                        {id: '<%= zone.getID() %>', text: '<%= zone.getZoneName() %>'},
                    <% } } } %>
                    ];

                    // Hiển thị/ẩn các ô khi chọn hình thức thanh toán
                    function handlePaymentChange() {
                        const paymentStatus = document.querySelector('input[name="paymentStatus"]:checked').value;
                        const partialPaymentContainer = document.getElementById('partialPaymentContainer');
                        const remainingAmountContainer = document.getElementById('remainingAmountContainer');

                        if (paymentStatus === 'partial') {
                            partialPaymentContainer.style.display = 'flex';
                            remainingAmountContainer.style.display = 'flex';
                            calculateRemainingAmount(); // Tính toán lại số tiền còn lại khi hiện form
                        } else {
                            partialPaymentContainer.style.display = 'none';
                            remainingAmountContainer.style.display = 'none';
                            document.getElementById('partialPayment').value = 0;
                            document.getElementById('remainingAmount').value = '';
                        }
                    }

                    // Tính số tiền còn lại khi nhập số tiền trả
                    function calculateRemainingAmount() {
                        const totalCostStr = document.getElementById('totalCost').value.replace(/\./g, '').replace(/,/g, '.');
                        const totalCost = parseFloat(totalCostStr) || 0;
                        const partialPayment = parseFloat(document.getElementById('partialPayment').value) || 0;

                        let remainingAmount = totalCost - partialPayment;
                        remainingAmount = remainingAmount < 0 ? 0 : remainingAmount;

                        document.getElementById('remainingAmount').value = remainingAmount.toLocaleString('vi-VN');
                    }

                    // Khởi tạo khi load trang
                    $(document).ready(function () {
                        // Sự kiện khi nhập số tiền trả
                        $('#partialPayment').on('input', calculateRemainingAmount);
                        // Khởi tạo ẩn/hiện các phần
                        handlePaymentChange();
                    });

                    // Update the calculateTotal function to also update the remaining amount
                    function calculateTotal() {
                        let grandTotal = 0;

                        $('#productList tr').each(function () {
                            // Lấy giá trị các input
                            let quantityVal = $(this).find('input[name="quantity"]').val();
                            let priceVal = $(this).find('input[name="price"]').val();
                            let discountVal = $(this).find('input[name="discount"]').val();

                            // Chuyển đổi về số, nếu rỗng thì thành 0
                            const quantity = quantityVal.trim() === "" ? 0 : parseInt(quantityVal);
                            const price = priceVal.trim() === "" ? 0 : parseFloat(priceVal);
                            const discount = discountVal.trim() === "" ? 0 : parseFloat(discountVal);

                            // Tính thành tiền
                            let totalPrice = (price * quantity) - discount;
                            totalPrice = totalPrice < 0 ? 0 : totalPrice; // Không cho âm tiền

                            // Cập nhật ô thành tiền, định dạng kiểu số Việt Nam
                            $(this).find('input[name="total"]').val(totalPrice.toLocaleString('vi-VN'));

                            // Cộng vào tổng tiền
                            grandTotal += totalPrice;
                        });

                        // Hiển thị tổng chi phí
                        $('#totalCost').val(grandTotal.toLocaleString('vi-VN'));

                        // Update remaining amount if partial payment is selected
                        if (document.getElementById('paymentStatus').value === 'partial') {
                            calculateRemainingAmount();
                        }
                    }


                    function addProductRow() {
                        const newRow = `
                        <tr>
                            <td>
                                <select class="area-select product-select" name="productName" multiple="multiple" required style="width: 100%;">
                                </select>
                            </td>
                            <td><input type="number" name="quantity" min="1" required onchange="calculateTotal()"></td>
                            <td>
                                <select class="area-select zone-select" name="area" multiple="multiple" required style="width: 100%;">
                                </select>
                            </td>
                            <td><input type="text" name="spec" placeholder="Kg/bao"></td>
                            <td><input type="number" name="price" min="0" value="0" required onchange="calculateTotal()"></td>
                            <td><input type="number" name="discount" min="0" value="0" onchange="calculateTotal()"></td>
                            <td><input type="text" name="total" readonly></td>
                            <td><button type="button" onclick="deleteProductRow(this)">Xóa</button></td>
                        </tr>
                    `;

                        $('#productList').append(newRow);

                        // Khởi tạo select2 với data cho dropdown vừa thêm
                        const lastProductSelect = $('#productList').find('tr:last .product-select');
                        const lastZoneSelect = $('#productList').find('tr:last .zone-select');

                        lastProductSelect.select2({
                            data: productList,
                            placeholder: "Chọn...",
                            allowClear: true
                        });

                        lastZoneSelect.select2({
                            data: zoneList,
                            placeholder: "Chọn...",
                            allowClear: true
                        });
                    }

                    function deleteProductRow(button) {
                        $(button).closest('tr').remove();
                        calculateTotal();
                    }
                </script>

                <!-- Trạng thái thanh toán -->
                <div class="input-container">
                    <label>Thanh toán:</label>
                    <div class="payment-options">
                        <div>
                            <input type="radio" id="payment-full" name="paymentStatus" value="full" checked onchange="handlePaymentChange()">
                            <label for="payment-full">Trả đủ</label>
                        </div>
                        <div>
                            <input type="radio" id="payment-partial" name="paymentStatus" value="partial" onchange="handlePaymentChange()">
                            <label for="payment-partial">Trả một phần</label>
                        </div>
                        <div>
                            <input type="radio" id="payment-none" name="paymentStatus" value="none" onchange="handlePaymentChange()">
                            <label for="payment-none">Ghi nợ</label>
                        </div>
                    </div>
                </div>

                <!-- Số tiền trả (hiện khi trả 1 phần) -->
                <div class="input-container" id="partialPaymentContainer" style="display: none;">
                    <label for="partialPayment">Số tiền trả:</label>
                    <input class="input-info" type="number" id="partialPayment" name="partialPayment" min="0" value="0" onchange="calculateRemainingAmount()">
                </div>

                <!-- Số tiền còn lại (hiện khi trả 1 phần) -->
                <div class="input-container" id="remainingAmountContainer" style="display: none;">
                    <label for="remainingAmount">Số tiền còn lại:</label>
                    <input class="input-info" type="text" id="remainingAmount" name="remainingAmount" readonly>
                </div>

                <div class="input-container">
                    <label for="totalCost">Tổng chi phí:</label>
                    <input class="input-info" type="text" id="totalCost" name="totalCost" readonly>
                </div>

                <div class="action-buttons">
                    <button id="createButton" type="submit">Tạo</button>
                    <button id="createButton" onclick="window.location.href = 'listorders'">Hủy</button>
                    <button id="createButton" type="button" onclick="openNewInvoiceTab()">Thêm Hóa Đơn Mới</button>
                </div>
            </div>
        </form>
    </body>
</html>