public class Configurations {

    private int boardSize, lengthToWiN, max_levels;
    // declaring instance variable gameboard
    private char[][] gameBoard;
    // declaring instance dictionary
    private HashDictionary dictionary;

    /*
     * The first parameter specifies the size of the board, the second is the length of the sequence needed to win the game, 
     * and the third is the maximum level of the game tree that will be explored by the program.
     */
    public Configurations(int boardSize, int lengthToWiN, int max_levels){
        this.boardSize = boardSize;
        this.lengthToWiN = lengthToWiN;
        this.max_levels = max_levels;
        // intializing instance variable 
        this.gameBoard = new char[boardSize][boardSize];

        for (int i = 0; i < boardSize; i++){
            for (int j = 0; j < boardSize; j++){
                gameBoard[i][j] = ' ';
            }
        }
    }

    

    public HashDictionary createDictionary(){
        this.dictionary = new HashDictionary(7879);
        return dictionary;
    }

    /*
     * This method first stores the characters of the board in a String; then it checks whether the String
     * representing the board is in the hashTable: If the String is in the hashTable this method
     * returns its associated score, otherwise it returns the value -1
     */
    public int repeatedConfiguration(HashDictionary hashTable){
        String boardConfiguration = boardConfiguration();
        return hashTable.get(boardConfiguration);
    }


    /*
     * This method first represents the content of the board as a String as described above; then it inserts this String 
     * and score in the hashDictionary
     */
    public void addConfiguration(HashDictionary hashDictionary, int score) throws DictionaryException{
        String boardConfiguration = boardConfiguration();
        Data data = new Data(boardConfiguration, score);
        try{
            hashDictionary.put(data);
        } catch (Exception e) {
            throw new DictionaryException();
        }
    }

    public void savePlay(int row, int col, char symbol){
        this.gameBoard[row][col] = symbol;
    }


    public boolean squareIsEmpty(int row, int col){
        return gameBoard[row][col] == ' ';
    }

    /*
     * Returns true if there is a continuous sequence of length at least k formed by tiles of the kind symbol on the board, 
     * where k is the length of the vertical or horizontal of diagonal line needed to win the game.
     */
    public boolean wins(char symbol){
        return checkRows(symbol) || checkColumns(symbol) || checkDiagonals(symbol);
    }

    public boolean isDraw() {
        for (int i = 0; i < boardSize; i++) {
            for (int j = 0; j < boardSize; j++) {
                if (gameBoard[i][j] == ' ') {
                    return false; // empty square means the game is not a draw yet
                }
            }
        }
        return !wins('X') && !wins('O'); // It's a draw if no one has won and there are no empty squares
    }

    /*
     * Returns one of the following values:
     * – 3, if the computer has won, i.e. there is a vertical or horizontal or diagonal line formed by tiles of type ’O’ on the board
     * – 0, if the human player has won
     * – 2, if the game is a draw, i.e. there are no empty positions in board and no player has won
     * – 1, if the game is still undecided, i.e. there are still empty positions in board and no player has won yet.
     */
    public int evalBoard() {
        if (wins('O')) {
            return 3;
        } else if (wins('X')) {
            return 0;
        } else if (isDraw()) {
            return 2;
        } else {
            return 1;
        }
    }



    /*
     * Helper methods associated with the public methods found below this area
     */

    private boolean checkRows(char symbol) {
        for (int i = 0; i < boardSize; i++) {
            int count = 0;
            for (int j = 0; j < boardSize; j++) {
                if (gameBoard[i][j] == symbol) {
                    count++;
                    if (count == lengthToWiN) return true;
                } else {
                    count = 0;
                }
            }
        }
        return false;
    }

        
    
    private boolean checkColumns(char symbol) {
        for (int j = 0; j < boardSize; j++) {
            int count = 0;
            for (int i = 0; i < boardSize; i++) {
                if (gameBoard[i][j] == symbol) {
                    count++;
                    if (count == lengthToWiN) return true;
                } else {
                    count = 0;
                }
            }
        }
        return false;
    }

    private boolean checkDiagonals(char symbol) {
        // Check for diagonals from top-left to bottom-right
        for (int i = 0; i <= boardSize - lengthToWiN; i++) {
            for (int j = 0; j <= boardSize - lengthToWiN; j++) {
                int count = 0;
                for (int k = 0; k < lengthToWiN; k++) {
                    if (gameBoard[i + k][j + k] == symbol) {
                        count++;
                        if (count == lengthToWiN) return true;
                    } else {
                        break;
                    }
                }
            }
        }
    
        // Check for diagonals from top-right to bottom-left
        for (int i = 0; i <= boardSize - lengthToWiN; i++) {
            for (int j = lengthToWiN - 1; j < boardSize; j++) {
                int count = 0;
                for (int k = 0; k < lengthToWiN; k++) {
                    if (gameBoard[i + k][j - k] == symbol) {
                        count++;
                        if (count == lengthToWiN) return true;
                    } else {
                        break;
                    }
                }
            }
        }
    
        return false;
    }

    //helper method that stores in a string object the currents board contents configuration
    private String boardConfiguration(){
        String boardContent = "";
        for (int i = 0; i < boardSize; i++){
            for (int j = i; j < boardSize; j++){
                boardContent = boardContent + gameBoard[i][j];
            }
        }
        return boardContent;
    }
}
