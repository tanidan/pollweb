package pollweb.controller;

import java.io.IOException;
import java.util.HashSet;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import pollweb.data.dao.PollWebDataLayer;
import pollweb.data.impl.PollImpl;
import pollweb.data.impl.QuestionImpl;
import pollweb.data.impl.UserImpl;
import pollweb.data.model.Poll;
import pollweb.data.model.Question;
import pollweb.data.util.DataException;
import pollweb.security.SecurityLayer;

/**
 * Servlet implementation class servlet_home
 */
public class PollCreate extends PollWebBaseController {

    private Poll tempPoll = null;

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
            this.getServletContext().getRequestDispatcher("/WEB-INF/JSP/poll_designing.jsp").forward(request, response);
        } else {
            //you are logged in, but you are not manager
            request.setAttribute("exception", new Exception("You have no rights to open this page"));
            action_error(request, response);
        }
    }

    private void action_create_users(HttpServletRequest request, HttpServletResponse response) throws IOException {
        String emails = request.getParameter("emails");
        String[] emailArray;

        emails = emails.replaceAll(" ", "");
        emails = emails.trim();

        emailArray = emails.split(",");

        try {
            for (String emailArray1 : emailArray) {
                if (emailArray1 != null) {
                    String password = new PasswordGenerator().generate(16); //generated for the user
                    UserImpl user = new UserImpl();
                    user.setEmail(emailArray1);
                    user.setPassword(password);
                    user.setPoll(tempPoll);

                    System.out.println(emailArray1);
                    //add to database
                    ((PollWebDataLayer) request.getAttribute("datalayer")).getUserDAO().storeUser(user);
                }
            }

            //redirect to homepage
            response.sendRedirect(request.getContextPath() + "/Home");

        } catch (DataException ex) {
            request.setAttribute("exception", ex);
            action_error(request, response);
        }
    }

    private void action_create_poll(HttpServletRequest request, HttpServletResponse response) throws IOException, ServletException {
        try {

            //create the poll
            //General informations of the Poll
            String poll_title = request.getParameter("poll_title");
            String opening_text = request.getParameter("opening_text");
            String closing_text = request.getParameter("closing_text");
            String poll_reserved = request.getParameter("reserved");
            
            tempPoll = new PollImpl();

            HttpSession s = SecurityLayer.checkSession(request);
            int managerID = (int) s.getAttribute("userid");
            pollweb.data.model.Manager manager = ((PollWebDataLayer) request.getAttribute("datalayer")).getManagerDAO().getManager(managerID);
            tempPoll.setManager(manager);

            tempPoll.setTitle(poll_title);
            tempPoll.setOpenText(opening_text);
            tempPoll.setCloseText(closing_text);

            Boolean reserved = poll_reserved.equals("yes");
            tempPoll.setReserved(reserved);
         
            ((PollWebDataLayer) request.getAttribute("datalayer")).getPollDAO().storePoll(tempPoll);
            
            int position = 1;
            while (!request.getParameter("question_name_" + position).equals("")) {
                QuestionImpl question = new QuestionImpl();
                question.setPosition(position);
                question.setText(request.getParameter("question_name_" + position));
                question.setNote(request.getParameter("question_note_" + position));
                question.setPoll(tempPoll);
                String question_mandatory = request.getParameter("mandatory_" + position);
                question.setType(request.getParameter("question_type_" + position));
                Boolean mandatory = question_mandatory.equals("yes");
                question.setMandatory(mandatory);
                if (question.getType().equals("single_choice") || question.getType().equals("multiple_choice")) {
                    String answer = "";
                    int option_numb = 0;
                    while (!request.getParameter("option_" + position + option_numb).equals("")) {
                    System.out.println("option_" + position + option_numb);
                        answer += request.getParameter("option_" + position + option_numb);
                        answer+= ',';
                        option_numb++;
                        if(option_numb == 4) {
                            break;
                        }
                    }
                    question.setAnswer(answer);

                }
                ((PollWebDataLayer) request.getAttribute("datalayer")).getQuestionDAO().storeQuestion(question);
                position++;
                if(position == 11) {
                    break;
                }
            }

            if (reserved) {
                if (s.getAttribute("role").equals("manager")) {
                    String log = "Logout";
                    request.setAttribute("log", log);
                    request.setAttribute("manager", "yes");
                getServletContext().getRequestDispatcher("/WEB-INF/JSP/poll_user.jsp").forward(request, response);
                } else {
                    //you are logged in, but you are not manager
                    request.setAttribute("exception", new Exception("You have no rights to open this page"));
                    action_error(request, response);
                }
            }
            
            //redirect at the end
            response.sendRedirect(request.getContextPath() + "/Home");
            
        } catch (DataException ex) {
            request.setAttribute("exception", ex);
            action_error(request, response);
        }
    }

    @Override
    protected void processRequest(HttpServletRequest request, HttpServletResponse response) throws ServletException {
        try {
            if (request.getParameter("OK") != null) {
                action_create_poll(request, response);
            } else if (request.getParameter("submit") != null) {
                action_create_users(request, response);
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
