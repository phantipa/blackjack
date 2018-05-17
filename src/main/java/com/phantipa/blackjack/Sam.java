package com.phantipa.blackjack;

public class Sam extends Player {

    public Sam(Card card1, Card card2) {
        super("sam", card1, card2);
    }

    @Override
    public boolean mustStop() {
        if (getValue() < 17) {
            return false;
        }
        return true;
    }

}
