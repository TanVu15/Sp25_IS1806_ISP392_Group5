package Controller.ProductsManager;

import DAL.DAOProducts;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;

import java.io.IOException;

public class DeleteProductServlet extends HttpServlet {

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        int deleteId = Integer.parseInt(request.getParameter("deleteid"));
        HttpSession session = request.getSession();
        int userId = (int) session.getAttribute("userid"); // Lấy ID người dùng từ session

        try {
            DAOProducts.INSTANCE.deleteProducts(deleteId, userId); // Gọi phương thức xóa sản phẩm
            response.sendRedirect("listproducts"); // Chuyển hướng về danh sách sản phẩm
        } catch (Exception e) {
            e.printStackTrace();
            request.setAttribute("message", "Đã xảy ra lỗi khi xóa sản phẩm.");
            request.getRequestDispatcher("ProductsManager/ListProducts.jsp").forward(request, response);
        }
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        // Không cần xử lý POST trong trường hợp này, có thể để trống hoặc ném lỗi
        response.sendError(HttpServletResponse.SC_METHOD_NOT_ALLOWED, "Chỉ hỗ trợ phương thức GET.");
    }

    @Override
    public String getServletInfo() {
        return "Servlet quản lý xóa sản phẩm";
    }
}