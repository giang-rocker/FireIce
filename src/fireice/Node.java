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
    int moveX, moveY; // will apply this move
    TileType rootPlayer;
    double winingRate;
    int nChild;

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
    }

    public void setApplyMove(int x, int y) {
        this.moveX = x;
        this.moveY = y;
    }

static double runMinimax(Node node, int depth, boolean maximizingPlayer) {
        //   System.out.println(node.rootPlayer);

        if (depth == 0 || node.boardGame.winner != TileType.EMPTY) { // end game, terminal 
            node.nChild = 1;
            double v = node.boardGame.evaluate(node.rootPlayer);
            if (v == 100) {
                node.winingRate = 1.0;
                if (node.boardGame.isDraw())
                    node.winingRate = 0;
            }
            else if (v == -100) {
                node.winingRate =0;
            }
            else 
                node.winingRate =(v/10.0);
            
           // node.winingRate = Math.max(0,node.winingRate);
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
static double  runAlphaBeta(Node node, int depth,double alpha, double beta, boolean maximizingPlayer) {
        //   System.out.println(node.rootPlayer);

        if (depth == 0 || node.boardGame.winner != TileType.EMPTY) { // end game, terminal 
            node.nChild = 1;
            double v = node.boardGame.evaluate(node.rootPlayer);
            if (v == 100) {
                node.winingRate = 1.0;
                if (node.boardGame.isDraw())
                    node.winingRate = 0;
            }
            else if (v == -100) {
                node.winingRate =0;  
            }
            else 
                node.winingRate =(v/10.0);
            
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
                        double minMaxValue = runAlphaBeta(childNode, depth - 1,alpha,beta, false);
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
                        
                        if (alpha> beta )
                            return value;
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
                        double minMaxValue = runAlphaBeta(childNode, depth - 1,alpha,beta, true);
                        value = Math.min(value, minMaxValue);
                        beta  = Math.min(value, beta);
                        // final decision
                        node.nChild += childNode.nChild;
                        node.winingRate += childNode.winingRate;
                        //System.out.println(node.nChild);
                        if (Math.abs(value - minMaxValue) < 0.001) {
                            node.setApplyMove(i, j); // final decision
                            node.value = value;
                            node.chance = childNode.winingRate * 1.0 / childNode.nChild;
                        }
                        
                        if (alpha> beta )
                            return value;
                    }
                }
            }
            return value;
        }
        return -1.0; // never be here
    }
    
    
static double runMinimax2(Node node, int depth, boolean maximizingPlayer) {
        //   System.out.println(node.rootPlayer);

        if (depth == 0 || node.boardGame.winner != TileType.EMPTY) { // end game, terminal 
            node.nChild = 1;
            double v = node.boardGame.evaluate2(node.rootPlayer);
            if (v == 100) {
                node.winingRate = 1.0;
                if (node.boardGame.isDraw())
                    node.winingRate = 0;
            }
            else if (v == -100) {
                node.winingRate =0;  
            }
            else 
                node.winingRate =(v/10.0);
            
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
                        double minMaxValue = childNode.boardGame.reward + runMinimax2(childNode, depth - 1, false);
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
                        double minMaxValue = childNode.boardGame.reward + runMinimax2(childNode, depth - 1, true);
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

static double runAlphaBeta2(Node node, int depth,double alpha, double beta, boolean maximizingPlayer) {
        //   System.out.println(node.rootPlayer);

         if (depth == 0 || node.boardGame.winner != TileType.EMPTY) { // end game, terminal 
            node.nChild = 1;
            double v = node.boardGame.evaluate2(node.rootPlayer);
            if (v == 100) {
                node.winingRate = 1.0;
                if (node.boardGame.isDraw())
                    node.winingRate = 0;
            }
            else if (v == -100) {
                node.winingRate =0;
            }
            else 
                node.winingRate =(v/10.0);
            
           // node.winingRate = Math.max(0,node.winingRate);
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
                        double minMaxValue = childNode.boardGame.reward + runAlphaBeta2(childNode, depth - 1,alpha,beta, false);
                        value = Math.max(value, minMaxValue);
                        
                        alpha =   Math.max(value, alpha);
                        
                        
                        node.nChild += childNode.nChild;
                        node.winingRate += childNode.winingRate;
                        
                        if (Math.abs(value - minMaxValue) < 0.1) {
                            node.setApplyMove(i, j); // final decision
                            node.value = value;
                            node.chance = childNode.winingRate * 1.0 / childNode.nChild;
                        }
                        
                        if (alpha > beta)
                            return value;
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
                        double minMaxValue =  childNode.boardGame.reward + runAlphaBeta2(childNode, depth - 1,alpha,beta, true);
                        value = Math.min(value, minMaxValue);
                        beta =   Math.min(value, beta);
                        
                        node.nChild += childNode.nChild;
                        node.winingRate += childNode.winingRate;
                        
                        if (Math.abs(value - minMaxValue) < 0.1) {
                            node.setApplyMove(i, j); // final decision
                            node.value = value;
                            node.chance = childNode.winingRate * 1.0 / childNode.nChild;
                        }
                        
                        if (alpha > beta)
                            return value;
                    }
                }
            }
            return value;
        }
        return -1.0; // never be here
    }

static double runMCTS(Node node, int depth, boolean maximizingPlayer) {
        //   System.out.println(node.rootPlayer);

        if (depth == 0 || node.boardGame.winner != TileType.EMPTY) { // end game, terminal 
            node.nChild = 1;
            double v = node.boardGame.simmulation(10,node.rootPlayer);
            node.winingRate = v;
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
                        double minMaxValue =   runMCTS(childNode, depth - 1, false);
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
                        double minMaxValue =  runMCTS(childNode, depth - 1, true);
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

}
