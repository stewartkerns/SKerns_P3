package SKerns_P3;
import java.util.ArrayList;
import java.util.Random;
import java.util.Scanner;

/**
 *
 * @author Stewart Kerns
 * @version 1.0
 */
public class GameModel {
    private final int NUM_CARDS = 52;
    private final ArrayList<Integer> cards = new ArrayList<>(NUM_CARDS);
    private int numPlayers;
    private String[] playerNames;
    private ArrayList<GenericQueue<Integer>> playerCardQueues;
    private GenericStack<Integer> discardStack;
    private GenericStack<Integer> shuffledStack;
    private int compareCard;

    private int playerTurn;
    private int playerCompCard;
    private int comparison;


    private int count = 52;



    public GameModel(){
        createCards();
        shuffleDeck();

        playerCardQueues = new ArrayList<>(numPlayers);
        discardStack = new GenericStack<>();
        shuffledStack = new GenericStack<>();
        numPlayers = 2;
        shuffledCardsIntoStack();
        initPlayerQueues();
        dealPlayersCards();
    }

    private void initPlayerQueues(){
        for (int i = 0; i < numPlayers; i++){
            playerCardQueues.add(new GenericQueue<Integer>());
        }
    }
    public String printPlayerQueue(){
        StringBuilder playerQueueString = new StringBuilder();
//        System.out.println(playerCardQueues.get(playerTurn));
        Scanner strScanner = new Scanner(
                playerCardQueues.get(playerTurn).toString());
        while (strScanner.hasNext()){
            playerQueueString.append("| " + strScanner.next() + " ");
        }
        playerQueueString.append("|");
        return playerQueueString.toString();
    }
    public String popComparisonCard(){
        compareCard = popShuffleCard();
        discardStack.push(compareCard);
        return "Discard pile card is: " + compareCard;
    }

    public String playerTurnString(){
        return "\nPlayer " + (playerTurn + 1) + "'s turn, cards:";
    }
    public String popPlayerCompCard(){
        discardStack.push(playerCardQueues.get(playerTurn).dequeue());
        playerCompCard = discardStack.peek();
        count++;
        return "Your current card is: " + playerCompCard;

    }

    private void compareCards(){
//        int playerCompareCard;
//        discardStack.push(playerCardQueues.get(playerTurn).dequeue());
//        playerCompareCard = discardStack.peek();
        comparison = playerCompCard - compareCard;
    }
    public String drawCards(){
        compareCards();
        int cardsToDraw = 0;
        String retString;
        if (comparison > 0){
            retString = "Player " + (playerTurn + 1) + "'s card is larger" +
                    ", they don't need to draw another card!";
        }
        else if (comparison == 0) {
            retString = "Player " + (playerTurn + 1) + "'s card is the " +
                    "same value. They will draw another card.";
            cardsToDraw = 1;
            count--;
        }
        else{
            retString = "Player " + (playerTurn + 1) + "'s card is lower" +
                    ". They will draw two cards.";
            cardsToDraw = 2;
            count--;
            count--;
        }
        for (int i = 0; i < cardsToDraw; i++){
//            if(tieGame()){
//                return "There are no more cards, the game is a tie.";
//            }
            playerCardQueues.get(playerTurn).enqueue(popShuffleCard());
        }
        return retString;
    }


    public String gameOutcome(){
        //if the winner
        if (emptyStacks()){
            return "There are no more cards. The game is a tie.";
        }
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
