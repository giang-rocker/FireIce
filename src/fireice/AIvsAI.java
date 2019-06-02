/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package fireice;

import java.io.IOException;
import java.util.Arrays;

/**
 *
 * @author giang
 */
public class AIvsAI {

    static void getMCTSMove(Board boardGame) {
        Node MCTSNode = new Node(boardGame);
        MCTSNode.rootPlayer = boardGame.currentPlayer;

       int maxDepth = 15;
        int remainingStone = boardGame.blueScore + boardGame.redScore;
        int sqrtSize = (int) (Math.sqrt(remainingStone));

        //MCTS
        if (remainingStone >= 32) {
            maxDepth = 3;
        } else if (remainingStone > 20) {
            maxDepth = 5;
        } 

        Node.runMCTS(MCTSNode, maxDepth, true);
        int sX = MCTSNode.moveX;
        int sY = MCTSNode.moveY;
        boardGame.takeTurn(sX, sY);
    }

    static void getMinMaxMove(Board boardGame) {
        Node MinMaxNode = new Node(boardGame);
        MinMaxNode.rootPlayer = boardGame.currentPlayer;

        int maxDepth = 15;
        int remainingStone = boardGame.blueScore + boardGame.redScore;

        if (remainingStone > 70) {
            maxDepth = 3;
        } else if (remainingStone >= 32) {
            maxDepth = 5;
        } else if (remainingStone > 20) {
            maxDepth = 7;
        }
        Node.runMinimax(MinMaxNode, maxDepth, true);
        int sX = MinMaxNode.moveX;
        int sY = MinMaxNode.moveY;
        boardGame.takeTurn(sX, sY);
    }
    
    static void getMinMaxMove2(Board boardGame) {
        Node MinMaxNode = new Node(boardGame);
        MinMaxNode.rootPlayer = boardGame.currentPlayer;

        int maxDepth = 15;
        int remainingStone = boardGame.blueScore + boardGame.redScore;

        if (remainingStone > 70) {
            maxDepth = 3;
        } else if (remainingStone >= 32) {
            maxDepth = 5;
        } else if (remainingStone > 20) {
            maxDepth = 7;
        }
        Node.runMinimax2(MinMaxNode, maxDepth, true);
        int sX = MinMaxNode.moveX;
        int sY = MinMaxNode.moveY;
        boardGame.takeTurn(sX, sY);
    }
    
static void getAlphaBeta(Board boardGame) {
        Node MinMaxNode = new Node(boardGame);
        MinMaxNode.rootPlayer = boardGame.currentPlayer;

        int maxDepth = 15;
        int remainingStone = boardGame.blueScore + boardGame.redScore;

                   
        if (remainingStone > 75) {
            maxDepth = 3;
        } else if (remainingStone >= 32) {
            maxDepth = 5;
        } else if (remainingStone > 20) {
            maxDepth = 7;
        }
       
        Node.runAlphaBeta(MinMaxNode, maxDepth,-299999999,299999999, true);
        int sX = MinMaxNode.moveX;
        int sY = MinMaxNode.moveY;
        boardGame.takeTurn(sX, sY);
    }
static void getAlphaBeta2(Board boardGame) {
        Node MinMaxNode = new Node(boardGame);
        MinMaxNode.rootPlayer = boardGame.currentPlayer;

        int maxDepth = 15;
        int remainingStone = boardGame.blueScore + boardGame.redScore;
 if (remainingStone > 80) {
                maxDepth = 5;
            } else if (remainingStone >= 36) {
                maxDepth = 7;
            } else if (remainingStone > 26) {
                maxDepth = 9;
            }
        Node.runAlphaBeta2(MinMaxNode, maxDepth,-299999999,299999999, true);
        int sX = MinMaxNode.moveX;
        int sY = MinMaxNode.moveY;
        boardGame.takeTurn(sX, sY);
    }

static void getAlphaBeta2a(Board boardGame) {
        Node MinMaxNode = new Node(boardGame);
        MinMaxNode.rootPlayer = boardGame.currentPlayer;

        int maxDepth = 15;
        int remainingStone = boardGame.blueScore + boardGame.redScore;
 if (remainingStone > 80) {
                maxDepth = 5;
            } else if (remainingStone >= 36) {
                maxDepth = 7;
            } else if (remainingStone > 26) {
                maxDepth = 9;
            }
       
        Node.runAlphaBeta2a(MinMaxNode, maxDepth,-299999999,299999999, true);
        int sX = MinMaxNode.moveX;
        int sY = MinMaxNode.moveY;
        boardGame.takeTurn(sX, sY);
    }

    public static void main(String[] args) throws IOException, InterruptedException {
        int nGame = 1;
        int[] nSize = {4,6,8,10};

        Board boardGame = new Board(4, true);

        int sX, sY;

        for (int i = 0; i < 4; i++) {
            System.out.println((2 * nGame) + " games. Size " + nSize[i]);
            int[] MCTSWin = {0, 0,0,0};
            double MinMaxThinkingTime = 0;
            double MCTSThinkingTime = 0;
            double startThinking = 0;
            int countMCTSMove = 0;
            int countMinMaxMove = 0;
            // MCTS first
            
            for (int g = 0; g < nGame; g++) {
                boardGame = new Board(nSize[i], true);
                
                while (boardGame.winner == TileType.EMPTY) {
                    startThinking = System.currentTimeMillis();
                    getAlphaBeta2(boardGame);
                    MCTSThinkingTime += (System.currentTimeMillis() - startThinking);
                    countMCTSMove++;
                    
                    if(boardGame.isDraw())
                         MCTSWin[1]++; // draw
                    else if (boardGame.winner == TileType.RED) 
                        MCTSWin[0]++;
                    
                    
                    if (boardGame.winner != TileType.EMPTY) 
                        break;
                    
                    
                    startThinking = System.currentTimeMillis();
                  getAlphaBeta2a(boardGame);
                    MinMaxThinkingTime += (System.currentTimeMillis() - startThinking);
                    countMinMaxMove++;
                    
                    if (boardGame.isDraw())
                        MCTSWin[1]++; // draw
                    else if (boardGame.winner == TileType.RED) 
                        MCTSWin[0]++;
                    
                }
            }
            System.out.println("MCTS Move First : Win " + MCTSWin[0] + " - Draw: " + (MCTSWin[1]) + " - Lose: " + (nGame - MCTSWin[0]-MCTSWin[1]));
            //  System.out.println("then 50 Games MinMax Move First ");
            for (int g = 0; g < nGame; g++) {
                boardGame = new Board(nSize[i], true);

                while (boardGame.winner == TileType.EMPTY) {
                    startThinking = System.currentTimeMillis();
                    getAlphaBeta2a(boardGame);
                    MinMaxThinkingTime += (System.currentTimeMillis() - startThinking);
                    countMinMaxMove++;
                    
                    if (boardGame.isDraw())
                        MCTSWin[3]++; // draw
                    else if (boardGame.winner == TileType.BLUE) 
                        MCTSWin[2]++;
                    
                    
                    if (boardGame.winner != TileType.EMPTY) 
                        break;
                    
                    
                    startThinking = System.currentTimeMillis();
                    getAlphaBeta2(boardGame);
                    MCTSThinkingTime += (System.currentTimeMillis() - startThinking);
                    countMCTSMove++;

                    if (boardGame.isDraw())
                        MCTSWin[3]++; // draw
                    else if (boardGame.winner == TileType.BLUE) 
                        MCTSWin[2]++;
                    

                }
                //     System.out.println("Game " + g +"/50. Moves: " + count);
            }
            System.out.println("MCTS Move Later : Win " + (MCTSWin[2])  + " - Draw: " + (MCTSWin[3]) + " - Lose: " + (nGame - MCTSWin[2]- MCTSWin[3]));
            System.out.println("AVG thinking time: MCTS: " + (Math.round(MCTSThinkingTime/countMCTSMove)) +"ms/move  MinMax: " + Math.round((MinMaxThinkingTime/countMinMaxMove))+"ms/move");
            System.out.println(((Math.round(MCTSThinkingTime)+ Math.round((MinMaxThinkingTime)))/(2*nGame))+"ms/game");
            System.out.println("Size " + nSize[i] + " MCTS wins: " + ((MCTSWin[0] + MCTSWin[2]) * 100.0) / (2 * nGame) + "% " +  "Draw: " + ((MCTSWin[1] + MCTSWin[3]) * 100.0) / (2 * nGame) + "% "  +  "Lose: " + ((( 2*nGame - Arrays.stream(MCTSWin).sum() ) * 100.0) / (2 * nGame)) + "%"  );
            
        }

    }

}
