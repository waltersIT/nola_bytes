// Author: Michael Boomgaart
// Date Created:03/20/24
// Editors: Michael Boomgaart, Matthew Levin, Omar Essa, Jalen Moore, Jada Farrell, Michael Boomgaart
// Date Last Edited: 04/29/24
// Purpose: A unit test to test database functionality
// changes: Added delete restaurant test
package NolaBytes.back_end.Testing;

import NolaBytes.Globals;
import NolaBytes.back_end.dbInteraction.*;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.openqa.selenium.InvalidArgumentException;

import javax.management.InvalidAttributeValueException;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.util.ArrayList;

import static org.junit.jupiter.api.Assertions.*;

// Java class to perform junit testing on JDBC classes
class DataBaseTest {

    private DataBase db;
    private Globals globals;

    // Function that establishes a connection with the database
    // to test that classes are receiving data from it
    // Function is called before each test function
    @BeforeEach
    void setUp() {
        DataBase dataBase = null;
        try {
            this.globals = new Globals(new FileInputStream("src/NolaBytes/config.properties"));
        } catch (FileNotFoundException e) {
            throw new RuntimeException(e);
        }
        dataBase = new DataBase(globals.testingDBUser, globals.testingDBPassword,
                globals.testingDBUrl, true);

        // Tests that the database exists and can be connected to
        assertNotNull(dataBase, "Database is null and probably not connected");
        this.db = dataBase;
    }

    // Function that tests that the Database class'
    // 'searchForRestaurant' function operates correctly
    @Test
    void searchForRestaurant() {

        // Calls the 'searchForRestaurant' function
        // with example case 'The Camellia Grill'
        ArrayList<Restaurant> restaurants = db.searchForRestaurant("The Camellia Grill");

        // Tests that the function call returns at least one restuarant
        assertNotEquals(0, restaurants.size());

        // Loops through all returned restaurants
        for (Restaurant restaurant : restaurants) {

            // Tests that each restaurant returned by the
            // function call contains the word 'the' in its name
            assertTrue(restaurant.getName().toLowerCase().contains("the"));
        }
        // Loops through all returned restaurants
        for (Restaurant restaurant : restaurants) {

            // Tests that each returned restaurant has a valid ID
            assertNotEquals(0, restaurant.getRestaurantID(), "nolaBytes.Restaurant ID is 0 (dats bad)");

            // Tests that each returned restaurant has a name
            // (is not null or an empty string)
            assertNotNull(restaurant.getName(), "nolaBytes.Restaurant name is null");
            assertNotEquals(restaurant.getName(), "");

            // Tests that each returned restaurant has a phone number
            // (is not null or an empty string)
            assertNotNull(restaurant.getPhoneNumber(), "nolaBytes.Restaurant phone number is null");
            assertNotEquals(restaurant.getPhoneNumber(), "");

            // Tests that each returned restaurant has an address
            // (is not null or an empty string)
            assertNotNull(restaurant.getAddress(), "nolaBytes.Restaurant address is null");
            assertNotEquals(restaurant.getAddress(), "");

            // Tests that each returned restaurant has a category
            // (is not null or an empty string)
            assertNotNull(restaurant.getCategory(), "nolaBytes.Restaurant category is null");
            assertNotEquals(restaurant.getCategory(), "");

            // Tests that each returned restaurant's website attribute is not null
            assertNotNull(restaurant.getWebsite(), "nolaBytes.Restaurant website is null");

            // Tests that each returned restaurant has an image URL
            // (is not null or an empty string)
            assertNotNull(restaurant.getImageURL(), "nolaBytes.Restaurant image URL is null");
            assertNotEquals(restaurant.getImageURL(), "");

            // Tests that each returned restaurant's hours attribute is not null
            assertNotNull(restaurant.getHours(), "nolaBytes.Restaurant hours is null");
        }
    }

    // Function that tests that data received from the database is correct
    // and that the JDBC successfully returns the reviews for a restaurant
    @Test
    void headlessSearchAndGetReviews() {

        // Creates an example Restaurant object to test against
        Restaurant testRestaurant = new Restaurant(
                33,
                "The Camellia Grill",
                "(504) 309-2679",
                "626 S Carrollton Ave New Orleans LA 70118",
                "American",
                "http://camelliagrillnola.com",
                "https://s3-media0.fl.yelpcdn.com/bphoto/D_3o6Xn7RwtpjzW9CEtAyA/l.jpg",

                new String[]{"8:00 AM - 7:00 PM", "8:00 AM - 7:00 PM", "8:00 AM - 7:00 PM",
                        "8:00 AM - 7:00 PM", "8:00 AM - 8:00 PM", "8:00 AM - 8:00 PM",
                        "8:00 AM - 8:00 PM"});

        // Calls 'searchForRestaurant' to search the database for the test example
        ArrayList<Restaurant> restaurants = db.searchForRestaurant(testRestaurant.getName());

        // Tests that 'searchForRestaurant' successfully
        // returned a restaurant from the database
        assertNotNull(restaurants, "Restaurants is null");

        // Tests that 'searchForRestaurant' only returned the test example
        assertEquals(1, restaurants.size(), "Restaurants size is not 1");

        // Tests that the ID of the returned restaurant matches the test example
        assertEquals(testRestaurant.getRestaurantID(), restaurants.get(0).getRestaurantID(),
                "Restaurant ID does not match");

        // Tests that the name of the returned restaurant matches the test example
        assertEquals(testRestaurant.getName(), restaurants.get(0).getName(), "Restaurant name does not match");

        // Tests that the phone number of the returned
        // restaurant matches the test example
        assertEquals(testRestaurant.getPhoneNumber(), restaurants.get(0).getPhoneNumber(),
                "Restaurant phone number does not match");

        // Tests that the address of the returned restaurant matches the test example
        assertEquals(testRestaurant.getAddress(), restaurants.get(0).getAddress(), "Restaurant address does not match");

        // Tests that the category of the returned restaurant matches the test example
        assertEquals(testRestaurant.getCategory(), restaurants.get(0).getCategory(),
                "Restaurant category does not match");

        // Tests that the website of the returned restaurant matches the test example
        assertEquals(testRestaurant.getWebsite(), restaurants.get(0).getWebsite(), "Restaurant website does not match");

        // Tests that the image URL of the returned restaurant matches the test example
        assertEquals(testRestaurant.getImageURL(), restaurants.get(0).getImageURL(),
                "Restaurant image URL does not match");

        // Tests that the hours attribute of the returned
        // website matches the test example
        assertArrayEquals(testRestaurant.getHours(), restaurants.get(0).getHours(), "Restaurant hours do not match");

        // Calls 'searchForRestaurant' to search by a test category
        restaurants = db.searchForRestaurant("American");

        // Tests that the list of returned restaurants is greater than 1
        assertTrue(1 < restaurants.size(), "Restaurants size is not 1");

        // Calls 'getReviews' to get the reviews for the first returned restaurant
        ArrayList<Review> reviews = db.getReviews(restaurants.get(0).getRestaurantID());

        // Loops through every returned review
        for (Review review : reviews) {

            // Tests that the review is neither null nor an empty string
            assertNotNull(review.getReview(), "Review is null");
            assertNotEquals(review.getReview(), "");

            // Tests that the ID for each review is not 0
            assertNotEquals(0, review.getReviewID(), "Review ID is 0");

            // Tests the restaurant ID for each review is not 0
            assertNotEquals(0, review.getRestaurantID(), "Restaurant ID is 0");

            // Tests that the user ID for each review is not 0
            assertNotEquals(0, review.getUserID(), "User ID is 0");

            // Tests that the rating for each review is not 0
            assertNotEquals(0, review.getReviewerRating(), "Reviewer rating is 0");

            // Tests that the reviewer for each review is neither null nor an empty string
            assertNotNull(review.getReviewerName(), "Reviewer name is null");
            assertNotEquals(review.getReviewerName(), "");

            // Tests that the restaurant ID for a given restaurant is correct
            assertEquals(restaurants.get(0).getRestaurantID(), review.getRestaurantID(),
                    "Restaurant ID does not match");
        }
    }

    // Function that tests if a user can successfully login and that
    // the user's information is correctly stored in the database
    @Test
    void userLoginTest() {

        try {
            // Creates a example User object for testing
            String username = "BanghyBootes";
            String password = "vBon4ydpyA";
            User user = db.getUser(username, password);

            // Tests that the user is in the database
            assertNotNull(user, "User is null");

            // Tests that the user's real name is in the database
            assertNotEquals("", user.getRealName(), "Real name is empty");

            // Tests that the user's username is correct
            assertEquals(username, user.getUsername(), "Username is incorrect");

            // Tests that the user's ID is correct
            assertTrue(user.getId() > 0, "User ID is incorrect");

            // Tests that the database correctly stores the user's number of reviews
            assertTrue(user.getReviewAmount() >= 0, "User review amount is incorrect");
        } catch (InvalidAttributeValueException e) {
            throw new RuntimeException(e);
        }
    }

    // Function that tests if the JDBC can successfully
    // receives all reviews for a restaurant
    @Test
    void getReviewFromRestaurant() {

        // Calls 'getReviews' to return all reviews for the restaurant with the ID 1
        ArrayList<Review> returnArray = db.getReviews(1);

        // Tests that 'getReviews' successfully returned reviews
        assertNotNull(returnArray, "Review array is null");

        // Loops through every returned review
        for (Review testReview : returnArray) {

            // Tests that each returned review is not null
            assertNotNull(testReview.getReview(), "Review is null");

            // Tests that the restaurant ID for each returned review is not 0
            assertNotEquals(0, testReview.getRestaurantID(), "Restaurant ID is 0");

            // Tests that the user ID for each returned review is not 0
            assertNotEquals(0, testReview.getUserID(), "User ID is 0");

            // Tests that the rating for each returned review is not 0
            assertNotEquals(0, testReview.getReviewerRating(), "Reviewer rating is 0");

            // Tests that the ID for each returned review is not 0
            assertNotEquals(0, testReview.getReviewID(), "Review ID is 0");

            // Tests that the rating for each review is within the correct range
            assertTrue(testReview.getReviewerRating() <= 5, "Rating is out of range");
        }
    }

    // this test should fail for now until line 597 of DataBase.java is fixed
    // Function that tests if a user can successfully be added then deleted
    @Test
    void addAndDeleteUser() {
        try {
            // Creates an example user
            String username = "joe";
            String password = "1234";
            String realname = "joe";

            // Adds the user to the database
            User user2 = db.addUser(username, password, realname);

            // Makes sure the user was added and is not null
            assertNotNull(user2, "User is null");
            assertNotNull(db.getUser(username, password), "User does not exist");

            // Makes sure the username and real name are not empty
            assertNotEquals("", user2.getUsername(), "Username is empty");
            assertNotEquals("", user2.getRealName(), "Real name is empty");

            // Makes sure the user id is not 0
            assertNotEquals(0, user2.getId(), "Review ID is 0");

            // Makes sure the example user's username and real name are the same
            // as what ended up being added to the database
            assertEquals(username, user2.getUsername(), "Incorrect username");
            assertEquals(realname, user2.getRealName(), "Incorrect real name");

            // Makes sure the number of reviews by the new user
            // is not initialized as a negative number
            assert user2.getReviewAmount() >= 0;

            // Deletes the user
            db.deleteUser(user2);

            user2 = db.getLatestUser();

            // Makes sure the user was deleted (the real name should be DELETED USER)
            assertEquals("DELETED USER", user2.getRealName(), "User not deleted");
        } catch (InvalidAttributeValueException e) {
            e.printStackTrace();
        }
    }

    @Test
    void deleteReview() {
        Review reviewToDelete = new Review();
        reviewToDelete.setReviewID(4451); // Set review ID to match the review you want to delete
        reviewToDelete.setUserID(456); // Set user ID of the review to adjust user's review count

        try {
            // Perform deletion
            db.deleteReview(reviewToDelete);

            // Check if review is deleted
            ArrayList<Review> deletedReview = db.getReviews(reviewToDelete.getReviewID());
            assertEquals(0, deletedReview.size(), "Review is deleted");

            // Check if user's review count is decreasing
            User user = db.getUser("MonitoredMarshbuck", "6p7tS7NaIr");
            assertEquals(0, user.getReviewAmount(), "User's review count is 0");

        } catch (InvalidAttributeValueException e) {
            throw new RuntimeException(e);
        }
    }

    // This test method verifies the functionality of adding a review to the database.
    // Defines a sample review text and reviewer rating.
    // Instantiates placeholder user and restaurant objects for testing.
    // Asserts that the retrieved review is not null and verifies that its text and rating match the values provided during creation.
    @Test
    void addReview() {
        try {
            // Creates an example review
            String reviewText = "Great food!";
            int reviewerRating = 5;

            // Create an example user and restaurant (placeholders)
            User user = db.getUser("MonitoredMarshbuck", "6p7tS7NaIr");
            Restaurant restaurant = db.getRestaurant(1);

            // Adds the review to the database
            newReview review = new newReview(reviewText, reviewerRating);
            db.addReview(review, restaurant, user);

            // Retrieves the added review from the database
            Review addedReview = db.getLatestReview();

            // Makes sure the review was added and is not null
            assertNotNull(addedReview, "Review is null");

            // Makes sure the review text and rating are correct
            assertEquals(reviewText, addedReview.getReview(), "Incorrect review text");
            assertEquals(reviewerRating, addedReview.getReviewerRating(), "Incorrect reviewer rating");
        } catch (InvalidAttributeValueException e) {
            e.printStackTrace();
        }
    }

    // This test method verifies the functionality of changing a user's password.
    @Test
    void changeUserPasswordTest() {
        // Username and password for MonitoredMarshbuck
        String username = "MonitoredMarshbuck";
        String oldPassword = "6p7tS7NaIr";
        String newPassword = "NewPassword"; // New password to be tested

        // Try changing the password
        boolean isPasswordChanged = db.changeUserPassword(username, oldPassword, newPassword);

        // Assert that the password change was successful
        assertTrue(isPasswordChanged, "Password change should be successful.");

        // Set password back to old password to not mess up other tests
        // Comment out of you want to see the new password in the db
        db.changeUserPassword(username, newPassword, oldPassword);
    }

    @Test
    void getAllUsers() {
        try {
            // Calls 'getReviews' to return all reviews for the restaurant with the ID 1
            ArrayList<User> returnArray = db.getAllUsers();

            // Tests that 'getReviews' successfully returned reviews
            assertNotNull(returnArray, "Review array is null");

            // Loops through every returned review
            for (User user : returnArray) {

                // Tests that each returned review is not null
                assertNotNull(user, "User is null");

                // Tests that the restaurant ID for each returned review is not 0
                assertNotEquals("", user.getRealName(), "Real name is empty");

                // Tests that the user ID for each returned review is not 0
                assertNotNull(user.getReviewAmount(), "Review count is null");

                // Tests that the rating for each returned review is not 0
                assertNotEquals("", user.getUsername(), "Username is empty");

                // Tests that the ID for each returned review is not 0
                assertNotEquals(0, user.getId(), "User ID is 0");
            }
        } catch (InvalidAttributeValueException e) {
            e.printStackTrace();
        }
    }

    @Test
    void editReviewTest() {
        // Required info to edit a review from BanghyBootes
        final int userID = 2;
        final int reviewID = 3997;

        // Get review from the user and match it to the to be edited review
        ArrayList<Review> reviews = db.getUserReviews(userID);
        assertTrue(reviews.stream().anyMatch(r -> r.getReviewID() == reviewID), "Review should exist for editing.");

        // Find the review to edit
        Review reviewToEdit = reviews.stream().filter(r -> r.getReviewID() == reviewID).findFirst().orElse(null);
        assertNotNull(reviewToEdit, "Review to edit should not be null.");

        // Store the old information to reset later
        String oldReviewText = reviewToEdit.getReview();

        // Get the rating of the review
        int oldRating = reviewToEdit.getReviewerRating();

        // New content for the review
        String newReviewText = "Testing review and rating editing";
        int newRating = 3;

        // Edit the review
        db.editReview(reviewToEdit, newReviewText, newRating);

        // Find the edited review
        reviews = db.getUserReviews(userID);
        Review updatedReview = reviews.stream().filter(r -> r.getReviewID() == reviewID).findFirst().orElse(null);

        // Asserts
        assertNotNull(updatedReview, "Updated review should not be null.");
        assertEquals(newReviewText, updatedReview.getReview(), "Review text should have been updated.");
        assertEquals(newRating, updatedReview.getReviewerRating(), "Review rating should have been updated.");

        // Reset to previous review info
        db.editReview(reviewToEdit, oldReviewText, oldRating);
    }

    @Test
    void addAndDeleteRestaurant() {
        try {
            Restaurant oldrest = db.getLatestRestaurant();

            System.out.println(oldrest.toString() + " 1st");
            Restaurant restToAdd = globals.testingRestaurants.get(1);
            db.addRestaurant(restToAdd);

            Restaurant latestRest = db.getLatestRestaurant();

            System.out.println(latestRest.toString() + " 2nd");

            // add asserts for all properties
            assertNotEquals(oldrest.getName(), latestRest.getName(), "did not add new restaurant");
            assertNotEquals(oldrest.getRestaurantID(), latestRest.getRestaurantID(), "did not add new restaurant ID");
            assertNotEquals(oldrest.getPhoneNumber(), latestRest.getPhoneNumber(), "did not add new restaurant Phonenumber");
            assertNotEquals(oldrest.getAddress(), latestRest.getAddress(), "did not add new restaurant address");
            assertNotEquals(oldrest.getCategory(), latestRest.getCategory(), "did not add new restaurant category");
            assertNotEquals(oldrest.getWebsite(), latestRest.getWebsite(), "did not add new restaurant website");
            assertNotEquals(oldrest.getImageURL(), latestRest.getImageURL(), "did not add new restaurant imageurl");
            for (int i = 0; i < 7; i++) {
                assertNotEquals(oldrest.getHours()[i], latestRest.getHours()[i], "did not add new restaurant hours on day index " + i); // gets each day's hours and asserts against them one by one
            }

            // add asserts for all properties

            assertEquals(restToAdd.getName(), latestRest.getName(), "newest data does not match");
            assertEquals(restToAdd.getPhoneNumber(), latestRest.getPhoneNumber(), "newest data does not match");
            assertEquals(restToAdd.getAddress(), latestRest.getAddress(), "newest data does not match");
            assertEquals(restToAdd.getCategory(), latestRest.getCategory(), "newest data does not match");
            assertEquals(restToAdd.getWebsite(), latestRest.getWebsite(), "newest data does not match");
            assertEquals(restToAdd.getImageURL(), latestRest.getImageURL(), "newest data does not match");
            for (int i = 0; i < 7; i++) {
                assertEquals(latestRest.getHours()[i], latestRest.getHours()[i], "did not add new restaurant hours on day index " + i); // gets each day's hours and asserts against them one by one
            }

            // Perform the deletion
            boolean isDeleted = db.deleteRestaurant(latestRest);
            assertTrue(isDeleted, "Restaurant should be deleted successfully.");
            // Verify that reviews are deleted
            assertTrue(db.getReviews(latestRest.getRestaurantID()).isEmpty(), "Reviews should be deleted.");
        } catch (InvalidArgumentException e) {
            throw new RuntimeException(e);
        }
    }
}