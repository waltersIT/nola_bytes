package NolaBytes;

import NolaBytes.back_end.dbInteraction.Restaurant;
import NolaBytes.back_end.dbInteraction.Review;
import NolaBytes.back_end.dbInteraction.User;

import java.io.FileInputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Properties;

public class Globals {

    public final boolean testingMode;
    public String dbUrl;
    public String dbName;
    public String dbUser;
    public String dbPassword;

    public String testingDBUrl;
    public String testingDBName;
    public String testingDBUser;
    public String testingDBPassword;

    public boolean dbAutoconnect;
    // add new testing variables here and in the constructor
    public String testingRestarauntSearchTerm;
    public ArrayList<Restaurant> testingRestaurants = new ArrayList<Restaurant>();
    public ArrayList<User> testingUsers = new ArrayList<User>();
    public ArrayList<Review> testingReviews = new ArrayList<Review>();

    public Globals(FileInputStream propsInput) {
        Properties props = new Properties();
        try {
            props.load(propsInput);
            this.testingMode = "true".equals(props.getProperty("TESTING_MODE")); // check if testing mode is enabled
            if (this.testingMode) {
                this.dbUrl = props.getProperty("DB_URL");
                this.dbName = props.getProperty("DB_NAME");
                this.dbUser = props.getProperty("DB_USER");
                this.dbPassword = props.getProperty("DB_PASSWORD");
                this.dbAutoconnect = "true".equals(props.getProperty("DB_AUTOCONNECT"));
                this.testingRestarauntSearchTerm = props.getProperty("TESTING_RESTAURANT_SEARCH_TERM");
                this.testingDBUrl = props.getProperty("TESTING_DB_URL");
                this.testingDBName = props.getProperty("TESTING_DB_NAME");
                this.testingDBUser = props.getProperty("TESTING_DB_USER");
                this.testingDBPassword = props.getProperty("TESTING_DB_PASSWORD");


                while (props.getProperty("TESTING_RESTAURANT_" + testingRestaurants.size() + "_NAME") != null) { // Number of testing restaurants, reviews, and users are dynamic add as many to the config file as you need
                    Restaurant restaurant = new Restaurant(Integer.parseInt(props.getProperty("TESTING_RESTAURANT_" + testingRestaurants.size() + "_ID")),
                            props.getProperty("TESTING_RESTAURANT_" + testingRestaurants.size() + "_NAME"),
                            props.getProperty("TESTING_RESTAURANT_" + testingRestaurants.size() + "_PHONE_NUMBER"),
                            props.getProperty("TESTING_RESTAURANT_" + testingRestaurants.size() + "_ADDRESS"),
                            props.getProperty("TESTING_RESTAURANT_" + testingRestaurants.size() + "_CATEGORY"),
                            props.getProperty("TESTING_RESTAURANT_" + testingRestaurants.size() + "_WEBSITE"),
                            props.getProperty("TESTING_RESTAURANT_" + testingRestaurants.size() + "_IMAGE_URL"),
                            new String[]{
                                    props.getProperty("TESTING_RESTAURANT_" + testingRestaurants.size() + "_HOURS0"), props.getProperty("TESTING_RESTAURANT_" + testingRestaurants.size() + "_HOURS1"), props.getProperty("TESTING_RESTAURANT_" + testingRestaurants.size() + "_HOURS2"), props.getProperty("TESTING_RESTAURANT_" + testingRestaurants.size() + "_HOURS3"), props.getProperty("TESTING_RESTAURANT_" + testingRestaurants.size() + "_HOURS4"), props.getProperty("TESTING_RESTAURANT_" + testingRestaurants.size() + "_HOURS5"), props.getProperty("TESTING_RESTAURANT_" + testingRestaurants.size() + "_HOURS6")});

                    this.testingRestaurants.add(restaurant);
                }
                while (props.getProperty("TESTING_USER_" + testingUsers.size() + "_USERNAME") != null) {
                    User user = new User(
                            props.getProperty("TESTING_USER_" + testingUsers.size() + "_USERNAME"),
                            props.getProperty("TESTING_USER_" + testingUsers.size() + "_PASSWORD"),
                            Integer.parseInt(props.getProperty("TESTING_USER_" + testingUsers.size() + "_REVIEW_AMOUNT")),
                            props.getProperty("TESTING_USER_" + testingUsers.size() + "_REAL_NAME"),
                            Integer.parseInt(props.getProperty("TESTING_USER_" + testingUsers.size() + "_ID")));
                    this.testingUsers.add(user);
                }
                while (props.getProperty("TESTING_REVIEW_" + testingReviews.size() + "_ID") != null) {
                    Review review = new Review(
                            Integer.parseInt(props.getProperty("TESTING_REVIEW_" + testingReviews.size() + "_ID")),
                            props.getProperty("TESTING_REVIEW_" + testingReviews.size() + "_REVIEW"),
                            Integer.parseInt(props.getProperty("TESTING_REVIEW_" + testingReviews.size() + "_RESTAURANT_ID")),
                            Integer.parseInt(props.getProperty("TESTING_REVIEW_" + testingReviews.size() + "_USER_ID")),
                            Integer.parseInt(props.getProperty("TESTING_REVIEW_" + testingReviews.size() + "_RATING")),
                            props.getProperty("TESTING_REVIEW_" + testingReviews.size() + "_REVIEWER_NAME"));
                    this.testingReviews.add(review);
                }


            } else {
                this.dbUrl = props.getProperty("DB_URL");
                this.dbName = props.getProperty("DB_NAME");
                this.dbUser = props.getProperty("DB_USER");
                this.dbPassword = props.getProperty("DB_PASSWORD");
                this.dbAutoconnect = "true".equals(props.getProperty("DB_AUTOCONNECT"));
            }
        } catch (IOException e) {
            throw new RuntimeException(e);
        }

    }
}
