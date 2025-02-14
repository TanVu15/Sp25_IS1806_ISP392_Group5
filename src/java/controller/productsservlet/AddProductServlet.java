package controller.productsservlet;

import dal.DAOProducts;
import model.Products;
import model.Users;
import jakarta.servlet.RequestDispatcher;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;

import java.io.IOException;
import java.sql.Date;

public class AddProductServlet extends HttpServlet {

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        RequestDispatcher dispatcher = request.getRequestDispatcher("HomePage/AddProduct.jsp");
        dispatcher.forward(request, response);
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        response.setContentType("text/html;charset=UTF-8");
        String productName = request.getParameter("productName");
        String description = request.getParameter("description");
        String image = request.getParameter("image");
        int price = Integer.parseInt(request.getParameter("price"));
        int quantity = Integer.parseInt(request.getParameter("quantity"));
        String location = request.getParameter("location");

        DAOProducts dao = DAOProducts.INSTANCE;
        Products product = new Products();
        HttpSession session = request.getSession();
        Users user = (Users) session.getAttribute("user");

        try {
            if (user != null) {
                product.setProductName(productName);
                product.setDescription(description);
                product.setImage(image);
                product.setPrice(price);
                product.setQuantity(quantity);
                product.setLocation(location);
                product.setCreateAt(new Date(System.currentTimeMillis())); // Thời gian tạo
                product.setCreateBy(user.getID()); // Người tạo

                dao.AddProducts(product, user.getID());
                response.sendRedirect("listproducts"); // Chuyển hướng về danh sách sản phẩm
            } else {
                request.setAttribute("errorMessage", "Thông tin không phù hợp.");
                RequestDispatcher dispatcher = request.getRequestDispatcher("HomePage/AddProduct.jsp");
                dispatcher.forward(request, response);
            }
        } catch (Exception e) {
            e.printStackTrace();
            RequestDispatcher dispatcher = request.getRequestDispatcher("HomePage/AddProduct.jsp");
            dispatcher.forward(request, response);
        }
    }

    @Override
    public String getServletInfo() {
        return "Servlet quản lý thêm sản phẩm";
    }
}