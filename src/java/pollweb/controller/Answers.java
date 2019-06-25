package pollweb.controller;

import java.io.IOException;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.annotation.Resource;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import javax.sql.DataSource;
import pollweb.data.dao.PollWebDataLayer;
import pollweb.data.model.Answer;
import pollweb.data.model.Manager;
import pollweb.data.model.Poll;
import pollweb.data.model.User;
import pollweb.data.util.DataException;
import pollweb.security.SecurityLayer;


@WebServlet("/Test")
public class Answers extends PollWebBaseController {

//    private void action_default(HttpServletRequest request, HttpServletResponse response) throws IOException, ServletException {
//        List<Answer> results = new ArrayList();
//        try {
//            HttpSession s = SecurityLayer.checkSession(request);
//            int managerID = (int) s.getAttribute("userid");
//            Manager manager = ((PollWebDataLayer) request.getAttribute("datalayer")).getManagerDAO().getManager(managerID);
//            
//            List<Answer> answers = ((PollWebDataLayer) request.getAttribute("datalayer")).getAnswerDAO().getAnswers();
//            System.out.println(answers);
//           
//            for (Answer result:answers){
//                int manager_id = result.getPoll().getManager().getKey();
//                if(manager_id == managerID){
//                    results.add(result);
//                }
//            }
//            
//            request.setAttribute("results", results);
//            this.getServletContext().getRequestDispatcher("/WEB-INF/JSP/answers.jsp").forward(request, response);
//                        
//        } catch (DataException ex) {
//            request.setAttribute("exception", ex);
//            action_error(request, response);
//        }
//    }

    @Override
    protected void processRequest(HttpServletRequest request, HttpServletResponse response) throws ServletException {
        try {
            //action_default(request, response);   
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
            this.getServletContext().getRequestDispatcher("/WEB-INF/JSP/answers.jsp").forward(request, response);
        } catch (IOException ex) {
            request.setAttribute("exception", ex);
            action_error(request, response);
        } catch (DataException ex) {
            request.setAttribute("exception", ex);
            action_error(request, response);
        }
    }

}
