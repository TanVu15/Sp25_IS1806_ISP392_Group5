<%-- 
    Document   : AddImportOrder
    Created on : Mar 9, 2025, 10:30:02 PM
    Author     : ADMIN
--%>

<%@page contentType="text/html" pageEncoding="UTF-8"%>
<%@ page import="model.Products, model.Zones" %>
<!DOCTYPE html>
<html lang="vi">
    <head>
        <meta charset="UTF-8">
        <meta name="viewport" content="width=device-width, initial-scale=1.0">
        <title>Hóa Đơn Nhập Kho</title>
        <link rel="stylesheet" href="css/addorder.css">
        <script>
            function calculateTotal() {
                let total = 0;
                const productList = document.querySelectorAll('#productList tr');

                productList.forEach(row => {
                    const quantity = parseInt(row.querySelector('input[name="quantity"]').value) || 0;
                    const price = parseFloat(row.querySelector('input[name="price"]').value) || 0;
                    const discount = parseFloat(row.querySelector('input[name="discount"]').value) || 0;

                    // Tính thành tiền cho hàng
                    const totalPrice = (price * quantity) - discount;

                    // Định dạng số nguyên (không có phần thập phân)
                    row.querySelector('input[name="total"]').value = totalPrice.toLocaleString('vi-VN');

                    // Cộng dồn vào tổng chi phí
                    total += totalPrice;
                });

                // Cập nhật tổng chi phí
                document.getElementById('totalCost').value = total.toLocaleString('vi-VN');
            }



            function addProductRow() {
                const table = document.getElementById('productList');
                const newRow = table.insertRow();
                newRow.innerHTML = `
                    <td><input type="text" name="productName" required oninput="searchProduct(this)"></td>
                    <td><input type="number" name="quantity" min="1" required onchange="calculateTotal()"></td>
                    <td><input type="text" name="area"></td>
                    <td><input type="text" name="spec"></td>
                    <td><input type="number" name="price" min="0" required onchange="calculateTotal()"></td>
                    <td><input type="number" name="discount" min="0" onchange="calculateTotal()"></td>
                    <td><input type="text" name="total" readonly></td>
                    <td><button type="button" onclick="deleteProductRow(this)">Xóa</button></td>
                `;
            }

            function deleteProductRow(button) {
                const row = button.parentElement.parentElement;
                row.parentElement.removeChild(row);
                calculateTotal(); // Cập nhật tổng chi phí sau khi xóa
            }

            function searchProduct(input) {
                // Logic tìm kiếm sản phẩm có thể được thêm vào đây.
                // Ví dụ: Gọi API hoặc lọc danh sách sản phẩm từ một mảng.
                console.log("Tìm kiếm sản phẩm: ", input.value);
            }
        </script>
    </head>
    <body>

        <form action="CreateInvoiceServlet" method="post" id="invoiceForm">

            <div class="invoice-details">
                <h2>Chi Tiết Hóa Đơn</h2>
                <h4>Loại Hóa Đơn: Nhập</h4>

                <div class="invoice-info">
                    <div class="input-container">
                        <label for="customerName">Tên Khách Hàng:</label>
                        <input class="input-info" type="text" id="customerName" placeholder="Tìm kiếm khách hàng...">
                    </div>
                    <div class="input-container">
                        <label for="customerPhone">Số ĐT:</label>
                        <input class="input-info" type="text" id="customerPhone" name="customerPhone">
                    </div>
                    <div class="input-container">
                        <label for="creater">Người tạo:</label>
                        <input class="input-info" type="text" id="creater" name="creater">
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
                                <td><input type="text" name="productName" required oninput="searchProduct(this)"></td>
                                <td><input type="number" name="quantity" min="1" required onchange="calculateTotal()"></td>
                                <td><input type="text" name="area"></td>
                                <td><input type="text" name="spec"></td>
                                <td><input type="number" name="price" min="0" required onchange="calculateTotal()"></td>
                                <td><input type="number" name="discount" min="0" onchange="calculateTotal()"></td>
                                <td><input type="text" name="total" readonly></td>
                                <td><button type="button" onclick="deleteProductRow(this)">Xóa</button></td>
                            </tr>
                        </tbody>
                    </table>
                    <button type="button" onclick="addProductRow()">Thêm Sản Phẩm</button>
                </div>

                <div class="input-container">
                    <label for="totalCost">Tổng chi phí:</label>
                    <input class="input-info" type="number" id="totalCost" name="totalCost" readonly>
                </div>
                <div class="action-buttons">
                    <button type="submit" id="createButton">Tạo</button>
                    <button type="button" id="cancelButton">Hủy</button>
                </div>
            </div>
        </form>

    </body>
</html>