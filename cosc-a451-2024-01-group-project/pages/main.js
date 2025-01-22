/*
    Author: Michael Boomgaart
    Date Created: 03/21/24
    Editors: Boris Alarcon, Matthew Levin, Michael Boomgaart, Jada Farrell
    Date Last Edited: 5/4/2024, 4/19/2024, 04/05/2024, ...,  4/30/24
    changes: Added add restaurant functions and improved other add/edit restaurant functions
    Added a redirect to profile function that is used in reviewTemplate
    Added event listeners to process entering a search and parsing requested information to the java class methods
    Purpose: Houses common javascript needed for html pages
*/

// index.html page functions
let usedCategories = []; // to check which categories have been used to prevent duplicates
document.querySelectorAll('.category-card').forEach(function (card) {
    // manually chosen categories moved from previous index.html
    let possibleCategories = ['American', 'Asian Fusion', 'Bar', 'BBQ', 'Breakfast', 'Brunch', 'Buffets', 'Burgers', 'Cafes', 'Cafeteria',
        'Cajun', 'Creole', 'Caribbean', 'Chicken Wings', 'Chinese', 'Colombian', 'Delis',
        'Dim Sum', 'Fast Food', 'French', 'Hot Dogs', 'Indian', 'Italian', 'Japanese', 'Korean',
        'Latin', 'Lebanese', 'Mediterranean', 'Mexican', 'Middle Eastern', 'New American', 'New Mexican',
        'Peruvian', 'Pizza', 'Pop-Up', 'Ramen', 'Salad', 'Sandwiches', 'Seafood', 'Senegalese', 'Soul Food',
        'Southern', 'Spanish', 'Steakhouses', 'Sushi Bars', 'Tacos', 'Tapas', 'Thai', 'Trinidadian',
        'Turkish', 'Vegan', 'Venezuelan', 'Vietnamese'];

    usedCategories.forEach(function (entry) {
        console.log(entry); // print for debugging
    })
    let index = Math.floor(Math.random() * possibleCategories.length); // randomize which categories show up
    let category = card.textContent; // set another variable for the text content to not immediately switch until checks are complete
    while (card.textContent == 'categoryContent') { // keep checking while the cards say the default categoryContent
        if (!usedCategories.includes(possibleCategories[index])) { // if not a used category
            usedCategories.push(possibleCategories[index]); // add to the used categories array
            category = possibleCategories[index]; // switch the variable
            card.textContent = category; // actually switch the text content
        } else { // if already used category
            index = Math.floor(Math.random() * possibleCategories.length); // randomize the chosen category again
            category = possibleCategories[index]; // switch the variable but not the text content
        }
    }

    card.addEventListener('click', function () {

        let category = this.textContent
        // for current local testing purposes, the if statement allows the servlet to be requested both locally and on web server
        if (window.location.protocol === 'file:') {  // if working locally
            window.location.href = 'http://localhost:8080/bytes/ServletSearchPage?category=' + category;  // redirect to search servlet html locally
        } else {
            window.location.href = window.location.protocol + '//' + window.location.hostname + ':' + window.location.port + '/bytes/ServletSearchPage?category=' + category;   // redirect to search servlet html on web server
        }
        //alert(`Searching for ${query}...`);
    });
});

// Hiding Sign In and Sign Up Buttons once user is logged in
if (document.getElementById('nav-buttons')) {
    let navButtons = document.getElementById('nav-buttons');
    let usernameDisplay = document.getElementById('username-display').innerText;

    if (usernameDisplay === 'usernameTag👤')
        navButtons.classList.add("show-nav-buttons");    // Make buttons visible if on default user
    else
        navButtons.classList.remove("show-nav-buttons"); // Hide buttons if real user is logged in
}

// This is for the image carousel
// Needs to be smoothed out at some point
document.addEventListener('DOMContentLoaded', function () {
    let currentImageIndex = 0;
    const images = document.querySelectorAll('.carousel-image');
    const indicators = document.querySelectorAll('.indicator-bar');

    function switchImage() {
        if (images.length === 0) return; // Guard clause in case there are no images
        images[currentImageIndex].classList.remove('visible');
        indicators[currentImageIndex].classList.remove('active');
        currentImageIndex = (currentImageIndex + 1) % images.length;
        images[currentImageIndex].classList.add('visible');
        indicators[currentImageIndex].classList.add('active');
    }

    // Set an interval for the image to switch
    setInterval(switchImage, 3000); // Switch every 3 seconds
});

// -------------------------------------------------------------------------------------
// Profile Redirect Functionality
function redirectToProfile(clicked) {
    if (window.location.protocol === 'file:') {  // if working locally
        window.location.href = 'http://localhost:8080/bytes/profile?' + 'userID=' + clicked;  // redirect to public profile servlet html locally based on the ID of the restaurant
    } else {
        window.location.href = window.location.protocol + '//' + window.location.hostname + ':' + window.location.port + '/bytes/profile?' + 'userID=' + clicked;   // redirect to public profile servlet html on web server
    }
}

// -------------------------------------------------------------------------------------
// Delete Account Page Functionality
function verifyDeleteAccount() {
    let password = document.getElementById('password').value;

    if (window.location.protocol === 'file:') {  // if working locally
        window.location.href = 'http://localhost:8080/bytes/ServletDeleteAccount' + '?password=' + password;
        console.log(window.location.href + 'true');
    } else {
        window.location.href = window.location.protocol + '//' + window.location.hostname + ':' + window.location.port + '/bytes/ServletDeleteAccount' + '?password=' + password;
        console.log(window.location.href + 'false');
    }
}

if (document.getElementById('delete-account-form')) {
    document.getElementById('delete-account-form').addEventListener('submit', function (event) {
        event.preventDefault();
        verifyDeleteAccount();
    });
}

if (document.getElementById('cancel')) {
    document.getElementById('cancel').addEventListener('click', function () {
        // Redirects the user back to the previous page or home.
        window.history.back();
    });
}

// -------------------------------------------------------------------------------------
// Change Password Page Functionality
function verifyChangePassword() {
    let oldPassword = document.getElementById('old-password').value;
    let newPassword = document.getElementById('new-password').value;

    if (window.location.protocol === 'file:') {  // if working locally
        window.location.href = 'http://localhost:8080/bytes/ServletChangePassword' + '?old-password=' + oldPassword + '&new-password=' + newPassword;
        console.log(window.location.href + 'true');
    } else {
        window.location.href = window.location.protocol + '//' + window.location.hostname + ':' + window.location.port + '/bytes/ServletChangePassword' + '?old-password=' + oldPassword + '&new-password=' + newPassword;
        console.log(window.location.href + 'false');
    }
}

if (document.getElementById('change-password-form')) {
    document.getElementById('change-password-form').addEventListener('submit', function (event) {
        event.preventDefault();
        verifyChangePassword();
    });
}

// -------------------------------------------------------------------------------------
// Change Name Page Functionality
function verifyChangeName() {
    let oldName = document.getElementById('old-name').value;
    let newName = document.getElementById('new-name').value;

    if (window.location.protocol === 'file:') {  // if working locally
        window.location.href = 'http://localhost:8080/bytes/ServletChangeName' + '?old-name=' + oldName + '&new-name=' + newName;
        console.log(window.location.href + 'true');
    } else {
        window.location.href = window.location.protocol + '//' + window.location.hostname + ':' + window.location.port + '/bytes/ServletChangeName' + '?old-name=' + oldName + '&new-name=' + newName;
        console.log(window.location.href + 'false');
    }
}

if (document.getElementById('change-name-form')) {
    document.getElementById('change-name-form').addEventListener('submit', function (event) {
        event.preventDefault();
        verifyChangeName();
    });
}

// ------------------------------------------------------------------------------------
// Change Username Page Functionality
function verifyChangeUsername() {
    let oldUsername = document.getElementById('old-username').value;
    let newUsername = document.getElementById('new-username').value;

    if (window.location.protocol === 'file:') {  // if working locally
        window.location.href = 'http://localhost:8080/bytes/ServletChangeUsername' + '?old-username=' + oldUsername + '&new-username=' + newUsername;
        console.log(window.location.href + 'true');
    } else {
        window.location.href = window.location.protocol + '//' + window.location.hostname + ':' + window.location.port + '/bytes/ServletChangeUsername' + '?old-username=' + oldUsername + '&new-username=' + newUsername;
        console.log(window.location.href + 'false');
    }
}

if (document.getElementById('change-username-form')) {
    document.getElementById('change-username-form').addEventListener('submit', function (event) {
        event.preventDefault();
        verifyChangeUsername();
    });
}

// -------------------------------------------------------------------------------------
// Sign Up page functions
if (document.getElementById('go-to-sign-up')) {
    document.getElementById('go-to-sign-up').addEventListener('click', function () {
        window.location.href = 'signUpPage.html';
    });
}

// Function to handle signing up
// This gets the elements from signUpPage.html and then redirects to
// localhost:8080/bytes/ServletSignUp with the inputs from signUpPage.html
// as parameters in the servlet
function verifyUserSignup() {
    let realname = document.getElementById('full-name').value;
    let username = document.getElementById('new-username').value;
    let password = document.getElementById('new-password').value;

    console.log('Realname:', realname, 'Username:', username, 'Password:', password)

    if (window.location.protocol === 'file:') {  // if working locally
        window.location.href = 'http://localhost:8080/bytes/ServletSignUp' + '?realname=' + realname + '&username=' + username + '&password=' + password;  // redirect to sign up servlet based on real name, username, and password
        console.log(window.location.href + 'true');
    } else {
        window.location.href = window.location.protocol + '//' + window.location.hostname + ':' + window.location.port + '/bytes/ServletSignUp' + '?realname=' + realname + '&username=' + username + '&password=' + password;   // redirect to sign up servlet html on web server
        console.log(window.location.href + 'false');
    }
}

// -------------------------------------------------------------------------------------
// Sign Up page functions
// Add sign-up form submission handling if needed here
const signUpForm = document.getElementById('sign-up-form');
if (signUpForm) {
    signUpForm.addEventListener('submit', function (event) {
        // Prevent the default form submission
        event.preventDefault();

        // Add any validation or processing here before redirecting
        verifyUserSignup();
        // For now, just redirect to index.html
        //window.location.href = 'index.html';
    });
}

// Back button on Sign Up page
const backToSignInBtn = document.getElementById('back-to-sign-in');
if (backToSignInBtn) {
    backToSignInBtn.addEventListener('click', function () {
        window.location.href = 'signInPage.html';
    });
}

// -------------------------------------------------------------------------------------
// Sign Out page functions
document.addEventListener('DOMContentLoaded', (event) => {
    const goToSignInBtn = document.getElementById('go-to-sign-in');
    if (goToSignInBtn) {
        goToSignInBtn.addEventListener('click', function () {
            window.location.href = 'signInPage.html';
        });
    } else {
        // console.error('you darn goofed moron');
    }
});

// Function to handle signing out
// This simply redirects the user to localhost:8080/bytes/ServletSignOut when the 
// sign up button is clicked in the my account page
function signOut() {
    if (window.location.protocol === 'file:') {  // if working locally
        window.location.href = 'http://localhost:8080/bytes/ServletSignOut';
        console.log(window.location.href + 'true');
    } else {
        window.location.href = window.location.protocol + '//' + window.location.hostname + ':' + window.location.port + '/bytes/ServletSignOut';
        console.log(window.location.href + 'false');
    }
}

// implementation for the search bar
function returnSearchString() {
    addEventListener('keydown', (e) => {
        if (e.code === 'Enter') {// check for keydown of the enter key

            let searchQuery = document.getElementById('main-search').value; // get the value of the search bar

            if (window.location.protocol === 'file:') {  // if working locally
                window.location.href = 'http://localhost:8080/bytes/ServletSearchPage?category=' + searchQuery;  // redirect to search servlet html locally
            } else {
                window.location.href = window.location.protocol + '//' + window.location.hostname + ':' + window.location.port + '/bytes/ServletSearchPage?category=' + searchQuery;   // redirect to search servlet html on web server
            }
        }
    })
}

function getReviewsFromDB(clicked) {
    //let searchQuery = document.getElementById('main-search').value; // get the value of the search bar

    let searchQuery = new URLSearchParams(window.location.search).get('category');

    if (window.location.protocol === 'file:') {  // if working locally
        window.location.href = 'http://localhost:8080/bytes/ServletSearchPage' + '?category=' + searchQuery + '&restaurantID=' + clicked;  // redirect to search servlet html locally based on the ID of the restaurant
    } else {
        window.location.href = window.location.protocol + '//' + window.location.hostname + ':' + window.location.port + '/bytes/ServletSearchPage' + '?category=' + searchQuery + '&restaurantID=' + clicked;   // redirect to search servlet html on web server
    }
}

// Functions for making the add restaurant popup visible
const addRestaurantPopup = document.getElementById("add-restaurant-id");

function openRestaurantPopup() {
    // Moves the page a bit down
    window.scrollTo({
        top: window.scrollY + 190,
        behavior: 'smooth' // This enables smooth scrolling
    });
    addRestaurantPopup.classList.add("open-add-restaurant-container");
    closeEditRestaurantPopup(); // remove edit restaurant pop up
    closeReviewPopup();    // remove leave review pop up
    showBtn2.classList.remove("show-edit-restaurant-btn");
    // Function to mask phone number (to reformat in like this (XXX) XXX-XXXX)
    document.getElementById("phone").addEventListener('input', function (e) {
        var x = e.target.value.replace(/\D/g, '').match(/(\d{0,3})(\d{0,3})(\d{0,4})/);
        e.target.value = !x[2] ? x[1] : '(' + x[1] + ') ' + x[2] + (x[3] ? '-' + x[3] : '');
    });
}

function closeRestaurantPopup() { // HPP's changes
    // Moves the page back up
    window.scrollTo({
        top: window.scrollY - 190,
        behavior: 'smooth' // This enables smooth scrolling
    });
    addRestaurantPopup.classList.remove("open-add-restaurant-container");   // remove add restaurant pop up
    showEditRestaurantBtn();
}

// // Function to redirect to ServletAddRestaurant
// function addRestaurantToDB() {
//     addRestaurantPopup.classList.remove("open-add-restaurant-container");

//     if (window.location.protocol === 'file:') {  // if working locally
//         window.location.href = 'http://localhost:8080/bytes/ServletAddRestaurant' // redirect to ServletLeaveReview
//     } else {
//         window.location.href = window.location.protocol + '//' + window.location.hostname + ':' + window.location.port + '/bytes/ServletAddRestaurant' // redirect to ServletLeaveReview
//     }
// }

// Functions for making the review button and the leave-review popup visible
const showBtn = document.getElementById("leaving-review");
const reviewPopup = document.getElementById("leave-review-id");

function showLeaveReviewBtn() {
    let searchQuery = new URLSearchParams(window.location.search).get('restaurantID');
    if (searchQuery != null)
        showBtn.classList.add("show-leave-review-btn");
    else
        console.log('No Restaurant Selected');
}

function openReviewPopup() {
    let searchQuery = new URLSearchParams(window.location.search).get('restaurantID');
    if (searchQuery != null) {
        reviewPopup.classList.add("open-leave-review-container");
        closeRestaurantPopup(); // remove add restaurant pop up
        closeEditRestaurantPopup(); // remove edit restaurant pop up
    }
}

function closeReviewPopup() { // HPP's changes
    let searchQuery = new URLSearchParams(window.location.search).get('restaurantID');
    if (searchQuery != null) {
        reviewPopup.classList.remove("open-leave-review-container");
        showEditRestaurantBtn();
    }
}

setTimeout(showLeaveReviewBtn, 1);

// Function to redirect to deleteReview check in html and ServletDeleteReview page
function deleteReview(clicked) {
    if (document.getElementById('del-rev') != null) {
        if (window.location.protocol === 'file:') {  // if working locally
            window.location.href = 'http://localhost:8080/bytes/deleteReview.html' + '?reviewId=' + clicked;  // redirect to search servlet html locally based on the ID of the restaurant
            console.log(window.location.href + 'true');
        } else {
            window.location.href = window.location.protocol + '//' + window.location.hostname + ':' + window.location.port + '/bytes/deleteReview.html' + '?reviewId=' + clicked;   // redirect to search servlet html on web server
            console.log(window.location.href + 'false');
        }
    } else {
        let searchQuery = new URLSearchParams(window.location.search).get('reviewId');
        if (window.location.protocol === 'file:') {  // if working locally
            window.location.href = 'http://localhost:8080/bytes/ServletDeleteReview?reviewId=' + searchQuery;  // redirect to search servlet html locally based on the ID of the restaurant
            console.log(window.location.href + 'true');
        } else {
            window.location.href = window.location.protocol + '//' + window.location.hostname + ':' + window.location.port + '/bytes/ServletDeleteReview?reviewId=' + searchQuery;   // redirect to search servlet html on web server
            console.log(window.location.href + 'false');
        }
    }
}

function deleteRestaurant(clicked) {
    event.stopPropagation();
    if (document.getElementById('del-res') != null) {
        if (window.location.protocol === 'file:') {  // if working locally
            window.location.href = 'http://localhost:8080/bytes/deleteRestaurant.html' + '?restaurantID=' + clicked;  // redirect to search servlet html locally based on the ID of the restaurant
            console.log(window.location.href + 'true');
        } else {
            window.location.href = window.location.protocol + '//' + window.location.hostname + ':' + window.location.port + '/bytes/deleteRestaurant.html' + '?restaurantID=' + clicked;   // redirect to search servlet html on web server
            console.log(window.location.href + 'false');
        }
    } else {
        let searchQuery = new URLSearchParams(window.location.search).get('restaurantID');
        if (window.location.protocol === 'file:') {  // if working locally
            window.location.href = 'http://localhost:8080/bytes/ServletDeleteRestaurant?restaurantID=' + searchQuery;  // redirect to search servlet html locally based on the ID of the restaurant
            console.log(window.location.href + 'true');
        } else {
            window.location.href = window.location.protocol + '//' + window.location.hostname + ':' + window.location.port + '/bytes/ServletDeleteRestaurant?restaurantID=' + searchQuery;   // redirect to search servlet html on web server
            console.log(window.location.href + 'false');
        }
    }
}

// Functions to close and open edit review popup
const reviewId = document.getElementById('edit-review-id');

function closeEditReviewPopup() { // HPP's changes
    reviewId.classList.remove('open-edit-review-container');
}

function openEditReviewPopup() {
    let searchQuery = new URLSearchParams(window.location.search).get('reviewId');
    if (searchQuery != null && reviewId != null)
        reviewId.classList.add('open-edit-review-container');
    else
        console.log('Not Currently Editing Review');
}

setTimeout(openEditReviewPopup, 1);

// Function to redirect to User Information servlet to populate edit review popup with selected review information
function editReviewPopup(clicked) {
    if (window.location.protocol === 'file:') {  // if working locally
        window.location.href = 'http://localhost:8080/bytes/ServletUserInformation' + '?reviewId=' + clicked;  // redirect to Servlet User Information locally based on the ID of the restaurant
        console.log(window.location.href + 'true');
    } else {
        window.location.href = window.location.protocol + '//' + window.location.hostname + ':' + window.location.port + '/bytes/ServletUserInformation' + '?reviewId=' + clicked;   // redirect to Servlet User Information on web server
        console.log(window.location.href + 'false');
    }
}

document.addEventListener('DOMContentLoaded', (event) => {
    console.log('DOM fully loaded and parsed');

    let signInForm = document.getElementById('sign-in-form');
    if (signInForm) {
        signInForm.addEventListener('submit', (event) => {
            event.preventDefault();
            verifyUserLogin();
        })
    } else {
        // console.error('Sign in form not found');
    }
})

function verifyUserLogin() {
    let username = document.getElementById('username').value;
    let password = document.getElementById('password').value;

    console.log('Username:', username, 'Password:', password)

    if (window.location.protocol === 'file:') {  // if working locally
        window.location.href = 'http://localhost:8080/bytes/ServletAuthenticator' + '?username=' + username + '&password=' + password;  // redirect to search servlet html locally based on the ID of the restaurant
        console.log(window.location.href + 'true');
    } else {
        window.location.href = window.location.protocol + '//' + window.location.hostname + ':' + window.location.port + '/bytes/ServletAuthenticator' + '?username=' + username + '&password=' + password;   // redirect to search servlet html on web server
        console.log(window.location.href + 'false');
    }
}

// Function to display an error message if invalid login credentials are entered
function showErrorMessage() {
    if (document.getElementById('error-message')) {
        let errMessage = document.getElementById('error-message');
        let signedIn = new URLSearchParams(window.location.search).get('signedIn');
        if (signedIn === 'false')
            errMessage.classList.add('show-error-box');
        else
            errMessage.classList.remove('show-error-box');
    } else
        console.log('You are logged in');
}

// Call the function to show password error message
setTimeout(showUsernameErrorMessage, 1);

// Function to display an error message for password-related issues
function showPasswordErrorMessage() {
    if (document.getElementById('change-password-error')) {
        let passwordErrorMessage = document.getElementById('change-password-error');
        let passwordError = new URLSearchParams(window.location.search).get('passwordError');
        if (passwordError === 'true')
            passwordErrorMessage.classList.add('show-error-box');
        else
            passwordErrorMessage.classList.remove('show-error-box');
    }
}

// Call the function to show password error message
setTimeout(showPasswordErrorMessage, 1);

// Function to display an error message for name-related issues
function showNameErrorMessage() {
    if (document.getElementById('change-name-error')) {
        let nameErrorMessage = document.getElementById('change-name-error');
        let nameError = new URLSearchParams(window.location.search).get('nameError');
        if (nameError === 'true')
            nameErrorMessage.classList.add('show-error-box');
        else
            nameErrorMessage.classList.remove('show-error-box');
    }
}

// Call the function to show password error message
setTimeout(showNameErrorMessage, 1);

// Function to display an error message for name-related issues
function showUsernameErrorMessage() {
    if (document.getElementById('change-username-error')) {
        let usernameErrorMessage = document.getElementById('change-username-error');
        let usernameError = new URLSearchParams(window.location.search).get('usernnameError');
        if (usernameError === 'true')
            usernameErrorMessage.classList.add('show-error-box');
        else
            usernameErrorMessage.classList.remove('show-error-box');
    }
}

// Call the function to show password error message
setTimeout(showUsernameErrorMessage, 1);

function myFunction(clicked) {
    var dots = document.getElementById(clicked.toString() + "dots");
    var moreText = document.getElementById(clicked.toString() + "more");
    var btnText = document.getElementById(clicked.toString());

    if (dots.style.display === "none") {
        dots.style.display = "inline";
        btnText.innerHTML = "Read more";
        moreText.style.display = "none";
    } else {
        dots.style.display = "none";
        btnText.innerHTML = "Read less";
        moreText.style.display = "inline";
    }
}

//functionality of edit restaurant
const showBtn2 = document.getElementById("editing-restaurant");
const restaurantPopup = document.getElementById("edit-restaurant-id");

function showEditRestaurantBtn() {
    let searchQuery = new URLSearchParams(window.location.search).get('restaurantID');
    if (searchQuery != null)
        showBtn2.classList.add("show-edit-restaurant-btn");
    else
        console.log('No Restaurant Selected');
}

function openEditRestaurantPopup() {
    let searchQuery = new URLSearchParams(window.location.search).get('restaurantID');
    if (searchQuery != null) {
        restaurantPopup.classList.add("open-edit-restaurant-container");
        addRestaurantPopup.classList.remove("open-add-restaurant-container");   // remove add restaurant pop up
        reviewPopup.classList.remove("open-leave-review-container");    // remove leave review pop up
    }
}

function closeEditRestaurantPopup() { // HPP's changes
    let searchQuery = new URLSearchParams(window.location.search).get('restaurantID');
    if (searchQuery != null)
        restaurantPopup.classList.remove("open-edit-restaurant-container");
}

setTimeout(showEditRestaurantBtn, 1);