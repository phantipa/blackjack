package com.phantipa.blackjack;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashSet;
import java.util.List;
import java.util.Scanner;

public class Blackjack {

    private static final String COMMA_REGEX = "\\s*,\\s*";
    private static final String CARD_PATTERN_REGEX = "^[CDHS]+([2-9]|[1][0]|[JQKA])";
    private static final String[] SUITS = {"C", "D", "H", "S"};
    private static final String[] VALUES = {"2", "3", "4", "5", "6", "7", "8", "9", "10", "J", "Q", "K", "A"};
    private static final int CARDS_SIZE = 52;
    private static Player winner;

    /**
     * This method presented summary of Blackjack including input, process and output.
     */
    public static void main(String args[]) {
        try {
            String output = process(args);
            System.out.println(output);
        } catch (FileNotFoundException ex) {
            System.out.println("File not found.");
        } catch (InvalidCardException ex) {
            System.out.println(ex.toString());
        }
    }

    /**
     * This method used for processing a single deck of playing cards.
     * Two players (are called Sam and the dealer) who will play against each other.
     * Blackjack is an initial score of 21 with two cards: A + [10, J, Q, K].
     *
     * First round:
     * Each player is given two cards from the top of a shuffled deck of cards.
     * Cards are given in the following order: [sam, dealer, sam, dealer]
     *
     * Second round:
     * If neither player has Blackjack then sam can start drawing cards from the top of the deck.
     * When sam has stopped drawing cards the dealer can start drawing cards from the top of the deck.
     *
     * Must stop rules:
     * Sam must stop drawing cards from the deck if their total reaches 17 or higher.
     * The dealer must stop drawing cards when their total is higher than sam.
     *
     * Find winner rules:
     * Check if either player has Blackjack with their initial hand (first round) and wins the game.
     * Sam wins when both players starts with Blackjack.
     * Dealer wins when both players starts with 22 (A + A).
     * Sam or Dealer has lost the game if their total is higher than 21.
     */
    public static String process(String args[]) throws FileNotFoundException, InvalidCardException {
        List<Card> cards = prepareCards(args);

        //First round
        int cardIdx = 4;
        Sam sam = new Sam(cards.get(0), cards.get(2));
        Dealer dealer = new Dealer(cards.get(1), cards.get(3));

        //Second round: Sam turn
        while (!sam.mustStop() && !dealer.isBJ()) {
            sam.addCard(cards.get(cardIdx++));
        }

        //Second round: Dealer turn
        dealer.setSamValue(sam.value);
        while (!dealer.mustStop() && !sam.isBJ()) {
            dealer.addCard(cards.get(cardIdx++));
        }

        winner = findWinner(sam, dealer);

        return renderResult(winner, sam, dealer);
    }

    /**
     * This method used for reading file and convert file to List<Card>
     *
     * @throws FileNotFoundException if the file does not exist
     * @throws InvalidCardException  if cards size != 52
     */
    public static List<Card> prepareCards(String[] args) throws FileNotFoundException, InvalidCardException {
        List<Card> cards = new ArrayList<>();

        if (args.length > 0) {
            File file = new File(args[0]);
            StringBuilder sb = new StringBuilder();

            readInputFile(file, sb);

            String[] cardTexts = sb.toString().split(COMMA_REGEX);

            createValidCards(cards, cardTexts);

        } else {
            generateShuffledCards(cards);
        }

        if (cards.size() != CARDS_SIZE) {
            throw new InvalidCardException("Cards size is not equal 52.", CardErrorCode.CARD_MISSING);
        }

        return cards;
    }

    private static void generateShuffledCards(List<Card> cards) {
        for (String s : SUITS) {
            for (String v : VALUES) {
                Card c = new Card(s + v);
                cards.add(c);
            }
        }
        Collections.shuffle(cards);
    }

    private static void readInputFile(File file, StringBuilder sb) throws FileNotFoundException {
        try (Scanner sc = new Scanner(file, "UTF-8")) {
            while (sc.hasNextLine()) {
                sb.append(sc.nextLine());
                if (sb.length() > 0)
                    break;
            }
        }
    }

    private static void createValidCards(List<Card> cards, String[] cardTexts) throws InvalidCardException {
        if (isValidCards(cardTexts)) {
            for (String text : cardTexts) {
                cards.add(new Card(text));
            }
        }
    }

    /**
     * This method used for validating cards.
     * @throws InvalidCardException  if cards mismatch or found duplicate cards
     */
    public static boolean isValidCards(String[] cardTexts) throws InvalidCardException {
        HashSet<String> set = new HashSet<>();

        for (String card : cardTexts) {
            if (!card.matches(CARD_PATTERN_REGEX)) {
                throw new InvalidCardException("Found format mismatch card: " + card, CardErrorCode.CARD_MISMATCH);
            } else if (!set.add(card)) {
                throw new InvalidCardException("Found duplicate cards: " + card, CardErrorCode.CARD_DUPLICATE);
            }
        }

        return true;
    }

    /**
     * This method used for preparing output including the name of the winner to standard out, together
     * with the hands of both sam and the dealer.
     * Using the following format:
     * [sam|dealer]
     * sam: card1, card2,..., cardN
     * dealer: card1, card2,..., cardN
     */
    public static String renderResult(Player winner, Player sam, Player dealer) {
        StringBuilder sb = new StringBuilder();
        sb.append(winner.getName()).append("\n");
        sb.append(sam.getName()).append(":").append(" ").append(sam.showCards()).append("\n");
        sb.append(dealer.getName()).append(":").append(" ").append(dealer.showCards());
        sb.deleteCharAt(sb.length() - 1);

        return sb.toString();
    }

    public static Player findWinner(Player sam, Player dealer) {
        if (sam.isBJ()) {
            return sam;
        }
        if (sam.isAA()) {
            return dealer;
        }

        if (dealer.getValue() <= 21) {
            return dealer;
        }

        return sam;
    }

}
