package com.mycompany.main;

// Imports Scanner so we can get user input
import java.util.Scanner;

// Imports used to keep lists of messages and to store them in a JSON file
import java.util.ArrayList;
import java.util.List;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Paths;

// Create the Main class
public class Main {

    // Create Scanner for user input
    static Scanner input = new Scanner(System.in);

    // Main method where the program starts
    public static void main(String[] args) {
        Login login = new Login();

        System.out.println("--- Registration ---");

        // Ask for first and last name (used in the welcome message)
        System.out.print("Enter your first name: ");
        String firstName = readLine().trim();
        System.out.print("Enter your last name: ");
        String lastName = readLine().trim();
        login.setNames(firstName, lastName);

        // Ask for the username until it is correctly formatted
        String username;
        while (true) {
            System.out.print("Enter your username: ");
            username = readLine().trim();
            if (login.checkUserName(username)) {
                System.out.println("Username successfully captured.");
                break;
            }
            System.out.println("Username is not correctly formatted; please ensure that your username "
                    + "contains an underscore and is no more than five characters in length.");
        }

        // Ask for the password until it meets the complexity rules
        String password;
        while (true) {
            System.out.print("Enter your password: ");
            password = readLine();
            if (login.checkPasswordComplexity(password)) {
                System.out.println("Password successfully captured.");
                break;
            }
            System.out.println("Password is not correctly formatted; please ensure that the password "
                    + "contains at least eight characters, a capital letter, a number, and a special character.");
        }

        // Ask for the cellphone number until it is correctly formatted
        String cellphone;
        while (true) {
            System.out.print("Enter your cellphone number (+27): ");
            cellphone = readLine().trim();
            if (login.checkCellPhoneNumber(cellphone)) {
                System.out.println("Cell number successfully captured.");
                break;
            }
            System.out.println("Cell number is incorrectly formatted or does not contain an "
                    + "international code; please correct the number and try again.");
        }

        // Call registration method
        System.out.println(login.registerUser(username, password, cellphone));

        // Prompt for login until it is successful
        System.out.println("\n--- Login ---");
        boolean loginSuccessful = false;
        while (!loginSuccessful) {
            System.out.print("Enter username to login: ");
            String loginUsernameInput = readLine().trim();

            System.out.print("Enter password to login: ");
            String loginPasswordInput = readLine();

            // Call login method
            loginSuccessful = login.loginUser(loginUsernameInput, loginPasswordInput);

            // Call login status method
            System.out.println(login.returnLoginStatus(loginSuccessful));
        }

        // Only a user who has logged in successfully can send messages (Part 2)
        quickChat(login);
    }

    // Reads a line from the user; if the input has ended the program exits instead of crashing
    static String readLine() {
        if (!input.hasNextLine()) {
            System.out.println("\nInput ended. Exiting the application.");
            System.exit(0);
        }
        return input.nextLine();
    }

    // Stores the messages in a JSON file, returns true if it was successful
    static boolean storeMessagesInJsonFile(List<Message> storedMessages) {
        StringBuilder json = new StringBuilder("[\n");
        for (int i = 0; i < storedMessages.size(); i++) {
            json.append(storedMessages.get(i).toJson());
            if (i < storedMessages.size() - 1) {
                json.append(",");
            }
            json.append("\n");
        }
        json.append("]\n");

        try {
            Files.write(Paths.get("messages.json"), json.toString().getBytes(StandardCharsets.UTF_8));
            return true;
        } catch (IOException | RuntimeException e) {
            return false;
        }
    }

    // Runs the QuickChat application (Part 2)
    public static void quickChat(Login login) {
        // Display the welcome message
        System.out.println("\nWelcome to QuickChat.");

        // The user defines how many messages they wish to enter
        int maxMessages = 0;
        while (maxMessages < 1) {
            System.out.print("How many messages do you wish to enter? ");
            try {
                maxMessages = Integer.parseInt(readLine().trim());
                if (maxMessages < 1) {
                    System.out.println("Please enter a number greater than 0.");
                }
            } catch (NumberFormatException e) {
                System.out.println("Please enter a valid number.");
            }
        }

        // Lists of messages that have been sent and stored
        List<Message> sentMessages = new ArrayList<>();
        List<Message> storedMessages = new ArrayList<>();

        // Counts the messages that have been sent or stored (deleted messages do not count)
        int messagesHandled = 0;

        // The application runs until the user selects quit
        boolean running = true;
        while (running) {
            // Display the numeric menu
            System.out.println("\nPlease choose an option:");
            System.out.println("1) Send Messages");
            System.out.println("2) Show recently sent messages");
            System.out.println("3) Quit");
            System.out.print("Option: ");

            String choice = readLine().trim();

            switch (choice) {
                case "1":
                    // Option 1: send messages until the chosen limit is reached
                    if (messagesHandled >= maxMessages) {
                        System.out.println("You have already entered all " + maxMessages + " messages.");
                        break;
                    }
                    while (messagesHandled < maxMessages) {
                        System.out.println("\n--- Message " + (messagesHandled + 1) + " of " + maxMessages + " ---");

                        // Ask for the recipient until the number is correctly formatted
                        String recipient;
                        while (true) {
                            System.out.print("Enter the recipient's cellphone number (+27): ");
                            recipient = readLine().trim();
                            if (login.checkCellPhoneNumber(recipient)) {
                                break;
                            }
                            System.out.println("Cell number is incorrectly formatted or does not contain an "
                                    + "international code; please correct the number and try again.");
                        }

                        // Ask for the message until it is valid
                        String text;
                        while (true) {
                            System.out.print("Enter your message (max " + Message.MAX_LENGTH + " characters): ");
                            text = readLine().trim();
                            if (text.isEmpty()) {
                                System.out.println("The message cannot be empty.");
                            } else if (!Message.checkMessageLength(text)) {
                                System.out.println(Message.messageLengthStatus(text));
                            } else {
                                System.out.println(Message.messageLengthStatus(text));
                                break;
                            }
                        }

                        // Create the message (the ID and hash are generated automatically)
                        Message message = new Message(recipient, text);

                        // Ask the user what to do with the message
                        boolean decided = false;
                        while (!decided) {
                            System.out.println("\nWhat would you like to do with this message?");
                            System.out.println("1) Send Message");
                            System.out.println("2) Disregard Message");
                            System.out.println("3) Store Message to send later");
                            System.out.print("Option: ");
                            String action = readLine().trim();

                            if (action.equals("1")) {
                                // Send the message and display its full details
                                message.markAsSent();
                                sentMessages.add(message);
                                messagesHandled++;
                                System.out.println("Message successfully sent");
                                System.out.println("\n" + message.printMessageDetails());
                                decided = true;
                            } else if (action.equals("2")) {
                                // Disregard the message
                                System.out.println("Press 0 to delete the message");
                                String confirm = readLine().trim();
                                if (confirm.equals("0")) {
                                    System.out.println("Message deleted.");
                                    decided = true;
                                } else {
                                    System.out.println("The message was not deleted.");
                                }
                            } else if (action.equals("3")) {
                                // Store the message in a JSON file to send later
                                storedMessages.add(message);
                                if (storeMessagesInJsonFile(storedMessages)) {
                                    System.out.println("Message successfully stored");
                                } else {
                                    System.out.println("The message could not be saved to the file, "
                                            + "but it was kept in memory.");
                                }
                                messagesHandled++;
                                decided = true;
                            } else {
                                System.out.println("Invalid option, please enter 1, 2 or 3.");
                            }
                        }
                    }
                    break;

                case "2":
                    // Option 2: still in development
                    System.out.println("Coming Soon.");
                    break;

                case "3":
                    // Option 3: quit the application
                    System.out.println("Total messages sent: " + Message.getNumMessagesSent());
                    System.out.println("Goodbye.");
                    running = false;
                    break;

                default:
                    System.out.println("Invalid option, please enter 1, 2 or 3.");
            }
        }
    }
}
