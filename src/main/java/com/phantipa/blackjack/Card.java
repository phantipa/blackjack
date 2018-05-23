package com.phantipa.blackjack;

public class Card {
    private String text;
    private Integer value;

    public Card(String text) {
        this.text = text;

        String symbol = text.substring(1);

        boolean isNumeric = symbol.chars().allMatch( Character::isDigit );

        if(isNumeric){
            value = Integer.parseInt(symbol);
        } else {
            if (symbol.equals("A")) {
                value = 11;
            } else {
                value = 10;
            }
        }
    }

    public String getText() {
        return text;
    }

    public Integer getValue() {
        return value;
    }
}
