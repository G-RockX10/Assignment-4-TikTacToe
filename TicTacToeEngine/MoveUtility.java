package TicTacToeEngine;
import TicTacToe.Move;

public class MoveUtility {
    public final int utility;
    public final Move move;

    public MoveUtility(int utility, Move move) {
        this.utility = utility;
        this.move = move;
    }

}
