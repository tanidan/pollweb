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
import pollweb.data.model.Poll;
import pollweb.data.util.DataException;
import pollweb.security.SecurityLayer;

/**
 * Servlet implementation class servlet_home
 */
public class Manager extends PollWebBaseController {

    private void action_default(HttpServletRequest request, HttpServletResponse response) throws IOException, ServletException {

        HttpSession s = SecurityLayer.checkSession(request);
        if (s == null) {
            //you have to login
            String log = "Login";
            request.setAttribute("log", log);
            request.setAttribute("message", "You have to log in");
            
            this.getServletContext().getRequestDispatcher("/WEB-INF/JSP/login.jsp").forward(request, response);
        } else if (s.getAttribute("role").equals("manager")) {
            String log = "Logout";
            request.setAttribute("log", log);
            request.setAttribute("manager", "yes");
            action_load_polls(request, response);
        } else {
            //you are logged in, but you are not manager
            request.setAttribute("exception", new Exception("You have no rights to open this page"));
            action_error(request, response);
        }
    }

    private void action_load_polls(HttpServletRequest request, HttpServletResponse response) throws IOException, ServletException {
        try {
            HttpSession s = SecurityLayer.checkSession(request);
            int managerID = (int) s.getAttribute("userid");
            pollweb.data.model.Manager manager = 
                            ((PollWebDataLayer) request.getAttribute("datalayer")).
                            getManagerDAO().getManager(managerID);
            List<Poll> polls = ((PollWebDataLayer) request.getAttribute("datalayer")).getPollDAO().getPolls(manager);
            request.setAttribute("polls", polls);
            this.getServletContext().getRequestDispatcher("/WEB-INF/JSP/mana.jsp").forward(request, response);
        } catch (DataException ex) {
            request.setAttribute("exception", ex);
            action_error(request, response);
        }
    }
   
    @Override
    protected void processRequest(HttpServletRequest request, HttpServletResponse response) throws ServletException {
        try {
            if (request.getParameter("addPoll") != null) {
                
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
        return "Login and create servlet";
    }// </editor-fold>

}
