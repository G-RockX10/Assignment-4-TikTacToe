package TicTacToe;

/**
 * Representation of a Tic-Tac-Toe move to be applied to a {@link Board} object.
 */
public class Move {

    /** The row index of the move. */
    public final int row;
    /** The column index of the move. */
    public final int col;
    /** The player (Board.X or Board.O) making the move. */
    public final int player;

    /**
     * Instantiates a new Move object.
     * @param row The row index of the move.
     * @param col The column index of the move.
     * @param player The player (Board.X or Board.O) making the move.
     */
    public Move(int row, int col, int player) {
        this.row = row;
        this.col = col;
        this.player = player;
    }

    @Override
    public String toString() {
        return (player == Board.X ? "X" : player== Board.O ? "O" : "???") 
            + " on (" + (row + 1) + ", " + (col + 1) + ")";
    }

    public static void main(String[] args) {
        System.out.println(new Move(0, 2, Board.X));
        System.out.println(new Move(1, 1, Board.O));
        System.out.println(new Move(2, 3, 20));

    }
    
}
