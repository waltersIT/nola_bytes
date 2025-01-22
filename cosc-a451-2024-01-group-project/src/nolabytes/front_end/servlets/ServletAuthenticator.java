// Author: ???
// Date Created: ???
// Editors: Boris Alarcon, ???
// Date Last edited: 4/15/24, ???
// Changes: Removed the getFileString and added setAttribute for realName
// Purpose: Account credential authenticator for User
package NolaBytes.front_end.servlets;

import NolaBytes.Globals;
import NolaBytes.back_end.dbInteraction.DataBase;
import NolaBytes.back_end.dbInteraction.User;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;

import javax.management.InvalidAttributeValueException;
import java.io.IOException;
import java.io.PrintWriter;

@WebServlet(name = "ServletAuthenticator", urlPatterns = {"/ServletAuthenticator"})
public class ServletAuthenticator extends Servlet {

    String template;
    DataBase db;
    Boolean signedIn = false;
    Globals globals;


    public void init() {
        this.globals = getGlobals("config.properties");
        this.db = new DataBase(globals.dbUser, globals.dbPassword, globals.dbUrl, true);
        log(this.db.toString());
        this.template = getFileString("index.html");
    }

    public void doGet(HttpServletRequest req, HttpServletResponse res) throws ServletException, IOException {
        res.setContentType("text/html");  // setting the content type
        PrintWriter pw = res.getWriter(); // get the stream to write the data
        // HttpSession session = req.getSession();

        String username = req.getParameter("username");
        String password = req.getParameter("password");
        try {
            pw.println("<p>" + username + " " + password + "</p>"); // writing html in the stream
            User user = db.getUser(username, password);

            HttpSession session = req.getSession();
            session.setAttribute("userID", user.getId());
            session.setAttribute("userName", username);
            session.setAttribute("realName", user.getRealName());
            session.setAttribute("password", password);
            session.setAttribute("reviewAmount", user.getReviewAmount());

            res.sendRedirect(req.getContextPath() + "/HomePage");

        } catch (InvalidAttributeValueException e) {
            pw.println("<p>Invalid username or password</p>");
            res.sendRedirect(req.getContextPath() + "/signInPage.html?signedIn=" + signedIn);
            e.printStackTrace();
        }

        pw.close();


        //closing the stream
    }
}
