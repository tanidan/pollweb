/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package pollweb.controller;

import java.io.IOException;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.annotation.Resource;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import javax.sql.DataSource;
import pollweb.data.dao.PollWebDataLayer;
import pollweb.data.model.User;
import pollweb.security.SecurityLayer;

/**
 *
 * @author venecia2
 */
public abstract class PollWebBaseController extends HttpServlet {

    @Resource(name = "jdbc/pollwebdb")
    private DataSource ds;

    protected abstract void processRequest(HttpServletRequest request, HttpServletResponse response) throws ServletException;

    private void processBaseRequest(HttpServletRequest request, HttpServletResponse response) {
        try (PollWebDataLayer datalayer = new PollWebDataLayer(ds)) {
            datalayer.init();
            request.setAttribute("datalayer", datalayer);
            processRequest(request, response);
        } catch (Exception ex) {
            ex.printStackTrace(); //for debugging only
//            (new FailureResult(getServletContext())).activate(
//                    (ex.getMessage() != null || ex.getCause() == null) ? ex.getMessage() : ex.getCause().getMessage(), request, response);
        }
    }

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        processBaseRequest(request, response);
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        processBaseRequest(request, response);
    }

    protected void action_error(HttpServletRequest request, HttpServletResponse response) {
        HttpSession s = SecurityLayer.checkSession(request);
        String log;
        if (s == null) {
            log = "Login";
        } else if (s.getAttribute("role").equals("manager")) {
            //you are a manager
            log = "Logout";
            request.setAttribute("manager", "yes");
        } else if (s.getAttribute("role").equals("admin")) {
            //you are the admin
            log = "Logout";
            request.setAttribute("admin", "yes");
        } else {
            log = "Logout";
        }
        request.setAttribute("log", log);

        String message;

        Exception ex = (Exception) request.getAttribute("exception");

        if (ex != null && ex.getMessage() != null) {
            message = ex.getMessage();
        } else if (ex != null) {
            message = ex.getClass().getName();
        } else {
            message = "Unknown Error";
        }
        request.setAttribute("message", message);
        try {
            this.getServletContext().getRequestDispatcher("/WEB-INF/JSP/error.jsp").forward(request, response);
        } catch (ServletException | IOException ex1) {
            Logger.getLogger(Login.class.getName()).log(Level.SEVERE, null, ex1);
        }
    }
}
