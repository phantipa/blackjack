# Blackjack

## How to run Blackjack?
    $ mvn clean install
    $ java -cp target/blackjack-1.0-SNAPSHOT.jar com.phantipa.blackjack.Main [FILE_NAME] 

### Some happy files for testing
    test.txt
    testbj.txt #both players starts with Blackjack, A + [10, J, Q, K]
    testaa.txt #both players starts with 22 (A + A)
    
### Some bad files for testing   
    testbadcard.txt
    testbadcard_duplicate.txt
    testbadcard_missing.txt

