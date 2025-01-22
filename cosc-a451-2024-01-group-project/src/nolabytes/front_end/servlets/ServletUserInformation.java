// Author: Boris Alarcon
// Date Created: 4/15/24
// Editors: Abigail Iliff, Boris Alarcon
// Date Last edited: 4/25/24, 4/15/24
// Changes: Added functionality needed to populate the edit review form with specified review information
// Purpose: Servlet for the account information page

package NolaBytes.front_end.servlets;

import NolaBytes.Globals;
import NolaBytes.back_end.dbInteraction.DataBase;
import NolaBytes.back_end.dbInteraction.Review;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;

import java.io.IOException;
import java.io.PrintWriter;

import static java.lang.Integer.parseInt;

@WebServlet(name = "ServletUserInformation", urlPatterns = {"/ServletUserInformation"})
public class ServletUserInformation extends Servlet {

    String reviewTemplate;
    DataBase db;
    Globals globals;

    public void init() {
        this.globals = getGlobals("config.properties");
        this.db = new DataBase(globals.dbUser, globals.dbPassword, globals.dbUrl, true);
        log(this.db.toString());
        this.reviewTemplate = getFileString("reviewTemplate.html");
    }

    public void doGet(HttpServletRequest req, HttpServletResponse res) throws IOException {
        HttpSession session = req.getSession();
        if (session.getAttribute("userName") == null) {  // if there's not a user logged-in in the current session
            res.sendRedirect("signInPage.html");    // redirect to sign in
        } else {  // user is logged in
            res.setContentType("text/html");    // setting the content type
            PrintWriter pw = res.getWriter();     // get the stream to write the data

            String basePage = getFileString("accountDetails.html"); // read the html file

            // Replacing basic Tags
            basePage = basePage.replace("realnameTag", session.getAttribute("realName").toString());
            basePage = basePage.replace("usernameTag", session.getAttribute("userName").toString());
            basePage = basePage.replace("reviewAmountTag", session.getAttribute("reviewAmount").toString());

            StringBuilder builder; // declare stringbuilder to store the html cards as they are being appended
            int userID = parseInt(session.getAttribute("userID").toString());

            // Variables for putting restaurant name on review card and edit review form
            String restaurantName = "";
            String reviewText = "";
            int reviewRating = 0;
            int reviewIdNum = 0;

            if (db.getUserReviews(userID) != null && !db.getUserReviews(userID).isEmpty()) { // Check if there are reviews from user
                builder = new StringBuilder(); // initialize stringbuilder object, so we can use it to append the reviews
                for (Review review : db.getUserReviews(userID)) {
                    restaurantName = db.getRestaurantName(review.getRestaurantID()); // Get Restaurant name for given review
                    reviewTemplate = reviewTemplate.replace("<!--RestaurantNameToken-->", restaurantName); // Replace default token with restaurant name on review
                    reviewIdNum = review.getReviewID();

                    reviewTemplate = reviewTemplate.replace("<!--EditReviewButton-->", "<div class=\"edit-rev\" id=\"ed-rev\"><button class=\"edit-review-btn\" id=\"IdNumber\" onclick=\"editReviewPopup(this.id)\">Edit Review</button></div>"); // Replace default token with edit review button
                    reviewTemplate = reviewTemplate.replace("<!--DeleteReviewButton-->", "<div class=\"delete-rev\" id=\"del-rev\"><button class=\"delete-review-btn\" id=\"IdNumber\" onclick=\"deleteReview(this.id)\">Delete Review</button></div>"); // Replace default token with delete restaurant button
                    reviewTemplate = reviewTemplate.replace("IdNumber", Integer.toString(reviewIdNum)); // Replace default token with restaurant name on review

                    builder.append(review.insertIntoTemplate(reviewTemplate)); // insert reviews into reviewTemplate.html, and append that code to the stringbuilder
                    log(review.toString());


                    reviewTemplate = reviewTemplate.replace(restaurantName, "<!--RestaurantNameToken-->");  // Reset restaurant name back to default, so it can be replaced
                    reviewTemplate = reviewTemplate.replace(Integer.toString(reviewIdNum), "IdNumber"); // Reset IdNum, so it can be replaced be correct review Id 
                }
                log(builder.toString());
                basePage = basePage.replace("<!--ReviewToken-->", "<section class=\"my-reviews\">" + builder + "</section>"); // insert the reviews
            } else {
                basePage = basePage.replace("<!--ReviewToken-->", "<p1 class=\"paragraph\"><i>No reviews<i></p1>"); // no reviews were found; display no reviews message
            }

            // Functionality to get the needed information for the edit review functionality
            String reviewId = req.getParameter("reviewId");
            if (reviewId != null && !reviewId.isEmpty()) {
                for (Review review : db.getUserReviews(userID)) {
                    if (review.getReviewID() == Integer.parseInt(reviewId))
                        restaurantName = db.getRestaurantName(review.getRestaurantID()); // Get Restaurant name for given review
                }
                basePage = basePage.replace("*restaurant*", restaurantName); // Replace default name with restaurant name for edit review form
                session.setAttribute("reviewId", reviewId);

                // Get Review Text and Rating, set it on the edit review form
                reviewText = db.getReview(Integer.parseInt(reviewId));
                reviewRating = db.getReviewerRating(Integer.parseInt(reviewId));
                basePage = basePage.replace("Thoughts:", reviewText);
                basePage = basePage.replace("unchecked" + reviewRating, "checked");
            }

            // reset basepage to empty string
            pw.println(basePage); // writing html in the stream
            pw.close();     //closing the stream
        }
    }
}