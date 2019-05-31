/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package fireice;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;
import java.util.Random;

/**
 *
 * @author giang
 */
enum TileType {
    RED, EMPTY, BLUE;
}

public class Board {

    TileType board[][];
    int nSize;
    int currentX;
    int currentY;
    //boolean playerTurn = true; // true for red and false for blue
    int redScore;
    int blueScore;
    TileType currentPlayer;
    TileType winner;
    
    List<Integer> redTile, blueTile;
    
    
    int reward  =0 ;

    TileType HumanPlayer;
    TileType AIPlayer;

    public Board(int n) {
        this.nSize = n;
        this.board = new TileType[n][n];
        this.redTile = new ArrayList<>();
        this.blueTile = new ArrayList<>();
    }

    public Board(int n, String boardConfiguration) {

        Random random = new Random();
        n = 4 + (random.nextInt(10000) % 4) * 2;
        System.out.println(n);
        this.redTile = new ArrayList<>();
        this.blueTile = new ArrayList<>();
        //=8;
        this.nSize = n;
        this.board = new TileType[n][n];
        int index = 0;
        for (int i = 0; i < this.nSize; i++) {
            for (int j = 0; j < this.nSize; j++) {
                if (boardConfiguration.charAt(index) == 'R') {
                    this.board[i][j] = TileType.RED;
                    redTile.add(i*this.nSize+j);
                } else if (boardConfiguration.charAt(index) == 'B') {
                    this.board[i][j] = TileType.BLUE;
                    blueTile.add(i*this.nSize+j);
                }
                index++;
            }
        }
        //randomBoard();

        boolean randomPlayer = random.nextBoolean();
        //this.changePlayerTurn();

        this.HumanPlayer = TileType.RED;
        this.AIPlayer = TileType.BLUE;

// true: Human first
        if (true) {
            this.currentPlayer = this.HumanPlayer;
            System.out.println("Human moves first");
        } else {
            this.currentPlayer = this.AIPlayer;
            System.out.println("AI moves first");
        }

        this.redScore = (this.nSize * this.nSize) / 2;
        this.blueScore = (this.nSize * this.nSize) / 2;
        this.winner = TileType.EMPTY;
        
    }

    public Board(int n, boolean isHumanPlayFirst) {

        this.nSize = n;
        this.board = new TileType[n][n];
        int index = 0;
        this.redTile = new ArrayList<>();
        this.blueTile = new ArrayList<>();
        
        randomBoard();
        
        this.currentPlayer = TileType.RED;

        this.redScore = (this.nSize * this.nSize) / 2;
        this.blueScore = (this.nSize * this.nSize) / 2;
        this.winner = TileType.EMPTY;
    }

    public Board(int n, String boardConfiguration, boolean isHumanRed, boolean isHumanPlayFirst) {

        // Random random = new Random();
        // n = 4+(random.nextInt(10000)%4)*2;
        System.out.println(n);
        this.redTile = new ArrayList<>();
        this.blueTile = new ArrayList<>();
        //=8;
        this.nSize = n;
        this.board = new TileType[n][n];
        int index = 0;
        for (int i = 0; i < this.nSize; i++) {
            for (int j = 0; j < this.nSize; j++) {
                if (boardConfiguration.charAt(index) == 'R') {
                    this.board[i][j] = TileType.RED;
                    redTile.add(i*this.nSize+j);
                } else if (boardConfiguration.charAt(index) == 'B') {
                    this.board[i][j] = TileType.BLUE;
                    blueTile.add(i*this.nSize+j);
                }
                index++;
            }
        }
        if (isHumanRed) {
            this.HumanPlayer = TileType.RED;
            this.AIPlayer = TileType.BLUE;
        } else {
            this.HumanPlayer = TileType.BLUE;
            this.AIPlayer = TileType.RED;
        }

        if (isHumanPlayFirst) {
            this.currentPlayer = this.HumanPlayer;
            System.out.println("Human moves first");
        } else {
            this.currentPlayer = this.AIPlayer;
            System.out.println("AI moves first");
        }

        this.redScore = (this.nSize * this.nSize) / 2;
        this.blueScore = (this.nSize * this.nSize) / 2;
        this.winner = TileType.EMPTY;
    }

    public Board(Board b) {
        this.redTile = new ArrayList<>();
        this.redTile.addAll(b.redTile);
        this.blueTile = new ArrayList<>();
        this.blueTile.addAll(b.blueTile);
        
        this.nSize = b.nSize;
        this.board = new TileType[b.nSize][b.nSize];

        // copy board
        for (int i = 0; i < this.nSize; i++) {
            for (int j = 0; j < this.nSize; j++) {
                this.board[i][j] = b.board[i][j];
            }
        }

        this.redScore = b.redScore;
        this.blueScore = b.blueScore;
        this.winner = b.winner;
        this.currentPlayer = b.currentPlayer;
    }

    void randomBoard() {
        this.board = new TileType[this.nSize][this.nSize];

        Random random = new Random();
        int index = 0;
        for (int i = 0; i < this.nSize; i++) {
            for (int j = 0; j < this.nSize; j++) {
                if (index % 2 == 0) {
                    this.board[i][j] = TileType.RED;
                     this.redTile.add(i*this.nSize+j);
                } else {
                    this.board[i][j] = TileType.BLUE;
                    this.blueTile.add(i*this.nSize+j);

                }
                index++;
            }
            index++;
        }

    }

    boolean takeTurn(int x, int y) {

        if (!isValidMove(x, y)) {
            return false;
        }

        this.currentX = x;
        this.currentY = y;

        if (this.board[this.currentX][this.currentY] == TileType.BLUE) {
            this.blueScore--;
        }

        if (this.board[this.currentX][this.currentY] == TileType.RED) {
            this.redScore--;
        }
        this.reward=1;
        
        //this.board[this.currentX][this.currentY] = TileType.EMPTY;
        removeTile(this.currentX,this.currentY);

        // check current remove first
        winner = checkEndGame();
        this.checkSideEffect();
        this.changePlayerTurn();
        winner = checkEndGame();
        
        return true;
    }
    
    boolean removeTile(int x,int y ) {
        this.board[x][y] = TileType.EMPTY;
        int index = this.redTile.indexOf(x*this.nSize+y);
        if (index!=-1)
            this.redTile.remove(this.redTile.indexOf(x*this.nSize+y));
        
        index = this.blueTile.indexOf(x*this.nSize+y);
        if (index!=-1)
            this.blueTile.remove(this.blueTile.indexOf(x*this.nSize+y));
        
        return true;
    }
    
    private void changePlayerTurn() {
        if (this.currentPlayer == TileType.BLUE) {
            this.currentPlayer = TileType.RED;
        } else {
            this.currentPlayer = TileType.BLUE;
        }
    }

    boolean isValidPosition(int x, int y) {

        if ((x < 0 || y < 0 || x >= this.nSize || y >= this.nSize)) {
            return false;
        }

        return true;
    }

    boolean isValidMove(int x, int y) {
        
        if(this.winner!=TileType.EMPTY)
            return false;

        if ((x < 0 || y < 0 || x >= this.nSize || y >= this.nSize)) {
            return false;
        }

        if (this.board[x][y] == TileType.EMPTY) {
            return false;
        }

        // check turn and color
        // playerTurn ==True : RED turn, playerTurn == False : BLUE turn
        if (this.currentPlayer == TileType.RED) {
            if (this.board[x][y] == TileType.BLUE) {
                return false;
            }
        }

        if (this.currentPlayer == TileType.BLUE) {
            if (this.board[x][y] == TileType.RED) {
                return false;
            }
        }

        return true;
    }

    void checkSideEffect() {
        int countR = 0;
        int countB =0 ;
        for (int i = -1; i <= 1; i++) {
            for (int j = -1; j <= 1; j++) {
                int dx = currentX + i;
                int dy = currentY + j;

                if (!isValidPosition(dx, dy)) {
                    continue;
                }

                if (Math.abs(i + j) == 1) {
                    if (this.board[dx][dy] == TileType.EMPTY) {
                        continue;
                    }
                    int countConnected = 0;
                    // check 4 direction 
                    for (int t = -1; t <= 1; t++) {
                        for (int k = -1; k <= 1; k++) {
                            if (Math.abs(t + k) == 1) {
                                int dt = dx + t;
                                int dk = dy + k;

                                if (!(isValidPosition(dt, dk))) {
                                    countConnected++;
                                } else if (this.board[dt][dk] != TileType.EMPTY) {
                                    countConnected++;
                                }

                            }
                        }
                    }
                    if (countConnected <= 2) {
                        if (this.board[dx][dy] == TileType.BLUE) {
                            this.blueScore--;
                            countB++;
                        }

                        if (this.board[dx][dy] == TileType.RED) {
                            this.redScore--;
                            countR++;
                        }
                        //this.board[dx][dy] = TileType.EMPTY;
                        removeTile(dx,dy);
                    }
                }

            }
        }
        
        if (this.currentPlayer==TileType.RED)
            this.reward += (countR + (4-countB));
        else 
            this.reward += (countB + (4-countR));
    }
 public boolean isDraw() {
    return (this.redScore ==0 && this.blueScore ==0);
    }
    TileType checkEndGame() {
        
        if (isDraw())
            return this.winner;
        
        if (this.redScore == 0) {
            return TileType.RED;
        } else if (this.blueScore == 0) {
            return TileType.BLUE;
        }
        
        
        return TileType.EMPTY;
    }

    double evaluate(TileType player) {
        double value = 0;
            
        if (this.winner == player)
            return 100;
        else if (this.winner != TileType.EMPTY && this.winner != player)
            return -100;
        
        if (this.blueScore == 0) {
            if (player == TileType.BLUE) {
                return 100;
            } else {
                return -100;
            }
        }

        if (this.redScore == 0) {
            if (player == TileType.RED) {
                return 100;
            } else {
                return -100;
            }
        }

        value = this.blueScore - this.redScore;

        if (player == TileType.BLUE) {
            return -value;
        }

        return value;
    }

    double evaluate2(TileType player) {
        
        if(this.winner!=TileType.EMPTY)
            return evaluate(player);
        
        double value = 0;
        double countRedRemovable =0;
        double countBlueRemovable =0;
        
        for (int cX =0; cX < this.nSize; cX++)
        for (int cY=0; cY < this.nSize; cY++)    {
            
            if (this.board[cX][cY] != this.currentPlayer)
                    continue;
            
            for (int i = -1; i <= 1; i++) {
                for (int j = -1; j <= 1; j++) {
                    int dx = cX + i;
                    int dy = cY + j;

                    if (!isValidPosition(dx, dy)) {
                        continue;
                    }
                    
                    if (this.board[dx][dy] == TileType.EMPTY) {
                            continue;
                    }
                    boolean f = false;
                    if (Math.abs(i + j) == 1) {
                        for (int t = -1; t <= 1 && !f; t++) 
                            for (int k = -1; k <= 1 && !f; k++) 
                                if (Math.abs(t + k) == 1) {
                                    int dt = dx + t;
                                    int dk = dy + k;

                                    if ((isValidPosition(dt, dk)) && this.board[dt][dk] != TileType.EMPTY)
                                        f = true;

                                }
                          
                            if (f) 
                                if (this.board[dx][dy]==TileType.RED)
                                    countRedRemovable++;
                                else  if (this.board[dx][dy]==TileType.BLUE)
                                    countRedRemovable++;
                        }
                    }

                }
        }
        
        value = countRedRemovable - countBlueRemovable;
        
        value/=(this.nSize*this.nSize);
        
        if(this.currentPlayer==TileType.BLUE)
            value = -value;
        
        
        
        return value ;
    }

    
    void randomMove() {
        // random move current player
        int dX = -1;
        int dY = -1;

        do {
            Random random = new Random();
            int index =-1;
            if (this.currentPlayer==TileType.RED && this.redTile.size()>0){
                index = random.nextInt(this.redTile.size());
                dX = this.redTile.get(index)/this.nSize;
                dY = this.redTile.get(index)%this.nSize;
            }
            else if (this.currentPlayer==TileType.BLUE&& this.blueTile.size()>0) {
                index = random.nextInt(this.blueTile.size());
                dX = this.blueTile.get(index)/this.nSize;
                dY = this.blueTile.get(index)%this.nSize;
            }
            
        } while (!this.takeTurn(dX, dY));
    }

    double simmulation(int nSimulation, TileType player) {
        double winningRate = 0.0;
        int countWin = 0;
        for (int i = 0; i < nSimulation; i++) {
            Board copyBoard = new Board(this);

            // random move
            while (copyBoard.winner == TileType.EMPTY) {
            copyBoard.randomMove(); // move of current player and take turn change player
            
            if (copyBoard.winner != TileType.EMPTY) 
                    break;
                
            copyBoard.randomMove();
            }

            // check result
            if (copyBoard.isDraw())
                continue;
            if (copyBoard.winner == player) {
                countWin++;
            } else  { // can not be empty
                countWin--;
            }

        }

        return countWin * 1.0 / nSimulation;
    }
}
