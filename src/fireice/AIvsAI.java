/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package fireice;

import java.io.IOException;

/**
 *
 * @author giang
 */
public class AIvsAI {

    static void getMCTSMove(Board boardGame) {
       Node MCTSNode = new Node(boardGame);
        MCTSNode.rootPlayer = boardGame.currentPlayer;

        int maxDepth = 10;
        int remainingStone = boardGame.blueScore + boardGame.redScore;

        if (remainingStone > 20) {
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

        if (remainingStone > 50) {
            maxDepth = 3;
        } else if (remainingStone > 30) {
            maxDepth = 5;
        } else if (remainingStone > 20) {
            maxDepth = 7;
        }
        Node.runMinimax(MinMaxNode, maxDepth, true);
        int sX = MinMaxNode.moveX;
        int sY = MinMaxNode.moveY;
        boardGame.takeTurn(sX, sY);
    }

    public static void main(String[] args) throws IOException, InterruptedException {
        int nGame = 50;
        int[] nSize = {4, 6, 8, 10};

       
        Board boardGame = new Board(4,true);
        
        int sX, sY;

        for (int i = 0; i < 4; i++) {
            System.out.println("100 games. Size " + nSize[i]);
            int[] MCTSWin= {0,0};
            // MCTS first
           // System.out.println("first 50 Games MCTS Move first ");
            for (int g = 0; g < nGame; g++) {
            //    System.out.println("Game " + g +"/50");
                boardGame = new Board(nSize[i],true);
                int count = 0;
                while (boardGame.winner == TileType.EMPTY) {
                   getMCTSMove(boardGame);
                  
                   if (boardGame.winner==TileType.RED)
                       MCTSWin[0]++;
                   count ++;
                   if (boardGame.winner!=TileType.EMPTY)
                       break;
                   
                   getMinMaxMove(boardGame);
                   
                   if (boardGame.winner==TileType.RED)
                       MCTSWin[0]++;
                 count ++; 
                }
            }
            System.out.println("MCST Move First : Win " + MCTSWin[0] + " - Lose: " + (50-MCTSWin[0]));           
          //  System.out.println("then 50 Games MinMax Move First ");
            for (int g = 0; g < nGame; g++) {
               // System.out.println("Game " + g +"/50");
                boardGame = new Board(nSize[i],true);
                int count =0 ;
                while (boardGame.winner == TileType.EMPTY) {
                    
                   getMinMaxMove(boardGame);
                   
                   if (boardGame.winner==TileType.BLUE)
                       MCTSWin[1]++;
                  count ++;
                   if (boardGame.winner!=TileType.EMPTY)
                       break;
                   
                   getMCTSMove(boardGame);
                   
                   if (boardGame.winner==TileType.BLUE)
                       MCTSWin[1]++;
                   count ++;
                   
                }
           //     System.out.println("Game " + g +"/50. Moves: " + count);
            }
           System.out.println("MinMax Move First : Win " + (50-MCTSWin[1]) + " - Lose: " + (MCTSWin[1]));
        }
        
        
    }

    
}
