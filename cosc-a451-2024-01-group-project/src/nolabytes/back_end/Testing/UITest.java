// Author: Omar Essa
// Date Created: 04/09/24
// Editors: Omar Essa, Jacob Roy
// Date Last Edited: 04/09/24
// Purpose: A file for UI tests to test certain aspects of the frontend website
// changes: added asserts to the UI tests, also added many Thread.sleep() instances to slow down the tests
//          so that testers can see what is happening, which required InterruptedException, which
//          I implemented using try catch

// Disclaimer for next QA team and for QA specialist: this code uses Selenium-generated Junit code, which
// requires many more jars to be added, which I added and was able to get working. If you do not like the
// large number of jars, then please speak to me, and we try to find a better solution

package NolaBytes.back_end.Testing;

// Generated by Selenium IDE

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.openqa.selenium.*;
import org.openqa.selenium.chrome.ChromeDriver;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

// Java class to perform Selenium UI testing on aspects of the website (so have the tomcat container
// open and running)
public class UITest {
    JavascriptExecutor js;
    private WebDriver driver;

    // Function that sets up a ChromeDriver (may need to include FireFox and other drivers later),
    // which is what allows the test to be connected to the localhost site
    @BeforeEach
    void setUp() {
        driver = new ChromeDriver();
        js = (JavascriptExecutor) driver;
    }

    // Function that tests clicking a card on the website to see
    // if it properly brings up the restaurant list or not
    @Test
    public void clickCard() {
        try {
            driver.get("http://localhost:8080/bytes/signInPage.html");
            driver.manage().window().setSize(new Dimension(1440, 900));
            Thread.sleep(2000);
            driver.findElement(By.id("username")).click();
            Thread.sleep(2000);
            driver.findElement(By.id("username")).sendKeys("MonitoredMarshbuck");
            Thread.sleep(2000);
            driver.findElement(By.id("password")).sendKeys("6p7tS7NaIr");
            Thread.sleep(2000);
            driver.findElement(By.cssSelector("#sign-in-form > button")).click();
            Thread.sleep(2000);
            driver.manage().window().setSize(new Dimension(1440, 816));
            driver.findElement(By.cssSelector(".category-card:nth-child(1)")).click();
            Thread.sleep(2000); // wait two seconds so the tester can see what happened
            {
                List<WebElement> elements = driver.findElements(By.cssSelector("main.results-content *:nth-child(1)"));
                assert (elements.size() > 0);
            }
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

    // Function that tests clicking on a card on the website and then
    // clicking on a restaurant to see if it properly brings up the review
    // list or not
    @Test
    public void clickCardThenRestaurant() {
        try {
            driver.get("http://localhost:8080/bytes/signInPage.html");
            driver.manage().window().setSize(new Dimension(1440, 900));
            Thread.sleep(2000);
            driver.findElement(By.id("username")).click();
            Thread.sleep(2000);
            driver.findElement(By.id("username")).sendKeys("MonitoredMarshbuck");
            Thread.sleep(2000);
            driver.findElement(By.id("password")).sendKeys("6p7tS7NaIr");
            Thread.sleep(2000);
            driver.findElement(By.cssSelector("#sign-in-form > button")).click();
            Thread.sleep(2000);
            driver.manage().window().setSize(new Dimension(1440, 816));
            driver.findElement(By.cssSelector(".category-card:nth-child(1)")).click();
            Thread.sleep(2000);
            {
                List<WebElement> elements = driver.findElements(By.cssSelector("main.results-content *:nth-child(1)"));
                assert (elements.size() > 0);
            }
            driver.findElement(By.cssSelector(".restaurant-card *:nth-child(1)")).click();
            Thread.sleep(2000);
            {
                List<WebElement> elements = driver.findElements(By.cssSelector(".reviews-column *:nth-child(1)"));
                assert (elements.size() > 0);
            }
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

    // Function that tests logging into the website (should fail to login because 
    // the credentials are incorrect)
    @Test
    public void logInInvalid() {
        try {
            driver.get("http://localhost:8080/bytes/");
            driver.manage().window().setSize(new Dimension(1440, 816));
            driver.findElement(By.linkText("Sign In")).click();
            Thread.sleep(2000);
            {
                List<WebElement> elements = driver.findElements(By.cssSelector(".sign-in-container *:nth-child(1)"));
                assert (elements.size() > 0);
            }
            driver.findElement(By.id("username")).click();
            Thread.sleep(2000);
            {
                String value = driver.findElement(By.id("username")).getAttribute("value");
                assertEquals("", value);
            }
            driver.findElement(By.id("username")).sendKeys("joe");
            {
                String value = driver.findElement(By.id("username")).getAttribute("value");
                assertEquals("joe", value);
            }
            driver.findElement(By.id("password")).click();
            Thread.sleep(2000);
            {
                String value = driver.findElement(By.id("password")).getAttribute("value");
                assertEquals("", value);
            }
            driver.findElement(By.id("password")).sendKeys("adads");
            {
                String value = driver.findElement(By.id("password")).getAttribute("value");
                assertEquals("adads", value);
            }
            driver.findElement(By.cssSelector(".form-container")).click();
            driver.findElement(By.cssSelector("#sign-in-form > button")).click();
            {
                List<WebElement> elements = driver.findElements(By.cssSelector(".category-card:nth-child(1)"));
                assert (elements.size() == 0);
            }
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

    // Function that tests logging into the website (should be able to login)
    @Test
    public void logInValid() {
        try {
            driver.get("http://localhost:8080/bytes/");
            driver.manage().window().setSize(new Dimension(1440, 816));
            driver.findElement(By.linkText("Sign In")).click();
            Thread.sleep(2000);
            {
                List<WebElement> elements = driver.findElements(By.cssSelector(".sign-in-container *:nth-child(1)"));
                assert (elements.size() > 0);
            }
            driver.findElement(By.id("username")).click();
            Thread.sleep(2000);
            {
                String value = driver.findElement(By.id("username")).getAttribute("value");
                assertEquals("", value);
            }
            driver.findElement(By.id("username")).sendKeys("QuicknessQuarrier");
            {
                String value = driver.findElement(By.id("username")).getAttribute("value");
                assertEquals("QuicknessQuarrier", value);
            }
            driver.findElement(By.id("password")).click();
            Thread.sleep(2000);
            {
                String value = driver.findElement(By.id("password")).getAttribute("value");
                assertEquals("", value);
            }
            driver.findElement(By.id("password")).sendKeys("DG3INuKmy9");
            {
                String value = driver.findElement(By.id("password")).getAttribute("value");
                assertEquals("DG3INuKmy9", value);
            }
            driver.findElement(By.cssSelector(".form-container")).click();
            driver.findElement(By.cssSelector("#sign-in-form > button")).click();
            {
                List<WebElement> elements = driver.findElements(By.cssSelector(".category-card:nth-child(1)"));
                assert (elements.size() > 0);
            }
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

    // Function that tests typing into the searchbar to find a restaurant or list
    // of restaurants on the website (this currently only works when you type the specific name
    // of a restaurant because it searches for the exact value in the card's header)
    @Test
    public void searchRestaurant() {
        try {
            driver.get("http://localhost:8080/bytes/signInPage.html");
            driver.manage().window().setSize(new Dimension(1440, 900));
            Thread.sleep(2000);
            driver.findElement(By.id("username")).click();
            Thread.sleep(2000);
            driver.findElement(By.id("username")).sendKeys("MonitoredMarshbuck");
            Thread.sleep(2000);
            driver.findElement(By.id("password")).sendKeys("6p7tS7NaIr");
            Thread.sleep(2000);
            driver.findElement(By.cssSelector("#sign-in-form > button")).click();
            Thread.sleep(2000);
            driver.manage().window().setSize(new Dimension(1440, 816));
            driver.findElement(By.id("main-search")).click();
            Thread.sleep(2000);
            {
                String value = driver.findElement(By.id("main-search")).getAttribute("value");
                assertEquals("", value);
            }
            driver.findElement(By.id("main-search")).sendKeys("Boucherie");
            {
                String value = driver.findElement(By.id("main-search")).getAttribute("value");
                assertEquals("Boucherie", value);
            }
            driver.findElement(By.id("main-search")).sendKeys(Keys.ENTER);
            assertEquals("Boucherie", driver.findElement(By.cssSelector(".restaurant-info > h3")).getText());
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

    // Function that tests signing up on the website 
    @Test
    public void signUp() {
        try {
            driver.get("http://localhost:8080/bytes/");
            driver.manage().window().setSize(new Dimension(1440, 816));
            driver.findElement(By.linkText("Sign Up")).click();
            Thread.sleep(2000);
            driver.findElement(By.id("full-name")).click();
            Thread.sleep(2000);
            {
                String value = driver.findElement(By.id("full-name")).getAttribute("value");
                assertEquals("", value);
            }
            driver.findElement(By.id("full-name")).sendKeys("fsf");
            {
                String value = driver.findElement(By.id("full-name")).getAttribute("value");
                assertEquals("fsf", value);
            }
            driver.findElement(By.id("new-username")).click();
            Thread.sleep(2000);
            {
                String value = driver.findElement(By.id("new-username")).getAttribute("value");
                assertEquals("", value);
            }
            driver.findElement(By.id("new-username")).sendKeys("dfsfds");
            {
                String value = driver.findElement(By.id("new-username")).getAttribute("value");
                assertEquals("dfsfds", value);
            }
            driver.findElement(By.id("new-password")).click();
            Thread.sleep(2000);
            {
                String value = driver.findElement(By.id("new-password")).getAttribute("value");
                assertEquals("", value);
            }
            driver.findElement(By.id("new-password")).sendKeys("fsfsd");
            {
                String value = driver.findElement(By.id("new-password")).getAttribute("value");
                assertEquals("fsfsd", value);
            }
            driver.findElement(By.cssSelector("#sign-up-form > button")).click();
            Thread.sleep(2000);
            {
                List<WebElement> elements = driver.findElements(By.cssSelector(".category-card:nth-child(1)"));
                assert (elements.size() > 0);
            }
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

    // Test for loging in used to make all UI test work
    @Test
    public void validLogInTest() {
        try {
            driver.get("http://localhost:8080/bytes/signInPage.html");
            driver.manage().window().setSize(new Dimension(1440, 900));
            Thread.sleep(2000);
            driver.findElement(By.id("username")).click();
            Thread.sleep(2000);
            driver.findElement(By.id("username")).sendKeys("MonitoredMarshbuck");
            Thread.sleep(2000);
            driver.findElement(By.id("password")).sendKeys("6p7tS7NaIr");
            Thread.sleep(2000);
            driver.findElement(By.cssSelector("#sign-in-form > button")).click();
            Thread.sleep(2000);
            assertTrue(driver.findElement(By.cssSelector(".category-card:nth-child(1)")).isDisplayed(), "User has signed in");
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

    // Test for using the read more button from the selected restraunt card
    @Test
    public void readMoreButton() {
        try {
            driver.get("http://localhost:8080/bytes/signInPage.html");
            Thread.sleep(2000);
            driver.manage().window().setSize(new Dimension(1440, 900));
            Thread.sleep(2000);
            driver.findElement(By.id("username")).click();
            Thread.sleep(2000);
            driver.findElement(By.id("username")).sendKeys("MonitoredMarshbuck");
            Thread.sleep(2000);
            driver.findElement(By.id("password")).sendKeys("6p7tS7NaIr");
            Thread.sleep(2000);
            driver.findElement(By.cssSelector("#sign-in-form > button")).click();
            Thread.sleep(2000);
            driver.findElement(By.id("main-search")).click();
            Thread.sleep(2000);
            driver.findElement(By.id("main-search")).sendKeys("wings");
            Thread.sleep(2000);
            driver.findElement(By.id("main-search")).sendKeys(Keys.ENTER);
            Thread.sleep(2000);
            driver.findElement(By.cssSelector("#\\31 22 .restaurant-info")).click();
            Thread.sleep(2000);
            driver.findElement(By.id("4913")).click();
            Thread.sleep(2000);
            assertTrue(driver.findElement(By.id("4913")).isEnabled(), "Read More button is not enabled");
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

    // function that tests session management (signing in and redirecting user when signed out)
    @Test
    public void session() {
        try {
            driver.get("http://localhost:8080/bytes/signInPage.html");
            Thread.sleep(2000);
            driver.findElement(By.id("username")).click();
            Thread.sleep(2000);
            driver.findElement(By.id("username")).sendKeys("MonitoredMarshbuck");
            Thread.sleep(2000);
            driver.findElement(By.id("password")).sendKeys("6p7tS7NaIr");
            Thread.sleep(2000);
            driver.findElement(By.cssSelector("#sign-in-form > button")).click();
            Thread.sleep(2000);
            driver.findElement(By.cssSelector(".user-info")).click();
            Thread.sleep(2000);
            driver.findElement(By.linkText("My Account")).click();
            Thread.sleep(2000);
            driver.findElement(By.cssSelector(".action-btn:nth-child(1)")).click();
            Thread.sleep(2000);
            assertTrue(driver.findElement(By.cssSelector(".welcome-box-container")).isDisplayed(), "Session is not validated");
            driver.findElement(By.id("go-to-sign-in")).click();
            Thread.sleep(2000);

            assertTrue(driver.findElement(By.cssSelector(".sign-in-container")).isDisplayed(), "Session is not validated");

        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

    // Test for using the edit read review from account page
    @Test
    public void editReviewTest() {
        try {
            driver.get("http://localhost:8080/bytes/signInPage.html");
            Thread.sleep(2000);
            driver.findElement(By.id("username")).click();
            Thread.sleep(2000);
            driver.findElement(By.id("username")).sendKeys("MonitoredMarshbuck");
            Thread.sleep(2000);
            driver.findElement(By.id("password")).sendKeys("6p7tS7NaIr");
            Thread.sleep(2000);
            driver.findElement(By.cssSelector("#sign-in-form > button")).click();
            Thread.sleep(2000);
            driver.findElement(By.cssSelector(".user-info")).click();
            Thread.sleep(2000);
            driver.findElement(By.linkText("My Account")).click();
            Thread.sleep(2000);
            driver.findElement(By.cssSelector("#ed-rev > #\\34 451")).click();
            assertTrue(driver.findElement(By.id("4451")).isEnabled(), "Edit review button is not enabled");
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

    // function to test deleting account
    @Test
    public void deleteAccountTest() {
        try {
            driver.get("http://localhost:8080/bytes/signInPage.html");
            Thread.sleep(2000);
            driver.findElement(By.id("username")).click();
            Thread.sleep(2000);
            driver.findElement(By.id("username")).sendKeys("MonitoredMarshbuck");
            Thread.sleep(2000);
            driver.findElement(By.id("password")).sendKeys("6p7tS7NaIr");
            Thread.sleep(2000);
            driver.findElement(By.cssSelector("#sign-in-form > button")).click();
            Thread.sleep(2000);
            driver.findElement(By.cssSelector(".user-info")).click();
            Thread.sleep(2000);
            driver.findElement(By.linkText("My Account")).click();
            Thread.sleep(2000);
            driver.findElement(By.cssSelector(".action-btn:nth-child(2)")).click(); // might need to change the number
            Thread.sleep(2000);
            assertTrue(driver.findElement(By.cssSelector(".delete-account-container")).isDisplayed(), "Delete Account is not validated");
            Thread.sleep(2000);
            driver.findElement(By.id("password")).sendKeys("6p7tS7NaIr");
            Thread.sleep(2000);
            driver.findElement(By.id("confirm-delete")).click();
            Thread.sleep(2000);

            assertTrue(driver.findElement(By.cssSelector(".sign-in-container")).isDisplayed(), "Sign in box found. Proceed to login.");
            driver.findElement(By.id("go-to-sign-in")).click();
            Thread.sleep(2000);
            assertTrue(driver.findElement(By.cssSelector(".sign-in-container")).isDisplayed(), "Login located. Enter credentials to test");

            driver.findElement(By.id("username")).click();
            Thread.sleep(2000);
            driver.findElement(By.id("username")).sendKeys("MonitoredMarshbuck");
            Thread.sleep(2000);
            driver.findElement(By.id("password")).sendKeys("6p7tS7NaIr");
            Thread.sleep(2000);
            driver.findElement(By.cssSelector("#sign-in-form > button")).click();
            assertTrue(driver.findElement(By.cssSelector(".show-error-box")).isDisplayed(), "urrrrr done. Invalid Username or Password.");

        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

    // UI Test for Changing Passwords for accounts.
    @Test
    public void changePassword() {
        driver.get("http://localhost:8080/bytes/signInPage.html");
        driver.manage().window().setSize(new Dimension(570, 300));
        driver.findElement(By.id("username")).click();
        driver.findElement(By.id("username")).sendKeys("MonitoredMarshbuck");
        driver.findElement(By.id("password")).click();
        driver.findElement(By.id("password")).sendKeys("6p7tS7NaIr");
        driver.findElement(By.cssSelector("#sign-in-form > button")).click();
        driver.findElement(By.id("username-display")).click();
        driver.findElement(By.linkText("My Account")).click();
        driver.findElement(By.cssSelector(".action-btn:nth-child(3)")).click();
        driver.findElement(By.id("old-password")).click();
        driver.findElement(By.id("old-password")).sendKeys("6p7tS7NaIr");
        driver.findElement(By.id("new-password")).click();
        driver.findElement(By.id("new-password")).sendKeys("6p7tS7NaIr1");
        driver.findElement(By.id("confirm-change-password")).click();
        driver.findElement(By.cssSelector(".user-info")).click();
        driver.findElement(By.linkText("Sign Out")).click();
        driver.findElement(By.id("go-to-sign-in")).click();
        driver.findElement(By.id("username")).click();
        driver.findElement(By.id("username")).sendKeys("MonitoredMarshbuck");
        driver.findElement(By.id("password")).click();
        driver.findElement(By.id("password")).sendKeys("6p7tS7NaIr1");
        driver.findElement(By.cssSelector("#sign-in-form > button")).click();
        driver.findElement(By.id("username-display")).click();
        driver.findElement(By.linkText("My Account")).click();
        driver.findElement(By.cssSelector(".action-btn:nth-child(3)")).click();
        driver.findElement(By.id("old-password")).click();
        driver.findElement(By.id("old-password")).sendKeys("6p7tS7NaIr1");
        driver.findElement(By.id("new-password")).click();
        driver.findElement(By.id("new-password")).sendKeys("6p7tS7NaIr");
        driver.findElement(By.id("confirm-change-password")).click();
        driver.findElement(By.cssSelector(".user-info")).click();
        driver.findElement(By.linkText("Sign Out")).click();
    }

    //Tests the leave a review function from selecting a resteraunt card
    @Test
    public void leaveReview() {
        try {
            driver.get("http://localhost:8080/bytes/signInPage.html");
            Thread.sleep(2000);
            driver.manage().window().setSize(new Dimension(1728, 1080));
            Thread.sleep(2000);
            driver.findElement(By.id("username")).click();
            Thread.sleep(2000);
            driver.findElement(By.id("username")).sendKeys("MonitoredMarshbuck");
            Thread.sleep(2000);
            driver.findElement(By.id("password")).sendKeys("6p7tS7NaIr");
            Thread.sleep(2000);
            driver.findElement(By.cssSelector("#sign-in-form > button")).click();
            Thread.sleep(2000);
            driver.findElement(By.cssSelector(".category-card:nth-child(5)")).click();
            Thread.sleep(2000);
            driver.findElement(By.cssSelector("li:nth-child(5)")).click();
            Thread.sleep(2000);
            driver.findElement(By.id("leaving-review")).click();
            Thread.sleep(2000);
            driver.findElement(By.cssSelector("label:nth-child(4)")).click();
            Thread.sleep(2000);
            driver.findElement(By.name("review-text")).click();
            Thread.sleep(2000);
            driver.findElement(By.name("review-text")).sendKeys("Good hot dogs. Solid environment. Staff was very helpful as well.");
            Thread.sleep(2000);
            driver.findElement(By.cssSelector("button:nth-child(4)")).click();
            Thread.sleep(2000);
            assertTrue(driver.findElement(By.cssSelector("rate")).isEnabled(), "review is not enabled");
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

    // Function that closes the driver so that there are not 20 browser windows open
    // the tester's computer
    @AfterEach
    void tearDown() {
        try {
            Thread.sleep(2000); // wait two seconds after every test so the tester can see what happened
            driver.quit();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

}