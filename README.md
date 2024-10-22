# The Tic-Tac-Toe Game

In this game two
players (a human and the computer) take turns putting tiles on a board having _n_ rows and _n_ columns,
and to win the game a player needs to put at least _k_ tiles on board positions in consecutive tiles, in a
Column, a row or a diagonal. When running the program to play the game we will specify the value
of _n_ and the value of _k_.

A row of length _k_ is formed by adding a tile to a row, column or diagonal as follows:
- Row: there is a sequence of _k_-1 tiles and there is at least one empty space on either end of
the sequence (see Figure 1(a)).
- Column: There is a sequence of _k_-1 tiles and there is at least one empty space above or below
(see Figure 1(b)).
- Diagonal: There is a sequence of _k_-1 tiles and there is at least one empty space on either end
of the diagonal (see Figure 1(c)).

![Figure 1](https://gitlab.com/myself9450436/the-tic-tac-toe-game/-/raw/main/Images/Figure1.png)

The _length_ of a sequence is the number of tiles in it. Figure 1 shows three different possibilities
for achieving a sequence of length 3. As mentioned above, to win the game a player needs to form
with their tiles a sequence of length at least _k_.


## The Algorithm for Playing Tic-Tac-Toe
The human player always starts the game and uses blue tiles; the computer uses red tiles. In each
turn the computer examines all free board positions and selects the best one to place a tile there;
to do this, each possible play is assigned a score. In this assignment we will use the following four
scores for plays:
- HUMAN WINS = 0: this score is given to a play that makes the human player win
- UNDECIDED = 1: this score is given to a play for which it is not clear which player will win
- DRAW = 2: this score is given to a play that will lead to a draw (i.e. no player wins the game)
- COMPUTER WINS = 3: this score is given to a play that will ensure that the computer wins.

For example, suppose that the board looks like the one in Figure 2(a) and the length of the
sequence needed to win is 4. If the computer plays in position 15, the game will end in a draw (see
Figure 2(b)), so the score for the computer playing in position 15 is 2 (DRAW). Similarly, the score for
the computer playing in position 16 is 0 (HUMAN WINS) as then the human player will win.

We define a _configuration_ as the way in which the tiles are positioned on the board. For example,
the configuration shown in Figure 2(b) corresponds to a game that ends up in a draw. The con-
figuration in Figure 2(c) corresponds to a game won by the human player, as there are 4 blue tiles
in a sequence (tiles 3, 7, 11, 15). Each configuration is also assigned one of the 4 above scores. So,
the configuration in Figure 2(b) gets a score of 2 and the configuration in Figure 2(c) gets a score of
0.

![Figure 2](https://gitlab.com/myself9450436/the-tic-tac-toe-game/-/raw/main/Images/Figure2.png)

To compute scores, the program will use a recursive algorithm that repeatedly simulates a play
from the computer followed by a play from the human, until an outcome for the game has been
decided. This recursive algorithm will implicitly create a tree formed by all the possible plays that
the players can make starting at the current configuration. This tree is called a _game tree_. An
example of a game tree is shown in Figure 3.
To make it easy for you to get started with GitLab, here's a list of recommended next steps.

Assume that after several moves the board is as the one shown at the top of Figure 3 and suppose
that it is the computer’s turn to play. The algorithm for computing scores will first try all possible
plays that the computer can make: play A: play at position 2, play B: play at position 10, and play
C: play at position 16. For each one of these plays, the algorithm will then consider all possible
plays by the human player: A1 and A2 (if the computer plays as in configuration A), B1 and B2 (if
the computer plays as in configuration B). Then, all possible responses by the computer are
attempted, and so on until the outcome of each possible sequence of plays is determined.

In Figure 3 each level of the tree is labelled by the player whose turn is next. So, levels 0 and 2 are
labelled “Computer” and the other 2 levels are labelled “Human”. After reaching final configurations
A11, A2, B11, B2, and C, the algorithm computes a score for each one of them depending on
whether the computer wins, the human wins, or the game is a draw. These scores are propagated
upwards as follows:

![Figure 3](https://gitlab.com/myself9450436/the-tic-tac-toe-game/-/raw/main/Images/Figure3.png)

- For a configuration c on a level labelled “Computer”, the highest score of the adjacent
configurations in the next level is selected as the score for c. This is because the higher the
score is, the better the outcome is for the computer.
- For a configuration c on a level labelled “Human”, the score of c is equal to the minimum score
of the adjacent configurations in the next level, because the lower the score is, the better the
outcome is for the human player.

The scores for the configurations in Figure 3 are the numbers in green. For example, for the con-
figuration at the top of Figure 3, putting a red tile in position 16 yields the configuration with the
highest score (2), hence the computer will choose to play in position 16. Similarly, for configuration
A in Figure 3, placing a blue tile in position 16 yields the configuration with the smallest score (0),
so the human would choose to play in position 16.

The algorithm for computing scores and for selecting the best available play. The
algorithm is given in Java, but we have omitted variable declarations and some initialization steps.

### Code 1.0: Algorithm for Computing Scores and Selecting Best Available Play

```java
private PosPlay computerPlay(char symbol, int highestScore, int lowestScore, int level) {
    if (level == 0) configurations = t.createDictionary();
    if (symbol == HUMAN) opponent = COMPUTER; else opponent = HUMAN;
    
    for(int row = 0; row < board size; row++)
        for(int column = 0; column < board size; column++)
            if(t.squareIsEmpty(row, column)) { // Empty position found
                t.savePlay(row, column, symbol);
                
                if (t.wins(symbol) || t.isDraw() || (level == max level))
                    reply = new PosPlay(t.evalBoard(), row, column);
                else {
                    lookupVal = t.repeatedConfiguration(configurations);  // (*)
                    if (lookupVal != -1)
                        reply = new PosPlay(lookupVal, row, column);
                }
                
                else {
                    reply = computerPlay(opponent, highestScore, lowestScore, level + 1);
                    t.addConfiguration(configurations, reply.getScore());
                }
            }
    
    t.savePlay(row, column, ' ');
    
    if ((symbol == COMPUTER && reply.getVal() > value) || // A better play was found
        (symbol == HUMAN && reply.getVal() < value)) {
        
        bestRow = row; bestColumn = column;
        value = reply.getVal();
        
        if (symbol == COMPUTER && value > highestScore) highestScore = value;
        else if (symbol == HUMAN && value < lowestScore) lowestScore = value;
        
        if (highestScore >= lowestScore) /* alpha/beta cut */
            return new PosPlay(value, bestRow, bestColumn);
    }
    
    return new PosPlay(value, bestRow, bestColumn);
}
```
The first parameter of the algorithm is the symbol (either HUMAN or COMPUTER) representing
the tiles of the player whose turn is next. The second and third parameters are the highest and lowest
scores for the board positions that have been examined so far. The last parameter is used to bound
the maximum number of levels of the game tree that the algorithm will consider. Since the number
of configurations in the game tree could be very large, to speed up the algorithm the value of the last
parameter specifies the highest level of the game tree that will be explored. Note that the smaller
the value of this parameter is, the faster the algorithm will be, but the worse it will play.

Also note that if we bound the number of levels of the game tree, it might not be possible to
determine the outcome of the game for some of the configurations in the lowest level of the tree.
For example, if in the game tree of Figure 3 we set the maximum level to 2, then the algorithm will
explore only levels 0, 1, and 2. At the bottom of the tree will appear configurations A1, A2, B1,
B2. Among these configurations, the scores for A1 and B2 are 0, as the human player wins in those
cases; however, the scores for the remaining configurations are not known as in none of these
configurations any player has won, and the configurations still include empty positions, so they do
not denote game draws. In this case, configurations A1, and B1 will receive a score of UNDECIDED
= 1.

# Speeding-up the Algorithm with a Dictionary
The above algorithm includes several tests that allow it to reduce the number of configurations that
need to be examined in the game tree.The most important test used to speed up the algorithm is the Dictionary. 

Every time that the score of a board configuration is computed,
the configuration and its score are stored in a dictionary, that you will implement using a hash table.
Then, when algorithm computerPlay is exploring the game tree trying to determine the computer’s
best move, before it expands a configuration _c_ it will look it up in the dictionary. If _c_ is in the
dictionary, then its score is simply extracted from the dictionary, instead of exploring the part of the
game tree below _c_.

For example, consider the game tree in Figure 4. The algorithm examines first the left branch
of the game tree, including configuration _D_ and all the configurations that appear below it. After
exploring the configurations below _D_, the algorithm computes the score for _D_ and then it stores _D_
and its score in the dictionary. When later the algorithm explores the right branch of the game tree,
configuration _D_ will be found again, but this time its score is simply obtained from the dictionary
instead of exploring all configurations below _D_, thus reducing the running time of the algorithm.

![Figure 4](https://gitlab.com/myself9450436/the-tic-tac-toe-game/-/raw/main/Images/Figure4.png)

# Implenting the Dictionary
The dictionary implementation was done using a hash dictionary. Because hash dictionary suffer from collisions, therefore, a hash table with separate chaining was implemented to resolve collisions. Furtheremore, to minimize the number of collisions, a polynomail function was implemented and then compressed using the MAD method.

### Code 2.0: Polynomial Hash and MadHash Functions Implementation
```java
private int polynomialHash(String config) {
    int hashValue = (int) config.charAt(config.length() - 1);
    for (int i = config.length() - 2; i >= 0; i--) {
        hashValue = (hashValue * base + (int) config.charAt(i));
    }

    return hashValue;
}

private int madHash(String config) {
    int hashValue = polynomialHash(config);
    int finalHash = ((a * hashValue + b) % p) % modulo;
    return (finalHash + modulo) % modulo;
}

```

The crucial aspect of the MAD Compression Function [(_ai_ + _b_) mod _p_] is to find the correct values of _a_ and _b_ that will result in the least number of collisions. After experimentation producing random generated numbers between [0-_p_] where _a_ > 0. I was able to find that when _a_ = 225549583 and _b_ = 77636339 the minimum amount of collisions produced are 1557 for this specific project.

# Tic-Tac-Toe GamePlay
![Tic-Tac-Toe Gameplay](https://gitlab.com/myself9450436/the-tic-tac-toe-game/-/raw/main/Images/GamePlay1.gif)




