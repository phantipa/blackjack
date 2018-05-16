package com.phantipa.blackjack;

public class Dealer extends Player{

    private Player sam;

    public Dealer(Card card1, Card card2, Player sam){
        super("dealer", card1, card2);
        this.sam = sam;
    }

    @Override
    public boolean shouldStop(){
        if (isBJ() || isAA() || sam.isBJ() || sam.getValue() > 21) {
            return true;
        }
        if (getValue() <= sam.getValue()) {
            return false;
        }
        return true;
    }

}
