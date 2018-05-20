package com.phantipa.blackjack;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

class PlayerTest {

    @DisplayName("Sam must stop drawing cards from the deck if their total reaches 17 or higher")
    @Test
    void mustStop_SamReach17_True() {
        Sam sam = new Sam(new Card("D10"), new Card("H7"));
        assertTrue(sam.mustStop());
    }

    @Test
    void mustStop_SamLowerThan17_False() {
        Sam sam = new Sam(new Card("D10"), new Card("H6"));
        assertFalse(sam.mustStop());
    }

    @DisplayName("Dealer must stop drawing cards when their total is higher than sam.")
    @Test
    void mustStop_DealerHigherThanSam_True() {
        Sam sam = new Sam(new Card("D10"), new Card("D7"));
        Dealer dealer = new Dealer(new Card("C10"), new Card("C8"), sam);
        assertTrue(dealer.mustStop());
    }

    @Test
    void mustStop_DealerLowerThanSam_False() {
        Sam sam = new Sam(new Card("D10"), new Card("D7"));
        Dealer dealer = new Dealer(new Card("C10"), new Card("C6"), sam);
        assertFalse(dealer.mustStop());
    }
}