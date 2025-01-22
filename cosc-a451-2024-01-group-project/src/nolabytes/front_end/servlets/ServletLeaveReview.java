// Author: Abigail Iliff
// Date Created: 04/11/2024
// Editors: Michael Boomgaart
// Date Last edited: 4/26/2024
// changes: Fixed a bug in which the users review count was not updating
//          on the 'My Account' page after leaving a review
// Purpose: Servlet to add leave review functionality

package NolaBytes.front_end.servlets;

import NolaBytes.Globals;
import NolaBytes.back_end.dbInteraction.DataBase;
import NolaBytes.back_end.dbInteraction.Restaurant;
import NolaBytes.back_end.dbInteraction.User;
import NolaBytes.back_end.dbInteraction.newReview;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;

import javax.management.InvalidAttributeValueException;
import java.io.IOException;
import java.io.PrintWriter;

@WebServlet(name = "ServletLeaveReview", urlPatterns = {"/ServletLeaveReview"})
public class ServletLeaveReview extends Servlet {

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
        HttpSession session = req.getSession(); // Gets the session for the current user

        String category = session.getAttribute("category").toString();
        // Get the parameters needed to leave the review, if they are a current parameter they are already a string, if not they must have .toString() to convert 
        String restaurantID = session.getAttribute("restaurantID").toString();
        String username = session.getAttribute("userName").toString();
        String password = session.getAttribute("password").toString();
        String reviewText = req.getParameter("review-text");
        String rate = req.getParameter("rate");

        // Create references to the Restaurant, User, and newReview classes, and instantiate newReview, so they can be passed to addReview method
        Restaurant restaurant;
        User user;
        newReview newReview = new newReview();

        // First check if all the required parameters exist, if they do not, you can't successfully create a new Review, so print error message
        if (restaurantID != null && !restaurantID.isEmpty()) {
            if (username != null && password != null && !username.isEmpty() && !password.isEmpty()) {
                if (reviewText != null && !reviewText.isEmpty() && rate != null && !rate.isEmpty()) {
                    try {
                        restaurant = db.getRestaurant(Integer.parseInt(restaurantID));   // Get the Restaurant object from the database using the restaurant ID
                        user = db.getUser(username, password);    // Get the User object from the database using username and password
                        // Messages to print to servlet page for debugging  
                        pw.println("<html><body>");
                        pw.println("Parameters Passed: <br/>" +
                                "Review: " + reviewText +
                                "<br/>Rating: " + rate +
                                "<br/>RestaurantID: " + restaurantID +
                                "<br/>Username: " + username +
                                "<br/>Password: " + password + "<br/><br/>");
                        pw.println("Returned Restaurant: " + restaurant + "<br/>");
                        pw.println("Returned User: " + user);
                        pw.println("</body></html>");

                        // Set the necessary fields for leaving a review, to create a new newReview object, then pass the newReview, Restaurant, and User objects to add a review
                        newReview.setReview(reviewText);
                        newReview.setReviewerRating(Integer.parseInt(rate));
                        db.addReview(newReview, restaurant, user);

                        // This updates the users session review count after leaving a review
                        try {
                            User updatedUser = db.getUser(username);
                            session.setAttribute("reviewAmount", updatedUser.getReviewAmount()); // Updates the session with the new review count
                        } catch (InvalidAttributeValueException e) {
                            e.printStackTrace();
                        }

                        // Redirect to the page the user was just on, so the leaveReview popup closes, and the user can still see the same page
                        res.sendRedirect(req.getContextPath() + "/ServletSearchPage?category=" + category + "&restaurantID=" + restaurantID);

                    } catch (InvalidAttributeValueException e) {
                        pw.println("<p>Invalid username or password</p>");
                        e.printStackTrace();
                    }
                } else
                    pw.println("<p>Missing Review or Rating. Please Fill Out the Entire Form<p>");
            }
        } else
            pw.println("<p>No parameters found<p>");
        pw.close();//closing the stream
    }
}


