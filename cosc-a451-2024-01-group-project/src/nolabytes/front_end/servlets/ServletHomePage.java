// Author: ???
// Date Created: ???
// Editors: Boris Alarcon, ???
// Date Last edited: 4/15/24, ???
// Changes: Redirect to sign in page if no valid user
// Removed the getFileString
// Purpose: Servlet for the home page of NolaBytes

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

@WebServlet(name = "ServletHomePage", urlPatterns = {"/HomePage"})
public class ServletHomePage extends Servlet {

    DataBase db;
    Globals globals;

    public void init() {
        this.globals = getGlobals("config.properties");
        this.db = new DataBase(globals.dbUser, globals.dbPassword, globals.dbUrl, true);
        log(this.db.toString());
    }

    public void doGet(HttpServletRequest req, HttpServletResponse res) throws ServletException, IOException {
        HttpSession session = req.getSession();
        if (session.getAttribute("userName") == null) {  // if there's not a user logged-in in the current session
            res.sendRedirect("signInPage.html");    // redirect to sign in
        } else {  // user is logged in
            res.setContentType("text/html");    // setting the content type
            PrintWriter pw = res.getWriter();     // get the stream to write the data

            String basePage = getFileString("index.html"); // read the html file
            basePage = basePage.replace("usernameTag", session.getAttribute("userName").toString());

            pw.println(basePage); // writing html in the stream
            pw.close();     //closing the stream
        }
    }
}
