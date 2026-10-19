package com.mycompany.main;

import static org.junit.jupiter.api.Assertions.*;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

// Tests the JSON storing method in Main
public class MainTest {

    private final Path file = Paths.get("messages.json");
    private byte[] originalFile;

    // Keeps a copy of any messages.json already in the project so the test does not wipe it
    @BeforeEach
    public void backUpFile() throws IOException {
        originalFile = Files.exists(file) ? Files.readAllBytes(file) : null;
    }

    @AfterEach
    public void restoreFile() throws IOException {
        if (originalFile != null) {
            Files.write(file, originalFile);
        } else {
            Files.deleteIfExists(file);
        }
    }

    @Test
    public void storedMessagesAreWrittenToJsonFile() throws IOException {
        List<Message> messages = new ArrayList<>();
        messages.add(new Message("0012345678", 0, "+27718693002", "Hi Mike, can you join us for dinner tonight?"));
        messages.add(new Message("0098765432", 1, "+27838884567", "Where are you?"));

        assertTrue(Main.storeMessagesInJsonFile(messages));

        String json = new String(Files.readAllBytes(file), "UTF-8");
        assertTrue(json.startsWith("["));
        assertTrue(json.contains("\"messageHash\": \"00:0:HITONIGHT\""));
        assertTrue(json.contains("\"messageHash\": \"00:1:WHEREYOU\""));
        assertTrue(json.contains("},\n  {"));
    }

    @Test
    public void emptyListWritesAnEmptyJsonArray() throws IOException {
        assertTrue(Main.storeMessagesInJsonFile(new ArrayList<Message>()));
        assertEquals("[\n]\n", new String(Files.readAllBytes(file), "UTF-8"));
    }
}
