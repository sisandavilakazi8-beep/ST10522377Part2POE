package com.mycompany.main;

import static org.junit.jupiter.api.Assertions.*;

import org.junit.jupiter.api.Test;

public class MessageTest {

    // Test data from the PoE
    private final String text1 = "Hi Mike, can you join us for dinner tonight?";
    private final String text2 = "Hi Keegan, did you receive the payment?";

    @Test
    public void messageHashForFirstMessage() {
        Message message = new Message("0012345678", 0, "+27718693002", text1);
        assertEquals("00:0:HITONIGHT", message.createMessageHash());
        assertEquals("00:0:HITONIGHT", message.getMessageHash());
    }

    @Test
    public void messageHashForAllTestMessagesInALoop() {
        String[] ids = {"0012345678", "0098765432"};
        String[] recipients = {"+27718693002", "08575975889"};
        String[] texts = {text1, text2};
        String[] expected = {"00:0:HITONIGHT", "00:1:HIPAYMENT"};

        for (int i = 0; i < texts.length; i++) {
            Message message = new Message(ids[i], i, recipients[i], texts[i]);
            assertEquals(expected[i], message.createMessageHash());
        }
    }

    @Test
    public void messageHashWithOneWord() {
        Message message = new Message("1234567890", 2, "+27718693002", "Thanks!");
        assertEquals("12:2:THANKSTHANKS", message.createMessageHash());
    }

    @Test
    public void messageHashUsesCurrentMessageNumber() {
        Message message = new Message("+27718693002", text1);
        String numberInHash = message.getMessageHash().split(":")[1];
        assertEquals(String.valueOf(Message.getNumMessagesSent()), numberInHash);
    }

    @Test
    public void generatedMessageIdIsTenDigits() {
        for (int i = 0; i < 50; i++) {
            Message message = new Message("+27718693002", text1);
            assertTrue(message.checkMessageId());
            assertTrue(message.getMessageId().matches("[1-9][0-9]{9}"));
        }
    }

    @Test
    public void messageIdWithWrongLengthOrLettersIsRejected() {
        assertFalse(new Message("12345678901", 0, "+27718693002", text1).checkMessageId());
        assertFalse(new Message("123456789", 0, "+27718693002", text1).checkMessageId());
        assertFalse(new Message("12345abcde", 0, "+27718693002", text1).checkMessageId());
    }

    @Test
    public void messageLengthWithinLimit() {
        assertTrue(Message.checkMessageLength(text1));
        assertTrue(Message.checkMessageLength("a".repeat(250)));
        assertEquals("Message sent", Message.messageLengthStatus(text1));
    }

    @Test
    public void messageLengthOverLimit() {
        assertFalse(Message.checkMessageLength("a".repeat(251)));
        assertFalse(Message.checkMessageLength(null));
        assertEquals("Please enter a message of less than 250 characters.",
                Message.messageLengthStatus("a".repeat(251)));
    }

    @Test
    public void markAsSentIncreasesTheTotal() {
        int before = Message.getNumMessagesSent();
        new Message("0012345678", 0, "+27718693002", text1).markAsSent();
        assertEquals(before + 1, Message.getNumMessagesSent());
    }

    @Test
    public void messageDetailsAreInTheRightOrder() {
        Message message = new Message("0012345678", 0, "+27718693002", text1);
        String expected = "Message ID: 0012345678"
                + "\nMessage Hash: 00:0:HITONIGHT"
                + "\nRecipient: +27718693002"
                + "\nMessage: " + text1;
        assertEquals(expected, message.printMessageDetails());
    }

    @Test
    public void jsonContainsAllTheFields() {
        Message message = new Message("0012345678", 0, "+27718693002", text1);
        String expected = "  {\n"
                + "    \"messageId\": \"0012345678\",\n"
                + "    \"messageHash\": \"00:0:HITONIGHT\",\n"
                + "    \"recipient\": \"+27718693002\",\n"
                + "    \"message\": \"" + text1 + "\"\n"
                + "  }";
        assertEquals(expected, message.toJson());
    }

    @Test
    public void jsonEscapesQuotesAndNewLines() {
        Message message = new Message("0012345678", 0, "+27718693002", "He said \"hi\"\nthere");
        assertTrue(message.toJson().contains("He said \\\"hi\\\"\\nthere"));
    }

    @Test
    public void gettersReturnTheStoredValues() {
        Message message = new Message("0012345678", 0, "+27718693002", text1);
        assertEquals("0012345678", message.getMessageId());
        assertEquals("+27718693002", message.getRecipient());
        assertEquals(text1, message.getMessageText());
    }
}
