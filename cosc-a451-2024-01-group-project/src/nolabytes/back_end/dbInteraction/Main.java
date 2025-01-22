package NolaBytes.back_end.dbInteraction;
// Author: Matthew Levin
// Editors: Matthew Levin, Zaynab Zeini
// Date Last edited: 3/13/2024
// Date Created: 3/8/2024
// Purpose: main class (probably temporary for now) 

import NolaBytes.Globals;

import javax.management.InvalidAttributeValueException;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Properties;

public class Main {
    public static void main(String[] args) throws SQLException, FileNotFoundException {

        long startTime = System.nanoTime();

        DataBase db = new DataBase("postgres", "qxk71SFft4u0", "jdbc:postgresql://localhost:5432/postgres", true);

        // Commented out for now, until the config file is finished and everythings tested!
        try {
            String configFilePath = "nolaBytes/config.properties";
            FileInputStream propsInput = new FileInputStream(configFilePath);
            Properties prop = new Properties();
            prop.load(propsInput);
            // System.out.println(prop.getProperty("DB_USER"));
            propsInput.close();
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }


        ArrayList<Restaurant> restaurants = db.searchForRestaurant("the");

        for (Restaurant restaurant : restaurants) {

            System.out.println(restaurant.toString());

            for (Review review : db.getReviews(restaurant.getRestaurantID())) {
                System.out.println(review.toString());
            }
        }
        //testing for each function and using 3996 as a base review id
        //THIS IS JUST A TEMPORARY THING TO MAKE SURE IT WAS WORKING
        //getReviewID
        //getReview
        String review = db.getReview(3996);
        System.out.println("rev " + review);
        //getRestaurantReviewID
        System.out.println("Rest ID " + db.getRestaurantReviewID(3996));
        //getUserID
        System.out.println("userUD " + db.getUserID(3996));
        //getReviewerRating
        System.out.println("reviewer Rating " + db.getReviewerRating(3996));

        User user = null;
        try {
            user = db.getUser("CoulibiacCartelism", "mkzlWQX9oy"); // should not throw an exception
            System.out.println(user.getRealName());
            System.out.println(user.getReviewAmount());
            System.out.println(user.getId());

            db.getRestaurant(1);
            newReview review1 = new newReview("This is a review", 5);
            db.addReview(review1, restaurants.get(0), user);
            db.deleteReview(db.getLatestReview());
        } catch (InvalidAttributeValueException e) {
            e.printStackTrace();
        }

        try { // add a user and then delete it
            User user2 = db.addUser("poopman", "kajsdhflkjadshf", "poopman");
            System.out.println(user2.getRealName());
            System.out.println(user2.getReviewAmount());
            System.out.println(user2.getId());
            db.deleteUser(user2);
        } catch (InvalidAttributeValueException e) {
            e.printStackTrace();
        }

        // should throw an exception because this user is invalid
        try {
            user = db.getUser("poopman", "kajsdhflkjadshf");
            System.out.println(user.getRealName());
            System.out.println(user.getReviewAmount());
            System.out.println(user.getId());
        } catch (InvalidAttributeValueException e) {
            e.printStackTrace();
        }

        db.addRestaurant(new Restaurant(0, "McDonalds", "123-456-7890", "1234 Street",
                "Fast Food", "www.mcdonalds.com", "www.mcdonalds.com",
                new String[]{"Monday: 9-5", "Tuesday: 9-5", "Wednesday: 9-5", "Thursday: 9-5", "Friday: 9-5", "Saturday: 9-5", "Sunday: 9-5"}));

        // edit review test (after creating a new review under BanghyBootes)
        // Review test = new Review(5463, "test", 97, 2, 3, "Connor B.");

        // db.editReview(test, "poo", 1);


        // edit restaurant test
        // Restaurant testRestaurant = new Restaurant(
        //         33,
        //         "The Camellia Grill",
        //         "(504) 309-2679",
        //         "626 S Carrollton Ave New Orleans LA 70118",
        //         "American",
        //         "http://camelliagrillnola.com",
        //         "https://s3-media0.fl.yelpcdn.com/bphoto/D_3o6Xn7RwtpjzW9CEtAyA/l.jpg",

        //         new String[]{"8:00 AM - 7:00 PM", "8:00 AM - 7:00 PM", "8:00 AM - 7:00 PM",
        //                 "8:00 AM - 7:00 PM", "8:00 AM - 8:00 PM", "8:00 AM - 8:00 PM",
        //                 "8:00 AM - 8:00 PM"});

        // db.editRestaurant(testRestaurant, "John", "(504) 504-5044", "780 St", "New American", "http://google.com");

        long endTime = System.nanoTime();

        long duration = (endTime - startTime);

        Globals globals = new Globals(new FileInputStream("src/NolaBytes/config.properties"));
        System.out.println(globals.dbUrl);
        System.out.println(globals.dbName);
        System.out.println(globals.dbUser);
        System.out.println(globals.dbPassword);
        System.out.println(globals.dbAutoconnect);
        System.out.println(globals.testingMode);
        System.out.println(globals.testingRestarauntSearchTerm);
        for (Restaurant restaurant : globals.testingRestaurants) {
            System.out.println(restaurant.toString());
        }
        for (User user1 : globals.testingUsers) {
            System.out.println(user1.getId());
            System.out.println(user1.getRealName());
            System.out.println(user1.getReviewAmount());
            System.out.println(user1.getUsername());
            System.out.println(user1.getPassword());
        }
        for (Review rev : globals.testingReviews) {
            System.out.println(rev.toString());
        }

        System.out.println("Time to execute: " + duration / 1000000 + "ms");
    }
}