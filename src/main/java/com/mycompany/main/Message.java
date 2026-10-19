package com.mycompany.main;

// Imports Random so we can generate the unique message ID
import java.util.Random;

// Create the Message class that stores the details of each message (Part 2)
public class Message {

    // Counts how many messages have been sent, starts at 0 and is incremented as messages are sent
    private static int numMessagesSent = 0;

    // Random generator used to create the message ID
    private static final Random random = new Random();

    // Maximum characters allowed in one message
    public static final int MAX_LENGTH = 250;

    // Unique ten-digit message ID used to retrieve or delete messages
    private final String messageId;

    // Message number used in the hash (0 for the first message sent, 1 for the second, etc.)
    private final int messageNumber;

    // Cellphone number of the person receiving the message
    private final String recipient;

    // The message text
    private final String messageText;

    // The message hash, e.g. 00:0:HITHANKS
    private final String messageHash;

    // Create a message and autogenerate its ID and hash
    public Message(String recipient, String messageText) {
        this(generateMessageId(), numMessagesSent, recipient, messageText);
    }

    // Create a message with a given ID and number (used by the unit tests so the hash is predictable)
    public Message(String messageId, int messageNumber, String recipient, String messageText) {
        this.messageId = messageId;
        this.messageNumber = messageNumber;
        this.recipient = recipient;
        this.messageText = messageText;
        this.messageHash = createMessageHash();
    }

    // Generates a random ten-digit number (the first digit is never 0)
    private static String generateMessageId() {
        StringBuilder id = new StringBuilder();
        id.append(random.nextInt(9) + 1);
        for (int i = 1; i < 10; i++) {
            id.append(random.nextInt(10));
        }
        return id.toString();
    }

    // Checks that the message ID is exactly ten digits long
    public boolean checkMessageId() {
        return messageId.length() == 10 && messageId.matches("[0-9]+");
    }

    // Checks that the message does not exceed 250 characters
    public static boolean checkMessageLength(String text) {
        return text != null && text.length() <= MAX_LENGTH;
    }

    // Returns the message required by the brief for the length check
    public static String messageLengthStatus(String text) {
        if (checkMessageLength(text)) {
            return "Message sent";
        } else {
            return "Please enter a message of less than 250 characters.";
        }
    }

    // Creates the hash: first two digits of the ID : message number : first word + last word (all caps)
    public String createMessageHash() {
        String[] words = messageText.trim().split("\\s+");
        // Punctuation is removed from the words so "tonight?" becomes TONIGHT
        String firstWord = words[0].replaceAll("[^A-Za-z0-9]", "");
        String lastWord = words[words.length - 1].replaceAll("[^A-Za-z0-9]", "");
        String firstTwo = messageId.substring(0, 2);
        return (firstTwo + ":" + messageNumber + ":" + firstWord + lastWord).toUpperCase();
    }

    // Marks the message as sent, which increments the number of messages sent
    public void markAsSent() {
        numMessagesSent++;
    }

    // Returns the full details of the message
    public String printMessageDetails() {
        return "Message ID: " + messageId
                + "\nMessage Hash: " + messageHash
                + "\nRecipient: " + recipient
                + "\nMessage: " + messageText;
    }

    // Returns the message as a JSON object (used when storing the message in a file)
    public String toJson() {
        return "  {\n"
                + "    \"messageId\": \"" + escapeJson(messageId) + "\",\n"
                + "    \"messageHash\": \"" + escapeJson(messageHash) + "\",\n"
                + "    \"recipient\": \"" + escapeJson(recipient) + "\",\n"
                + "    \"message\": \"" + escapeJson(messageText) + "\"\n"
                + "  }";
    }

    // Escapes special characters so the text is valid inside a JSON string
    private static String escapeJson(String text) {
        StringBuilder out = new StringBuilder();
        for (char c : text.toCharArray()) {
            switch (c) {
                case '"':
                    out.append("\\\"");
                    break;
                case '\\':
                    out.append("\\\\");
                    break;
                case '\n':
                    out.append("\\n");
                    break;
                case '\r':
                    out.append("\\r");
                    break;
                case '\t':
                    out.append("\\t");
                    break;
                default:
                    if (c < 0x20) {
                        out.append(String.format("\\u%04x", (int) c));
                    } else {
                        out.append(c);
                    }
            }
        }
        return out.toString();
    }

    // Returns how many messages have been sent so far
    public static int getNumMessagesSent() {
        return numMessagesSent;
    }

    public String getMessageId() {
        return messageId;
    }

    public String getMessageHash() {
        return messageHash;
    }

    public String getRecipient() {
        return recipient;
    }

    public String getMessageText() {
        return messageText;
    }
}
