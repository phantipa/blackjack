package com.phantipa.blackjack;

import jdk.nashorn.internal.ir.annotations.Ignore;
import org.junit.jupiter.api.*;

import java.io.FileNotFoundException;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class BlackjackTest {

    private static final String[] SUITS = {"C", "D", "H", "S"};
    private static final String[] VALUES = {"2", "3", "4", "5", "6", "7", "8", "9", "10", "J", "Q", "K", "A"};

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

        String[] str;

        @BeforeEach()
        void initStringArg(){
            str = new String[1];
        }

        @DisplayName("The game should be able to read a file containing a deck of cards, " +
                "taking the reference to the file as a command line argument, as a starting point.")
        @Test
        void prepareCards_InvalidFile_ExitProgram() throws FileNotFoundException {
            str[0] = "test_bj.txt";

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
            str[0] = "invalid_file_name";

            assertThrows(FileNotFoundException.class,
                    () -> Blackjack.prepareCards(str));
        }

        @Test
        void prepareCards_Missing_ThrowIndexOutOfBoundsException() {
            str[0] = "test_invalid_missing.txt";

            assertThrows(IndexOutOfBoundsException.class,
                    () -> Blackjack.prepareCards(str));
        }

        @Ignore
        @Test
        void prepareCards_Duplicate_ThrowIndexOutOfBoundsException() {
            str[0] = "test_invalid_duplicate.txt";
            String[] cardsArr = generateCardsArr();
            cardsArr[0] = "CA";
            cardsArr[1] = "CA";

            assertFalse(Blackjack.isValidCards(cardsArr));
        }

        @Test
        void prepareCards_CreateCardArrFail(){
            str[0] = "test_invalid.txt";

            String[] cardsArr = generateCardsArr();
            cardsArr[0] = "XX";

            assertFalse(Blackjack.isValidCards(cardsArr));
        }

        private String[] generateCardsArr(){
            String[] cards = new String[52];
            int i = 0;
            for (String s : SUITS) {
                for (String v : VALUES) {
                    cards[i]=s+v;
                    i++;
                }
            }
            return cards;
        }
    }

    @Nested
    @DisplayName("Tests with input file")
    class BlackjackWithFile {

        String[] str;

        @BeforeEach()
        void initStringArg(){
            str = new String[1];
        }

        @Test
        void testWithFile_BJ() throws FileNotFoundException {
            str[0] = "test_bj.txt";
            Player winner = testWithInputFile(str).getWinner();

            assertEquals("sam: DA, H10", winner.showCards());
        }

        @Test
        void testWithFile_AA() throws FileNotFoundException {
            str[0] = "test_aa.txt";
            Player winner = testWithInputFile(str).getWinner();

            assertEquals("dealer: CA, HA", winner.showCards());
        }

        @Test
        void testWithFile_SamWin() throws FileNotFoundException {
            str[0] = "test_samwin.txt";
            Player winner = testWithInputFile(str).getWinner();

            assertEquals("sam: C8, S5, C7", winner.showCards());
        }

        @Test
        void testWithFile_DealerWin() throws FileNotFoundException {
            str[0] = "test_dealerwin.txt";
            Player winner = testWithInputFile(str).getWinner();

            assertEquals("dealer: C5, C2, C3, S3, S7", winner.showCards());
        }

        private Blackjack testWithInputFile(String[] str) throws FileNotFoundException {
            List<Card> cards = Blackjack.prepareCards(str);
            Blackjack bj = new Blackjack(cards);
            bj.process();
            return bj;
        }
    }

}