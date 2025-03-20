/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package Controller.Queue;

import dal.DAOCustomers;
import dal.DAOOrderItem;
import dal.DAOOrders;
import dal.DAOProducts;
import jakarta.servlet.http.HttpSession;
import java.util.ArrayList;
import model.OrderItems;
import model.Orders;
import model.Products;
import model.Users;

public class ImportOrderTask {

    private String customerName;
    private String customerPhone;
    private ArrayList<Products> productList;
    private double totalCost;
    private HttpSession session; // Thêm session vào class

    // Constructor thêm session
    public ImportOrderTask(String customerName, String customerPhone, ArrayList<Products> productList, double totalCost, HttpSession session) {
        this.customerName = customerName;
        this.customerPhone = customerPhone;
        this.productList = productList;
        this.totalCost = totalCost;
        this.session = session; // Gán session
    }

    public void processOrder() {
        
        try {
            DAOOrders daoOrders = DAOOrders.INSTANCE;
            DAOCustomers daoCustomers = DAOCustomers.INSTANCE;
            DAOProducts daoProducts = DAOProducts.INSTANCE;
            DAOOrderItem daoOrderItem = DAOOrderItem.INSTANCE; // Thêm DAOOrderItem
            

            // 1. Lấy thông tin người tạo từ session
            Users creator = (Users) session.getAttribute("user");
            if (creator == null) {
                System.out.println("User is not logged in.");
                return;
            }

            // 2. Lấy Customer ID từ tên và số điện thoại
            int customerID = daoCustomers.getCustomerIdByNameAndShop(customerName, creator.getShopID());
            if (customerID == -1) {
                System.out.println("Customer not found. You need to add the customer first.");
                return;
            }

            // 3. Tạo đơn hàng mới
            Orders order = new Orders();
            order.setCustomerID(customerID);
            order.setTotalAmount((float) totalCost);
            order.setShopID(creator.getShopID());
            order.setStatus(1); // Đơn nhập hàng

            // 4. Thêm đơn hàng và lấy OrderID
            int orderID = daoOrders.addOrdersreturnID(order, creator.getID());
            if (orderID == -1) {
                System.out.println("Failed to create order.");
                return;
            }

            System.out.println("Added order for customer: " + customerName + " with Order ID: " + orderID);

            // 5. Thêm sản phẩm vào OrderItems và cập nhật số lượng kho
            for (Products product : productList) {
                OrderItems orderItem = new OrderItems();
                orderItem.setOrderID(orderID);
                orderItem.setProductName(product.getProductName());
                orderItem.setQuantity(product.getQuantity());
                orderItem.setPrice(product.getPrice());
                orderItem.setUnitPrice(product.getPrice()); // Nếu có khuyến mãi, cần cập nhật từ form
                orderItem.setDescription(""); // Mô tả đơn hàng nhập
                orderItem.setShopID(creator.getShopID());

                // Thêm thông tin thời gian & người tạo
                orderItem.setCreateBy(creator.getID());
                orderItem.setIsDelete(0);
                orderItem.setDeletedAt(null);
                orderItem.setDeleteBy(0);

                // Thêm vào OrderItems
                daoOrderItem.AddOrderItems(orderItem, creator.getID());
                

                // Cập nhật số lượng sản phẩm trong kho (nhập hàng nên số lượng tăng)
                //daoProducts.updateProductQuantity(product.getProductName(), product.getQuantity(),creator.getShopID());
            }

            System.out.println("Successfully processed import order!");

        } catch (Exception e) {
            System.err.println("Error processing order: " + e.getMessage());
            e.printStackTrace();
        }
    }

}


