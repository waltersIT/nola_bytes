package NolaBytes.back_end.dbInteraction;
// Author: Walter DeVeas
// Date Created: 3/18/2024
// Editors: Boris Alarcon, Walter DeVeas, Matthew Levin, Michael Boomgaart
// Date Last edited: 4/19/24, 04/05/24
// Changes: insertIntoTemplate now inserts the userID into the template for it to serve as a URL parameter when searching a public profile page.
// Purpose: to represent and store all the information about a review as an object

public class Review {
    private int reviewID;
    private String review;
    private int restaurantID;
    private int userID;
    private int reviewerRating;
    private String reviewerName;

    /**
     * Default constructor
     */
    public Review() {
    }

    /**
     * Parameterized constructor for the Review class.
     */
    public Review(int reviewID, String review, int restaurantID, int userID, int reviewerRating, String reviewerName) {
        this.reviewID = reviewID; // foreign key referencing the actual content of the review
        this.review = review; // content of the review
        this.restaurantID = restaurantID; // foreign key referencing the restaurant
        this.userID = userID; // foreign key referencing the user who wrote the review
        this.reviewerRating = reviewerRating; // rating given by reviewer
        this.reviewerName = reviewerName; // name of reviewer
    }

    /**
     * Getter and setter methods
     */

    public int getReviewID() { // Getter method to retrieve the review's ID
        return reviewID;
    }

    public void setReviewID(int reviewID) { // Setter method to assign a new review ID
        this.reviewID = reviewID;
    }

    public String getReview() { // Getter method to retrieve the text content of the review
        return review;
    }

    public void setReview(String review) { // Setter method to update the text content of the review
        this.review = review;
    }

    public String getReviewerName() { // Getter method to retrieve the reviewer's name
        return reviewerName;
    }

    public int getRestaurantID() { // Getter method to retrieve the ID of the restaurant being reviewed
        return restaurantID;
    }

    public void setRestaurantID(int restaurantID) { // Setter method to assign the restaurant ID
        this.restaurantID = restaurantID;
    }

    public int getUserID() { // Getter method to retrieve the ID of the user
        return userID;
    }

    public void setUserID(int userID) { // Setter method to assign the user ID
        this.userID = userID;
    }

    public int getReviewerRating() { // Getter method to retrieve the numerical rating given by reviewer
        return reviewerRating;
    }

    public void setReviewerRating(int reviewerRating) { // Setter method to assign a new rating to the review
        this.reviewerRating = reviewerRating;
    }

    //    i have ramen sitting here and it smells yummy
    public String insertIntoTemplate(String template) {
        template = template.replace("reviewerName", this.reviewerName);
        template = template.replace("reviewerRating", Integer.toString(this.reviewerRating));
        template = template.replace("userIdNum", Integer.toString(this.userID));    // this is handled as a URL parameter so when it redirects to a profile page, it sends the userID as a parameter
        if (this.review.length() > 69) {
            template = template.replace("revContent", "<p>" + this.review.substring(0, 69) + "<span id=\"" + this.reviewID + "dots" + "\">...</span><span style=\"display: none\" id=\"" + this.reviewID + "more" + "\">" + this.review.substring(69) + "</span>" + "</p>" + "<button id=\"IdNum\" onclick=\"myFunction(this.id)\">Read more</button>");
            template = template.replace("IdNum", Integer.toString(this.reviewID));
        } else {
            template = template.replace("revContent", "<p>" + this.review + "</p>");
        }


        return template;
    }

    @Override
    public String toString() {
        return "nolaBytes.Review{" +
                "reviewID=" + reviewID +
                ", review='" + review + '\'' +
                ", restaurantID='" + restaurantID + '\'' +
                ", userID='" + userID + '\'' +
                ", reviewerRating='" + reviewerRating + '\'' +
                ", reviewerName='" + reviewerName + '\'' +
                '}';
    }
}