package SKerns_P3;

import java.util.Scanner;

public class SillyCardGame {

    public static void main(String[] args){
//    GameModel game = new GameModel();
    Scanner keyboardIn = new Scanner(System.in);
//    game.printShuffle();
//
//    game.dealPlayersCards();
//    game.setPlayerTurn(0);
//    System.out.println(game.printPlayerQueue());
//    game.setPlayerTurn(1);
//    System.out.println(game.printPlayerQueue());
    playGame(keyboardIn);
//    System.out.println(GameModel.gameOutcome(j));
    }
    public static void playGame(Scanner keyboardIn) {
        int playerWinner = 0;
        GameModel game = new GameModel();
        do {
            for (int i = 0; i < game.getNumPlayers() && game.playerResult();
                 i++) {
                game.setPlayerTurn(i);
                System.out.println(game.playerTurnString());
                System.out.println(game.printPlayerQueue());
                System.out.println(game.popComparisonCard());
                System.out.println(game.popPlayerCompCard());
                System.out.println(game.drawCards());
                game.printSizes();
            }

        } while (game.playerResult());
        System.out.println(game.gameOutcome());
    }
    public static boolean repeatProgram(Scanner keyboardIn){
        //ask the user if they want to repeat the program
        System.out.print("\nWould you like to play again?" +
                "(enter no to quit): ");

        //return true if they enter anything other than "no"
        return !(keyboardIn.nextLine().equalsIgnoreCase("NO"));
    }
}


