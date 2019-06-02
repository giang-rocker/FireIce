/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package fireice;

import java.util.ArrayList;
import java.util.List;

/**
 *
 * @author giang
 */
public class Node {

    Board boardGame;
    int nVisited;
    double value;
    double chance;
    boolean isTerminal;
    double avg = 0;
    int moveX, moveY; // will apply this move
    TileType rootPlayer;
    double winingRate;
    int nChild;
    int pX, pY;
    int rootBlueScore = 0;
    int rootRedScore = 0;

    public Node(Board boardGame) {
        this.boardGame = new Board(boardGame);
        nVisited = 0;
        value = 0;
        chance = 0;
        isTerminal = false;
        moveX = -1;
        moveY = -1;
        winingRate = 0;
        nChild = 0;
        pX = -1;
        pY = -1;
        rootBlueScore  = boardGame.blueScore;
        rootRedScore  = boardGame.redScore;
    }

    public void setApplyMove(int x, int y) {
        this.moveX = x;
        this.moveY = y;
    }

    static double runMinimax(Node node, int depth, boolean maximizingPlayer) {
        //   System.out.println(node.rootPlayer);

        if (depth == 0 || node.boardGame.winner != TileType.EMPTY || node.boardGame.isDraw()) { // end game, terminal 
            node.nChild = 1;
            double v = node.boardGame.evaluate(node.rootPlayer);
            if (v == 10000) {
                node.winingRate = 1.0;
                if (node.boardGame.isDraw()) {
                    node.winingRate = 0;
                }
            } else if (v == -10000) {
                node.winingRate = 0;
            } else {
                node.winingRate = (v / 10.0);
            }

            //System.out.println("value MinMax: "+ v + " " + node.rootPlayer);
            //node.boardGame.printBoard();;
            return v;
        }

        if (maximizingPlayer) {
            double value = -299999999;
            int nSize = node.boardGame.nSize;
            for (int i = 0; i < nSize; i++) {
                for (int j = 0; j < nSize; j++) {
                    if (node.boardGame.currentPlayer == node.boardGame.board[i][j]) {

                        Node childNode = new Node((node.boardGame));
                        // backup root player
                        childNode.rootPlayer = node.rootPlayer;
                        //childNode.setApplyMove(i, j);
                        childNode.boardGame.takeTurn(i, j);
                        // apply immedieatly ?
                        //nodeChilds.add(childNode);
                        double minMaxValue = runMinimax(childNode, depth - 1, false);
                        value = Math.max(value, minMaxValue);

                        // final decision
                        node.nChild += childNode.nChild;
                        node.winingRate += childNode.winingRate;
                        //System.out.println(node.nChild);
                        if (Math.abs(value - minMaxValue) < 0.1) {
                            node.setApplyMove(i, j); // final decision
                            node.value = value;
                            node.chance = childNode.winingRate * 1.0 / childNode.nChild;
                        }
                    }
                }
            }

            return value;
        } else if (!maximizingPlayer) {
            double value = 299999999;
            int nSize = node.boardGame.nSize;
            for (int i = 0; i < nSize; i++) {
                for (int j = 0; j < nSize; j++) {
                    if (node.boardGame.currentPlayer == node.boardGame.board[i][j]) {
                        Node childNode = new Node((node.boardGame));
                        childNode.rootPlayer = node.rootPlayer;
                        //childNode.setApplyMove(i, j);
                        childNode.boardGame.takeTurn(i, j);
                        // apply immedieatly ?
                        //nodeChilds.add(childNode);
                        double minMaxValue = runMinimax(childNode, depth - 1, true);
                        node.avg += minMaxValue;
                        value = Math.min(value, minMaxValue);

                        // final decision
                        node.nChild += childNode.nChild;
                        node.winingRate += childNode.winingRate;
                        //System.out.println(node.nChild);
                        if (Math.abs(value - minMaxValue) < 0.1) {
                            node.setApplyMove(i, j); // final decision
                            node.value = value;
                            node.chance = childNode.winingRate * 1.0 / childNode.nChild;
                        }
                    }
                }
            }
            return value;
        }
        return -1.0; // never be here
    }

    static double runAlphaBeta(Node node, int depth, double alpha, double beta, boolean maximizingPlayer) {
        //   System.out.println(node.rootPlayer);

        if (depth == 0 || node.boardGame.winner != TileType.EMPTY || node.boardGame.isDraw()) { // end game, terminal 
            node.nChild = 1;
            double v = node.boardGame.evaluate(node.rootPlayer);
            if (v == 10000) {
                node.winingRate = 1.0;
                if (node.boardGame.isDraw()) {
                    node.winingRate = 0;
                }
            } else if (v == -10000) {
                node.winingRate = 0;
            } else {
                node.winingRate = (v / 10.0);
            }

            System.out.println("value AB : " + v + "");
            // node.boardGame.printBoard();

            return v;
        }

        if (maximizingPlayer) {
            double value = -299999999;
            int nSize = node.boardGame.nSize;
            for (int i = 0; i < nSize; i++) {
                for (int j = 0; j < nSize; j++) {
                    if (node.boardGame.currentPlayer == node.boardGame.board[i][j]) {

                        Node childNode = new Node((node.boardGame));
                        // backup root player
                        childNode.rootPlayer = node.rootPlayer;
                        System.out.println("Max depth " + depth + " move " + i + " " + j);
                        childNode.boardGame.takeTurn(i, j);
                        // apply immedieatly ?
                        //nodeChilds.add(childNode);
                        double minMaxValue = runAlphaBeta(childNode, depth - 1, alpha, beta, false);
                        System.out.println("depth " + depth + " value " + minMaxValue);
                        value = Math.max(value, minMaxValue);
                        alpha = Math.max(value, alpha);

                        // final decision
                        node.nChild += childNode.nChild;
                        node.winingRate += childNode.winingRate;
                        //System.out.println(node.nChild);
                        if (Math.abs(value - minMaxValue) < 0.001) {
                            node.setApplyMove(i, j); // final decision
                            node.value = value;
                            node.chance = childNode.winingRate * 1.0 / childNode.nChild;
                        }

                        if (alpha > beta || Math.abs(alpha - beta) < 0.0001) {
                            return value;
                        }
                    }
                }
            }

            return value;
        } else if (!maximizingPlayer) {
            double value = 299999999;
            int nSize = node.boardGame.nSize;
            for (int i = 0; i < nSize; i++) {
                for (int j = 0; j < nSize; j++) {
                    if (node.boardGame.currentPlayer == node.boardGame.board[i][j]) {
                        Node childNode = new Node((node.boardGame));
                        childNode.rootPlayer = node.rootPlayer;
                        //childNode.setApplyMove(i, j);
                        childNode.boardGame.takeTurn(i, j);
                        // apply immedieatly ?
                        //nodeChilds.add(childNode);
                        double minMaxValue = runAlphaBeta(childNode, depth - 1, alpha, beta, true);
                        System.out.println("Min depth " + depth + " move " + i + " " + j);
                        System.out.println("depth " + depth + " value " + minMaxValue);
                        value = Math.min(value, minMaxValue);
                        beta = Math.min(value, beta);
                        // final decision
                        node.nChild += childNode.nChild;
                        node.winingRate += childNode.winingRate;
                        //System.out.println(node.nChild);
                        if (Math.abs(value - minMaxValue) < 0.001) {
                            node.setApplyMove(i, j); // final decision
                            node.value = value;
                            node.chance = childNode.winingRate * 1.0 / childNode.nChild;
                        }

                        if (alpha > beta || Math.abs(alpha - beta) < 0.0001) {
                            return value;
                        }
                    }
                }
            }
            return value;
        }
        return -1.0; // never be here
    }

    static double runMinimax2(Node node, int depth, boolean maximizingPlayer) {
        //   System.out.println(node.rootPlayer);

        if (depth == 0 || node.boardGame.winner != TileType.EMPTY || node.boardGame.isDraw()) { // end game, terminal 
            node.nChild = 1;

            double v = node.boardGame.evaluate(node.rootPlayer);
            if (v == 10000) {
             
                node.winingRate = 1.0;
                if (node.boardGame.isDraw()) {
                    node.winingRate = 0;

                }
            } else if (v == -10000) {
                node.winingRate = 0;
            } else {
                node.winingRate = (v / 10.0);
            }

            int reward= (node.rootRedScore - node.boardGame.redScore) -  (node.rootBlueScore - node.boardGame.blueScore) ;
            
            if (node.rootPlayer==TileType.BLUE)
                reward = - reward;
            
            return v + reward;
        }

        if (maximizingPlayer) {
            double valueX = -299999999;
            int nSize = node.boardGame.nSize;
            for (int i = 0; i < nSize; i++) {
                for (int j = 0; j < nSize; j++) {
                    if (node.boardGame.currentPlayer == node.boardGame.board[i][j]) {

                        Node childNode = new Node((node.boardGame));
                        // backup root player
                        childNode.rootPlayer = node.rootPlayer;
                        //childNode.setApplyMove(i, j);
                        childNode.boardGame.takeTurn(i, j);
                           childNode.pX = i;
                        childNode.pY = j;
                        childNode.rootBlueScore = node.rootBlueScore;
                        childNode.rootRedScore = node.rootRedScore;
                        

                        // apply immedieatly ?
                        //nodeChilds.add(childNode);
                        double minMaxValue =  runMinimax2(childNode, depth - 1, false);

                        //minMaxValue = Math.min(minMaxValue, 10000);
                        //minMaxValue = Math.max(minMaxValue, -10000);
                       // valueX = Math.max(valueX, minMaxValue);
                     //   System.out.println("Move max  depth "+ depth +" at " + i + " "+ j +" value " + minMaxValue +" " +  node.nChild +" child");
                            
                        // final decision
                        node.nChild += childNode.nChild;
                        node.winingRate += childNode.winingRate;
                        //System.out.println(node.nChild);
                        if (valueX < minMaxValue) {
                           valueX =  minMaxValue;
                            node.setApplyMove(i, j); // final decision
                            node.value = valueX;
                            node.chance = childNode.winingRate * 1.0 / childNode.nChild;
                        }
                        
//                        if (i==0 && j ==0 && depth == 15)
//                            System.out.println(i + " " + j + " bestValue: " + valueX + " currentMinMax " + minMaxValue +" reward " + childNode.boardGame.reward);
//                        
//                        
//                        if (i==0 && j ==1 && depth == 15)
//                            System.out.println(i + " " + j + " bestValue: " + valueX + " currentMinMax " + minMaxValue+" reward " + childNode.boardGame.reward);
                  
                    }
                }
            }

            return node.value ;
        } else if (!maximizingPlayer) {
            double valueX = 299999999;
            int nSize = node.boardGame.nSize;
            for (int i = 0; i < nSize; i++) {
                for (int j = 0; j < nSize; j++) {
                    if (node.boardGame.currentPlayer == node.boardGame.board[i][j]) {
                        Node childNode = new Node((node.boardGame));
                        childNode.rootPlayer = node.rootPlayer;
                        //childNode.setApplyMove(i, j);
                        childNode.boardGame.takeTurn(i, j);
                           childNode.pX = i;
                        childNode.pY = j;
                         childNode.rootBlueScore = node.rootBlueScore;
                        childNode.rootRedScore = node.rootRedScore;
                        
                        // apply immedieatly ?
                        //nodeChilds.add(childNode);
                        double minMaxValue =  runMinimax2(childNode, depth - 1, true);

                        //minMaxValue = Math.min(minMaxValue, 10000);
                        //minMaxValue = Math.max(minMaxValue, -10000);
                     //   valueX = Math.min(valueX, minMaxValue);
                    //    System.out.println("Move min  depth "+ depth +" at " + i + " "+ j +" value " + minMaxValue +" " +  node.nChild +" child");
                    
                         // final decision
                        node.nChild += childNode.nChild;
                        node.winingRate += childNode.winingRate;
                        //System.out.println(node.nChild);
                        if (valueX > minMaxValue) {
                           valueX =  minMaxValue;
                            node.setApplyMove(i, j); // final decision
                            node.value = valueX;
                            node.chance = childNode.winingRate * 1.0 / childNode.nChild;
                        }
                        
//                           if (node.pX == 0 && node.pY == 1 && depth == 14)
//                            System.out.println( "XXX MinNode depth " + depth + " at" +i + " " + j + " bestValue: " + valueX + " bestValue: " + valueX + " currentMinMax " + minMaxValue +" reward " + childNode.boardGame.reward);
//                       
//                    

                   
                    }
                }
            }
           return node.value ;
        }
        return -1.0; // never be here
    }

    static double runAlphaBeta2(Node node, int depth, double alpha, double beta, boolean maximizingPlayer) {
        //   System.out.println(node.rootPlayer);

        if (depth == 0 || node.boardGame.winner != TileType.EMPTY || node.boardGame.isDraw()) { // end game, terminal 
            node.nChild = 1;
            double v = node.boardGame.evaluate2(node.rootPlayer);
            if (v == 10000) {
                node.winingRate = 1.0;
                if (node.boardGame.isDraw()) {
                    node.winingRate = 0;
                }
            } else if (v == -10000) {
                node.winingRate = 0;
            } else {
                node.winingRate = (v / 10.0);
            }

            // node.winingRate = Math.max(0,node.winingRate);
            // node.boardGame.printBoard();
             System.out.println("valueAB: " + v);
            return v;
        }

        if (maximizingPlayer) {
            double value = -299999999;
            int nSize = node.boardGame.nSize;
            for (int i = 0; i < nSize; i++) {
                for (int j = 0; j < nSize; j++) {
                    if (node.boardGame.currentPlayer == node.boardGame.board[i][j]) {

                        Node childNode = new Node((node.boardGame));
                        childNode.rootPlayer = node.rootPlayer;
                        childNode.boardGame.takeTurn(i, j);
                        double minMaxValue = runAlphaBeta2(childNode, depth - 1, alpha, beta, false);
                        minMaxValue = Math.min(minMaxValue, 10000);
                        minMaxValue = Math.max(minMaxValue, -10000);
                        value = Math.max(value, minMaxValue);

                        alpha = Math.max(value, alpha);

                        node.nChild += childNode.nChild;
                        node.winingRate += childNode.winingRate;

                        if (Math.abs(value - minMaxValue) < 0.001) {
                            node.setApplyMove(i, j); // final decision
                            node.value = value;
                            node.chance = childNode.winingRate * 1.0 / childNode.nChild;
                        }

                        if (alpha > beta || Math.abs(alpha - beta) < 0.001) {
                            return value;
                        }

                    }
                }
            }

            return value;
        } else if (!maximizingPlayer) {
            double value = 299999999;
            int nSize = node.boardGame.nSize;
            for (int i = 0; i < nSize; i++) {
                for (int j = 0; j < nSize; j++) {
                    if (node.boardGame.currentPlayer == node.boardGame.board[i][j]) {
                        Node childNode = new Node((node.boardGame));
                        childNode.rootPlayer = node.rootPlayer;
                        childNode.boardGame.takeTurn(i, j);
                        double minMaxValue = runAlphaBeta2(childNode, depth - 1, alpha, beta, true);

                        minMaxValue = Math.min(minMaxValue, 10000);
                        minMaxValue = Math.max(minMaxValue, -10000);
                        value = Math.min(value, minMaxValue);
                        beta = Math.min(value, beta);

                        node.nChild += childNode.nChild;
                        node.winingRate += childNode.winingRate;

                        if (Math.abs(value - minMaxValue) < 0.001) {
                            node.setApplyMove(i, j); // final decision
                            node.value = value;
                            node.chance = childNode.winingRate * 1.0 / childNode.nChild;
                        }

                        if (alpha > beta || Math.abs(alpha - beta) < 0.001) {
                            return value;
                        }
                    }
                }
            }
            return value;
        }
        return -1.0; // never be here
    }

    static double runAlphaBeta2a(Node node, int depth, double alpha, double beta, boolean maximizingPlayer) {
        //   System.out.println(node.rootPlayer);

        //   System.out.println(node.rootPlayer);
        if (depth == 0 || node.boardGame.winner != TileType.EMPTY || node.boardGame.isDraw()) { // end game, terminal 
            node.nChild = 1;

            double v = node.boardGame.evaluate(node.rootPlayer);
            if (v == 10000) {
                node.winingRate = 1.0;
                if (node.boardGame.isDraw()) {
                    node.winingRate = 0;

                }
            } else if (v == -10000) {
                node.winingRate = 0;
            } else {
                node.winingRate = (v / 10.0);
            }
         //   System.out.println(v);
         //   return 0;
          int reward= (node.rootRedScore - node.boardGame.redScore) -  (node.rootBlueScore - node.boardGame.blueScore) ;
            
            if (node.rootPlayer==TileType.BLUE)
                reward = - reward;
            
            return v + reward;
        }

        if (maximizingPlayer) {
            double valueX = -299999999;
            int nSize = node.boardGame.nSize;
            for (int i = 0; i < nSize; i++) {
                for (int j = 0; j < nSize; j++) {
                    if (node.boardGame.currentPlayer == node.boardGame.board[i][j]) {

                        Node childNode = new Node((node.boardGame));
                        // backup root player
                        childNode.rootPlayer = node.rootPlayer;
                        childNode.boardGame.takeTurn(i, j);
                        childNode.pX = i;
                        childNode.pY = j;
                           childNode.rootBlueScore = node.rootBlueScore;
                        childNode.rootRedScore = node.rootRedScore;
                      
                        
                        //nodeChilds.add(childNode);
                        double minMaxValue =  runAlphaBeta2a(childNode, depth - 1, alpha, beta, false);
                        
                      //   System.out.println("Move Max depth "+ depth +" ParrentMove " + node.pX + " " + node.pY +" -> child Move " + i + " "+ j +" value " + minMaxValue +" " +  node.nChild +" child");
                     
                        node.nChild += childNode.nChild;
                        
                            
                       if (valueX < minMaxValue) {
                           valueX =  minMaxValue;
                            node.setApplyMove(i, j); // final decision
                            node.value = valueX;
                            node.chance = childNode.winingRate * 1.0 / childNode.nChild;
                        }
                        
                       // alpha = Math.max(alpha, node.value);
                        if (alpha < node.value)
                            alpha = node.value;
                        
                        // final decision
                         
                        if (alpha >= beta) {
                          //   System.out.println("BREAK depth " + depth + "at node" + i + " " + j + " bestValue: " + valueX +  " currentMinMax " + minMaxValue +"  alpha " + alpha + " beta " + beta);
                        return node.value ;   
                        //return valueX;
                        }
                        
//                        if (i==0 && j ==0 && depth == 15)
//                            System.out.println(i + " " + j + " bestValue: " + valueX +  " bestValue: " + valueX + " currentMinMax " + minMaxValue +"  alpha " + alpha + " beta " + beta+" reward " + childNode.boardGame.reward);
//                        
//                        
//                        if (i==0 && j ==1 && depth == 15)
//                            System.out.println(i + " " + j + " bestValue: " + valueX + " bestValue: " + valueX + " currentMinMax " + minMaxValue +"  alpha " + alpha + " beta " + beta+" reward " + childNode.boardGame.reward);
//                        
//                   
                       
         
                    }
                }
            }

           return node.value ;
            
        } else if (!maximizingPlayer) {
            double valueX = 299999999;
            int nSize = node.boardGame.nSize;
            for (int i = 0; i < nSize; i++) {
                for (int j = 0; j < nSize; j++) {
                    if (node.boardGame.currentPlayer == node.boardGame.board[i][j]) {
                        Node childNode = new Node((node.boardGame));
                        childNode.rootPlayer = node.rootPlayer;
                        //childNode.setApplyMove(i, j);
                        childNode.boardGame.takeTurn(i, j);
                        childNode.pX = i;
                        childNode.pY = j;
                           childNode.rootBlueScore = node.rootBlueScore;
                        childNode.rootRedScore = node.rootRedScore;
                      
                        // apply immedieatly ?
                        //nodeChilds.add(childNode);
                        double minMaxValue = runAlphaBeta2a(childNode, depth - 1, alpha, beta, true);
                       // minMaxValue = Math.min(minMaxValue, 10000);
                      //  minMaxValue = Math.max(minMaxValue, -10000);
                        
                        
                        if (valueX > minMaxValue) {
                           valueX =  minMaxValue;
                            node.setApplyMove(i, j); // final decision
                            node.value = valueX;
                            node.chance = childNode.winingRate * 1.0 / childNode.nChild;
                        }
                        
                        //beta = Math.min(beta, node.value);
                        if (beta > node.value)
                            beta = node.value;
                        
                        
                        node.nChild += childNode.nChild;
                       
//                             
//                        if (node.pX == 0 && node.pY == 1 && depth == 14)
//                            System.out.println( "MinNode depth " + depth + " at" +i + " " + j + " bestValue: " + valueX + " bestValue: " + valueX + " currentMinMax " + minMaxValue +"  alpha " + alpha + " beta " + beta+" reward " + childNode.boardGame.reward);
//                       
                        if (alpha >= beta) {
                       //    System.out.println("BREAK depth " + depth + "at node" + i + " " + j + " bestValue: " + valueX  + " currentMinMax " + minMaxValue +"  alpha " + alpha + " beta " + beta);
                       return node.value ;
//                       return valueX;
                        }
                        
                        
                        
                        node.winingRate += childNode.winingRate;
                        //System.out.println(node.nChild);
                        
                    }
                }
            }
           return node.value ;
        }
        return -1.0; // never be here
    }

    static double runMCTS(Node node, int depth, boolean maximizingPlayer) {
        //   System.out.println(node.rootPlayer);

        if (depth == 0 || node.boardGame.winner != TileType.EMPTY) { // end game, terminal 
            node.nChild = 1;
            double v = node.boardGame.simmulation(5, node.rootPlayer);
            node.winingRate = v;
            //    System.out.println("leaf at depth " + depth);
            return v;
        }

        if (maximizingPlayer) {
            double value = -299999999;
            int nSize = node.boardGame.nSize;
            for (int i = 0; i < nSize; i++) {
                for (int j = 0; j < nSize; j++) {
                    if (node.boardGame.currentPlayer == node.boardGame.board[i][j]) {

                        Node childNode = new Node((node.boardGame));
                        // backup root player
                        childNode.rootPlayer = node.rootPlayer;
                        //childNode.setApplyMove(i, j);
                        childNode.boardGame.takeTurn(i, j);
                        // apply immedieatly ?
                        //nodeChilds.add(childNode);
                        double minMaxValue = runMCTS(childNode, depth - 1, false);
                        value = Math.max(value, minMaxValue);

                        // final decision
                        node.nChild += childNode.nChild;
                        node.winingRate += childNode.winingRate;
                        //System.out.println(node.nChild);
                        if (Math.abs(value - minMaxValue) < 0.0001) {
                            node.setApplyMove(i, j); // final decision
                            node.value = value;
                            node.chance = childNode.winingRate * 1.0 / childNode.nChild;
                        }
                    }
                }
            }
            //   System.out.println("apply move" + node.moveX +" "+ node.moveY);
            return value;
        } else if (!maximizingPlayer) {
            double value = 299999999;
            int nSize = node.boardGame.nSize;
            for (int i = 0; i < nSize; i++) {
                for (int j = 0; j < nSize; j++) {
                    if (node.boardGame.currentPlayer == node.boardGame.board[i][j]) {
                        Node childNode = new Node((node.boardGame));
                        childNode.rootPlayer = node.rootPlayer;
                        //childNode.setApplyMove(i, j);
                        childNode.boardGame.takeTurn(i, j);
                        // apply immedieatly ?
                        //nodeChilds.add(childNode);
                        double minMaxValue = runMCTS(childNode, depth - 1, true);
                        value = Math.min(value, minMaxValue);

                        // final decision
                        node.nChild += childNode.nChild;
                        node.winingRate += childNode.winingRate;
                        //System.out.println(node.nChild);
                        if (Math.abs(value - minMaxValue) < 0.0001) {
                            node.setApplyMove(i, j); // final decision
                            node.value = value;
                            node.chance = childNode.winingRate * 1.0 / childNode.nChild;
                        }
                    }
                }
            }
            return value;
        }
        return -1.0; // never be here
    }

}
