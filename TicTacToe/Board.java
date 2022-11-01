package TicTacToe;

import java.util.Dictionary;
import java.util.Hashtable;
import java.util.Random;

/**
 * Representation of Tic-Tac-Toe grid state.
 */
public class Board {    
    //* Class constants */
    
    /** 
     * Representation of the state of a cell, line of cells, or the game.
     * <ul>
     * <li><b> NA </b>- Cell is empty, line is not won by either player, or game is unfinished </li>
     * <li><b> X </b>- Cell has an X, or line/game is won by X player </li>
     * <li><b> O </b>- Cell has an O, or line/game is won by O player </li>
     * <li><b> DRAW </b>- Game is finished and neither player has won </li>
     * <li><b> BAD </b>- Board is in an invalid state (wrong number of Xs/Os or more than one line completed) </li>
     * </ul>
     */
    public static final int NA = 0, X = 1, O = 2, DRAW = -1, BAD = -2;

    //* Object variables */

    /** The contents of the Tic-Tac-Toe board. */
    private int[] grid;
    /** The dimensions (size x size) of the board. */
    private int size;

    /**
     * Instantiates a new Board object with an empty grid.
     * @param size The size of the board (both width and height).
     */
    public Board(int size) {
        // Initialize the board and populate it with empty spaces
        this.size = size;
        this.grid = new int[size * size];
        for (int i = 0; i < size * size; i++) grid[i] = NA;
    }

    /**
     * Gets the value of a cell on the board.
     * @param row The row index of the cell to get.
     * @param col The column index of the cell to get.
     * @return NA (0), X (1), or O (2) - the contents of the cell.
     */
    public int cell(int row, int col) {
        return grid[row * size + col];
    }

    /**
     * Sets the value of a cell on the board.
     * @param row The row index of the cell to set.
     * @param col The column index of the cell to set.
     * @param val NA (0), X (1), or O (2) - the new contents of the cell.
     */
    public void set(int row, int col, int val) {
        grid[row * size + col] = val;
    }

    /** 
     * Evaluates a line of cells.
     * @param line The line to evaluate as an array of integers.
     * @return X (1) or O (2) if the line consists of only Xs or Os; NA (0) otherwise.
    */
    private static int evalLine(int[] line) {
        for (int cell : line) {
            if (cell == NA || cell != line[0]) return NA;
        }
        return line[0];
    }

    /** Evaluates a row of cells on the board.
     * @param rowNum The index of the row to evaluate.
     * @return X (1) or O (2) if the line consists of only Xs or Os; NA (0) otherwise.
    */
    private int evalRow(int rowNum) {
        // Build line array from row
        int[] row = new int[size];
        for (int i = 0; i < size; i++) {
            row[i] = cell(rowNum, i);
        }

        return evalLine(row);
    }

    /** 
     * Evaluates a column of cells on the board.
     * @param colNum The index of the column to evaluate.
     * @return X (1) or O (2) if the line consists of only Xs or Os; NA (0) otherwise.
    */
    private int evalCol(int colNum) {
        // Build line array from column
        int[] col = new int[size];
        for (int i = 0; i < size; i++) {
            col[i] = cell(i, colNum);
        }

        return evalLine(col);
    }

    /** 
     * Evaluates the board's diagonal from the top left corner to the bottom right.
     * @return X (1) or O (2) if the line consists of only Xs or Os; NA (0) otherwise.
    */
    private int evalDiagL() {
        // Build line array from diagonal
        int[] diag = new int[size];
        for (int i = 0; i < size; i++) {
            diag[i] = cell(i, i);
        }

        return evalLine(diag);
    }

    /** 
     * Evaluates the board's diagonal from the top right corner to the bottom left.
     * @return X (1) or O (2) if the line consists of only Xs or Os; NA (0) otherwise.
    */
    private int evalDiagR() {
        // Build line array from diagonal
        int[] diag = new int[size];
        for (int i = 0; i < size; i++) {
            diag[i] = cell(i, size - (i + 1));
        }

        return evalLine(diag);
    }

    private boolean onDiagL(int row, int col) { 
        return row == col;
    }
    
    private boolean onDiagR(int row, int col) {
        return row == size - col - 1;
    }

    private boolean onCenter(int row, int col) {
        return row == col && row == size / 2.0;
    }

    private boolean isHighlighted(int row, int col, boolean lineRow, boolean lineCol, boolean lineDiagL, boolean lineDiagR) {
        return lineRow || lineCol || (lineDiagL && onDiagL(row, col)) || (lineDiagR && onDiagR(row, col));
    }
   
    /**
     * @return The next player to move, or DRAW/BAD if the game is drawn or in an invalid state.
     */
    public int nextTurn() {
        // ! Note: calculating the next move like this with an array check instead of just an extra class
        // ! variable is inefficient, but a little simpler in the long run. Should be fine with small board
        // ! sizes, but if performance is a concern, a refactoring may be in order.
        int xCount = 0, oCount = 0;
        for (int cell : grid) {
            if (cell == X) xCount++;
            else if (cell == O) oCount++;
        }
        if(xCount + oCount == size * size) return DRAW;
        else if(xCount - oCount == 0) return X;
        else if(xCount - oCount == 1) return O;
        else return BAD;
    }

    /**
     * Evaluates the state of the game.
     * @return X or O if there is a winner, DRAW if there are no more moves left,
     *  NA if the game is in progress, or BAD if the board state is invalid.
     */
    public int evalGame() {
        // Check if there is an invalid number of Xs and Os
        int turnState = nextTurn();
        if (turnState != X && turnState != O) return turnState;

        int winner = NA;
        // Check for winner (or if more than one line completed)
        {
            int lines = 0;
            // Rows/columns
            for (int i = 0; i < size; i++) {
                if (evalRow(i) != NA) {
                    lines++;
                    winner = evalRow(i);
                }
                if (evalCol(i) != NA) {
                    lines++;
                    winner = evalCol(i);
                }
                if (lines > 1) return BAD;
            }
            // Diagonals
            if (evalDiagL() != NA) {
                lines++;
                winner = evalDiagL();
            }
            if (evalDiagR() != NA) {
                lines++;
                winner = evalDiagR();
            }
            if (lines > 1) return BAD;
        }
        return winner;
    }

     /**
     * Builds a string representation of the board for printing.
     * @param prompts Whether or not the representation should include number prompts.
     * @return The string representation of the board, tab-indented.
     */
    public String boardString(boolean prompts) {
        // Symbols to be used for representation
        Dictionary<Integer, Character> contents = new Hashtable<>(3);
        contents.put(NA, ' ');
        contents.put(X, 'X');
        contents.put(O, 'O');
        char cross       = '+', //'\u254B', 
             vert        = '|', //'\u2503', 
             horiz       = '-', //'\u2501',
             vertLined   = '-', //'\u2542', 
             horizLined  = '|', //'\u253F',
             diagL       = '\\', //'\u2572', 
             diagR       = '/', //'\u2571', 
             diagX       = 'X'; //'\u2573';
        
        StringBuilder output = new StringBuilder((2 * size - 1) * (4 * size));
        int promptNum = 1;
        // Variables to indicate if a row, column, or diagonal should be lined
        boolean lineRow, lineCol;
        boolean lineDiagL = (evalDiagL() != 0), lineDiagR = (evalDiagR() != 0);

        //Loop through rows
        for (int i = 0; i < size; i++) {
            // Indicates if current row should be lined
            lineRow = (evalRow(i) != 0);

            // Add horizontal line above each row after the first
            if (i > 0) {
                output.append('\t');
                for (int j = 0; j < size; j++) {
                    // Indicates if current column should be lined
                    lineCol = (evalCol(j) != 0);
                    // Add cross if needed, with diagonal or double diagonal line if needed
                    if (j > 0) {
                        if (lineDiagL && lineDiagR && onCenter(i, j)) output.append(diagX);
                        else if (lineDiagL && onDiagL(i, j)) output.append(diagL);
                        else if (lineDiagR && onDiagR(i, j - 1)) output.append(diagR);
                        else output.append(cross);
                    }
                    for (int k = 0; k < 3; k++) output.append(lineCol && k == 1 ? horizLined : horiz);
                }
                output.append('\n');
            }
            
            // Start each row with an indentation
            output.append('\t');

            // Loop through columns
            for (int j = 0; j < size; j++) {
                // Indicates if current row or column should be lined
                lineCol = (evalCol(j) != 0);

                // Add divider if needed (and line it if needed)
                if (j > 0) output.append(lineRow ? vertLined : vert);

                // If cell should be highlighted, add curly brace, otherwise pad with space
                output.append(isHighlighted(i, j, lineRow, lineCol, lineDiagL, lineDiagR) ? '{' : ' ');
                // If prompts are on and this is an empty cell, add and increment prompt number 
                if (prompts && cell(i, j) == NA) {
                    output.append(promptNum);
                    if (promptNum < 10) output.append(' ');
                    promptNum++;
                }
                // Otherwise populate with contents normally
                else {
                    // Look up the appropriate symbol according to the contents of the grid
                    output.append(contents.get(cell(i,j)));
                    // If cell should be highlighted, add curly brace, otherwise pad with space
                    output.append(isHighlighted(i, j, lineRow, lineCol, lineDiagL, lineDiagR) ? '}' : ' ');
                }
            }

            // Go to the next line
            if (i < size - 1) output.append('\n');
        }
        
        return output.toString();
    }

    @Override
    public String toString() {
        return boardString(false);
    }
    
    /**
     * Returns the string representation of the given state integer.
     * @param state An integer representing a cell or board state.
     * @return X, O, NA, BAD, or DRAW.
     */
    public static String stateIntString(int state) {
        switch(state) {
            case -2: return "BAD";
            case -1: return "DRAW";
            case 0: return "N/A";
            case 1: return "X";
            case 2: return "O";
            default: return "";
        }
    }
    

    public static void main(String[] args) {
        String divider = "=======================================================================";
        int size = 4;
        Board test = new Board(size);
        Random rand = new Random();
        // The set of possible cell contents to randomly pick from
        int[] dist = {NA, X, O};
        for (int i = 0; i < size * size; i++) {
            test.set(i / size, i % size, dist[rand.nextInt(dist.length)]);
        }

        System.out.println(divider);
        System.out.println(test);
        System.out.print("Game state? ");
        System.out.println(stateIntString(test.evalGame()));
        System.out.println(divider);
    }

}
