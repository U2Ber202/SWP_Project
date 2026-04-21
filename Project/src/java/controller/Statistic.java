/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/JSP_Servlet/Servlet.java to edit this template
 */
package controller;

import dal.StatisticDAO;
import java.io.IOException;
import java.io.PrintWriter;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import model.Satistic;
import model.Account;
import model.Store;
import dal.StoreDAO;
import jakarta.servlet.http.HttpSession;
import java.util.List;
import util.RoleHelper;

/**
 *
 * @author phuoc
 */
@WebServlet(name = "Statistic", urlPatterns = {"/statistic"})
public class Statistic extends HttpServlet {

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
            out.println("<title>Servlet Statistic</title>");
            out.println("</head>");
            out.println("<body>");
            out.println("<h1>Servlet Statistic at " + request.getContextPath() + "</h1>");
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
        //processRequest(request, response);
        model.Account a = (model.Account) request.getSession().getAttribute("acc");
        if (a == null) {
            response.sendRedirect("login.jsp");
            return;
        }

        StatisticDAO dao = new StatisticDAO();
        try {
            HttpSession session = request.getSession();
            Account acc = (Account) session.getAttribute("acc");
            
            Satistic s = new Satistic();
            
            if (RoleHelper.isAdmin(acc)) {
                s = dao.getAll();
            } 
            else if (RoleHelper.isOwner(acc)) {
                StoreDAO storeDAO = new StoreDAO();
                Store store = storeDAO.getStoreByOwnerId(acc.getUid());
                if (store != null) {
                    s = dao.getForStore(store.getId());
                } else {
                    s = new Satistic(0,0,0,0);
                }
            } 
            else {
                response.sendRedirect("home");
                return;
            }
            
            request.setAttribute("totalOrders", s.getTotalOrders());
            request.setAttribute("totalSales", s.getTotalSales());
            request.setAttribute("totalOrdersMonth", s.getTotalOrdersMonth());
            request.setAttribute("totalSalesMonth", s.getTotalSalesMonth());
            request.getRequestDispatcher("statistic.jsp").forward(request, response);
        } catch (Exception e) {
            e.printStackTrace();
            response.sendRedirect("home");
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
        processRequest(request, response);
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
