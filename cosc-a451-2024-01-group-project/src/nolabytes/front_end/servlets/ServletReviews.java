// Author: Matt Levin
// Date Created: ???
// Editors: Boris Alarcon, Matt Levin
// Date Last edited: 4/15/24, ???
// Changes: Implemented the ability to test getting all restaurant reviews from a specified user
// Purpose: basic servlet review retrieval tester

package NolaBytes.front_end.servlets;

import NolaBytes.Globals;
import NolaBytes.back_end.dbInteraction.DataBase;
import NolaBytes.back_end.dbInteraction.Review;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import java.io.IOException;
import java.io.PrintWriter;

@WebServlet(name = "ServletReviewsPage", urlPatterns = {"/ServletReviewsPage"})
public class ServletReviews extends Servlet {

    String template;
    DataBase db;
    Globals globals;

    public void init() {
        this.globals = getGlobals("config.properties");
        this.db = new DataBase(globals.dbUser, globals.dbPassword, globals.dbUrl, true);
        log(this.db.toString());
        this.template = getFileString("cardTemplate.html");
    }

    public void doGet(HttpServletRequest req, HttpServletResponse res) throws ServletException, IOException {
        res.setContentType("text/html"); // setting the content type
        PrintWriter pw = res.getWriter(); // get the stream to write the data

        int restaurantID = -1;  // set restaurantID = 0 if all fails
        try {   // try to get the restaurantID parameter
            restaurantID = Integer.parseInt(req.getParameter("restaurantID")); // Retrieving the restaurantID parameter so that we can pass it into the getReviews method
        } catch (Exception e) {
            e.printStackTrace();
        }

        int userID = -1;    // set userID = 0 if all fails
        try {   // try to get the userID parameter
            userID = Integer.parseInt(req.getParameter("userID")); // Retrieving the userID parameter (if any) so that we can pass it into the getUserReviews method
        } catch (Exception e) {
            e.printStackTrace();
        }

        // check the restaurantID and make sure it is not 0
        if (restaurantID > 0) {
            for (Review review : db.getReviews(restaurantID)) {
                pw.println("<p>" + review.toString() + "</p>");

            }
        } else if (userID > 0) { // if there's no specific restaurantID to display reviews, check userID in case wanting to display ALL restaurant reviews from a specified userID
            for (Review review : db.getUserReviews(userID)) {
                pw.println("<p>" + review.toString() + "</p>");
            }
        } else {
            pw.println("<p>No search was done.</p>");
        }

        // reset basepage to empty string
        //basePage = "";
        //for (String pagePart : splitPage) {
        //    basePage += pagePart;
        //}
        //pw.println(basePage); // writing html in the stream
        pw.close();     //closing the stream
    }
}
