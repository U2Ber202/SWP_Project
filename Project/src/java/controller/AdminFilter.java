/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/JSP_Servlet/Filter.java to edit this template
 */

package controller;

import dal.StoreDAO;
import java.io.IOException;
import java.io.PrintStream;
import java.io.PrintWriter;
import java.io.StringWriter;
import jakarta.servlet.Filter;
import jakarta.servlet.FilterChain;
import jakarta.servlet.FilterConfig;
import jakarta.servlet.ServletException;
import jakarta.servlet.ServletRequest;
import jakarta.servlet.ServletResponse;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import util.RoleHelper;

public class AdminFilter implements Filter {

    private static final boolean debug = true;
    private FilterConfig filterConfig = null;

    public AdminFilter() {
    }

    private void doBeforeProcessing(ServletRequest request, ServletResponse response)
            throws IOException, ServletException {
        if (debug) {
            log("AdminFilter:DoBeforeProcessing");
        }
    }

    private void doAfterProcessing(ServletRequest request, ServletResponse response)
            throws IOException, ServletException {
        if (debug) {
            log("AdminFilter:DoAfterProcessing");
        }
    }

    @Override
    public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain)
            throws IOException, ServletException {

        HttpServletRequest req = (HttpServletRequest) request;
        HttpServletResponse res = (HttpServletResponse) response;
        HttpSession session = req.getSession(false);

        model.Account acc = (session != null) ? (model.Account) session.getAttribute("acc") : null;
        model.Store ownerStore = null;
        model.Store warehouseStore = null;

        if (acc == null || !acc.isActive()) {
            res.sendRedirect(req.getContextPath() + "/login");
            return;
        }

        if (!RoleHelper.isAdmin(acc)) {
            StoreDAO storeDAO = new StoreDAO();
            if (RoleHelper.isOwner(acc)) {
                ownerStore = storeDAO.getStoreByOwnerId(acc.getUid());
            }
            if (RoleHelper.isWarehouseManager(acc)) {
                warehouseStore = storeDAO.getStoreByWarehouseManagerId(acc.getUid());
                if (warehouseStore != null) {
                    session.setAttribute("warehouseStoreId", String.valueOf(warehouseStore.getId()));
                }
            }
            session.setAttribute("ownerStore", ownerStore);
            session.setAttribute("warehouseStore", warehouseStore);
        } else {
            session.removeAttribute("ownerStore");
            session.removeAttribute("warehouseStore");
        }

        String url = req.getServletPath();

        if (url.equals("/managerAccount") || url.equals("/manageStore")) {
            if (RoleHelper.isAdmin(acc)) {
                chain.doFilter(request, response);
            } else {
                res.sendRedirect(req.getContextPath() + "/home");
            }
            return;
        }

        if (url.equals("/manager") || url.equals("/managerCategory")) {
            if ((RoleHelper.isOwner(acc) && ownerStore != null) || (RoleHelper.isWarehouseManager(acc) && warehouseStore != null)) {
                chain.doFilter(request, response);
            } else {
                res.sendRedirect(req.getContextPath() + "/home");
            }
            return;
        }

        if (url.equals("/orders") || url.equals("/shipping") || url.equals("/orderdetail")) {
            if ((RoleHelper.isOwner(acc) && ownerStore != null) || RoleHelper.isShipper(acc)) {
                chain.doFilter(request, response);
            } else {
                res.sendRedirect(req.getContextPath() + "/home");
            }
            return;
        }

        if (url.equals("/statistic") || url.equals("/feedbacks")) {
            if (RoleHelper.isAdmin(acc) || (RoleHelper.isOwner(acc) && ownerStore != null)) {
                chain.doFilter(request, response);
            } else {
                res.sendRedirect(req.getContextPath() + "/home");
            }
            return;
        }

        chain.doFilter(request, response);
    }

    public FilterConfig getFilterConfig() {
        return this.filterConfig;
    }

    public void setFilterConfig(FilterConfig filterConfig) {
        this.filterConfig = filterConfig;
    }

    @Override
    public void destroy() {
    }

    @Override
    public void init(FilterConfig filterConfig) {
        this.filterConfig = filterConfig;
        if (filterConfig != null && debug) {
            log("AdminFilter:Initializing filter");
        }
    }

    @Override
    public String toString() {
        if (filterConfig == null) {
            return "AdminFilter()";
        }
        StringBuffer sb = new StringBuffer("AdminFilter(");
        sb.append(filterConfig);
        sb.append(")");
        return sb.toString();
    }

    private void sendProcessingError(Throwable t, ServletResponse response) {
        String stackTrace = getStackTrace(t);

        if (stackTrace != null && !stackTrace.equals("")) {
            try {
                response.setContentType("text/html");
                PrintStream ps = new PrintStream(response.getOutputStream());
                PrintWriter pw = new PrintWriter(ps);
                pw.print("<html>\n<head>\n<title>Error</title>\n</head>\n<body>\n");
                pw.print("<h1>The resource did not process correctly</h1>\n<pre>\n");
                pw.print(stackTrace);
                pw.print("</pre></body>\n</html>");
                pw.close();
                ps.close();
                response.getOutputStream().close();
            } catch (Exception ex) {
            }
        } else {
            try {
                PrintStream ps = new PrintStream(response.getOutputStream());
                t.printStackTrace(ps);
                ps.close();
                response.getOutputStream().close();
            } catch (Exception ex) {
            }
        }
    }

    public static String getStackTrace(Throwable t) {
        String stackTrace = null;
        try {
            StringWriter sw = new StringWriter();
            PrintWriter pw = new PrintWriter(sw);
            t.printStackTrace(pw);
            pw.close();
            sw.close();
            stackTrace = sw.getBuffer().toString();
        } catch (Exception ex) {
        }
        return stackTrace;
    }

    public void log(String msg) {
        filterConfig.getServletContext().log(msg);
    }
}