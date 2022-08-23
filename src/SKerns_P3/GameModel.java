package SKerns_P3;
import java.util.ArrayList;
import java.util.Random;
import java.util.Scanner;

//TODO remove all the counts
/**
 *
 * @author Stewart Kerns
 * @version 1.0
 */
public class GameModel {
    //create a final integer to hold the number of cards
    //TODO should I delete this since the LinkedList will expand?
    private final int NUM_CARDS = 52;
    //create an ArrayList that will hold the cards initially for shuffling
    private final ArrayList<Integer> cards = new ArrayList<>(NUM_CARDS);
    //declare an int for the number of players
    private int numPlayers;
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


    private int count = 52;


    /**
     * This is a no arg constructor that initializes the various fields as well
     * as calling the required methods to create the cards, shuffle them, put
     * them into the shuffled stack, initialize the player queues, and then
     * distribute the cards to the players in round-robin fashion.
     */
    public GameModel(){
        //create the stack of cards using the createCards method
        createCards();
        //Shuffle the deck of cards using the shuffleDeck method
        shuffleDeck();
        //set the number of player to 2
        numPlayers = 2;

        //initialize the ArrayList of player queues to the number of players
        playerCardQueues = new ArrayList<>(numPlayers);
        //initialize the discard stack
        discardStack = new GenericStack<>();
        //initialize the shuffle stack
        shuffledStack = new GenericStack<>();

        //TODO initialize all the fields?

        //push the shuffled cards created earlier onto the shuffled stack
        shuffledCardsIntoStack();
        //initiate the player's queues
        initPlayerQueues();
        //deal each of the players their cards
        dealPlayersCards();
    }

    /**
     * This method initiates each of the player's queues depending on how many
     * players are playing
     */
    private void initPlayerQueues(){
        //initiate a queue for each player
        for (int i = 0; i < numPlayers; i++){
            playerCardQueues.add(new GenericQueue<Integer>());
        }
    }

    /**
     * This method returns a formatted String of the current player's card queue
     *
     * @return String hold current player's card holding
     */
    public String printPlayerQueue(){
        //create a new StringBuilder object to make the string
        StringBuilder playerQueueString = new StringBuilder();
        //create a new Scanner object to scan through the toString of the queue
        Scanner strScanner = new Scanner(
                playerCardQueues.get(playerTurn).toString());

        //Scan through the queue string while there are more values
        while (strScanner.hasNext()){
            //add the next card to the string as well as a | for formatting
            playerQueueString.append("| " + strScanner.next() + " ");
        }

        //add a final | to the end of the string for formatting
        playerQueueString.append("|");
        //close the scanner object
        strScanner.close();

        //return the string built by the StringBuilder
        return playerQueueString.toString();
    }

    /**
     * This method pops a comparison card off of the shuffled stack and onto
     * the discard stack.  It then returns a String labeling what the card was
     *
     * @return a String telling what the value of the card popped was
     */
    public String popComparisonCard(){
        //pop a card off the shuffled stack and set compareCard to it
        compareCard = popShuffleCard();
        //push the popped card value onto the discard stack
        discardStack.push(compareCard);

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
        return "\nPlayer " + (playerTurn + 1) + "'s turn, cards:";
    }

    /**
     * This method pops a card off of the shuffled stack and pushed onto the
     * discard stack. The field playerCompCard is then set equal to the value
     * of the card that was popped.  It returns a String telling what the value
     * was.
     *
     * @return String noting the value of the popped card
     */
    public String popPlayerCompCard(){
        //push the dequeued card from the player onto the discard stack
        discardStack.push(playerCardQueues.get(playerTurn).dequeue());
        //set the playerCompCard equal to the card that was dequeued
        playerCompCard = discardStack.peek();
        count++;

        //return a String telling what the player's card played was
        return "Your current card is: " + playerCompCard;
    }

    /**
     * This method compares the two fields that hold the comparison card from
     * the shuffled deck and the comparison card from the user, it then sets
     * field comparison to the difference
     */
    private void compareCards(){
//        int playerCompareCard;
//        discardStack.push(playerCardQueues.get(playerTurn).dequeue());
//        playerCompareCard = discardStack.peek();

        //calculate the difference between the cards
        comparison = playerCompCard - compareCard;
    }

    /**
     * This method looks at the comparison of the card that was dealt and the
     * player's card and then returns a String based on the result of the
     * outcome
     *
     * @return String based on the outcome of the card comparison
     */
    public String turnResult(){
        //run the compareCards method for the comparison field to be set
        compareCards();

        // create a variable holding how many cards will be drawn and set it
        //to 0 initially
        int cardsToDraw = 0;
        //declare a String that will be output
        String resultString;

        //if the player's card is greater, set resultString, no cards drawn
        if (comparison > 0){
            resultString = "Player " + (playerTurn + 1) + "'s card is larger" +
                    ", they don't need to draw another card!";
        }

        //if the player's card is the same, set resultString and draw one card
        else if (comparison == 0) {
            resultString = "Player " + (playerTurn + 1) + "'s card is the " +
                    "same value. They will draw another card.";
            cardsToDraw = 1;
            count--;
        }

        //if the player's card is smaller, set resultString and draw two cards
        else{
            resultString = "Player " + (playerTurn + 1) + "'s card is lower" +
                    ". They will draw two cards.";
            cardsToDraw = 2;
            count--;
            count--;
        }

        //run through the for loop for however many cards need to be drawn
        for (int i = 0; i < cardsToDraw; i++){
//            if(tieGame()){
//                return "There are no more cards, the game is a tie.";
//            }
            //enqueue a card off the shuffle deck
            playerCardQueues.get(playerTurn).enqueue(popShuffleCard());
        }

        //return the resultStrin
        return resultString;
    }

    /**
     * This method when called checks if there was a tie and outputs
     * correspondingly if so and if not, then it returns which player won
     *
     * @return String describing what the outcome of the game was
     */
    public String gameOutcome(){
        //if the stacks are empty, return that there was a tie
        if (emptyStacks()){
            return "There are no more cards. The game is a tie.";
        }
        //otherwise, return which player won TODO should I have a +1 method since I use it often?
        return "Player " + (playerTurn + 1) + " is the winner!";
    }

    /**
     * This method flips the cards over from the discard stack into the
     * shuffled stack
     */
    public void flipCards(){
        //flip cards from the discard stack into the shuffled stack while the
        //discard stack still have cards in it
        while (!discardStack.empty()){
            shuffledStack.push(discardStack.pop());
        }
    }
    private boolean emptyStacks(){
        return (shuffledStack.empty() && discardStack.empty());
    }
    /**
     * This method checks if the current player has emptied their queue and if
     * so, returns their number plus one (for signifying someone won).  If all
     * the cards have been depleted from the shuffle and discard stack, then it
     * returns a -1 to signify a tie. If neither of those conditions exist,
     * it returns a 0 to signify no one has won
     *
     *
     * TODO fix this description and method
     * @return int value to determine the outcome of the game
     */
    public boolean playerResult(){
        //if the current player has emptied their queue, they have won
        if (playerCardQueues.get(playerTurn).empty() || emptyStacks()) {
            return false;
        }
//            return playerTurn + 1;
//        }
//
//        //if both deal stacks are empty, the game is over
//        else if (shuffledStack.empty() && discardStack.empty()) {
//            return -1;
//            return false;
//        }

        //if neither of those conditions happened, return a 0
//        return 0;
        return true;
    }

    /**
     * This method pops a card off the shuffled stack and returns the value, if
     * the shuffled stack is empty, it uses the flipCards method to flip the
     * discard stack over and into the shuffled stack.  It then pops and
     * returns the card off the shuffled stack.
     *
     * @return integer popped off the shuffledStack
     */
    private int popShuffleCard(){
        //if the shuffled stack has cards, pop one and return the value
        if (!shuffledStack.empty()){
            return shuffledStack.pop();
        }
        //if the shuffled stack is empty, flip the cards and then pop/return
        //the value
        else {
            flipCards();
            return shuffledStack.pop();
        }
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
            for (int j = 0; j < numPlayers; j++){
                playerCardQueues.get(j).enqueue(shuffledStack.pop());
                //decrement the count of cards
                count--;
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
        //TODO explain this
        for (int i = cards.size(); i > 1; i--) {
            int j = rand.nextInt(i);
            int temp = cards.get(i - 1);
            cards.set(i - 1, cards.get(j));
            cards.set(j, temp);
        }

    }

    /**
     * This method is a getter for playerTurn
     *
     * @return integer of playerTurn
     */
    public int getPlayerTurn(){
        //return the playerTurn
        return playerTurn + 1;
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
    public int getNumPlayers(){
        //return the number of players
        return numPlayers;
    }





    //----------------------------------------------------------------------
    //Test methods
    public void printShuffle(){
        System.out.println(cards.size());
        for (int i = 0; i < cards.size(); i++)
            System.out.println(cards.get(i));
    }
    public void printSizes(){
        System.out.println("\nNumber of cards left in stacks: " + count + "\n");
    }
}
