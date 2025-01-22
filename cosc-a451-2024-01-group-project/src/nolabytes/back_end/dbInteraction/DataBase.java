package NolaBytes.back_end.dbInteraction;
// Author: Matthew Levin
// Editors: Matthew Levin, Walter Devas, Bryana, Hannah Patton, Zaynab Zeini, Patrick Carter, Jada Farrell, Michael Boomgaart
// Date Last edited: 4/29/2024
// Date Created: 3/8/2024
// Purpose: to abstract the database and provide a way to interact with it
// Changes: Added a method to delete a restaurant

import javax.management.InvalidAttributeValueException;
import java.sql.*;
import java.util.ArrayList;

public class DataBase {
    private final String dbUsername;
    private final String dbPassword;
    private final String dbUrl;
    boolean connected;
    private Connection connection = null;

    /**
     * Constructor to auto-connect to the database
     *
     * @param username    - passed the username used to access the database
     * @param password    - passed the password used to access the database
     * @param url         - passed the url to access the database, includes jdbc prefix, the database host, the port and the database name/username
     * @param autoconnect - passed either true or false to determine if the database can be auto-connected to
     */
    public DataBase(String username, String password, String url, boolean autoconnect) {

        try {
            Class.forName("org.postgresql.Driver");
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        }

        this.dbUsername = username;
        this.dbPassword = password;
        this.dbUrl = url;

        if (autoconnect) {
            this.connect();
        }
    }

    /**
     * Method to search for a restaurant based on a query
     *
     * @param searchQuery - the query that is passed to search for a restaurant of type string (restaurant name)
     * @return - returns an array of restaurants that matched the search query
     **/
    public ArrayList<NolaBytes.back_end.dbInteraction.Restaurant> searchForRestaurant(String searchQuery) {
        searchQuery = "%" + searchQuery + "%"; // add wildcards to the search query
        ArrayList<NolaBytes.back_end.dbInteraction.Restaurant> returnArray = new ArrayList<NolaBytes.back_end.dbInteraction.Restaurant>(); // declare an arraylist to return
        try {
            PreparedStatement st = connection.prepareStatement( // cool sql query that does a loose search for a restaurant based on name and address (case-insensitive)
                    "SELECT restaurant.restaurant_id, name, phone, address, website, mon, tue, wed, thu, fri, sat, sun, img_url, category,\n" +
                            "(CASE WHEN name ILIKE ? THEN 1 ELSE 0 END +\n" +
                            "CASE WHEN restaurant.category ILIKE ? THEN 1 ELSE 0 END) AS relevance_score\n" +
                            "FROM yelp.restaurant\n" +
                            "inner join yelp.opening_hours as hours on restaurant.restaurant_id = hours.restaurant_id\n" +
                            "left join yelp.photo p on restaurant.restaurant_id = p.restaurant_id\n" +
                            "WHERE name ILIKE ? \n" +
                            "OR restaurant.category ILIKE ? \n" +
                            "ORDER BY relevance_score DESC"
            );
            st.setString(1, searchQuery);
            st.setString(2, searchQuery);
            st.setString(3, searchQuery);
            st.setString(4, searchQuery);

            ResultSet rs = st.executeQuery();

            // put each result returned from the database into a restaurant object and add it to the arraylist
            while (rs.next()) {
                int restaurantID = rs.getInt("restaurant_id");
                String name = rs.getString("name");
                String phone = rs.getString("phone");
                String address = rs.getString("address");
                String website = rs.getString("website");
                String imageURL = rs.getString("img_url");
                String category = rs.getString("category");
                String[] hours = {rs.getString("mon"), rs.getString("tue"), rs.getString("wed"), rs.getString("thu"), rs.getString("fri"), rs.getString("sat"), rs.getString("sun")};

                returnArray.add(new NolaBytes.back_end.dbInteraction.Restaurant(restaurantID, name, phone, address, category, website, imageURL, hours));
            }
            rs.close();
            st.close();
        } catch (SQLException exception) { // catch any sql exceptions and print the stack trace
            exception.printStackTrace();
        }
        return returnArray;
    }

    /**
     * Method to find a user based on their login credentials, and check that the credentials are entered correctly
     *
     * @param username - passed a username entered by the user
     * @param password - passed a password entered by the user
     * @return - returns a "new user object": contains their username, their password, how many reviews they made, their real name, and their user ID
     * @throws InvalidAttributeValueException
     */
    public NolaBytes.back_end.dbInteraction.User getUser(String username, String password) throws InvalidAttributeValueException {
        try { // use a prepared statement to prevent sql injection
            PreparedStatement st = connection.prepareStatement("SELECT * FROM yelp.user where username = ? and password = ?"); // check the database for a matching username and password (should only return one result)
            st.setString(1, username); // add the username to the prepared statement
            st.setString(2, password); // add the password to the prepared statement

            ResultSet rs = st.executeQuery(); // execute the prepared statement and get a result set

            if (rs.next() && rs.getString("username").equals(username) && rs.getString("password").equals(password)) { // double-check the username and password to make sure they match
                return new NolaBytes.back_end.dbInteraction.User(rs.getString("username"), // if they match, return a new user object with the data from the database
                        rs.getString("password"),
                        rs.getInt("review_count"),
                        rs.getString("real_name"),
                        rs.getInt("user_id"));
            } else {
                throw new InvalidAttributeValueException("Invalid username or password"); // if not throw an exception
            }

        } catch (SQLException exception) {
            throw new InvalidAttributeValueException("Database does not contain a matching username and password");
        }
    }

    /**
     * Method to find a user to see if it already exists with a given username (to prevent duplicate usernames)
     *
     * @param username - passed a username entered by the user
     * @return - returns a "new user object": contains their username, their password, how many reviews they made, their real name, and their user ID
     * @throws InvalidAttributeValueException
     */
    public NolaBytes.back_end.dbInteraction.User getUser(String username) throws InvalidAttributeValueException {
        try { // use a prepared statement to prevent sql injection
            PreparedStatement st = connection.prepareStatement("SELECT * FROM yelp.user where username = ?"); // check the database for a matching username and password (should only return one result)
            st.setString(1, username); // add the username to the prepared statement

            ResultSet rs = st.executeQuery(); // execute the prepared statement and get a result set

            if (rs.next() && rs.getString("username").equals(username)) { // double-check the username and password to make sure they match
                return new NolaBytes.back_end.dbInteraction.User(rs.getString("username"), // if they match, return a new user object with the data from the database
                        rs.getString("password"),
                        rs.getInt("review_count"),
                        rs.getString("real_name"),
                        rs.getInt("user_id"));
            } else {
                throw new InvalidAttributeValueException("Invalid username or password"); // if not throw an exception
            }

        } catch (SQLException exception) {
            throw new InvalidAttributeValueException("Database does not contain a matching username and password");
        }
    }

    /**
     * Method to return all existing users
     *
     * @return - returns an ArrayList of Users
     * @throws SQLException
     */
    public ArrayList<NolaBytes.back_end.dbInteraction.User> getAllUsers() throws InvalidAttributeValueException {
        ArrayList<NolaBytes.back_end.dbInteraction.User> returnArray = null;
        try { // use a prepared statement to prevent sql injection
            PreparedStatement st = connection.prepareStatement("SELECT * FROM yelp.user WHERE username IS NOT NULL"); // find all existing, not deleted users in the User table

            ResultSet rs = st.executeQuery(); // execute the prepared statement and get a result set
            returnArray = new ArrayList<NolaBytes.back_end.dbInteraction.User>();

            while (rs.next()) { // iterate through the ResultSet
                String username = rs.getString("username");
                String password = rs.getString("password");
                int reviewCount = rs.getInt("review_count");
                String realname = rs.getString("real_name");
                int userID = rs.getInt("user_id");
                returnArray.add(new NolaBytes.back_end.dbInteraction.User(username, password, reviewCount, realname, userID));
            }

            return returnArray; // return the ArrayList

        } catch (SQLException exception) {
            return returnArray; // should return null if there is an exception
        }
    }

    /**
     * Method to connect to the database
     *
     * @return - returns true if the connection the database worked, returns false otherwise
     */
    public boolean connect() {
        try {
            this.connection = DriverManager.getConnection(dbUrl, dbUsername, dbPassword); // passes login info to the driver
            this.connected = true;
            return true;
        } catch (Exception exception) {
            this.connected = false;
            exception.printStackTrace();
            return false;
        }
    }

    /**
     * Method to get the hours for each restaurant from the database
     *
     * @param restaurantID - passed a restaurant ID, to be able to find the hours for the given restaurant
     * @return - returns an array of strings that are the hours for a given restaurant
     */
    public String[] getRestaurantHours(int restaurantID) {
        String[] returnString = new String[7]; // initializing return array

        try {
            Statement st = connection.createStatement(); // providing access to the database to execute queries
            ResultSet rs = st.executeQuery("SELECT * FROM yelp.opening_hours where opening_hours.restaurant_id = " + restaurantID); // constructing query to match restaurantID to the table and storing the resulting set
            rs.next(); // move to the next row provided there's only one matching restaurant

            for (int i = 1; i <= 7; i++) {
                returnString[i - 1] = rs.getString(i); // index starts from 1 and iterates to 7 (days of the week) and retrieves value from matching column
            }

            rs.close(); //release
            st.close(); // release

            return returnString;

        } catch (SQLException exception) { // sanity check
            exception.printStackTrace();
        }
        return returnString;
    }

    /**
     * Method to get the restaurant ID for a given restaurant
     *
     * @param restaurantName - passed the name of the restaurant, to find the corresponding restaurant ID
     * @return - returns the restaurant ID as an integer
     */
    public int getRestaurantID(String restaurantName) {
        int returnInt = -1;
        try {
            Statement st = connection.createStatement();
            ResultSet rs = st.executeQuery("SELECT restaurant_id FROM yelp.restaurant where restaurant.name = " + restaurantName);
            rs.next();
            returnInt = rs.getInt(1);
            rs.close();
            st.close();
            return returnInt;
        } catch (SQLException exception) {
            exception.printStackTrace();
        }
        return returnInt;
    }

    /**
     * Method to get the restaurant name
     *
     * @param restaurantID - passed the restaurant ID, so the corresponding restaurant name can be found
     * @return - returns the restaurant name as a string
     */
    public String getRestaurantName(int restaurantID) {
        String returnString = "";
        try {
            Statement st = connection.createStatement();
            ResultSet rs = st.executeQuery("SELECT name FROM yelp.restaurant where restaurant.restaurant_id = " + restaurantID);
            rs.next();
            returnString = rs.getString(1);
            rs.close();
            st.close();
            return returnString;
        } catch (SQLException exception) {
            exception.printStackTrace();
        }
        return returnString;
    }

    /**
     * Method to get a restaurant phone number for a given restaurant
     *
     * @param restaurantID - passed a restaurant ID, so the corresponding restaurant phone number can be found
     * @return - returns the restaurant phone number as a string
     */
    public String getRestaurantPhoneNumber(int restaurantID) {
        String returnString = "";
        try {
            Statement st = connection.createStatement();
            ResultSet rs = st.executeQuery("SELECT phone FROM yelp.restaurant where restaurant.restaurant_id = " + restaurantID);
            rs.next();
            returnString = rs.getString(1);
            rs.close();
            st.close();
            return returnString;
        } catch (SQLException exception) {
            exception.printStackTrace();
        }
        return returnString;
    }

    /**
     * Method to get a restaurant address for a given restaurant
     *
     * @param restaurantID - passed a restaurant ID, so the corresponding restaurant address can be found
     * @return - returns the restaurant address as a string
     */
    public String getRestaurantAddress(int restaurantID) {
        String returnString = "";
        try {
            Statement st = connection.createStatement();
            ResultSet rs = st.executeQuery("SELECT address FROM yelp.restaurant where restaurant.restaurant_id = " + restaurantID);
            rs.next();
            returnString = rs.getString(1);
            rs.close();
            st.close();
            return returnString;
        } catch (SQLException exception) {
            exception.printStackTrace();
        }
        return returnString;
    }

    /**
     * Method to get a restaurant website for the given restaurant
     *
     * @param restaurantID - passed a restaurant ID, so the corresponding restaurant website can be found
     * @return - returns a restaurant website as a string
     */
    public String getRestaurantWebsite(int restaurantID) {
        String returnString = "";
        try {
            Statement st = connection.createStatement();
            ResultSet rs = st.executeQuery("SELECT website FROM yelp.restaurant where restaurant.restaurant_id = " + restaurantID);
            rs.next();
            returnString = rs.getString(1);
            rs.close();
            st.close();
            return returnString;
        } catch (SQLException exception) {
            exception.printStackTrace();
        }
        return returnString;
    }

    /**
     * Method to get an image URL for the given restaurant
     *
     * @param restaurantID - passed a restaurant ID, so the corresponding image URL can be found
     * @return - returns a restaurant image URL as a string
     */
    public String getRestaurantImageURL(int restaurantID) {
        String returnString = "";
        try {
            Statement st = connection.createStatement();
            ResultSet rs = st.executeQuery("SELECT photo.img_url FROM yelp.photo where photo.restaurant_id = " + restaurantID);
            rs.next();
            returnString = rs.getString(1);
            rs.close();
            st.close();
            return returnString;
        } catch (SQLException exception) {
            exception.printStackTrace();
        }
        return returnString;
    }

    /**
     * Method to find a review based on a review ID
     *
     * @param reviewID - passed a review ID
     * @return - returns a review as a string
     */
    public String getReview(int reviewID) {
        String returnString = "";
        try {
            Statement st = connection.createStatement();
            ResultSet rs = st.executeQuery("SELECT review FROM yelp.review WHERE review.review_id = " + reviewID);
            if (rs.next()) {
                returnString = rs.getString(1);
            } else {
                returnString = "No review found for ID: " + reviewID;
            }
            st.close();
            rs.close();
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return returnString;
    }

    /**
     * Method to get an ID for a restaurant using the reviewID of a review
     *
     * @param reviewID - passed the ID of the review as an int
     * @return - returns the restaurant's ID as an int
     */
    public int getRestaurantReviewID(int reviewID) {
        int returnInt = 0;
        try {
            Statement st = connection.createStatement();
            ResultSet rs = st.executeQuery("SELECT restaurant_id FROM yelp.review WHERE review.review_id = " + reviewID);
            if (rs.next()) {
                returnInt = rs.getInt(1);
            } else {
                returnInt = -1;
                //-1 for the error code
            }
            rs.close();
            st.close();
            return returnInt;
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return returnInt;
    }

    /**
     * Method to get an ID for a user using the reviewID of a review
     *
     * @param reviewID - passed the ID of the review as an int
     * @return - returns the user's ID as an int
     */
    public int getUserID(int reviewID) {
        int returnInt = 0;
        try {
            Statement st = connection.createStatement();
            ResultSet rs = st.executeQuery("SELECT user_id FROM yelp.review WHERE review.review_id = " + reviewID);
            if (rs.next()) {
                returnInt = rs.getInt(1);
            } else {
                returnInt = -1;
            }
            rs.close();
            st.close();
            return returnInt;
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return returnInt;
    }

    /**
     * Method to get a rating of a review using the reviewID
     *
     * @param reviewID - passed the ID of the review as an int
     * @return - returns the reviewer's rating as an int
     */
    public int getReviewerRating(int reviewID) {
        int returnInt = 0;
        try {
            Statement st = connection.createStatement();
            ResultSet rs = st.executeQuery("SELECT reviewer_rating FROM yelp.review WHERE review.review_id = " + reviewID);
            if (rs.next()) {
                returnInt = rs.getInt(1);
            } else {
                returnInt = -1;
            }
            rs.close();
            st.close();
            return returnInt;
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return returnInt;
    }

    /**
     * Method to get all the reviews for a given restaurant (using the restaurantID)
     *
     * @param restaurantID - passed the ID of the restaurant
     * @return - returns an array of reviews, each of which has an reviewID, review, userID, rating, and the user's real name
     */
    public ArrayList<NolaBytes.back_end.dbInteraction.Review> getReviews(int restaurantID) {
        ArrayList<NolaBytes.back_end.dbInteraction.Review> returnArray = null; // null by default in case there is an error with the database
        try {
            Statement st = connection.createStatement();
            ResultSet rs = st.executeQuery(
                    "SELECT review_id, review, review.user_id, reviewer_rating, restaurant_id, real_name \n" +
                            "FROM yelp.review \n" +
                            "left join yelp.\"user\" as usr \n" +
                            "on review.user_id = usr.user_id\n" +
                            "WHERE review.restaurant_id = " + restaurantID); // sql query to get all the reviews for a given restaurant

            returnArray = new ArrayList<NolaBytes.back_end.dbInteraction.Review>(); // Make the arraylist object AFTER getting a return from the database and not getting a sql exception

            while (rs.next()) { // put each result returned from the database into a review object and add it to the arraylist
                int reviewID = rs.getInt("review_id");
                String review = rs.getString("review");
                int userID = rs.getInt("user_id");
                int reviewerRating = rs.getInt("reviewer_rating");
                String reviewerName = rs.getString("real_name");
                returnArray.add(new NolaBytes.back_end.dbInteraction.Review(reviewID, review, restaurantID, userID, reviewerRating, reviewerName)); // if it just returns an empty arraylist then there are no reviews for that restaurant
            }
            rs.close();
            st.close();
            return returnArray;

        } catch (SQLException e) {
            e.printStackTrace();
            return returnArray; // should return null if there is an error
        }

    }

    /**
     * Method to add a review to a restaurant
     *
     * @param reviewToAdd - passed the review that will be added
     * @param restaurant  - passed the restaurant the review is being added for
     * @param user        - passed the user who is adding the review
     */
    public void addReview(NolaBytes.back_end.dbInteraction.newReview reviewToAdd, NolaBytes.back_end.dbInteraction.Restaurant restaurant, NolaBytes.back_end.dbInteraction.User user) {
        try {
            PreparedStatement st = connection.prepareStatement("insert into yelp.review(restaurant_id, review, reviewer_rating, user_id) values(?, ?, ?, ?);"); // check the database for a matching username and password (should only return one result)
            st.setInt(1, restaurant.getRestaurantID()); // add parameters in a using a prepared statement to prevent sql injection
            st.setString(2, reviewToAdd.getReview());
            st.setInt(3, reviewToAdd.getReviewerRating());
            st.setInt(4, user.getId());
            st.execute();
            st = connection.prepareStatement("update yelp.\"user\" set review_count = review_count + 1 where user_id = ?;");
            st.setInt(1, user.getId());
            st.execute();
            st.close();
            System.out.println("nolaBytes.Review added");
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    /**
     * Method to edit a review from a user
     *
     * @param reviewToEdit - passed the review that will be edited
     * @param newText      - the new review text
     * @param newRating    - the new review rating
     */
    public void editReview(NolaBytes.back_end.dbInteraction.Review reviewToEdit, String newText, int newRating) {
        try {
            PreparedStatement st = connection.prepareStatement("SELECT review, reviewer_rating FROM yelp.review WHERE user_id = ? AND review_id = ?;");
            st.setInt(1, reviewToEdit.getUserID());
            st.setInt(2, reviewToEdit.getReviewID());
            ResultSet rs = st.executeQuery();
            if (!rs.next()) {
                System.out.println("nolaBytes.Review not found");
                throw new SQLException("given review was not found in the database");
            }
            st = connection.prepareStatement("UPDATE yelp.review SET review = ? WHERE review_id = ?;");
            st.setString(1, newText);
            st.setInt(2, reviewToEdit.getReviewID());
            st.execute();

            st = connection.prepareStatement("UPDATE yelp.review SET reviewer_rating = ? WHERE review_id = ?;");
            st.setInt(1, newRating);
            st.setInt(2, reviewToEdit.getReviewID());
            st.execute();

            st.close();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    /**
     * Method to delete a review from a restaurant
     *
     * @param reviewToDelete - passed the review that will be added
     */
    public void deleteReview(NolaBytes.back_end.dbInteraction.Review reviewToDelete) {
        try {
            PreparedStatement st = connection.prepareStatement("select review_id from yelp.review where review_id = ?;");
            st.setInt(1, reviewToDelete.getReviewID());
            ResultSet rs = st.executeQuery();
            if (!rs.next()) {
                System.out.println("nolaBytes.Review not found");
                throw new SQLException("given reviwe was not found in the database");
            }
            st = connection.prepareStatement("delete from yelp.review where review_id = ?;"); // check the database for a matching username and password (should only return one result)
            st.setInt(1, reviewToDelete.getReviewID()); // add the username to the prepared statement
            st.execute();
            st = connection.prepareStatement("update yelp.\"user\" set review_count = review_count - 1 where user_id = ?;");
            st.setInt(1, reviewToDelete.getUserID());
            st.execute();
            st.close();
            System.out.println("nolaBytes.Review deleted");
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    /**
     * Method to retrieve a restaurant using the restaurantID
     *
     * @param restaurantID - passed the ID for the restaurant that will be retrieved
     * @return - returns a Restaurant from the database, including ID, name, phone, address, website, category, hours, and image URL
     */
    public NolaBytes.back_end.dbInteraction.Restaurant getRestaurant(int restaurantID) {
        NolaBytes.back_end.dbInteraction.Restaurant returnRestaurant = null;
        try {
            Statement st = connection.createStatement();
            ResultSet rs = st.executeQuery(
                    "SELECT restaurant.restaurant_id, name, phone, address, website, mon, tue, wed, thu, fri, sat, sun, img_url, category\n" +
                            "from yelp.restaurant\n" +
                            "join yelp.opening_hours as hours on restaurant.restaurant_id = hours.restaurant_id\n" +
                            "join yelp.photo as p on restaurant.restaurant_id = p.restaurant_id\n" +
                            "where restaurant.restaurant_id = " + restaurantID + ";");
            rs.next();
            int dbRestaurantID = rs.getInt("restaurant_id");
            String name = rs.getString("name");
            String phone = rs.getString("phone");
            String address = rs.getString("address");
            String website = rs.getString("website");
            String category = rs.getString("category");
            String[] hours = {rs.getString("mon"), rs.getString("tue"), rs.getString("wed"), rs.getString("thu"), rs.getString("fri"), rs.getString("sat"), rs.getString("sun")};

            String imageURL = rs.getString("img_url");
            returnRestaurant = new NolaBytes.back_end.dbInteraction.Restaurant(dbRestaurantID, name, phone, address, category, website, imageURL, hours);
            rs.close();
            st.close();
            return returnRestaurant;
        } catch (SQLException e) {
            e.printStackTrace();
            return returnRestaurant;
        }
    }

    /**
     * Method for testing, retrieving the latest review added to the Restaurant
     *
     * @return - returns a Review, including reviewID, review, userID, rating, and the user's real name
     */
    public NolaBytes.back_end.dbInteraction.Review getLatestReview() {
        NolaBytes.back_end.dbInteraction.Review returnReview = null;
        try {
            Statement st = connection.createStatement();
            ResultSet rs = st.executeQuery("SELECT * FROM yelp.review join yelp.\"user\" u on u.user_id = review.user_id ORDER BY review_id DESC LIMIT 1");
            rs.next();
            int reviewID = rs.getInt("review_id");
            String review = rs.getString("review");
            int restaurantID = rs.getInt("restaurant_id");
            int userID = rs.getInt("user_id");
            int reviewerRating = rs.getInt("reviewer_rating");
            String reviewerName = rs.getString("real_name");
            returnReview = new NolaBytes.back_end.dbInteraction.Review(reviewID, review, restaurantID, userID, reviewerRating, reviewerName);
            rs.close();
            st.close();
            return returnReview;
        } catch (SQLException e) {
            e.printStackTrace();
            return returnReview;
        }
    }

    /**
     * Method for testing, retrieving the latest user added to the database
     *
     * @return - returns a User
     */
    public NolaBytes.back_end.dbInteraction.User getLatestUser() {
        NolaBytes.back_end.dbInteraction.User returnUser = null;
        try {
            Statement st = connection.createStatement();
            ResultSet rs = st.executeQuery("SELECT * FROM yelp.\"user\" ORDER BY user_id DESC LIMIT 1");
            rs.next();
            int userID = rs.getInt("user_id");
            String username = rs.getString("username");
            String password = rs.getString("password");
            String realName = rs.getString("real_name");
            int reviewCount = rs.getInt("review_count");
            returnUser = new NolaBytes.back_end.dbInteraction.User(username, password, reviewCount, realName, userID);
            rs.close();
            st.close();
            return returnUser;
        } catch (SQLException e) {
            e.printStackTrace();
            return returnUser;
        }
    }

    /**
     * Method for 'deleting' a user, does so by writing null to the username and password fields but keeps the user in the database for the Reviews that depend on them
     *
     * @param user - passed the user to be deleted
     */
    public void deleteUser(NolaBytes.back_end.dbInteraction.User user) {
        try {
            PreparedStatement st = connection.prepareStatement("UPDATE yelp.\"user\" \n" +
                    "SET username = null, \n" +
                    "    password = null, \n" +
                    "    real_name = 'DELETED USER' \n" +
                    "WHERE user_id = ?;"); // check the database for a matching username and password (should only return one result)
            st.setInt(1, user.getId()); // add the username to the prepared statement
            st.execute();
            st.close();
            System.out.println("nolaBytes.User deleted");
        } catch (SQLException e) {
            e.printStackTrace();
        }

    }

    /**
     * Method for changing a user's password
     *
     * @param username    - passed the username of the user
     * @param oldPassword - passed the old password (current password) of the user
     * @param newPassword - passed the new password of the user
     * @return - returns the new users password
     */
    public boolean changeUserPassword(String username, String oldPassword, String newPassword) {
        try {
            // Verify that the old password matches.
            PreparedStatement checkStmt = connection.prepareStatement("SELECT password FROM yelp.\"user\" WHERE username = ?;");
            checkStmt.setString(1, username);
            ResultSet rs = checkStmt.executeQuery();
            if (!rs.next() || !rs.getString("password").equals(oldPassword)) {
                return false; // For if old password doesn't match
            }
            rs.close();
            checkStmt.close();

            // If old password is correct update to the new password.
            PreparedStatement updateStmt = connection.prepareStatement("UPDATE yelp.\"user\" SET password = ? WHERE username = ?;");
            updateStmt.setString(1, newPassword);
            updateStmt.setString(2, username);
            updateStmt.executeUpdate();
            updateStmt.close();

            // return that the password was updated
            return true;

        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }

    /**
     * Method for changing a user's password
     *
     * @param user        - passed the user object
     * @param newUsername - passed the new username
     * @return - returns the new user's username
     */
    public void changeUserUsername(NolaBytes.back_end.dbInteraction.User user, String newUsername) throws InvalidAttributeValueException {
        try {
            PreparedStatement checkStmt = connection.prepareStatement("SELECT username FROM yelp.\"user\" WHERE user_id = ?;");
            checkStmt.setInt(1, user.getId());
            ResultSet rs = checkStmt.executeQuery();
            rs.close();
            checkStmt.close();

            PreparedStatement st = connection.prepareStatement("UPDATE yelp.\"user\" SET username = ? WHERE user_id = ?");
            st.setString(1, newUsername);
            st.setInt(2, user.getId());
            int rowsUpdated = st.executeUpdate();
            st.close();
            if (rowsUpdated == 0) {
                throw new InvalidAttributeValueException("User not found or username unchanged.");
            }
            System.out.println("Username updated successfully.");
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public void changeName(String username, String oldName, String newName) {
        try {
            // Verify that the old name matches for the provided username.
            PreparedStatement checkStmt = connection.prepareStatement("SELECT real_name FROM yelp.\"user\" WHERE username = ?;");
            checkStmt.setString(1, username);
            ResultSet rs = checkStmt.executeQuery();

            if (!rs.next()) {
                // Old name doesn't match for the provided username
                return;
            }

            rs.close();
            checkStmt.close();
            // If old name is correct for the provided username, update to the new name.
            PreparedStatement updateStmt = connection.prepareStatement("UPDATE yelp.\"user\" SET real_name = ? WHERE username = ?;");
            updateStmt.setString(1, newName);
            updateStmt.setString(2, username); // Use oldName to identify the user
            updateStmt.executeUpdate();
            updateStmt.close();

            // Indicate that the name was updated
            // You can return something here if needed
        } catch (SQLException e) {
            // Handle database error
            e.printStackTrace();
        }
    }


    /**
     * Method for adding a user to the database
     *
     * @param username - passed the username of the user
     * @param password - passed the password of the user
     * @param realName - passed the real name of the user
     * @return - returns a User that was just added, including the username, password, real name, and a review count of 0
     */
    public NolaBytes.back_end.dbInteraction.User addUser(String username, String password, String realName) throws InvalidAttributeValueException {
        NolaBytes.back_end.dbInteraction.User returnUser = null;


        // checking if the user already exists first
        try {
            PreparedStatement existingUserCheckStatement = connection.prepareStatement("SELECT * FROM yelp.\"user\" WHERE username = ? AND password = ?;");
            existingUserCheckStatement.setString(1, username);
            existingUserCheckStatement.setString(2, password);
            ResultSet badRs = existingUserCheckStatement.executeQuery();
            badRs.next();
            badRs.getString("username");
            throw new InvalidAttributeValueException("User already exists"); // if you get this far then the user already exists and that is bad
        } catch (SQLException e) {
            // do nothing if the sql exception happens (that is supposed to happen)
        }


        try {
            // Set the user_id_seq sequence value to the max user_id + 1
            Statement seqStatement = connection.createStatement();
            //new user gets a new ID number that’s higher than the current one

            PreparedStatement st = connection.prepareStatement("INSERT INTO yelp.\"user\"(username, password, real_name , review_count) VALUES (?, ?, ?, ?);");
            st.setString(1, username); // adding the username to the prepared statement
            st.setString(2, password); // adding the password to the prepared statement
            st.setString(3, realName); // adding the real name to the prepared statement
            st.setInt(4, 0); // add the review count as 0
            st.execute();

            // ... rest of the logic to retrieve and return the newly added user remains the same ...

            // get the user that was just added and return it
            st = connection.prepareStatement("SELECT * FROM yelp.\"user\" WHERE username = ? AND password = ?;");
            st.setString(1, username);
            st.setString(2, password);

            ResultSet rs = st.executeQuery();
            rs.next();
            returnUser = new NolaBytes.back_end.dbInteraction.User(
                    rs.getString("username"),
                    rs.getString("password"),
                    rs.getInt("review_count"),
                    rs.getString("real_name"),
                    rs.getInt("user_id")
            );
            rs.close();
            st.close();


            return returnUser;
        } catch (SQLException e) {
            e.printStackTrace();
            return returnUser;

        }
    }

    /**
     * Method to get all the reviews from a given user
     *
     * @param userID - passed the ID of the user
     * @return - returns an array of reviews, each of which has an reviewID, review, userID, rating, and the user's real name
     */
    public ArrayList<NolaBytes.back_end.dbInteraction.Review> getUserReviews(int userID) {
        ArrayList<NolaBytes.back_end.dbInteraction.Review> returnArray = null; // null by default in case there is an error with the database
        try {
            Statement st = connection.createStatement();
            ResultSet rs = st.executeQuery(
                    "SELECT review_id, review, review.user_id, reviewer_rating, restaurant_id, real_name \n" +
                            "FROM yelp.review \n" +
                            "left join yelp.\"user\" as usr \n" +
                            "on review.user_id = usr.user_id\n" +
                            "WHERE review.user_id = " + userID); // sql query to get all the reviews for a given restaurant

            returnArray = new ArrayList<NolaBytes.back_end.dbInteraction.Review>(); // Make the arraylist object AFTER getting a return from the database and not getting a sql exception

            while (rs.next()) { // put each result returned from the database into a review object and add it to the arraylist
                int reviewID = rs.getInt("review_id");
                String review = rs.getString("review");
                int restaurantID = rs.getInt("restaurant_id");
                int reviewerRating = rs.getInt("reviewer_rating");
                String reviewerName = rs.getString("real_name");
                returnArray.add(new NolaBytes.back_end.dbInteraction.Review(reviewID, review, restaurantID, userID, reviewerRating, reviewerName)); // if it just returns an empty arraylist then there are no reviews for that restaurant
            }
            rs.close();
            st.close();
            return returnArray;

        } catch (SQLException e) {
            e.printStackTrace();
            return returnArray; // should return null if there is an error
        }

    }


    /**
     * Method to edit a restaurant
     *
     * @param reviewToEdit - passed the review that will be edited
     * @param newReview    - the new review text
     * @param newRating    - the new review rating
     */
    public void editRestaurant(NolaBytes.back_end.dbInteraction.Restaurant restaurantToEdit, String newName, String newPhoneNumber, String newAddress, String newCategory, String newWebsite) {
        try {
            PreparedStatement st = connection.prepareStatement("SELECT * FROM yelp.restaurant WHERE restaurant_id = ?;");
            st.setInt(1, restaurantToEdit.getRestaurantID());
            ResultSet rs = st.executeQuery();
            if (!rs.next()) {
                System.out.println("nolaBytes.Restaurant not found");
                throw new SQLException("given restaurant was not found in the database");
            }
            st = connection.prepareStatement("UPDATE yelp.restaurant SET name = ? WHERE restaurant_id = ?;");
            st.setString(1, newName);
            st.setInt(2, restaurantToEdit.getRestaurantID());
            st.execute();

            st = connection.prepareStatement("UPDATE yelp.restaurant SET phone = ? WHERE restaurant_id = ?;");
            st.setString(1, newPhoneNumber);
            st.setInt(2, restaurantToEdit.getRestaurantID());
            st.execute();

            st = connection.prepareStatement("UPDATE yelp.restaurant SET address = ? WHERE restaurant_id = ?;");
            st.setString(1, newAddress);
            st.setInt(2, restaurantToEdit.getRestaurantID());
            st.execute();

            st = connection.prepareStatement("UPDATE yelp.restaurant SET category = ? WHERE restaurant_id = ?;");
            st.setString(1, newCategory);
            st.setInt(2, restaurantToEdit.getRestaurantID());
            st.execute();

            st = connection.prepareStatement("UPDATE yelp.restaurant SET website = ? WHERE restaurant_id = ?;");
            st.setString(1, newWebsite);
            st.setInt(2, restaurantToEdit.getRestaurantID());
            st.execute();

            st.close();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public void addRestaurant(NolaBytes.back_end.dbInteraction.Restaurant restaurant) {
        try {
            PreparedStatement st = connection.prepareStatement("insert into yelp.restaurant(name, phone, address, website, category) values(?, ?, ?, ?, ?);");
            st.setString(1, restaurant.getName());
            st.setString(2, restaurant.getPhoneNumber());
            st.setString(3, restaurant.getAddress());
            st.setString(4, restaurant.getWebsite());
            st.setString(5, restaurant.getCategory());
            st.execute();
            st = connection.prepareStatement("insert into yelp.opening_hours(mon, tue, wed, thu, fri, sat, sun, restaurant_id) values(?, ?, ?, ?, ?, ?, ?, ?);");

            int latestRestaurantId = getLatestRestaurantID(); // use this for when you need to get the latest restaurant ID (the database doent like adding a restaruant) 
            String[] hours = restaurant.getHours(); // THIS SHIT IS WHAT HAPPENS WHEN YOU MOVE THE FINISH LINE WOLFE!
            for (int i = 0; i < 7; i++) {
                st.setString(i + 1, hours[i]);
            }
            st.setInt(8, latestRestaurantId);
            st.execute();
            st = connection.prepareStatement("insert into yelp.photo(restaurant_id, img_url) values(?, ?);");
            st.setInt(1, latestRestaurantId);
            st.setString(2, restaurant.getImageURL());
            st.execute();
            st.close();
            System.out.println("nolaBytes.Restaurant added");
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public boolean deleteRestaurant(NolaBytes.back_end.dbInteraction.Restaurant restaurant) {
        boolean isSuccess = false;
        try {
            connection.setAutoCommit(false); // Start transaction

            // 1. Delete Reviews
            ArrayList<NolaBytes.back_end.dbInteraction.Review> restaurantReviews = getReviews(restaurant.getRestaurantID());
            for (NolaBytes.back_end.dbInteraction.Review review : restaurantReviews) {
                deleteReview(review);
            }

            // 2. Delete Photos (if needed)
            PreparedStatement deletePhotosStmt = connection.prepareStatement(
                    "DELETE FROM yelp.photo WHERE restaurant_id = ?;");
            deletePhotosStmt.setInt(1, restaurant.getRestaurantID());
            deletePhotosStmt.execute();
            deletePhotosStmt.close();
            System.out.println("Photo deleted for restaurant ID " + restaurant.getRestaurantID());

            // 3. Delete Hours
            PreparedStatement deleteHoursStmt = connection.prepareStatement(
                    "DELETE FROM yelp.opening_hours WHERE restaurant_id = ?;");
            deleteHoursStmt.setInt(1, restaurant.getRestaurantID());
            deleteHoursStmt.execute();
            deleteHoursStmt.close();
            System.out.println("Hours deleted for restaurant ID " + restaurant.getRestaurantID());

            // 4. Delete Restaurant
            PreparedStatement deleteRestaurantStmt = connection.prepareStatement(
                    "DELETE FROM yelp.restaurant WHERE restaurant_id = ?;");
            deleteRestaurantStmt.setInt(1, restaurant.getRestaurantID());
            deleteRestaurantStmt.execute();
            deleteRestaurantStmt.close();
            System.out.println("Restaurant ID " + restaurant.getRestaurantID() + " has been deleted.");

            connection.commit(); // Commit transaction
            isSuccess = true; // Set success to true as all operations were successful
        } catch (SQLException e) {
            try {
                connection.rollback(); // Rollback transaction on error
            } catch (SQLException ex) {
                ex.printStackTrace();
            }
            e.printStackTrace();
        } finally {
            try {
                connection.setAutoCommit(true); // Restore default behavior
            } catch (SQLException ex) {
                ex.printStackTrace();
            }
        }
        return isSuccess;
    }

    private int getLatestRestaurantID() {
        int returnInt = -1;
        try {
            Statement st = connection.createStatement();
            ResultSet rs = st.executeQuery("SELECT restaurant_id FROM yelp.restaurant ORDER BY restaurant_id DESC LIMIT 1;");
            rs.next();
            returnInt = rs.getInt(1);
            rs.close();
            st.close();
            return returnInt;
        } catch (SQLException exception) {
            System.out.println("Error getting latest restaurant ID was invalid");
            exception.printStackTrace();
        }
        return returnInt;
    }

    public NolaBytes.back_end.dbInteraction.Restaurant getLatestRestaurant() {
        NolaBytes.back_end.dbInteraction.Restaurant returnRestaurant = null;
        try {
            Statement st = connection.createStatement();
            ResultSet rs = st.executeQuery(
                    "SELECT * FROM yelp.restaurant \n" +
                            "    join yelp.opening_hours as op_hrs\n" +
                            "    on restaurant.restaurant_id = op_hrs.restaurant_id \n" +
                            "    left join yelp.photo p on restaurant.restaurant_id = p.restaurant_id\n" +
                            "         ORDER BY restaurant.restaurant_id desc LIMIT 1;");
            rs.next();
            int restaurantID = rs.getInt("restaurant_id");
            String name = rs.getString("name");
            String phone = rs.getString("phone");
            String address = rs.getString("address");
            String website = rs.getString("website");
            String category = rs.getString("category");
            String[] hours = {rs.getString("mon"), rs.getString("tue"), rs.getString("wed"), rs.getString("thu"), rs.getString("fri"), rs.getString("sat"), rs.getString("sun")};
            String imageURL = rs.getString("img_url");
            returnRestaurant = new NolaBytes.back_end.dbInteraction.Restaurant(restaurantID, name, phone, address, category, website, imageURL, hours);
            rs.close();
            st.close();
            return returnRestaurant;
        } catch (SQLException e) {
            e.printStackTrace();
            return returnRestaurant;
        }
    }

    public String getUserName(int user_id) {
        String userName = "";
        try {
            Statement st = connection.createStatement();
            ResultSet rs = st.executeQuery("SELECT username FROM yelp.\"user\" WHERE user_id = " + user_id);

            if (rs.next()) {
                userName = rs.getString(1);
            } else {
                userName = "";
            }
            rs.close();
            st.close();
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return userName;
    }

}

