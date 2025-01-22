// Author: Omar Essa
// Date Created: 4/13/2024
// Editors: Omar Essa, Michael Boomgaart
// Date Last edited: 4/13/2024
// Purpose: takes in what is entered into the sign up page and creates the user then lets them into the website
// Changes: added a getreviewAmount to prevent a bug on signing up where you
//          cant access the 'my account' page because your review amount was null.


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

@WebServlet(name = "ServletSignUp", urlPatterns = {"/ServletSignUp"})
public class ServletSignUp extends Servlet {

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

        // take the inputs from the sign up form and get them as parameters
        String realname = req.getParameter("realname");
        String username = req.getParameter("username");
        String password = req.getParameter("password");
        try {
            db.getUser(username);

            res.sendRedirect(req.getContextPath() + "/signUpPage.html"); // redirect back to the sign up page


        } catch (InvalidAttributeValueException e) { // if the wrong credentials are entered
            try {
                pw.println("<p>" + username + " " + password + "</p>"); // writing html in the stream
                User user = db.addUser(username, password, realname); // add a new user in the database based on the inputted parameters
                System.out.println(user.getUsername());
                HttpSession session = req.getSession(); // create a new session
                session.setAttribute("userID", user.getId()); // set the user's existing ID as a parameter based on the newly created user
                session.setAttribute("userName", user.getUsername()); // set the user's existing username as a parameter based on the newly created user
                session.setAttribute("realName", user.getRealName()); // set the user's existing real name as a parameter based on the newly created user
                session.setAttribute("password", password);
                session.setAttribute("reviewAmount", Integer.toString(user.getReviewAmount()));
                res.sendRedirect(req.getContextPath() + "/HomePage"); // redirect back to the sign up page
            } catch (InvalidAttributeValueException e1) {
                e1.printStackTrace();
            }
            e.printStackTrace();
        }

        pw.close();


        //closing the stream
    }
}