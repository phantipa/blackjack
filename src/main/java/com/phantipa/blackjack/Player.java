package com.phantipa.blackjack;

import java.util.ArrayList;
import java.util.List;

abstract class Player {
    public static final int BLACKJACK_VALUE = 21;
    public static final int AA_VALUE = 22;
    protected String name;
    protected List<Card> cards = new ArrayList<>();
    protected Integer value;
    protected boolean bj;
    protected boolean aa;

    public Player(String name, Card card1, Card card2) {
        this.name = name;
        cards.add(card1);
        cards.add(card2);
        value = card1.getValue() + card2.getValue();
        bj = value == BLACKJACK_VALUE;
        aa = value == AA_VALUE;
    }

    public String getName() {
        return name;
    }

    public String showCards() {
        StringBuffer sb = new StringBuffer();
        sb.append(name).append(":");
        for(Card card : cards) {
            sb.append(" ").append(card.getText()).append(",");
        }
        sb.deleteCharAt(sb.length()-1);

        return sb.toString();
    }

    public void addCard(Card card) {
        value += card.getValue();
        cards.add(card);
    }

    public int getValue() {
        return value;
    }

    public boolean isBJ() {
        return bj;
    }

    public boolean isAA() {
        return aa;
    }

    public abstract boolean mustStop();
}