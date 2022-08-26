/*
 * Stewart Kerns
 * CPSC 5002, Seattle University
 * This is free and unencumbered software released into the public domain.
 */

package SKerns_P3;

//import the ArrayList class
import java.util.ArrayList;
//import the Random class
import java.util.Random;
//import the Scanner class
import java.util.Scanner;


/**
 * This method creates a game model for the SillyCardGame program. It uses
 * stacks in order to hold the cards in two piles and an ArrayList of queues
 * for each player. It provides the necessary public methods in order to run,
 * returning a string for which player's turn it is, a string for the queues,
 * strings that contain the cards to be compared, the result of the
 * comparison, and the result of the game.
 *
 * @author Stewart Kerns
 * @version 1.0
 */
public class GameModel {

    //declare a final int for the number of players
    private final int NUM_PLAYERS;

    //create an ArrayList that will hold the cards initially for shuffling
    private final ArrayList<Integer> cards = new ArrayList<>();
    //declare an ArrayList that will hold each of the player's card queues
    private ArrayList<GenericQueue<Integer>> playerCardQueues;
    //declare a stack that will hold the discarded cards
    private GenericStack<Integer> discardStack;
    //declare a stack that will hold the shuffled cards
    private GenericStack<Integer> shuffledStack;

    //declare an int to hold the value of the card being compared to
    private int compareCard;
    //declare an int to hold the element value of the current player's turn
    private int playerTurn;
    //declare an int to hold the value of the players card to compare with
    private int playerCompCard;
    //declare an int to hold the value of the comparison between cards
    private int comparison;
    //this boolean holds the bool of if there was a tie game or not
    private boolean tieGame;


    /**
     * This is a no arg constructor that initializes the various fields as well
     * as calling the required methods to create the cards, shuffle them, put
     * them into the shuffled stack, initialize the player queues, distributes
     * the cards to the players in round-robin fashion, Finally it pops the
     * first card onto the discard stack.
     */
    public GameModel(){

        //create the stack of cards using the createCards method
        createCards();
        //Shuffle the deck of cards using the shuffleDeck method
        shuffleDeck();

        //set the number of player to 2
        NUM_PLAYERS = 2;

        //initialize the ArrayList of player queues to the number of players
        playerCardQueues = new ArrayList<>(NUM_PLAYERS);
        //initialize the discard stack
        discardStack = new GenericStack<>();
        //initialize the shuffle stack
        shuffledStack = new GenericStack<>();

        //initialize the tieGame to false
        tieGame = false;
        //initialize comparison to 0
        comparison = 0;
        //initialize compareCard to 0
        compareCard = 0;
        //initialize playerCompCard to 0
        playerCompCard = 0;
        //initialize playerTurn to 0;
        playerTurn = 0;

        //push the shuffled cards created earlier onto the shuffled stack
        shuffledCardsIntoStack();
        //initiate the player's queues
        initPlayerQueues();
        //deal each of the players their cards
        dealPlayersCards();

        //pop the first card off the shuffle stack and push onto discard stack
        discardStack.push(popShuffleCard());
    }


    /**
     * This method initiates each of the player's queues depending on how many
     * players are playing
     */
    private void initPlayerQueues(){

        //initiate a queue for each player
        for (int i = 0; i < NUM_PLAYERS; i++){
            playerCardQueues.add(new GenericQueue<Integer>());
        }
    }


    /**
     * This method returns a formatted String of the current player's card queue
     *
     * @return String hold current player's card holding
     */
    public String printPlayerQueue(){

        //create a final int for max width
        final int MAX_WIDTH = 18;

        //create a new StringBuilder object to make the string
        StringBuilder playerQueueString = new StringBuilder();
        //create a new Scanner object to scan through the toString of the queue
        Scanner strScanner = new Scanner(
                playerCardQueues.get(playerTurn).toString());

        //create a String to hold the String to be appended
        String appString;
        //create a count to add a new line for 80 limit formatting
        int count = 0;

        //Scan through the queue string while there are more values
        while (strScanner.hasNext()){

            //concatenate the string before putting into the StringBuilder
            appString = "| " + strScanner.next() + " ";

            //increment the count;
            count++;

            //add an if statement to ensure the output doesn't go over 80 cols
            if (count == MAX_WIDTH){
                //add a | for formatting and a new line
                playerQueueString.append("|\n");
                //reset the count
                count = 0;
            }

            //add the next card to the string as well as a | for formatting
            playerQueueString.append(appString);
        }

        //add a final | to the end of the string for formatting
        playerQueueString.append("|");

        //close the scanner object
        strScanner.close();

        //return the string built by the StringBuilder
        return playerQueueString.toString();
    }


    /**
     * This method peeks at the card on top of the discard stack.  It then
     * returns a String labeling what the card was
     *
     * @return a String telling what the value of the card peeked at was
     * @throws IllegalArgumentException if the stack is empty
     */
    public String comparisonCardResult() throws IllegalArgumentException {

        //Set the comparison card value to the top of the discard stack
        compareCard = discardStack.peek();

        //return the value
        return "Discard pile card is: " + compareCard;
    }


    /**
     * This method returns a String to label what the current player's cards are
     *
     * @return String labeling what the current player's cards are
     */
    public String playerTurnString(){

        //return the String telling what the current player's cards are
        return "\nPlayer " + playerName() + "'s turn, cards:";
    }


    /**
     * This method pops a card off of the shuffled stack and pushed onto the
     * discard stack. The field playerCompCard is then set equal to the value
     * of the card that was popped.  It returns a String telling what the value
     * was.
     *
     * @return String noting the value of the popped card
     * @throws IllegalArgumentException if the queue is empty
     */
    public String checkPlayerCompCard() throws IllegalArgumentException {

        //set the player's compare card to the value of their front
        playerCompCard = playerCardQueues.get(playerTurn).peek();

        //return a String telling what the player's card played was
        return "Your current card is: " + playerCompCard;
    }


    /**
     * This method dequeues the player card at the end of their turn and pushes
     * it onto the discard stack
     *
     * @throws IllegalArgumentException if the queue is empty
     */
    public void playerCardToDiscard() throws IllegalArgumentException{

        //discard the player card into the discard stack
        discardStack.push(playerCardQueues.get(playerTurn).dequeue());
    }


    /**
     * This method compares the two fields that hold the comparison card from
     * the shuffled deck and the comparison card from the user, it then sets
     * field comparison to the difference
     */
    private void compareCards(){

        //calculate the difference between the cards
        comparison = playerCompCard - compareCard;
    }


    /**
     * This method looks at the comparison of the card that was dealt and the
     * player's card and then returns a String based on the result of the
     * outcome. If the stacks will be completely emptied after the turn, this
     * method will trigger the chain of events to declare a tie.
     *
     * @return String based on the outcome of the card comparison
     * @throws IllegalArgumentException if the stack is empty
     */
    public String turnResult() throws IllegalArgumentException{

        //run the compareCards method for the comparison field to be set
        compareCards();

        // create a variable holding how many cards will be drawn and set it
        //to 0 initially
        int cardsToDraw = 0;
        //declare a String that will be output
        String resultString;

        //if the player's card is greater, set resultString, no cards drawn
        if (comparison > 0){
            resultString = "Player " + playerName() + "'s card is larger" +
                    ", they don't need to draw another card!";
        }

        //if the player's card is the same, set resultString and draw one card
        else if (comparison == 0) {
            resultString = "Player " + playerName() + "'s card is the " +
                    "same value. They will draw another card.";
            cardsToDraw = 1;
        }

        //if the player's card is smaller, set resultString and draw two cards
        else {
            resultString = "Player " + playerName() + "'s card is lower" +
                    ". They will draw two cards.";
            cardsToDraw = 2;

            //if the game was a tie, inform the player that there are no more
            //cards to draw
            if(tieCase()){
                //add a note about the stacks being emptied
                resultString += "\nYou've run out of cards to draw!";
                //return the resultString
                return resultString;
            }
        }

        //run through the for loop for however many cards need to be drawn
        for (int i = 0; i < cardsToDraw; i++){
            //enqueue a card off the shuffle deck
            playerCardQueues.get(playerTurn).enqueue(popShuffleCard());
        }

        //return the resultString
        return resultString;
    }


    /**
     * This method is called once the games is over. It checks if there was a
     * tie and outputs correspondingly if so and if not, then it returns which
     * player won

     * @return String describing what the outcome of the game was
     */
    public String gameOutcome(){

        //if the stacks were emptied, return that there was a tie
        if (tieGame){
            return "The game was a tie.";
        }

        //otherwise, return which player won
        return "Player " + playerName() + " is the winner!";
    }


    /**
     * This method flips the cards over from the discard stack into the
     * shuffled stack
     *
     * @throws IllegalArgumentException if the stack is empty
     */
    public void flipCards() throws IllegalArgumentException{

        //flip cards from the discard stack into the shuffled stack while the
        //discard stack still have cards in it
        while (!discardStack.empty()){
            shuffledStack.push(discardStack.pop());
        }
    }


    /**
     * This method is used to catch the rare case of all the cards being dealt
     * out of both stacks and causing a tie game. It can only happen when a
     * player needs to draw two cards from the stack and there is only one in
     * the discard
     *
     * @return boolean value of if the game has become a tie
     */
    private boolean tieCase(){

        //if the shuffledStack is empty and the discard stack only has one card
        //set tieGame to true and return it
        tieGame = (shuffledStack.empty() && discardStack.size() == 1);

        //return tieGame
        return tieGame;
    }


    /**
     * This method checks if the current player has emptied their queue or if
     * there is a tie condition.  If either of these conditions are true, it
     * returns false.  Otherwise, it will return true to keep running the game
     *
     * @return boolean of if the game should continue to run or not
     */
    public boolean playerResult(){

        //if the current player has emptied their queue or there was a tie game
        //return false to signify the end of the game otherwise return true to
        //continue the game
        return !(playerCardQueues.get(playerTurn).empty() || tieGame);
    }


    /**
     * This method checks to see if the shuffled stack is empty and if so, it
     * flips the cards. It then pops the shuffled stack and returns the value
     *
     * @return integer of card popped off the shuffledStack
     */
    private int popShuffleCard(){

        //if the shuffled stack is empty, flip the cards
        if (shuffledStack.empty()){
            flipCards();
        }

        //pop the shuffled stack and return the value
        return shuffledStack.pop();
    }


    /**
     * This method deals the players cards in round-robin fashion meaning that
     * it deals each player one card, one at a time, before starting with the
     * first player again
     */
    public void dealPlayersCards(){

        //create a final int for how many cards will be dealt to players
        final int CARDS_PER_PLAYER = 7;

        //create a nested for loop to distribute cards to each player
        for (int i = 0; i < CARDS_PER_PLAYER; i++){
            //second loop gives a card to each player
            for (int j = 0; j < NUM_PLAYERS; j++){
                playerCardQueues.get(j).enqueue(shuffledStack.pop());
            }
        }
    }


    /**
     * This method uses the cards ArrayList and pushes each of the shuffled
     * elements onto the shuffledStack
     */
    private void shuffledCardsIntoStack(){

        //push each element onto the stack
        for (int i = 0; i < cards.size(); i++)
            shuffledStack.push(cards.get(i));
    }


    /**
     * This method creates a deck of cards using an ArrayList.  It puts 52
     * cards of 13 different values (1-13) and 4 suits into the ArrayList
     * un-shuffled
     */
    private void createCards(){

        //create a final int for the number of suits
        final int NUM_SUITS = 4;
        //create a final int for the number of values available
        final int NUM_VALUES = 13;

        //create a nested for loop to assign the 52 possible card values
        for (int i = 1; i <= NUM_SUITS; i++){
            for (int j = 1; j <= NUM_VALUES; j++){
                //assign each one of the cards
                cards.add((i * j) - 1, j);
            }
        }
    }


    /**
     * Shuffles the cards using the
     * <a href="https://en.wikipedia.org/wiki/Fisher%E2%80%93Yates_shuffle">
     * Fisher-Yates algorithm</a>
     *
     */
    private void shuffleDeck() {

        //create a new Random object
        Random rand = new Random();

        //use the Fisher-Yates algorithm provided in order to shuffle the
        // ArrayList
        for (int i = cards.size(); i > 1; i--) {
            int j = rand.nextInt(i);
            int temp = cards.get(i - 1);
            cards.set(i - 1, cards.get(j));
            cards.set(j, temp);
        }
    }


    /**
     * This method returns a String value of the current players name which is
     * one numerical value higher than the element of the array they're in
     *
     * @return String of the player's name
     */
    public String playerName(){

        //return the playerTurn
        return "" + (playerTurn + 1);
    }


    /**
     * This method is a setter for playerTurn
     *
     * @param playerTurn int to set playerTurn to
     */
    public void setPlayerTurn(int playerTurn){

        //set the playerTurn;
        this.playerTurn = playerTurn;
    }


    /**
     * This method is a getter for numPlayers
     *
     * @return int for number of players
     */
    public int getNUM_PLAYERS(){

        //return the number of players
        return NUM_PLAYERS;
    }
}
