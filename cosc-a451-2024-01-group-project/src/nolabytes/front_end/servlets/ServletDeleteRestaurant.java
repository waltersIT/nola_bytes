// Author: Zaynab Zeini
// Date created: 04/28/2024
// Editors: 
// Purpose: functionality for deleting restaurants from the database


package NolaBytes.front_end.servlets;

import NolaBytes.Globals;
import NolaBytes.back_end.dbInteraction.DataBase;
import NolaBytes.back_end.dbInteraction.Restaurant;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;

import java.io.IOException;
import java.io.PrintWriter;


@WebServlet(name = "ServletDeleteRestaurantPage", urlPatterns = {"/ServletDeleteRestaurant"})
public class ServletDeleteRestaurant extends Servlet {

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

        // Get the restaurant id from the session to delete restaurant
        String restaurantIDString = req.getParameter("restaurantID");

        if (restaurantIDString != null && !restaurantIDString.isEmpty()) {
            // for (Restaurant restaurant : db.getRestaurants(Integer.parseInt(restaurantIDString))) {
            Restaurant restaurant = db.getRestaurant(Integer.parseInt(restaurantIDString));
            // Messages for debugging 
            pw.println("RestaurantID: " + restaurantIDString + "<br/>");
            pw.println("Restaurant To Delete: " + restaurant + "<br/>");

            // Delete Review Functionality
            db.deleteRestaurant(restaurant);
            res.sendRedirect(req.getContextPath() + "/HomePage");


        }

        pw.close();
    }


}