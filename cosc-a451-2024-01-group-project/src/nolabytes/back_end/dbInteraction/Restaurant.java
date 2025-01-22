package NolaBytes.back_end.dbInteraction;// Author: Matthew Levin
// Editors: Matthew Levin
// Date Last edited: 3/13/2024
// Date Created: 3/8/2024
// Changes: Added bool to detect if card is selected
// Purpose: to represent and store all the information about a restaurant as an object

import java.util.Arrays;

public class Restaurant {
    private int restaurantID;
    private String name;
    private String phoneNumber;
    private String address;
    private String category;

    private String website;
    private String imageURL;
    private String[] hours;

    /**
    * Default constructor
    */
    public Restaurant() {
    }

    /**
    * Parameterized constructor for the Restaurant class.
    *
    * @param id the ID of the restaurant as an int
    * @param name the name of the restaurant as a String
    * @param phoneNumber the phone number of the restaurant as a String
    * @param address the address of the restaurant as a String
    * @param category the category of the restaurant as a String
    * @param website the website link of the restaurant as a String
    * @param imageURL the url for an image of the restaurant as a String
    * @param hours the opening hours of the restaurant as an array of Strings
    */
    public Restaurant(int id, String name, String phoneNumber, String address, String category, String website, String imageURL, String[] hours) {
        this.restaurantID = id;
        this.name = name;
        this.phoneNumber = phoneNumber;
        this.address = address;
        this.category = category;
        this.website = website;
        this.imageURL = imageURL;
        this.hours = hours;
    }

    /**
    * Gets the ID of the restaurant.
    *
    * @return the int ID of the restaurant.
    */
    public int getRestaurantID() {
        return restaurantID;
    }

    /**
    * Sets the phone number of the restaurant.
    *
    * @param restaurantID the ID of the restaurant as an int
    */
    public void setRestaurantID(int restaurantID) {
        this.restaurantID = restaurantID;
    }

    /**
    * Gets the name of the restaurant.
    *
    * @return the String name of the restaurant.
    */
    public String getName() {
        return name;
    }

    /**
    * Sets the name of the restaurant.
    *
    * @param name the name of the restaurant as a String
    */
    public void setName(String name) {
        this.name = name;
    }

    /**
    * Gets the phone number of the restaurant.
    *
    * @return the String phone number of the restaurant.
    */
    public String getPhoneNumber() {
        return phoneNumber;
    }

    /**
    * Sets the phone number of the restaurant.
    *
    * @param phoneNumber the phone number of the restaurant as a String
    */
    public void setPhoneNumber(String phoneNumber) {
        this.phoneNumber = phoneNumber;
    }

    /**
    * Gets the address of the restaurant.
    *
    * @return the String address of the restaurant.
    */
    public String getAddress() {
        return address;
    }

    /**
    * Sets the address of the restaurant.
    *
    * @param address the address of the restaurant as a String
    */
    public void setAddress(String address) {
        this.address = address;
    }

    /**
    * Gets the categroy of the restaurant.
    *
    * @return the String category of the restaurant.
    */
    public String getCategory() {
        return category;
    }

    /**
    * Sets the category of the restaurant.
    *
    * @param category the category of the restaurant as a String
    */
    public void setCategory(String category) {
        this.category = category;
    }

    /**
    * Gets the website link of the restaurant.
    *
    * @return the String website link of the restaurant.
    */
    public String getWebsite() {
        return website;
    }

    /**
    * Sets the website link of the restaurant.
    *
    * @param website the website link of the restaurant as a String
    */
    public void setWebsite(String website) {
        this.website = website;
    }

    /**
    * Gets the url for an image of the restaurant.
    *
    * @return the String url for an image of the restaurant.
    */
    public String getImageURL() {
        return imageURL;
    }

    /**
    * Sets the url for an image of the restaurant.
    *
    * @param imageURL the url for an image of the restaurant as a String
    */
    public void setImageURL(String imageURL) {
        this.imageURL = imageURL;
    }

    /**
    * Gets the array for the opening hours of the restaurant.
    *
    * @return the array of Strings for the opening hours of the restaurant.
    */
    public String[] getHours() {
        return hours;
    }

    /**
    * Sets the opening hours of the restaurant.
    *
    * @param hours the opening hours of the restaurant as an array of Strings
    */
    public void setHours(String[] hours) {
        this.hours = hours;
    }

    public String insertIntoTemplate(String template, boolean isSelected) {
        template = template.replace("Name", this.name);
        template = template.replace("phoneNumber", this.phoneNumber);
        template = template.replace("address", this.address);
        template = template.replace("category", this.category);
        template = template.replace("websiteUrl", this.website);
        template = template.replace("IdNum", Integer.toString(this.restaurantID));
        template = template.replace("hours[0]", this.hours[0]);
        template = template.replace("hours[1]", this.hours[1]);
        template = template.replace("hours[2]", this.hours[2]);
        template = template.replace("hours[3]", this.hours[3]);
        template = template.replace("hours[4]", this.hours[4]);
        template = template.replace("hours[5]", this.hours[5]);
        template = template.replace("hours[6]", this.hours[6]);

        if (isSelected) {
            template = template.replace("class=\"restaurant-card\"", "class=\"restaurant-card selected-card\"");
        }

        return template;
    }

    @Override
    public String toString() {
        return "nolaBytes.Restaurant{" +
                "restaurantID=" + restaurantID +
                ", name='" + name + '\'' +
                ", phoneNumber='" + phoneNumber + '\'' +
                ", address='" + address + '\'' +
                ", category='" + category + '\'' +
                ", website='" + website + '\'' +
                ", imageURL=" + imageURL +
                ", hours=" + Arrays.toString(hours) +
                '}';
    }

}