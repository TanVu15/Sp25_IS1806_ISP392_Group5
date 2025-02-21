package Controller.productsservlet;

import dal.DAOProducts;
import model.Products;
import model.Users;
import jakarta.servlet.RequestDispatcher;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.MultipartConfig;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import jakarta.servlet.http.Part;

import java.io.File;
import java.io.IOException;
import java.sql.Date;

@MultipartConfig
public class AddProductServlet extends HttpServlet {

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        RequestDispatcher dispatcher = request.getRequestDispatcher("ProductsManager/AddProduct.jsp");
        dispatcher.forward(request, response);
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        response.setContentType("text/html;charset=UTF-8");
        String productName = request.getParameter("productName");
        String description = request.getParameter("description");
        Part filePart = request.getPart("image"); // Nhận tệp hình ảnh
        String priceParam = request.getParameter("price");
        String location = request.getParameter("location");

        int price = 0;

        if (priceParam != null && !priceParam.isEmpty()) {
            price = Integer.parseInt(priceParam);
            if (price < 0) {
                request.setAttribute("errorMessage", "Giá không thể âm.");
                RequestDispatcher dispatcher = request.getRequestDispatcher("ProductsManager/AddProduct.jsp");
                dispatcher.forward(request, response);
                return;
            }
        }

        int quantity = 0; // Default quantity to 0 for new products
        String imageLink = "";

        // Lấy đường dẫn thư mục ảnh
        String imageDirectory = getServletContext().getRealPath("/Image/");

        // Lưu ảnh vào thư mục
        if (filePart != null && filePart.getSize() > 0) {
            String fileName = filePart.getSubmittedFileName();
            File dir = new File(imageDirectory);
            // Tạo thư mục nếu nó không tồn tại
            if (!dir.exists()) {
                dir.mkdir();
            }
            File file = new File(dir, fileName);
            filePart.write(file.getAbsolutePath());
            imageLink = "Image/" + fileName; // Đường dẫn lưu trữ ảnh
        }

        DAOProducts dao = DAOProducts.INSTANCE;
        Products product = new Products();
        HttpSession session = request.getSession();
        Users user = (Users) session.getAttribute("user");

        try {
            if (user != null) {
                product.setProductName(productName);
                product.setDescription(description);
                product.setImageLink(imageLink); 
                product.setPrice(price);
                product.setQuantity(quantity);
                product.setLocation(location);
                product.setCreateAt(new Date(System.currentTimeMillis())); 
                product.setCreateBy(user.getID()); 

                dao.AddProducts(product, user.getID());
                response.sendRedirect("listproducts"); 
            } else {
                request.setAttribute("errorMessage", "Thông tin không phù hợp.");
                RequestDispatcher dispatcher = request.getRequestDispatcher("ProductsManager/AddProduct.jsp");
                dispatcher.forward(request, response);
            }
        } catch (Exception e) {
            e.printStackTrace();
            RequestDispatcher dispatcher = request.getRequestDispatcher("ProductsManager/AddProduct.jsp");
            dispatcher.forward(request, response);
        }
    }

    @Override
    public String getServletInfo() {
        return "Servlet quản lý thêm sản phẩm";
    }
}