package pollweb.security;

import java.io.IOException;
import java.util.Calendar;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

public class SecurityLayer {

    //--------- SESSION SECURITY ------------        
    //this method executed a set of standard chacks on the current session.
    //If the session exists and is valid, it is rerutned, otherwise
    //the session is invalidated and the method returns null
    public static HttpSession checkSession(HttpServletRequest r) {
        boolean check = true;

        HttpSession s = r.getSession(false);
        //first, let's see is the sessione is active
        if (s == null) {
            return null;
        }

        //second, check is the session contains valid data
        if (s.getAttribute("userid") == null) {
            check = false;
            //check if the client ip chaged
        } else if ((s.getAttribute("ip") == null) || !((String) s.getAttribute("ip")).equals(r.getRemoteHost())) {
            check = false;
            //check if the session is timed out
        } else {
            //session start timestamp
            Calendar begin = (Calendar) s.getAttribute("inizio-sessione");
            //last action timestamp
            Calendar last = (Calendar) s.getAttribute("ultima-azione");
            //current timestamp
            Calendar now = Calendar.getInstance();
            if (begin == null) {
                check = false;
            } else {
                //seconds from the session start
                long secondsfrombegin = (now.getTimeInMillis() - begin.getTimeInMillis()) / 1000;
                //after three hours the session is invalidated
                if (secondsfrombegin > 3 * 60 * 60) {
                    check = false;
                } else if (last != null) {
                    //seconds from the last valid action
                    long secondsfromlast = (now.getTimeInMillis() - last.getTimeInMillis()) / 1000;
                    //after 30 minutes since the last action the session is invalidated                    
                    if (secondsfromlast > 30 * 60) {
                        check = false;
                    }
                }
            }
        }
        if (!check) {
            s.invalidate();
            return null;
        } else {
            //if che checks are ok, update the last action timestamp
            s.setAttribute("ultima-azione", Calendar.getInstance());
            return s;
        }
    }

    public static HttpSession createSession(HttpServletRequest request, String username, int userid, String role){
        HttpSession s = request.getSession(true);
        s.setAttribute("username", username);
        s.setAttribute("ip", request.getRemoteHost());
        s.setAttribute("inizio-sessione", Calendar.getInstance());
        s.setAttribute("userid", userid);
        s.setAttribute("role", role);
        return s;
    }
    
    public static HttpSession createSession(HttpServletRequest request, String username, int userid) {
        HttpSession s = request.getSession(true);
        s.setAttribute("username", username);
        s.setAttribute("ip", request.getRemoteHost());
        s.setAttribute("inizio-sessione", Calendar.getInstance());
        s.setAttribute("userid", userid);
        return s;
    }

    public static void disposeSession(HttpServletRequest request) {
        HttpSession s = request.getSession(false);
        if (s != null) {
            s.invalidate();
        }
    }

    //--------- DATA SECURITY ------------    
    //this function adds backslashes in front of
    //all the "malicious" charcaters, usually exploited
    //to perform SQL injection through form parameters
    public static String addSlashes(String s) {
        return s.replaceAll("(['\"\\\\])", "\\\\$1");
    }

    //this function removes the slashes added by addSlashes
    public static String stripSlashes(String s) {
        return s.replaceAll("\\\\(['\"\\\\])", "$1");
    }

    public static int checkNumeric(String s) throws NumberFormatException {
        //convert the string to a number, ensuring its validity
        if (s != null) {
            //if the conversion fails, an exception is raised
            return Integer.parseInt(s);
        } else {
            throw new NumberFormatException("String argument is null");
        }
    }

    //--------- CONNECTION SECURITY ------------
    //checks if the HTTPS protocol is in use
    public static boolean checkHttps(HttpServletRequest r) {
        return r.isSecure();
        //String httpsheader = r.getHeader("HTTPS");
        //return (httpsheader != null && httpsheader.toLowerCase().equals("on"));
    }

    //this function redirects the browser on the current address, but
    //with https protocol
    public static void redirectToHttps(HttpServletRequest request, HttpServletResponse response) throws ServletException {
        //estraiamo le parti della request url
        String server = request.getServerName();
        //int port = request.getServerPort();
        String context = request.getContextPath();
        String path = request.getServletPath();
        String info = request.getPathInfo();
        String query = request.getQueryString();

        //rebuild the url changing port and protocol AS SPECIFIED IN THE SERVER CONFIGURATION
        String newUrl = "https://" + server + ":8443" +  context + path + (info != null ? info : "") + (query != null ? "?" + query : "");
        try {
            //redirect
            response.sendRedirect(newUrl);
        } catch (IOException ex) {
            try {
                //in case of problems, first try to send a standard HTTP error message
                response.sendError(response.SC_INTERNAL_SERVER_ERROR, "Cannot redirect to HTTPS, blocking request");
            } catch (IOException ex1) {
                //otherwise, raise an exception
                throw new ServletException("Cannot redirect to https!");
            }
        }
    }
}
