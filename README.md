# Blackjack

## How to run Blackjack?
    $ mvn clean install    
    $ java -jar target/blackjack-1.0.jar [FILE_NAME]

### Some happy files for testing
    test_bj_bothplayer.txt #both players starts with Blackjack, A + [10, J, Q, K]
    test_aa.bothplayer.txt #both players starts with 22 (A + A)
    test_bj_onlydealer.txt
    test_bj_onlysam.txt
    test_aa_onlydealer.txt
    test_samwin.txt
    test_dealerwin.txt
    
### Some bad files for testing   
    test_invalid_mismatch.txt
    test_invalid_duplicate.txt
    test_invalid_missing.txt
