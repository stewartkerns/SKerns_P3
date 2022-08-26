/*
 * Stewart Kerns
 * CPSC 5002, Seattle University
 * This is free and unencumbered software released into the public domain.
 */

package SKerns_P3;

//import the Scanner class
import java.util.Scanner;


/**
 * This program uses the GameModel class in order to play the SillyCardGame in
 * which two players have a queue of 7 shuffled cards at the start of the game
 * and take turns comparing their current card to the top of discarded cards
 * pile. Based on if their card is higher, lower, or even, they have to pull a
 * varying amount of new cards from the shuffled pile
 *
 * @author Stewart Kerns
 * @version 1.0
 */
public class SillyCardGame {


    /**
     * The main method welcomes the user to the game, allows the user to play
     * as many times as they would like to, until they press "no", and then
     * prints a goodbye message before exiting the game.
     *
     * @param args a String array containing the command line arguments
     * @throws IllegalArgumentException if a stack or queue is empty and
     * accessed
     */
    public static void main(String[] args) throws IllegalArgumentException {

        //create a Scanner object to take in user input
        Scanner keyboardIn = new Scanner(System.in);

        //give the user a welcome message
        welcome();

        //play the game as many times as the user would like
        do {
            playGame();
        }while (repeatProgram(keyboardIn));

        //give the user a goodbye message
        goodbye();

        //close the scanner object
        keyboardIn.close();
    }


    /**
     * This method plays a single instance of the silly card game.  It displays
     * which player turn it is, then what their cards the hold are. It then
     * displays the current discard pile card, the current player card, and the
     * required action based on the comparison between the two.  Finally, it
     * displays the outcome of the game at the very end.
     *
     * @throws IllegalArgumentException if a stack or queue is empty and
     * accessed
     */
    public static void playGame() throws IllegalArgumentException {

        //create a new object of the GameModel class
        GameModel game = new GameModel();

        //continue to loop through the player's turns while the game has not
        //ended
        do {
            //run each of the player's turns in a for loop until the game ends
            for (int i = 0; i < game.getNUM_PLAYERS() && game.playerResult();
                 i++) {
                //set the current player's turn
                game.setPlayerTurn(i);

                //print out which player's turn it is and their queue
                System.out.println(game.playerTurnString());
                System.out.println(game.printPlayerQueue());

                //print out the two comparison cards
                System.out.println(game.comparisonCardResult());
                System.out.println(game.checkPlayerCompCard());

                //print out the required action by the player
                System.out.println(game.turnResult());

                //move the player's played card into the discard stack
                game.playerCardToDiscard();
            }
        } while (game.playerResult());

        //print the outcome of the game
        System.out.println(game.gameOutcome());
    }


    /**
     * This method asks the user for input on whether they would like to
     * continue playing again or not. They are required to type "no" to return
     * a false and exit the game.
     *
     * @param keyboardIn a Scanner object to take in user input
     * @return boolean value of whether they want to continue playing or not
     */
    public static boolean repeatProgram(Scanner keyboardIn){

        //ask the user if they want to repeat the program
        System.out.print("\nWould you like to play again?" +
                "(enter no to quit): ");

        //return true if they enter anything other than "no"
        return !(keyboardIn.nextLine().equalsIgnoreCase("NO"));
    }


    /**
     * This method prints a welcome message to the user.
     */
    public static void welcome(){

        //print the welcome message
        System.out.print("Welcome the the SillyCardGame! This game can be " +
                "played with two players.\nEach player will be dealt 7 cards " +
                "before a card is placed on the discard stack.\nPlayers will " +
                "take turns playing their card and comparing to the" +
                " discard\nstack. First player to get rid of all their cards " +
                "wins.\n");
    }


    /**
     * This method prints a goodbye message to the user
     */
    public static void goodbye(){

        //print the goodbye message
        System.out.println("Thanks for playing!");
    }
}


