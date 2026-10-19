package com.mycompany.main;

// Create the Login class that holds all the registration and login methods
public class Login {

    // Stores the user's first name
    private String firstName;

    // Stores the user's last name
    private String lastName;

    // Stores the registered username
    private String registeredUsername;

    // Store the registered password
    private String registeredPassword;

    // Store the registered cellphone number
    private String registeredCellPhone;

    // Sets the first and last name used in the welcome message
    public void setNames(String firstName, String lastName) {
        this.firstName = firstName;
        this.lastName = lastName;
    }

    // Create the username checking method
    public boolean checkUserName(String username) {
        // Username must have exactly 5 characters and contain a special
        // character such as an underscore or @ (e.g. kyl_1, Eli@1)
        if (username == null || username.length() != 5) {
            return false;
        }
        return username.matches(".*[^a-zA-Z0-9].*");
    }

    // Create the password checking method
    public boolean checkPasswordComplexity(String password) {
        // Password must have at least 8 characters
        // and contain a capital letter, a number and a special character
        if (password != null
                && password.length() >= 8
                && password.matches(".*[A-Z].*")
                && password.matches(".*[0-9].*")
                && password.matches(".*[^a-zA-Z0-9].*")) {
            return true;
        } else {
            return false;
        }
    }

    // Create the cellphone checking method
    public boolean checkCellPhoneNumber(String number) {
        // Number must start with +27 followed by exactly 9 digits
        // Reference: Oracle (2024). Class Pattern, Java Platform SE API.
        // Available at: https://docs.oracle.com/en/java/javase/21/docs/api/java.base/java/util/regex/Pattern.html
        // [Accessed 16 October 2026].
        if (number != null && number.matches("^\\+27[0-9]{9}$")) {
            return true;
        } else {
            return false;
        }
    }

    // Create the registration method, returns the relevant message
    public String registerUser(String username, String password, String cellphone) {
        if (!checkUserName(username)) {
            return "Username is not correctly formatted; please ensure that your username "
                    + "contains an underscore and is no more than five characters in length.";
        }
        if (!checkPasswordComplexity(password)) {
            return "Password is not correctly formatted; please ensure that the password "
                    + "contains at least eight characters, a capital letter, a number, and a special character.";
        }

        // Both conditions are met so the details are stored
        registeredUsername = username;
        registeredPassword = password;
        registeredCellPhone = cellphone;
        return "Username successfully captured.\nPassword successfully captured.\n"
                + "The user has been registered successfully.";
    }

    // Create the login method
    public boolean loginUser(String loginUsername, String loginPassword) {
        // Compare entered details with registered details
        return registeredUsername != null
                && registeredPassword != null
                && registeredUsername.equals(loginUsername)
                && registeredPassword.equals(loginPassword);
    }

    // Create the login status method
    public String returnLoginStatus(boolean loginSuccessful) {
        if (loginSuccessful) {
            return "Welcome " + firstName + " ," + lastName + " it is great to see you.";
        } else {
            return "Username or password incorrect, please try again.";
        }
    }
}
