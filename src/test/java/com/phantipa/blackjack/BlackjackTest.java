package com.phantipa.blackjack;

import org.junit.After;
import org.junit.Before;
import org.junit.jupiter.api.Test;

import java.io.ByteArrayOutputStream;
import java.io.PrintStream;

import static org.junit.jupiter.api.Assertions.*;

class BlackjackTest {

    private final ByteArrayOutputStream outContent = new ByteArrayOutputStream();

    @Before
    public void setUpStreams() {
        System.setOut(new PrintStream(outContent));
    }

    @After
    public void restoreStreams() {
        System.setOut(System.out);
    }

    @Test
    void main() {
        Blackjack.main(new String[0]);
    }

    @Test
    void main_InvalidFileName_PrintFileNotFound() {
        String[] str = new String[1];
        str[0] = "";
        Blackjack.main(str);
        assertEquals("File not found.", outContent.toString());
    }

}