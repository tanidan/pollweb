package pollweb.controller;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileWriter;
import java.io.IOException;
import java.util.List;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import pollweb.data.dao.PollWebDataLayer;
import pollweb.data.impl.AnswerImpl;
import pollweb.data.model.Answer;
import pollweb.data.model.Poll;
import pollweb.data.model.Question;
import pollweb.data.model.User;
import pollweb.data.util.DataException;
import pollweb.security.SecurityLayer;

/**
 * Servlet implementation class servlet_home
 */
public class PollShow extends PollWebBaseController {
    int poll_id = 0;
    private void action_default(HttpServletRequest request, HttpServletResponse response) throws IOException, ServletException {
        try {
            if (request.getParameter("pollID") != null) {
                System.out.println(request.getParameter("pollID"));
                poll_id = Integer.parseInt(request.getParameter("pollID"));
            } else {
                throw new NumberFormatException("String argument is null");
            }

            Poll poll = ((PollWebDataLayer) request.getAttribute("datalayer")).getPollDAO().getPoll(poll_id);
            List<Question> questions = ((PollWebDataLayer) request.getAttribute("datalayer")).getQuestionDAO().getQuestionsByPoll(poll);
            if (questions.isEmpty()) {
                request.setAttribute("exception", new Exception("No questions were found"));
                action_error(request, response);
            }

            HttpSession s = SecurityLayer.checkSession(request);
            if (poll.isReserved()) {
                String role = (String) s.getAttribute("role");
                int ID = (int) s.getAttribute("userid");
                if (role.equals("user")) {

                    User user = ((PollWebDataLayer) request.getAttribute("datalayer")).getUserDAO().getUser(ID);
                    if (user.getPoll().getKey() != poll.getKey()) {
                        request.setAttribute("exception", new Exception("You have no rights to visit this form"));
                        action_error(request, response);
                    }
                } else if (role.equals("manager") && poll.getManager().getKey() != ID) {
                    request.setAttribute("exception", new Exception("This poll is not belongs to you"));
                    action_error(request, response);
                }
            }
            String log;
            if (s == null) {
                log = "Login";
            } else if (s.getAttribute("role") == "manager") {
                request.setAttribute("manager", "yes");
                log = "Logout";
            } else if (s.getAttribute("role") == "admin") {
                request.setAttribute("admin", "yes");
                log = "Logout";
            } else if (s.getAttribute("role") == "user") {
                User user = ((PollWebDataLayer) request.getAttribute("datalayer")).getUserDAO().getUser((int) s.getAttribute("userid"));
                if (user.getPoll() != null) {
                    request.setAttribute("signed_poll", user.getPoll());
                }
                log = "Logout";
            } else {
                log = "Logout";
            }
            
            request.setAttribute("log", log);
            request.setAttribute("poll", poll);
            request.setAttribute("questions", questions);

            this.getServletContext().getRequestDispatcher("/WEB-INF/JSP/poll_example.jsp").forward(request, response);

        } catch (DataException ex) {
            request.setAttribute("exception", ex);
            action_error(request, response);
        }
    }

    private void action_modify(HttpServletRequest request, HttpServletResponse response) throws IOException, ServletException {
        response.sendRedirect(request.getContextPath() + "/Home");
    }
    
    private void action_write_answers(HttpServletRequest request, HttpServletResponse response) throws IOException, ServletException{
        AnswerImpl tempAnswer = null;
        String contextPath = getServletContext().getRealPath("/");
        String csvFilePath=contextPath+"\\test.csv";
        File answerFile = new File(csvFilePath);
        FileWriter fr = new FileWriter(answerFile);
        
        StringBuffer header = new StringBuffer();
        final String commaDelimeter = ",";        
        try{
        HttpSession s = SecurityLayer.checkSession(request);
        
            int user_id = 0; 
            if (s == null) {
            } else if (s.getAttribute("userid") != null) {
                user_id = (int) s.getAttribute("userid");
            }      
            
            Poll poll = ((PollWebDataLayer) request.getAttribute("datalayer")).getPollDAO().getPoll(poll_id);
            User user = ((PollWebDataLayer) request.getAttribute("datalayer")).getUserDAO().getUser(user_id);
            List<Question> questions = ((PollWebDataLayer) request.getAttribute("datalayer")).getQuestionDAO().getQuestionsByPoll(poll);
        
            try{
            for(Question question:questions){
                header.append(question.getText());
                header.append(commaDelimeter);
            }
            fr.append(header.toString());
            fr.append(System.lineSeparator());           

            for(Question question:questions){
                String answer = request.getParameter(String.valueOf(question.getPosition()));
                fr.append(answer);
                fr.append(commaDelimeter);
            }
            
            } catch (Exception ex) {
                request.setAttribute("exception", ex);
                action_error(request, response);           

            }finally {
                fr.flush();
                fr.close();            
        }
        System.out.println(answerFile);
        answerFile = new File(csvFilePath);

        tempAnswer = new AnswerImpl();
        tempAnswer.setUser(user);
        tempAnswer.setPoll(poll);
        tempAnswer.setFile(answerFile);
        ((PollWebDataLayer) request.getAttribute("datalayer")).getAnswerDAO().storeAnswer(tempAnswer);
        
        }catch (DataException ex) {
            request.setAttribute("exception", ex);
            action_error(request, response);
        }
        
        response.sendRedirect(request.getContextPath() + "/Home");
    }

    @Override
    protected void processRequest(HttpServletRequest request, HttpServletResponse response) throws ServletException {
        try {
            if (request.getParameter("Send") != null) {
                action_write_answers(request, response);
            } else {
                action_default(request, response);
            }
        } catch (IOException ex) {
            request.setAttribute("exception", ex);
            action_error(request, response);
        }
    }
}
