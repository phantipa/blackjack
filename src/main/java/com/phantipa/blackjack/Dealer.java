package com.phantipa.blackjack;

public class Dealer extends Player {

    public void setSamValue(Integer samValue) {
        this.samValue = samValue;
    }

    private Integer samValue;

    public Dealer(Card card1, Card card2) {
        super("dealer", card1, card2);
    }

    @Override
    public boolean mustStop(){
        if (isBJ() || isAA() || samValue > 21) {
            return true;
        }
        return getValue() > samValue;
    }

}
