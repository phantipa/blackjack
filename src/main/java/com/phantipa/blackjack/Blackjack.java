package com.phantipa.blackjack;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Scanner;

public class Blackjack {

    private static final String COMMA_REGEX = "\\s*,\\s*";
    private static final String[] SUITS = {"C", "D", "H", "S"};
    private static final String[] VALUES = {"2", "3", "4", "5", "6", "7", "8", "9", "10", "J", "Q", "K", "A"};
    private List<Card> cards;

    public Blackjack() {
    }

    public Blackjack(List<Card> cards) {
        this.cards = cards;
    }

    public static void main(String args[]) {
        try {
            List<Card> cards = prepareCards(args);
            Blackjack bj = new Blackjack(cards);
            bj.process();
            System.out.println();
        } catch (FileNotFoundException ex) {
            System.out.println("File not found.");
        }

    }

    public static List<Card> prepareCards(String[] args) throws FileNotFoundException {
        List<Card> cards = new ArrayList<>();

        if (args.length > 0) {
            File file = new File(args[0]);
            StringBuilder sb = new StringBuilder();

            try (Scanner sc = new Scanner(file)) {
                while (sc.hasNextLine()) {
                    sb.append(sc.nextLine());
                    if (sb.length() > 0)
                        break;
                }
            }

            //TODO Validate invalid cards
            String[] cardTexts = sb.toString().split(COMMA_REGEX);

            for (String text : cardTexts) {
                Card c = new Card(text);
                cards.add(c);
            }

        } else {
            for (String s : SUITS) {
                for (String v : VALUES) {
                    Card c = new Card(s + v);
                    cards.add(c);
                }
            }
            Collections.shuffle(cards);
        }

        return cards;
    }

    public void process() {
        int cardIdx = 4;

        Sam sam = new Sam(cards.get(0), cards.get(2));
        while (!sam.mustStop()) {
            sam.addCard(cards.get(cardIdx++));
        }

        Dealer dealer = new Dealer(cards.get(1), cards.get(3), sam);
        while (!dealer.mustStop()) {
            dealer.addCard(cards.get(cardIdx++));
        }

        Player winner = findWinner(sam, dealer);

        renderResult(winner, sam, dealer);
    }

    public void renderResult(Player winner, Player sam, Player dealer) {
        System.out.println(winner.getName());
        sam.showCards();
        dealer.showCards();
    }

    public Player findWinner(Player sam, Player dealer) {
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
