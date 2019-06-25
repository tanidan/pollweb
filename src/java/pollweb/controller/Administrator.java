package pollweb.controller;

import java.io.IOException;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import pollweb.data.dao.PollWebDataLayer;
import pollweb.data.impl.ManagerImpl;
import pollweb.data.model.Manager;
import pollweb.data.util.DataException;
import pollweb.security.SecurityLayer;

public class Administrator extends PollWebBaseController {

    private void action_default(HttpServletRequest request, HttpServletResponse response) throws IOException, ServletException {

        HttpSession s = SecurityLayer.checkSession(request);
        if (s == null) {
            //you have to login
            String log = "Login";
            request.setAttribute("log", log);
            request.setAttribute("message", "You have to log in");
            
            this.getServletContext().getRequestDispatcher("/WEB-INF/JSP/login.jsp").forward(request, response);
        } else if (s.getAttribute("role").equals("admin")) {
            //you are the admin
            String log = "Logout";
            request.setAttribute("log", log);
        request.setAttribute("admin", "yes");
            action_load_managers(request, response);
            this.getServletContext().getRequestDispatcher("/WEB-INF/JSP/admin.jsp").forward(request, response);
        } else {
            //you are logged in, but you are not admin
            request.setAttribute("exception", new Exception("You have no rights to open this page"));
            action_error(request, response);
        }

    }

    private void action_load_managers(HttpServletRequest request, HttpServletResponse response) throws IOException {
        try {

            List<Manager> managers = ((PollWebDataLayer) request.getAttribute("datalayer")).getManagerDAO().getManagers();
            request.setAttribute("managers", managers);

        } catch (DataException ex) {
            request.setAttribute("exception", ex);
            action_error(request, response);
        }
    }

    private void action_create_manager(HttpServletRequest request, HttpServletResponse response) throws IOException {

        String managerEmail = request.getParameter("email");
        String managerPassword = request.getParameter("password");

        if (!managerEmail.isEmpty() && !managerPassword.isEmpty()) {
            try {
                ManagerImpl manager = new ManagerImpl();
                manager.setEmail(managerEmail);
                manager.setPassword(managerPassword);

                //add to database
                ((PollWebDataLayer) request.getAttribute("datalayer")).getManagerDAO().storeManager(manager);

                //redirect to admin page
                //reload the page with the new data
                action_default(request, response);

            } catch (DataException | ServletException ex) {
                request.setAttribute("exception", ex);
                action_error(request, response);
            }

        } else {
            request.setAttribute("exception", new Exception("Creation failed"));
            action_error(request, response);
        }
    }

    @Override
    protected void processRequest(HttpServletRequest request, HttpServletResponse response) throws ServletException {
        try {
            if (request.getParameter("create") != null) {
                action_create_manager(request, response);
            } else {
                action_default(request, response);
            }
        } catch (IOException ex) {
            request.setAttribute("exception", ex);
            action_error(request, response);
        }
    }

    /**
     * Returns a short description of the servlet.
     *
     * @return a String containing servlet description
     */
    @Override
    public String getServletInfo() {
        return "Create manager and ??? servlet";
    }// </editor-fold>

}
