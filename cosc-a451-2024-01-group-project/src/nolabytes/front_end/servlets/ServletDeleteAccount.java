// Author: Omar Essa
// Date Created: 4/17/2024
// Editors: Omar Essa
// Date Last edited: 4/18/2024
// Purpose: Signs the user out and then deletes their account

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

@WebServlet(name = "Delete Account", urlPatterns = {"/ServletDeleteAccount"})
public class ServletDeleteAccount extends Servlet {

    String template;
    DataBase db;
    Globals globals;


    public void init() {
        this.globals = getGlobals("config.properties");
        this.db = new DataBase(globals.dbUser, globals.dbPassword, globals.dbUrl, true);
        log(this.db.toString());
    }

    public void doGet(HttpServletRequest req, HttpServletResponse res) throws ServletException, IOException {
        res.setContentType("text/html");  // setting the content type
        PrintWriter pw = res.getWriter(); // get the stream to write the data

        String enteredPassword = req.getParameter("password");
        pw.println("entered: " + enteredPassword);
        try {
            HttpSession session = req.getSession(); // get the existing session
            String username = session.getAttribute("userName").toString();
            String password = session.getAttribute("password").toString();
            pw.println("should be: " + password);
            if (enteredPassword.equals(password)) {
                User user = db.getUser(username, password);
                session.invalidate(); // invalidate the session and unbind any objects bound to it
                db.deleteUser(user);
                res.sendRedirect(req.getContextPath() + "/signout.html"); // redirect to the signout.html page, which asks the user to sign in again
            } else {
                res.sendRedirect(req.getContextPath() + "/deleteAccount.html");
            }
        } catch (InvalidAttributeValueException e) {
            pw.println("<p>Invalid username or password</p>");
            res.sendRedirect(req.getContextPath() + "/deleteAccount.html"); // redirect to the deleteAccount.html page, which asks for the password again to delete user
            e.printStackTrace();
        }

        pw.close();


        //closing the stream
    }

}
