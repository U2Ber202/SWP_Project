/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/JSP_Servlet/Servlet.java to edit this template
 */
package controller;

import dal.OrderDAO;
import dal.ShippingDAO;
import java.io.IOException;
import java.io.PrintWriter;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import model.Account;
import model.Order;
import model.Shipping;
import model.Store;
import dal.StoreDAO;
import util.RoleHelper;

/**
 *
 * @author phuoc
 */
@WebServlet(name = "ShippingController", urlPatterns = {"/shipping"})
public class ShippingController extends HttpServlet {

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
            out.println("<title>Servlet ShippingController</title>");
            out.println("</head>");
            out.println("<body>");
            out.println("<h1>Servlet ShippingController at " + request.getContextPath() + "</h1>");
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
        // processRequest(request, response);
        Account acc = (Account) request.getSession().getAttribute("acc");
        if (!RoleHelper.canManageShipping(acc)) {
            response.sendRedirect("home");
            return;
        }
        ShippingDAO dao = new ShippingDAO();
        String id = request.getParameter("orderId");
        try {
            int orderId = Integer.parseInt(id);
            Order order;
            Shipping shipping;
            if (RoleHelper.isOwner(acc)) {
                Store store = new StoreDAO().getStoreByOwnerId(acc.getUid());
                if (store == null) {
                    response.sendRedirect("orders");
                    return;
                }
                order = new OrderDAO().getOrderByIdAndStoreId(orderId, store.getId());
                if (order == null) {
                    response.sendRedirect("orders");
                    return;
                }
                shipping = dao.getShippingByOrderIdAndStoreId(orderId, store.getId());
            } else {
                order = new OrderDAO().getOrderByIdAndShipperId(orderId, acc.getUid());
                if (order == null) {
                    response.sendRedirect("orders");
                    return;
                }
                shipping = dao.getShippingByOrderIdAndShipperId(orderId, acc.getUid());
            }
            if (order == null) {
                response.sendRedirect("orders");
                return;
            }
            request.setAttribute("shipping", shipping);
            request.setAttribute("orderId", orderId);
        } catch (NumberFormatException e) {
            response.sendRedirect("orders");
            return;
        }

        request.getRequestDispatcher("shiping.jsp").forward(request, response);
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
        // processRequest(request, response);
        Account acc = (Account) request.getSession().getAttribute("acc");
        if (!RoleHelper.canManageShipping(acc)) {
            response.sendRedirect("home");
            return;
        }
        String id = request.getParameter("id");
        String orderId = request.getParameter("orderId");
        String newStatus = request.getParameter("status");
        try {
            int shippingId = Integer.parseInt(id);
            int parsedOrderId = Integer.parseInt(orderId);
            if (RoleHelper.isOwner(acc)) {
                // Owner chỉ được xem, không được cập nhật trạng thái
                response.sendRedirect("shipping?orderId=" + parsedOrderId);
                return;
            } else {
                Order order = new OrderDAO().getOrderByIdAndShipperId(parsedOrderId, acc.getUid());
                if (order == null) {
                    response.sendRedirect("orders");
                    return;
                }
                new ShippingDAO().updateStatusByShipper(shippingId, acc.getUid(), newStatus);
                request.getSession().setAttribute("success", "Cập nhật trạng thái giao hàng thành công!");
            }
            response.sendRedirect("shipping?orderId=" + parsedOrderId);
        } catch (NumberFormatException e) {
            response.sendRedirect("orders");
            return;
        }
        
        // Redirect để làm mới danh sách
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

}
