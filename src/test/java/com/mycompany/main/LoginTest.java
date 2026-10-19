package com.mycompany.main;

import static org.junit.jupiter.api.Assertions.*;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

public class LoginTest {

    private Login login;

    @BeforeEach
    public void setUp() {
        login = new Login();
        login.setNames("Kyle", "Smith");
    }

    @Test
    public void usernameCorrectlyFormatted() {
        assertTrue(login.checkUserName("kyl_1"));
    }

    @Test
    public void usernameIncorrectlyFormatted() {
        assertFalse(login.checkUserName("kyle!!!!!!!"));
        assertFalse(login.checkUserName("kyle1"));
        assertFalse(login.checkUserName(null));
    }

    @Test
    public void passwordMeetsComplexity() {
        assertTrue(login.checkPasswordComplexity("Ch&&sec@ke99!"));
    }

    @Test
    public void passwordDoesNotMeetComplexity() {
        assertFalse(login.checkPasswordComplexity("password"));
        assertFalse(login.checkPasswordComplexity("Password1"));
        assertFalse(login.checkPasswordComplexity("Pass@1"));
        assertFalse(login.checkPasswordComplexity(null));
    }

    @Test
    public void cellPhoneCorrectlyFormatted() {
        assertTrue(login.checkCellPhoneNumber("+27838968976"));
    }

    @Test
    public void cellPhoneIncorrectlyFormatted() {
        assertFalse(login.checkCellPhoneNumber("08966553"));
        assertFalse(login.checkCellPhoneNumber("0838968976"));
        assertFalse(login.checkCellPhoneNumber("+2783896897"));
        assertFalse(login.checkCellPhoneNumber("+278389689761"));
        assertFalse(login.checkCellPhoneNumber(null));
    }

    @Test
    public void registerUserSuccess() {
        String expected = "Username successfully captured.\nPassword successfully captured.\n"
                + "The user has been registered successfully.";
        assertEquals(expected, login.registerUser("kyl_1", "Ch&&sec@ke99!", "+27838968976"));
    }

    @Test
    public void registerUserBadUsername() {
        String expected = "Username is not correctly formatted; please ensure that your username "
                + "contains an underscore and is no more than five characters in length.";
        assertEquals(expected, login.registerUser("kyle!!!!!!!", "Ch&&sec@ke99!", "+27838968976"));
    }

    @Test
    public void registerUserBadPassword() {
        String expected = "Password is not correctly formatted; please ensure that the password "
                + "contains at least eight characters, a capital letter, a number, and a special character.";
        assertEquals(expected, login.registerUser("kyl_1", "password", "+27838968976"));
    }

    @Test
    public void loginSuccessful() {
        login.registerUser("kyl_1", "Ch&&sec@ke99!", "+27838968976");
        assertTrue(login.loginUser("kyl_1", "Ch&&sec@ke99!"));
    }

    @Test
    public void loginFailedWithWrongDetails() {
        login.registerUser("kyl_1", "Ch&&sec@ke99!", "+27838968976");
        assertFalse(login.loginUser("kyl_1", "wrongPass1!"));
        assertFalse(login.loginUser("abc_1", "Ch&&sec@ke99!"));
        assertFalse(login.loginUser(null, null));
    }

    @Test
    public void loginFailsWhenNobodyHasRegistered() {
        assertFalse(login.loginUser("kyl_1", "Ch&&sec@ke99!"));
    }

    @Test
    public void failedRegistrationDoesNotAllowLogin() {
        login.registerUser("kyle!!!!!!!", "Ch&&sec@ke99!", "+27838968976");
        assertFalse(login.loginUser("kyle!!!!!!!", "Ch&&sec@ke99!"));
    }

    @Test
    public void loginStatusMessages() {
        assertEquals("Welcome Kyle ,Smith it is great to see you.", login.returnLoginStatus(true));
        assertEquals("Username or password incorrect, please try again.", login.returnLoginStatus(false));
    }
}
