// Author: Abigail Iliff
// Date Created: 4/25/24
// Editors: Abigail Iliff
// Date Last edited: 4/25/24
// Changes:
// Purpose: Servlet to perform the edit review functionality 

package NolaBytes.front_end.servlets;

import NolaBytes.Globals;
import NolaBytes.back_end.dbInteraction.DataBase;
import NolaBytes.back_end.dbInteraction.Review;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;

import java.io.IOException;
import java.io.PrintWriter;

@WebServlet(name = "ServletEditReview", urlPatterns = {"/ServletEditReview"})
public class ServletEditReview extends Servlet {

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
        // Get all required parameters to edit a review
        String reviewId = session.getAttribute("reviewId").toString();
        String userId = session.getAttribute("userID").toString();
        String newText = req.getParameter("edit-review-text");
        String newRate = req.getParameter("rate");

        if (reviewId != null && !reviewId.isEmpty() && newText != null && !newText.isEmpty() && newRate != null && !newRate.isEmpty()) {
            int rating = db.getReviewerRating(Integer.parseInt(reviewId));
            String reviewText = db.getReview(Integer.parseInt(reviewId));

            for (Review review : db.getUserReviews(Integer.parseInt(userId))) {
                if (review.getReviewID() == Integer.parseInt(reviewId)) {
                    // Messages for debugging 
                    pw.println("<html><body>");
                    pw.println("Old Review: " + review +
                            "<br/>OldReviewText: " + reviewText +
                            "<br/>OldRating: " + rating +
                            "<br/>" + "<br/>");
                    pw.println("New Review: " + newText + "<br/>" +
                            "New Rating: " + newRate);
                    pw.println("</body></html>");

                    // Placeholder to edit review -- commented out until properly tested and connected to edit review method
                    db.editReview(review, newText, Integer.parseInt(newRate));
                    res.sendRedirect(req.getContextPath() + "/ServletUserInformation");
                }
            }
        }
        pw.close();
    }
}
