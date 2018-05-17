# Blackjack

## How to run Blackjack?
    $ mvn clean install
    $ java -cp target/blackjack-1.0-SNAPSHOT.jar com.phantipa.blackjack.Main 

## How to test Blackjack with command line argument
- Test to read a file containing a deck of cards.

		$ java -cp target/blackjack-1.0-SNAPSHOT.jar com.phantipa.blackjack.Main test.txt 

- Test existing file with Blackjack value, Sam win.

		$ java -cp target/blackjack-1.0-SNAPSHOT.jar com.phantipa.blackjack.Main testbj.txt 

- Test existing file with AA value, Dealer win.

		$ java -cp target/blackjack-1.0-SNAPSHOT.jar com.phantipa.blackjack.Main testaa.txt 

- Test invalid filename or not exist file. Expected output is “File not found.”

		$ java -cp target/blackjack-1.0-SNAPSHOT.jar com.phantipa.blackjack.Main AnInvalidFileName

- Test no file provide,a new shuffled deck of 52 unique cards should be initialized.

		$ java -cp target/blackjack-1.0-SNAPSHOT.jar com.phantipa.blackjack.Main

