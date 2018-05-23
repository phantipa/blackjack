package com.phantipa.blackjack;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;

import java.io.FileNotFoundException;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;

class BlackjackTest {

    private static final String[] SUITS = {"C", "D", "H", "S"};
    private static final String[] VALUES = {"2", "3", "4", "5", "6", "7", "8", "9", "10", "J", "Q", "K", "A"};
    public static final String SAM = "sam";
    public static final String DEALER = "dealer";

    @Nested
    @DisplayName("Tests for method process with input file")
    class BlackjackProcessTest {

        String[] fileName = new String[1];

        @Test
        void testWithBJ_OnlySam_ThenSamWin() throws FileNotFoundException, InvalidCardException {
            fileName[0] = "test_bj_onlysam.txt";

            String expectedOutput = "sam\n" +
                    "sam: DA, DK \n" +
                    "dealer: H10, D10";

            assertEquals(expectedOutput, Blackjack.process(fileName));
        }

        @Test
        void testWithBJ_OnlyDealer_ThenDealerWin() throws FileNotFoundException, InvalidCardException {
            fileName[0] = "test_bj_onlydealer.txt";

            String expectedOutput = "dealer\n" +
                    "sam: S2, C2 \n" +
                    "dealer: DK, SA";

            assertEquals(expectedOutput, Blackjack.process(fileName));
        }

        @Test
        void testWithBJ_ThenSamWin() throws FileNotFoundException, InvalidCardException {
            fileName[0] = "test_bj_bothplayer.txt";

            String expectedOutput = "sam\n" +
                    "sam: DA, H10 \n" +
                    "dealer: SA, CJ";

            assertEquals(expectedOutput, Blackjack.process(fileName));
        }

        @Test
        void testWithFileAA_ThenDealerWin() throws FileNotFoundException, InvalidCardException {
            fileName[0] = "test_aa_bothplayer.txt";

            String expectedOutput = "dealer\n" +
                    "sam: DA, SA \n" +
                    "dealer: CA, HA";

            assertEquals(expectedOutput, Blackjack.process(fileName));
        }

        @Test
        void testWithFileAA_OnlyDealer_ThenSamWin() throws FileNotFoundException, InvalidCardException {
            fileName[0] = "test_aa_onlydealer.txt";

            String expectedOutput = "sam\n" +
                    "sam: H10, D10 \n" +
                    "dealer: SA, DA";

            assertEquals(expectedOutput, Blackjack.process(fileName));
        }

        @Test
        void testWithFileSamWin_ThenSamWin() throws FileNotFoundException, InvalidCardException {
            fileName[0] = "test_samwin.txt";

            String expectedOutput = "sam\n" +
                    "sam: C8, S5, C7 \n" +
                    "dealer: S8, DK, S6";

            assertEquals(expectedOutput, Blackjack.process(fileName));
        }

        @Test
        void testWithFileDealerWin_ThenDealerWin() throws FileNotFoundException, InvalidCardException {
            fileName[0] = "test_dealerwin.txt";

            String expectedOutput = "dealer\n" +
                    "sam: CA, C7 \n" +
                    "dealer: C5, C2, C3, S3, S7";

            assertEquals(expectedOutput, Blackjack.process(fileName));
        }
    }

    @Nested
    @DisplayName("Tests for the method findWinner")
    class RuleToBeWinnerTest {

        @DisplayName("Sam wins when he starts with Blackjack and Dealer gets lower than Sam")
        @Test
        void findWinner_BJ_SamWin() {
            Sam sam = new Sam(new Card("DA"), new Card("H10"));
            Dealer dealer = new Dealer(new Card("SA"), new Card("C9"));
            dealer.setSamValue(sam.value);

            assertEquals(sam, Blackjack.findWinner(sam, dealer));
        }

        @DisplayName("Dealer wins when he starts with Blackjack and Sam gets lower than Dealer")
        @Test
        void findWinner_BJ_DealerWin() {
            Sam sam = new Sam(new Card("DA"), new Card("H9"));
            Dealer dealer = new Dealer(new Card("SA"), new Card("C10"));
            dealer.setSamValue(sam.value);

            assertEquals(dealer, Blackjack.findWinner(sam, dealer));
        }

        @DisplayName("Sam wins when both players starts with Blackjack")
        @Test
        void findWinner_BothBJ_SamWin() {
            Sam sam = new Sam(new Card("DA"), new Card("H10"));
            Dealer dealer = new Dealer(new Card("SA"), new Card("CJ"));
            dealer.setSamValue(sam.value);

            assertEquals(sam, Blackjack.findWinner(sam, dealer));
        }

        @DisplayName("Dealer wins when both players starts with 22 (A + A)")
        @Test
        void findWinner_BothAA_DealerWin() {
            Sam sam = new Sam(new Card("CA"), new Card("HA"));
            Dealer dealer = new Dealer(new Card("DA"), new Card("SA"));
            dealer.setSamValue(sam.value);

            assertEquals(dealer, Blackjack.findWinner(sam, dealer));
        }

        @DisplayName("Sam has lost the game if their total is higher than 21")
        @Test
        void findWinner_SamHigherThan21_DealerWin() {
            Sam sam = new Sam(new Card("SA"), new Card("HA"));
            Dealer dealer = new Dealer(new Card("DA"), new Card("C2"));
            dealer.setSamValue(sam.value);

            assertEquals(dealer, Blackjack.findWinner(sam, dealer));
        }

        @DisplayName("Dealer has lost the game if their total is higher than 21")
        @Test
        void findWinner_DealerHigherThan21_SamWin() {
            Sam sam = new Sam(new Card("SA"), new Card("H10"));
            Dealer dealer = new Dealer(new Card("DA"), new Card("CA"));
            dealer.setSamValue(sam.value);

            assertEquals(sam, Blackjack.findWinner(sam, dealer));
        }

        @DisplayName("Determine which player wins the game (highest score wins)")
        @Test
        void findWinner_DealerHighest_DealerWin() {
            Sam sam = new Sam(new Card("H2"), new Card("C3"));
            sam.addCard(new Card("D2"));
            sam.addCard(new Card("DA"));

            Dealer dealer = new Dealer(new Card("D9"), new Card("HQ"));
            dealer.setSamValue(sam.value);

            assertEquals(dealer, Blackjack.findWinner(sam, dealer));
        }

    }

    @Nested
    @DisplayName("Tests for input file validation")
    class CardInitialTest {

        String[] str;

        @BeforeEach()
        void initStringArg() {
            str = new String[1];
        }

        @DisplayName("If no file is provided, a new shuffled deck of 52 unique cards should be initialized.")
        @Test
        void prepareCards_NoFileNameProvided_InitializedNewCards() throws FileNotFoundException, InvalidCardException {
            List<Card> cards = Blackjack.prepareCards(new String[0]);

            assertNotNull(cards);
            assertEquals(52, cards.size());
        }

        @DisplayName("The game should be able to read a file containing a deck of cards, " +
                "taking the reference to the file as a command line argument, as a starting point.")
        @Test
        void prepareCards_InvalidFile_ExitProgram() throws FileNotFoundException, InvalidCardException {
            str[0] = "test_bj_bothplayer.txt";

            List<Card> cards = Blackjack.prepareCards(str);

            assertNotNull(cards);
            assertEquals(52, cards.size());
        }

        @Test
        void prepareCards_InvalidFile_ThrowFileNotFound() {
            str[0] = "invalid_file_name";

            assertThrows(FileNotFoundException.class,
                    () -> Blackjack.prepareCards(str));
        }

        @Test
        void prepareCards_Missing_ThrowInvalidCardException() {
            str[0] = "test_invalid_missing.txt";

            assertThrows(InvalidCardException.class,
                    () -> Blackjack.prepareCards(str));
        }

        @Test
        void prepareCards_Duplicate_ThrowInvalidCardException() {
            str[0] = "test_invalid_duplicate.txt";
            String[] cardsArr = generateCardsArr();
            cardsArr[0] = "CA";
            cardsArr[1] = "CA";

            assertThrows(InvalidCardException.class, () -> Blackjack.isValidCards(str));
        }

        @Test
        void prepareCards_CreateCardArrFail() {
            str[0] = "test_invalid.txt";

            String[] cardsArr = generateCardsArr();
            cardsArr[0] = "XX";

            assertThrows(InvalidCardException.class, () -> Blackjack.isValidCards(str));
        }

        private String[] generateCardsArr() {
            String[] cards = new String[52];
            int i = 0;
            for (String s : SUITS) {
                for (String v : VALUES) {
                    cards[i] = s + v;
                    i++;
                }
            }
            return cards;
        }
    }

}