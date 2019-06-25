/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
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
import pollweb.data.model.User;
import pollweb.data.util.DataException;
import pollweb.security.SecurityLayer;

/**
 *
 * @author venecia2
 */
public class Home extends PollWebBaseController {

    private void action_default(HttpServletRequest request, HttpServletResponse response) throws IOException, ServletException {
        try {
            String log;
            //check session
            HttpSession s = SecurityLayer.checkSession(request);
            if (s == null) {
                log = "Login";
                request.setAttribute("log", log);
            } else {
                log = "Logout";
                request.setAttribute("log", log);
                if (s.getAttribute("role") == "user") {
                    User user = ((PollWebDataLayer) request.getAttribute("datalayer")).getUserDAO().getUser((int) s.getAttribute("userid"));
                    if(user.getPoll() != null) {   
                        request.setAttribute("signed_poll", user.getPoll());
                    }
                } else if (s.getAttribute("role") == "manager") {
                        request.setAttribute("manager", "yes");
                } else if (s.getAttribute("role") == "admin") {
                        request.setAttribute("admin", "yes");
                }
            }       
            List<Poll> polls = ((PollWebDataLayer) request.getAttribute("datalayer")).getPollDAO().getUnsignedPolls();
            
            request.setAttribute("polls", polls);
            this.getServletContext().getRequestDispatcher("/WEB-INF/JSP/home.jsp").forward(request, response);

        } catch (DataException ex) {
            request.setAttribute("exception", ex);
            action_error(request, response);
        }
    }

    @Override
    protected void processRequest(HttpServletRequest request, HttpServletResponse response) throws ServletException {
        try {
            action_default(request, response);
        } catch (IOException ex) {
            request.setAttribute("exception", ex);
            action_error(request, response);
        }
    }
}
