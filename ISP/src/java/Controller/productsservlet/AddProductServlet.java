package Controller.productsservlet;

import dal.DAOCustomers;
import dal.DAOOrders;
import dal.DAOProducts;
import dal.DAOZones; 
import model.Products;
import model.Users;
import model.Zones;
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
import java.util.ArrayList;

@MultipartConfig
@WebServlet("/AddProduct")
public class AddProductServlet extends HttpServlet {

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        DAOOrders daoOrders = new DAOOrders();
        DAOCustomers daoCustomers = new DAOCustomers();
        DAOProducts daoProducts = new DAOProducts();
        HttpSession session = request.getSession();
        request.setAttribute("message", "");
        Users user = (Users) session.getAttribute("user");
        request.setAttribute("user", user);
        
        if (user.getShopID() == 0 && user.getRoleid() == 2) {
            RequestDispatcher requestDispatcher = request.getRequestDispatcher("createshop");
            requestDispatcher.forward(request, response);
            return;
        }
        
        // Get the list of zones
        DAOZones zoneDAO = new DAOZones();
        ArrayList<Zones> zones = zoneDAO.getAllZones();
        request.setAttribute("zones", zones); // Add the zones list to the request

        RequestDispatcher dispatcher = request.getRequestDispatcher("ProductsManager/AddProduct.jsp");
        dispatcher.forward(request, response);
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        response.setContentType("text/html;charset=UTF-8");
        String productName = request.getParameter("productName");
        String description = request.getParameter("description");
        Part filePart = request.getPart("image");
        String priceParam = request.getParameter("price");
        String[] zoneIDs = request.getParameterValues("zoneIDs"); // Get all zoneIDs

        int price = 0;

        // Validate price
        if (priceParam != null && !priceParam.isEmpty()) {
            price = Integer.parseInt(priceParam);
            if (price < 0) {
                request.setAttribute("errorMessage", "Price cannot be negative.");
                doGet(request, response);
                return;
            }
        }

        int quantity = 0; // Default quantity for the new product
        String imageLink = "";

        // Save image to directory
        String imageDirectory = getServletContext().getRealPath("/Image/");
        if (filePart != null && filePart.getSize() > 0) {
            String fileName = filePart.getSubmittedFileName();
            File dir = new File(imageDirectory);
            if (!dir.exists()) {
                dir.mkdir();
            }
            File file = new File(dir, fileName);
            filePart.write(file.getAbsolutePath());
            imageLink = "Image/" + fileName; // Path to the stored image
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
                product.setCreateAt(new Date(System.currentTimeMillis()));
                product.setCreateBy(user.getID());
                product.setShopID(user.getShopID()); // Set ShopID
                
                // Add product to the database
                int newProductId = dao.addProducts(product, user.getID());

                // Update Zones for the new product
                if (zoneIDs != null) {
                    for (String zoneID : zoneIDs) {
                        int zoneIdInt = Integer.parseInt(zoneID);
                        // Set the ProductID in the Zones table directly
                        dao.updateZoneWithProduct(zoneIdInt, newProductId);
                    }
                }

                response.sendRedirect("listproducts");
            } else {
                request.setAttribute("errorMessage", "User information is not valid.");
                doGet(request, response);
            }
        } catch (Exception e) {
            e.printStackTrace();
            request.setAttribute("errorMessage", "An error occurred. Please try again.");
            doGet(request, response);
        }
    }

    @Override
    public String getServletInfo() {
        return "Servlet for managing product addition.";
    }
}