package NolaBytes.back_end.dbInteraction;
// Joaquin A. Vazquez, Zaynab Zeini
// 3/18/2024
// Create nolaBytes.User class.


public class User {
    public String realName;
    private int reviewAmount;
    private String username;
    private String password;
    private int id;


    /**
     * Default constructor
     */
    public User() {
    }


    /**
     * Parameterized constructor for the User class.
     */
    public User(String username, String password, int reviewAmount, String realName, int id) {
        this.username = username;
        this.password = password;
        this.reviewAmount = reviewAmount;
        this.realName = realName;
        this.id = id;
    }


    // Getters and Setter methods

    public int getReviewAmount() { // Returns the number of reviews written by this user
        return reviewAmount;
    }

    public void setReviewAmount(int reviewAmount) { // Updates the number of reviews for this user
        this.reviewAmount = reviewAmount;
    }

    public String getRealName() { // Returns the user's real name
        return realName;
    }

    public void setRealName(String realName) { // Sets the user's real name
        this.realName = realName;
    }

    public String getPassword() { // Returns the user's password
        return password;
    }

    public String getUsername() { // Returns the user's username
        return username;
    }

    public int getId() { // Returns the user's unique ID
        return id;
    }

    public String insertIntoTemplate(String template) {
        template = template.replace("realnameTag", this.realName);
        template = template.replace("usernameTag", this.username);
        template = template.replace("IdNum", Integer.toString(this.id));
        return template;
    }
}
