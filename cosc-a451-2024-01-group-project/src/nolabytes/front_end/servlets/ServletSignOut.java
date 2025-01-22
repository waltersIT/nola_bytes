// Author: Omar Essa
// Date Created: 4/14/2024
// Editors: Omar Essa
// Date Last edited: 4/14/2024
// Purpose: Signs the user out by invalidating the session, then lets the user sign in again

package NolaBytes.front_end.servlets;

import NolaBytes.Globals;
import NolaBytes.back_end.dbInteraction.DataBase;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;

import java.io.IOException;
import java.io.PrintWriter;

@WebServlet(name = "Sign Out", urlPatterns = {"/ServletSignOut"})
public class ServletSignOut extends Servlet {

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

        HttpSession session = req.getSession(); // get the existing session
        session.invalidate(); // invalidate the session and unbind any objects bound to it
        res.sendRedirect(req.getContextPath() + "/signout.html"); // redirect to the signout.html page, which asks the user to sign in again

        pw.close();


        //closing the stream
    }

}
