/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/JSP_Servlet/Servlet.java to edit this template
 */
package Controller.orderservlet;

import dal.DAOCustomers;
import dal.DAOProducts;
import java.io.IOException;
import java.io.PrintWriter;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import java.text.NumberFormat;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;
import model.Customers;
import model.Products;
import model.Users;

/**
 *
 * @author ADMIN
 */
public class SearchOrder extends HttpServlet {

    /**
     * Processes requests for both HTTP <code>GET</code> and <code>POST</code>
     * methods.
     *
     * @param request servlet request
     * @param response servlet response
     * @throws ServletException if a servlet-specific error occurs
     * @throws IOException if an I/O error occurs
     */
    protected void processRequest(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        response.setContentType("text/html;charset=UTF-8");
        try (PrintWriter out = response.getWriter()) {
            /* TODO output your page here. You may use following sample code. */
            out.println("<!DOCTYPE html>");
            out.println("<html>");
            out.println("<head>");
            out.println("<title>Servlet SearchOrder</title>");
            out.println("</head>");
            out.println("<body>");
            out.println("<h1>Servlet SearchOrder at " + request.getContextPath() + "</h1>");
            out.println("</body>");
            out.println("</html>");
        }
    }

    // <editor-fold defaultstate="collapsed" desc="HttpServlet methods. Click on the + sign on the left to edit the code.">
    /**
     * Handles the HTTP <code>GET</code> method.
     *
     * @param request servlet request
     * @param response servlet response
     * @throws ServletException if a servlet-specific error occurs
     * @throws IOException if an I/O error occurs
     */
    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        NumberFormat formatter = NumberFormat.getInstance(new Locale("vi", "VN"));
        response.setContentType("text/html;charset=UTF-8");
        PrintWriter out = response.getWriter();
        String keyword = request.getParameter("searchProduct");

        int orderType = Integer.parseInt(request.getParameter("orderType"));

        if (keyword != null && !keyword.trim().isEmpty()) {
            ArrayList<Products> products = DAOProducts.INSTANCE.searchProductsByName(keyword);

            if (products.isEmpty()) {
                out.println("<p>Không tìm thấy sản phẩm.</p>");
            } else {
                // Bạn có thể bọc toàn bộ danh sách trong 1 container lớn
                out.println("<div class='search-suggestions'>");

                for (Products product : products) {

                    // Lấy danh sách unitSizes từ bảng ProductUnits (đã trả về List<Integer>)
                    List<Integer> unitSizes = DAOProducts.INSTANCE.getProductUnitsByProductID(product.getID());

                    // Chuyển danh sách unitSizes thành chuỗi JavaScript Array
                    StringBuilder unitSizesStr = new StringBuilder("[");
                    for (int i = 0; i < unitSizes.size(); i++) {
                        unitSizesStr.append(unitSizes.get(i));
                        if (i < unitSizes.size() - 1) {
                            unitSizesStr.append(",");
                        }
                    }
                    unitSizesStr.append("]");

                    if (orderType == 0) {
                        // Xuất HTML với unitSizes được truyền vào addProductToOrder()
                        out.println("<div class='product-item' onclick=\"addProductToOrder('"
                                + product.getID() + "','"
                                + product.getProductName() + "',"
                                + unitSizesStr.toString() + ")\">");
                        // Container chứa nội dung
                        out.println("<div class='product-content'>");

                        // Hàng chứa tên, số lượng và giá
                        out.println("<div class='product-info'>");
                        out.println("<h3 class='product-name'>" + product.getProductName() + "</h3>");
                        out.println("<p class='product-quantity'>Số lượng: " + product.getQuantity() + "</p>");
                        out.println("<p class='product-price'>Giá Bán: " + formatter.format(product.getPrice()) + "</p>");
                        out.println("</div>");
                        out.println("</div>"); // đóng div .product-content
                        out.println("</div>"); // đóng div .product-item

                    } else {

                        // Xuất HTML với unitSizes được truyền vào addProductToOrder()
                        out.println("<div class='product-item' onclick=\"addProductToOrder('"
                                + product.getID() + "','"
                                + product.getProductName() + "','"
                                + (int) product.getPrice() + "','"
                                + product.getQuantity() + "',"
                                + unitSizesStr.toString() + ")\">");

                        // Container chứa nội dung
                        out.println("<div class='product-content'>");

                        // Hàng chứa tên, số lượng và giá
                        out.println("<div class='product-info'>");
                        out.println("<h3 class='product-name'>" + product.getProductName() + "</h3>" + 
                                "<p class='product-quantity'>Số lượng: " + product.getQuantity() + "</p>" +
                                "<p class='product-price'>Giá Bán: " + formatter.format(product.getPrice()) + "</p>");
                        out.println("</div>");
                        out.println("</div>"); // đóng div .product-content
                        out.println("</div>"); // đóng div .product-item
                    }

                }// kết thúc for

                out.println("</div>"); // đóng container lớn
            }
        }

    }

    /**
     * Handles the HTTP <code>POST</code> method.
     *
     * @param request servlet request
     * @param response servlet response
     * @throws ServletException if a servlet-specific error occurs
     * @throws IOException if an I/O error occurs
     */
    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        PrintWriter out = response.getWriter();
        request.setCharacterEncoding("UTF-8");
        HttpSession session = request.getSession();
        Users user = (Users) session.getAttribute("user");
        if (user == null) {
            response.sendRedirect("login");
            return;
        }

        String searchValue = request.getParameter("phone");
        ArrayList<Customers> customers = DAOCustomers.INSTANCE.findByNameOrPhone(searchValue);

        if (customers.isEmpty()) {
            out.println("<p>Không tìm thấy khách hàng.</p>");
            out.println("</div>");
        } else {
            // Bạn có thể bọc toàn bộ danh sách trong 1 container lớn

            out.println("<div class='search-suggestions'>");
            for (Customers customer : customers) {
                out.println("<div class='customer-item' onclick=\"selectCustomer('"
                        + customer.getID() + "', '"
                        + customer.getName() + "', '"
                        + customer.getPhone() + "', '"
                        + "')\">");

                out.println("<h3>" + customer.getName() + "</h3>" + "<p>SĐT: " + customer.getPhone() + "</p>");
                out.println("</div>");
            }
            out.println("</div>");

        }
    }

    /**
     * Returns a short description of the servlet.
     *
     * @return a String containing servlet description
     */
    @Override
    public String getServletInfo() {
        return "Short description";
    }// </editor-fold>

    public static void main(String[] args) {
        ArrayList<Customers> customers = DAOCustomers.INSTANCE.findByNameOrPhone("Việt");

        if (customers.isEmpty()) {
            System.out.println("<p>Không tìm thấy khach hang.</p>");
            System.out.println("<div class='customer-item add-new' onclick='openAddCustomerPopup()'>");
            System.out.println("<p>➕ Thêm khách hàng mới</p>");
            System.out.println("</div>");
        } else {
            // Bạn có thể bọc toàn bộ danh sách trong 1 container lớn

            System.out.println("<div class='search-suggestions'>");
            for (Customers customer : customers) {
                System.out.println("<div class='customer-item' onclick=\"selectCustomer('"
                        + customer.getID() + "', '"
                        + customer.getName() + "', '"
                        + customer.getPhone() + "', '"
                        + "')\">");

                System.out.println("<h3>" + customer.getName() + "</h3>");
                System.out.println("<p>SĐT: " + customer.getPhone() + "</p>");
                System.out.println("</div>");
            }
            System.out.println("</div>");

        }
    }

}
