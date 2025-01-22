// Author: Hannah Patton
// Date created: 04/23/2024
// Editors: 
// Purpose: functionality for deleting reviews from the database


package NolaBytes.front_end.servlets;

import NolaBytes.Globals;
import NolaBytes.back_end.dbInteraction.DataBase;
import NolaBytes.back_end.dbInteraction.Review;
import NolaBytes.back_end.dbInteraction.User;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;

import javax.management.InvalidAttributeValueException;
import java.io.IOException;
import java.io.PrintWriter;

@WebServlet(name = "ServletDeleteReviewPage", urlPatterns = {"/ServletDeleteReview"})
public class ServletDeleteReview extends Servlet {

    DataBase db;
    Globals globals;

    public void init() {
        this.globals = getGlobals("config.properties");
        this.db = new DataBase(globals.dbUser, globals.dbPassword, globals.dbUrl, true);
        log(this.db.toString());
    }

    public void doGet(HttpServletRequest req, HttpServletResponse res) throws ServletException, IOException {
        res.setContentType("text/html");        // setting the content type
        PrintWriter pw = res.getWriter();           // get the stream to write the data
        HttpSession session = req.getSession();     // get the existing session

        // Get the review and user id from the session to delete review
        String reviewIdString = req.getParameter("reviewId");
        String userIdString = session.getAttribute("userID").toString();
        String username = session.getAttribute("userName").toString();

        // Check for the review corelating to the specified review id and delete that review
        if (reviewIdString != null && userIdString != null && !reviewIdString.isEmpty() && !userIdString.isEmpty()) {
            for (Review review : db.getUserReviews(Integer.parseInt(userIdString))) {
                if (review.getReviewID() == Integer.parseInt(reviewIdString)) {

                    // Messages for debugging
                    pw.println("UserId: " + userIdString + "<br/>");
                    pw.println("ReviewId: " + reviewIdString + "<br/>");
                    pw.println("Review To Delete: " + review + "<br/>");

                    // Delete Review Functionality
                    db.deleteReview(review);

                    try {
                        User updatedUser = db.getUser(username);
                        session.setAttribute("reviewAmount", updatedUser.getReviewAmount()); // Updates the session with the new review count
                    } catch (InvalidAttributeValueException e) {
                        e.printStackTrace();
                    }

                    res.sendRedirect(req.getContextPath() + "/ServletUserInformation");
                }
            }
        }
        pw.close();
    }
}