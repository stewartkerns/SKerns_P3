package SKerns_P3;
import java.util.ArrayList;
import java.util.Random;

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



    public GameModel(){
        createCards();
        shuffleDeck();
        playerCardQueues = new ArrayList<>();
        discardStack = new GenericStack<>();
        shuffledStack = new GenericStack<>();
        numPlayers = 2;
    }

    public void popComparisonCard(){
        compareCard = popShuffleCard();
    }

    private int compareCards(int playerQueuePos){
        int playerCompareCard;
        int comparison;
        discardStack.push(playerCardQueues.get(playerQueuePos).dequeue());
        playerCompareCard = discardStack.peek();
        comparison = playerCompareCard - compareCard;
        return comparison;
    }
    private int drawCards(int comparison, int playerQueuePos){
        int cardsToDraw = 0;
        if (comparison > 0){
            System.out.print("Player " + playerQueuePos + "'s card is larger " +
                    "and they don't need to draw another card.");
        }
        else if (comparison == 0) {
            System.out.println("Player " + playerQueuePos + "'s card is the " +
                    "same value. They will draw another card.");
            cardsToDraw = 1;
        }
        else{
            System.out.println("Player " + playerQueuePos + "'s card is lower" +
                    ". They will draw two cards.");
            cardsToDraw = 2;
        }
        for (int i = 0; i < cardsToDraw; i++){
            if(tieGame()){
                return -1;
            }
            playerCardQueues.get(playerQueuePos).enqueue(popShuffleCard());
        }
        return 0;
    }

    public void flipCards(){
        while (!discardStack.empty()){
            shuffledStack.push(discardStack.pop());
        }
    }

//    compareCard = shuffledStack.peek();
//            discardStack.push(shuffledStack.pop());
    private boolean tieGame(){
        return (shuffledStack.empty() && discardStack.empty());
    }
    private int popShuffleCard(){
        if (!shuffledStack.empty()){
            return shuffledStack.pop();
        }
        else{
            if (discardStack.empty()) {
                System.out.println("Both piles are empty");
                return -1;
            }
            else{
                flipCards();
                return popShuffleCard();
            }
        }
    }


    private void dealPlayersCards(){
        final int CARDS_PER_PLAYER = 7;
        for (int i = 0; i < CARDS_PER_PLAYER; i++){
            for (int j = 0; j < playerCardQueues.size(); j++){
                playerCardQueues.get(i).enqueue(shuffledStack.pop());
            }
        }
    }

    private void shuffledCardsIntoStack(){
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
        Random rand = new Random();
        for (int i = cards.size(); i > 1; i--) {
            int j = rand.nextInt(i);
            int temp = cards.get(i - 1);
            cards.set(i - 1, cards.get(j));
            cards.set(j, temp);
        }
        shuffledCardsIntoStack();
    }






    //----------------------------------------------------------------------
    //Test methods
    public void printShuffle(){
        System.out.println(cards.size());
        for (int i = 0; i < cards.size(); i++)
            System.out.println(cards.get(i));
    }

}
