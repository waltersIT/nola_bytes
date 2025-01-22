// Author: Omar Essa
// Date Created: 4/23/2024
// Editors: Omar Essa, Jamil Sharif
// Date Last edited: 4/24/2024, 4/27/2024
// Purpose: Allows the user to change their password

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


@WebServlet(name = "Change Password", urlPatterns = {"/ServletChangePassword"})
public class ServletChangePassword extends Servlet {
    DataBase db;
    Globals globals;


    public void init() {
        this.globals = getGlobals("config.properties");
        this.db = new DataBase(globals.dbUser, globals.dbPassword, globals.dbUrl, true);
        log(this.db.toString());
    }

    public void doGet(HttpServletRequest req, HttpServletResponse res) throws ServletException, IOException {
        res.setContentType("text/html");
        PrintWriter pw = res.getWriter();

        String oldPassword = req.getParameter("old-password");
        String newPassword = req.getParameter("new-password");

        HttpSession session = req.getSession(); // get the existing session
        String username = session.getAttribute("userName").toString();
        String password = session.getAttribute("password").toString();
        pw.println("should be: " + password);
        if (oldPassword.equals(password) && !oldPassword.equals(newPassword)) {
            db.changeUserPassword(username, oldPassword, newPassword);
            res.sendRedirect(req.getContextPath() + "/ServletSignOut"); // redirect to the accountDetails.html page handled by the ServletUserInformation servlet
        } else {
            res.sendRedirect(req.getContextPath() + "/changePassword.html");
        }

        //closing the stream
        pw.close();
    }

}
