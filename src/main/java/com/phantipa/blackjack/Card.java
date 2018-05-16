package com.phantipa.blackjack;

public class Card {
    private String text;
    private Integer value;

    public Card(String text) {
        this.text = text;

        String symbol = text.substring(1);
        try {
            value = Integer.parseInt(symbol);
        } catch (NumberFormatException ex) {
            if (symbol.length()==1){
                if (symbol.equalsIgnoreCase("A")) {
                    value = 11;
                } else {
                    value = 10;
                }
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
