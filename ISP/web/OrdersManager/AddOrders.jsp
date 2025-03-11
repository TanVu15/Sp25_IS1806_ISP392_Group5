<%@page contentType="text/html" pageEncoding="UTF-8"%>
<%@ page import="model.Orders" %>
<!DOCTYPE html>
<html lang="vi">
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title>Hóa Đơn Nhập Kho</title>
    <link rel="stylesheet" href="assets/css/order.css">
</head>
<body>

<div class="search-container">
    <div class="customer-search">
        <h2 class="search-heading">Tìm Khách Hàng</h2>
        <input type="text" placeholder="Nhập tên khách hàng..." id="customerSearch">
    </div>
    <div class="product-search">
        <h2 class="search-heading">Tìm Sản Phẩm</h2>
        <input type="text" placeholder="Nhập tên sản phẩm..." id="productSearch">
    </div>
</div>

<div class="invoice-details">
    <h2>Chi Tiết Hóa Đơn</h2>
    <h4>Loại Hóa Đơn: Nhập</h4>
    
    <div class="invoice-info">
        <div class="input-container">
            <label for="customerName">Tên Khách Hàng:</label>
            <input class="input-info" type="text" id="customerName">
        </div>
        <div class="input-container">
            <label for="customerPhone">Số ĐT:</label>
            <input class="input-info" type="text" id="customerPhone">
        </div>
        <div class="input-container">
            <label for="creater">Người tạo:</label>
            <input class="input-info" type="text" id="creater" placeholder="">
        </div>
    </div>
    
    <div class="product-list">
        <table>
            <thead>
                <tr>
                    <th>Tên Sản Phẩm</th>
                    <th>Số Lượng</th>
                    <th>Khu Vực</th> <!-- Cột Khu Vực -->
                    <th>Quy Cách</th>
                    <th>Giá Gốc</th>
                    <th>Giảm Giá</th>
                    <th>Thành Tiền</th>
                    <th>Xóa</th>
                </tr>
            </thead>
            <tbody id="productList">
                <!-- Sản phẩm sẽ được thêm vào đây -->
            </tbody>
        </table>
    </div>

    <div class="input-container">
        <label for="totalCost">Tổng chi phí:</label>
        <input class="input-info" type="number" id="totalCost" readonly>
    </div>
    <div class="action-buttons">
        <button id="createButton">Tạo</button>
        <button id="cancelButton">Hủy</button>
    </div>
</div>

<script>
    const productSearch = document.getElementById('productSearch');
    const customerSearch = document.getElementById('customerSearch');
    const customerName = document.getElementById('customerName');
    const productList = document.getElementById('productList');
    const totalCost = document.getElementById('totalCost');

// thêm product mới cho bảng hóa đơn
    productSearch.addEventListener('keypress', function (event) {
        if (event.key === 'Enter' && productSearch.value) {
            const newRow = document.createElement('tr');
            newRow.innerHTML = `
                <td>${productSearch.value}</td>
                <td><input type="number" value="1" min="1" style="width: 100px" class="qty"></td>
                <td><input type="text" placeholder="Khu vực" class="area"></td> <!-- Input Khu Vực -->
                <td><input type="text" placeholder="kg/bao" class="spec"></td>
                <td><input type="number" placeholder="Giá gốc" class="price"></td>
                <td><input type="number" placeholder="Giảm giá" class="discount" value="0"></td>
                <td><input type="text" placeholder="Thành tiền" style="width: 180px" readonly class="total"></td>
                <td><button class="delete-btn">Xóa</button></td>
            `;
            productList.appendChild(newRow);
            productSearch.value = ''; // Xóa ô tìm kiếm sau khi thêm sản phẩm

            const qtyInput = newRow.querySelector('.qty');
            const priceInput = newRow.querySelector('.price');
            const discountInput = newRow.querySelector('.discount');
            const specInput = newRow.querySelector('.spec');
            const totalInput = newRow.querySelector('.total');
            const deleteBtn = newRow.querySelector('.delete-btn');

            const calculateTotal = () => {
                const qty = parseFloat(qtyInput.value) || 0;
                const price = parseFloat(priceInput.value) || 0;
                const discount = parseFloat(discountInput.value) || 0;
                const total = (price - discount) * qty;
                totalInput.value = formatCurrency(total);
                updateGrandTotal(); // Cập nhật tổng chi phí
            };

            const formatCurrency = (value) => {
                return value.toString().replace(/\B(?=(\d{3})+(?!\d))/g, '.');
            };

            const updateGrandTotal = () => {
                let grandTotal = 0;
                const totals = document.querySelectorAll('.total');
                totals.forEach(total => {
                    grandTotal += parseFloat(total.value.replace(/\./g, '')) || 0;
                });
                totalCost.value = formatCurrency(grandTotal);
            };

            qtyInput.addEventListener('input', calculateTotal);
            priceInput.addEventListener('input', calculateTotal);
            discountInput.addEventListener('input', calculateTotal);
            specInput.addEventListener('input', calculateTotal);

            deleteBtn.addEventListener('click', () => {
                productList.removeChild(newRow);
                updateGrandTotal(); // Cập nhật tổng chi phí khi xóa sản phẩm
            });
        }
    });

    customerSearch.addEventListener('keypress', function (event) {
        if (event.key === 'Enter' && customerSearch.value) {
            customerName.value = customerSearch.value; // Điền tên khách hàng vào trường
            customerSearch.value = ''; // Xóa ô tìm kiếm tên khách hàng
            customerName.focus(); // Đưa con trỏ đến ô tên khách hàng
        }
    });
</script>

</body>
</html>