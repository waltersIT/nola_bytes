package NolaBytes.back_end.dbInteraction;

// Author: Michael Boomgaart
// Editors: Michael Boomgaart
// Date Last edited: 04/15/24
// Date Created: 04/15/2024
// Purpose: to represent and store all relevant information for a new review


// DO NOT USE THIS OUTSIDE OF STORING A NEWLY WRITTEN REVIEW FROM A USER
// PLEASE!!

public class newReview {
    private String review;
    private int reviewerRating;

    /*
     * Default constructor 
     */
    public newReview() {
    }
    
    /**
     * Parameterized constructor for the Review class.
     */
    public newReview(String review, int reviewerRating) {
        this.review = review; // content of the review
        this.reviewerRating = reviewerRating; // rating given by reviewer
    }

    /**
     * Getter and setter methods
     */

    String getReview() { // Getter method to retrieve the text content of the review
        return review;
    }

    public void setReview(String review) { // Setter method to update the text content of the review
        this.review = review;
    }

    int getReviewerRating() { // Getter method to retrieve the numerical rating given by reviewer
        return reviewerRating;
    }

    public void setReviewerRating(int reviewerRating) { // Setter method to assign a new rating to the review
        this.reviewerRating = reviewerRating;
    }


    @Override
    public String toString() {
        return "nolaBytes.Review{" +
                "review='" + review + '\'' +
                ", reviewerRating='" + reviewerRating + '\'' +
                '}';
    }
}
