// Author: Boris Alarcon
// Date Created: 3/26/2024
// Editors: Boris Alarcon, Matthew Levin, Michael Boomgaart
// Date Last edited: 4/16/24, 4/6/2024
// Changes: While a restaurant is selected, the restaurant moves to the top; did this by modifying the stringBuilder when appending restaurants into RestaurantToken.
// Redirect to sign in page if no valid user
// edited methods to allow a card to be detected and displayed
// Purpose: basic search servlet

package NolaBytes.front_end.servlets;

// Import required java libraries

import NolaBytes.Globals;
import NolaBytes.back_end.dbInteraction.DataBase;
import NolaBytes.back_end.dbInteraction.Restaurant;
import NolaBytes.back_end.dbInteraction.Review;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;

import java.io.IOException;
import java.io.PrintWriter;


@WebServlet(name = "ServletSearchPage", urlPatterns = {"/ServletSearchPage"})
public class ServletSearchPage extends Servlet {

    String restaurantTemplate;
    String reviewTemplate;
    DataBase db;
    Globals globals;

    public void init() {
        this.globals = getGlobals("config.properties"); // TODO: Change this to the correct path
        this.db = new DataBase(globals.dbUser, globals.dbPassword, globals.dbUrl, true);
        log(this.db.toString());
        this.restaurantTemplate = getFileString("cardTemplate.html");
        this.reviewTemplate = getFileString("reviewTemplate.html");

    }


    public void doGet(HttpServletRequest req, HttpServletResponse res) throws ServletException, IOException {
        HttpSession session = req.getSession();
        if (session.getAttribute("userName") == null) {  // if there's not a user logged-in in the current session
            res.sendRedirect("signInPage.html");    // redirect to sign in
        } else {  // user is logged in
            res.setContentType("text/html");    // setting the content type
            PrintWriter pw = res.getWriter();     // get the stream to write the data

            // Retrieving "category" parameter
            String category = req.getParameter("category");
            String restaurantID = req.getParameter("restaurantID");

            // Used to get the restaurants name from database
            String restaurantName = "";
            int resIDNum = 0;

            // HttpSession session = req.getSession();
            String basePage = getFileString("restaurantResults.html"); // read the html file
            basePage = basePage.replace("usernameTag", session.getAttribute("userName").toString());

            StringBuilder builder; // stringbuilder to store the html cards as they are being appended

            // Display restaurant search
            if (category != null && !category.isEmpty() && (restaurantID == null || restaurantID.isEmpty())) { // Check if "category" parameter is provided and display appropriate message also check if restaurantID is provided
                builder = new StringBuilder();
                for (Restaurant restaurant : db.searchForRestaurant(category)) {
                    // restaurantTemplate = restaurantTemplate.replace("<!--DeleteRestaurantButton-->", "<div class=\"delete-rev\" id=\"del-res\"><button class=\"delete-review-btn\" id=\"IdNumber\" onclick=\"deleteRestaurant(this.id)\">Delete Restaurant</button></div>"); // Replace default token with delete restaurant button
                    builder.append(restaurant.insertIntoTemplate(restaurantTemplate, false));
                    log(restaurant.toString());
                }
                basePage = basePage.replace("<!--RestaurantToken-->", builder.toString());

                // Display restaurant and reviews search
            } else if (restaurantID != null && !restaurantID.isEmpty() && category != null && !category.isEmpty()) {
                builder = new StringBuilder(); // initialize stringbuilder as empty, so we can contatenate the html cards
                for (Restaurant restaurant : db.searchForRestaurant(category)) {    // append the selected Restaurant first
                    boolean isSelected = String.valueOf(restaurant.getRestaurantID()).equals(restaurantID);
                    if (isSelected) {
                        // restaurantTemplate = restaurantTemplate.replace("<!--DeleteRestaurantButton-->", "<div class=\"delete-rev\" id=\"del-res\"><button class=\"delete-review-btn\" id=\"IdNumber\" onclick=\"deleteRestaurant(this.id)\">Delete Restaurant</button></div>"); // Replace default token with delete restaurant button
                        builder.append(restaurant.insertIntoTemplate(restaurantTemplate, isSelected)); // append the html card to the stringbuilder
                        log(restaurant.toString());
                    }
                }
                for (Restaurant restaurant : db.searchForRestaurant(category)) {    // append the non-selected Restaurants
                    boolean isSelected = String.valueOf(restaurant.getRestaurantID()).equals(restaurantID);
                    if (!isSelected) {
                        // restaurantTemplate = restaurantTemplate.replace("<!--DeleteRestaurantButton-->", "<div class=\"delete-rev\" id=\"del-res\"><button class=\"delete-review-btn\" id=\"IdNumber\" onclick=\"deleteRestaurant(this.id)\">Delete Restaurant</button></div>"); // Replace default token with delete restaurant button
                        builder.append(restaurant.insertIntoTemplate(restaurantTemplate, isSelected)); // append the html card to the stringbuilder
                        log(restaurant.toString());
                    }
                }
                basePage = basePage.replace("<!--RestaurantToken-->", builder.toString()); // replace the token in the html file with the stringbuilder

                builder = new StringBuilder(); // initialize another stringbuilder object at the same variable so we can use it to append the reviews
                for (Review review : db.getReviews(Integer.parseInt(restaurantID))) {
                    builder.append(review.insertIntoTemplate(reviewTemplate)); // append reviews to the stringbuilder
                    log(review.toString());
                }

                basePage = basePage.replace("<!--ReviewToken-->", builder.toString()); // insert the reviews

                resIDNum = Integer.parseInt(restaurantID);     // Make restaurantID parameter into an int
                restaurantName = db.getRestaurantName(resIDNum); // Get restaurant name from the database based on the ID
                basePage = basePage.replace("*restaurant*", restaurantName); // Replace the restaurant placeholder with actual restaurant name on review form
                session.setAttribute("restaurantID", resIDNum);
                session.setAttribute("category", category);
            } else {
                pw.println("<p>No search was done.</p>");
            }

            // reset basepage to empty string
            pw.println(basePage); // writing html in the stream
            pw.close();     //closing the stream
        }
    }
}