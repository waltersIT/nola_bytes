// Author: Boris Alarcon
// Date Created: 4/19/20
// Editors: Boris Alarcon, Michael Boomgaart
// Date Last edited: 4/19/24
// Changes: got the other users username

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

@WebServlet(name = "ServletPublicProfile", urlPatterns = {"/profile"})
public class ServletPublicProfile extends Servlet {

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

            String basePage = getFileString("publicProfile.html"); // read the html file

            // if userID is provided, has reviews on file, and is not a deletedUser
            if (req.getParameter("userID") != null && !req.getParameter("userID").isEmpty() && db.getUserReviews(Integer.parseInt(req.getParameter("userID"))) != null && !db.getUserReviews(Integer.parseInt(req.getParameter("userID"))).isEmpty() && !db.getUserReviews(Integer.parseInt(req.getParameter("userID"))).get(0).getReviewerName().equals("DELETED USER")) {
                int userID = Integer.parseInt(req.getParameter("userID")); // Retrieve userID parameter

                if (userID == Integer.parseInt(session.getAttribute("userID").toString())) {
                    res.sendRedirect("ServletUserInformation");    // redirect to my account page
                }

                Review review = db.getUserReviews(userID).get(0);   // Retrieve first review to extract personal information from

                // Replacing basic Tags
                basePage = basePage.replace("myUsernameTag", session.getAttribute("userName").toString());
                basePage = basePage.replace("realnameTag", review.getReviewerName());
                basePage = basePage.replace("usernameTag", db.getUserName(userID));
                basePage = basePage.replace("reviewAmountTag", String.valueOf(db.getUserReviews(userID).size()));

                StringBuilder builder; // declare stringbuilder to store the html cards as they are being appended
                String restaurantName = "";
                builder = new StringBuilder(); // initialize stringbuilder object, so we can use it to append the reviews
                for (Review r : db.getUserReviews(userID)) {
                    restaurantName = db.getRestaurantName(review.getRestaurantID()); // Get Restaurant name for given review
                    reviewTemplate = reviewTemplate.replace("<!--RestaurantNameToken-->", restaurantName); // Replace default token with restaurant name on review

                    builder.append(r.insertIntoTemplate(reviewTemplate)); // insert reviews into reviewTemplate.html, and append that code to the stringbuilder
                    log(r.toString());

                    reviewTemplate = reviewTemplate.replace(restaurantName, "<!--RestaurantNameToken-->");  // Reset restaurant name back to default, so it can be replaced
                }
                log(builder.toString());
                basePage = basePage.replace("<!--ReviewToken-->", "<section class=\"my-reviews\">" + builder + "</section>"); // insert the reviews

                // reset basepage to empty string
                pw.println(basePage); // writing html in the stream
                pw.close();     //closing the stream
            } else { // username doesn't exist
                res.sendRedirect("userDoesNotExist.html");    // redirect to userDoesNotExist.html
            }
        }
    }
}