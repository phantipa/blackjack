package com.phantipa.blackjack;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;

import java.io.FileNotFoundException;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.junit.jupiter.api.Assertions.assertFalse;

class BlackjackTest {

    @Nested
    @DisplayName("Tests for the method findWinner")
    class Winner {

        @DisplayName("Sam wins when both players starts with Blackjack")
        @Test
        void findWinner_BJ_SamWin() {
            Sam sam = new Sam(new Card("DA"), new Card("H10"));
            Dealer dealer = new Dealer(new Card("SA"), new Card("CJ"), sam);

            assertEquals(sam, new Blackjack().findWinner(sam, dealer));
        }

        @DisplayName("Dealer wins when both players starts with 22 (A + A)")
        @Test
        void findWinner_AA_DealerWin() {
            Sam sam = new Sam(new Card("CA"), new Card("HA"));
            Dealer dealer = new Dealer(new Card("DA"), new Card("SA"), sam);

            assertEquals(dealer, new Blackjack().findWinner(sam, dealer));
        }

        @DisplayName("Sam has lost the game if their total is higher than 21")
        @Test
        void findWinner_SamHigherThan21_DealerWin() {
            Sam sam = new Sam(new Card("SA"), new Card("HA"));
            Dealer dealer = new Dealer(new Card("DA"), new Card("C2"), sam);

            assertEquals(dealer, new Blackjack().findWinner(sam, dealer));
        }

        @DisplayName("Dealer has lost the game if their total is higher than 21")
        @Test
        void findWinner_DealerHigherThan21_SamWin() {
            Sam sam = new Sam(new Card("SA"), new Card("H10"));
            Dealer dealer = new Dealer(new Card("DA"), new Card("CA"), sam);

            assertEquals(sam, new Blackjack().findWinner(sam, dealer));
        }

        @DisplayName("Determine which player wins the game (highest score wins)")
        @Test
        void findWinner_DealerHighest_DealerWin() {
            Sam sam = new Sam(new Card("H2"), new Card("C3"));
            sam.addCard(new Card("D2"));
            sam.addCard(new Card("DA"));

            Dealer dealer = new Dealer(new Card("D9"), new Card("HQ"), sam);

            assertEquals(dealer, new Blackjack().findWinner(sam, dealer));
        }

    }

    @Nested
    @DisplayName("Tests for input validation")
    class CardInit {
        @DisplayName("The game should be able to read a file containing a deck of cards, " +
                "taking the reference to the file as a command line argument, as a starting point.")
        @Test
        void prepareCards_InvalidFile_ExitProgram() throws FileNotFoundException {
            String[] str = new String[1];
            str[0] = "test.txt";

            List<Card> cards = Blackjack.prepareCards(str);

            assertNotNull(cards);
            assertEquals(52, cards.size());
        }

        @DisplayName("If no file is provided, a new shuffled deck of 52 unique cards should be initialized.")
        @Test
        void prepareCards_NoFileNameProvided_InitializedNewCards() throws FileNotFoundException {
            List<Card> cards = Blackjack.prepareCards(new String[0]);

            assertNotNull(cards);
            assertEquals(52, cards.size());
        }

        @Test
        void prepareCards_InvalidFile_ThrowFileNotFound() {
            String[] str = new String[1];
            str[0] = "InvalidFileName.txt";

            assertThrows(FileNotFoundException.class,
                    () -> {
                        Blackjack.prepareCards(str);
                    });
        }

        @Test
        void prepareCards_CreateCardArrFail_ThrowIndexOutOfBoundsException() {
            String[] str = new String[1];
            str[0] = "testbadcard.txt";

            assertThrows(IndexOutOfBoundsException.class,
                    () -> {
                        testInputBadCard(str);
                    });
        }

        @Test
        void prepareCards_Duplicate_ThrowIndexOutOfBoundsException() {
            String[] str = new String[1];
            str[0] = "testbadcard_duplicate.txt";

            assertThrows(IndexOutOfBoundsException.class,
                    () -> {
                        testInputBadCard(str);
                    });
        }

        @Test
        void prepareCards_Missing_ThrowIndexOutOfBoundsException() {
            String[] str = new String[1];
            str[0] = "testbadcard_missing.txt";

            assertThrows(IndexOutOfBoundsException.class,
                    () -> {
                        testInputBadCard(str);
                    });
        }

        private void testInputBadCard(String[] str) throws FileNotFoundException {
            List<Card> cards = Blackjack.prepareCards(str);
            Blackjack bj = new Blackjack(cards);
            bj.process();
        }

    }

    @Nested
    @DisplayName("Tests for the method Player.mustStop")
    class StopDrawingRule {
        @DisplayName("Sam must stop drawing cards from the deck if their total reaches 17 or higher")
        @Test
        void mustStop_SamReach17_True() {
            Sam sam = new Sam(new Card("D10"), new Card("H7"));
            assertTrue(sam.mustStop());
        }

        @Test
        void mustStop_SamLowerThan17_False() {
            Sam sam = new Sam(new Card("D10"), new Card("H6"));
            assertFalse(sam.mustStop());
        }

        @DisplayName("Dealer must stop drawing cards when their total is higher than sam.")
        @Test
        void mustStop_DealerHigherThanSam_True() {
            Sam sam = new Sam(new Card("D10"), new Card("D7"));
            Dealer dealer = new Dealer(new Card("C10"), new Card("C8"), sam);
            assertTrue(dealer.mustStop());
        }

        @Test
        void mustStop_DealerLowerThanSam_False() {
            Sam sam = new Sam(new Card("D10"), new Card("D7"));
            Dealer dealer = new Dealer(new Card("C10"), new Card("C6"), sam);
            assertFalse(dealer.mustStop());
        }
    }

}