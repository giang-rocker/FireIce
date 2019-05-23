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
    TileType rootPlayer ;
    
    
    public Node (Board boardGame){
        this.boardGame = new Board(boardGame);
        nVisited = 0 ;
        value = 0;
        chance = 0;
        isTerminal = false;
        moveX=-1;
        moveY = -1;
    }
    
    public void setApplyMove(int x, int y ) {
        this.moveX = x;
        this.moveY = y ;
    }
    
    static double runMinimax(Node node, int depth,boolean maximizingPlayer) {
     //   System.out.println(node.rootPlayer);
        
    if (depth == 0 || node.boardGame.winner!= TileType.EMPTY){ // end game, terminal 
      //  System.out.println(depth);
       // System.out.println( node.boardGame.evaluate(node.rootPlayer));
        return  node.boardGame.evaluate(node.rootPlayer);
    }
    
    if (maximizingPlayer) {
        double value = -9999999;
        int nSize = node.boardGame.nSize;
        for (int i =0; i <nSize; i++) 
            for (int j=0 ; j < nSize; j++) {
                if (node.boardGame.currentPlayer == node.boardGame.board[i][j] ) {
                    
                    Node childNode = new Node((node.boardGame));
                    // backup root player
                    childNode.rootPlayer = node.rootPlayer;
                    //childNode.setApplyMove(i, j);
                    childNode.boardGame.takeTurn(i, j);
                    // apply immedieatly ?
                    //nodeChilds.add(childNode);
                    double minMaxValue = runMinimax(childNode, depth - 1,false);
                    value = Math.max(value,minMaxValue );
                   
                    // final decision
                    if ( Math.abs(value - minMaxValue) < 0.1) {
                        node.setApplyMove(i, j); // final decision
                        node.value = value;
                    }
                   
                }
            }
        
        return value;
    }
    else if (!maximizingPlayer) {
       double value = 999999999;
        int nSize = node.boardGame.nSize;
        for (int i =0; i <nSize; i++) 
            for (int j=0 ; j < nSize; j++) {
                if (node.boardGame.currentPlayer == node.boardGame.board[i][j] ) {
                    Node childNode = new Node((node.boardGame));
                    childNode.rootPlayer = node.rootPlayer;
                    //childNode.setApplyMove(i, j);
                    childNode.boardGame.takeTurn(i, j);
                    // apply immedieatly ?
                    //nodeChilds.add(childNode);
                     double minMaxValue = runMinimax(childNode, depth - 1,true);
                    value = Math.min(value, minMaxValue);
                    
                    // change move
                    if (Math.abs(value - minMaxValue)< 0.1) {
                        node.setApplyMove(i, j); // final decision
                        node.value = value;
                    }
                }
            }
        return value;
        }
    return -1.0; // never be here
    }
    
    public double runExpectiminimax(Node node, int depth) {
      double alpha = 0;
//    if node is a terminal node or depth = 0
//        return the heuristic value of node
//    if the adversary is to play at node
//        // Return value of minimum-valued child node
//        let α := +∞
//        foreach child of node
//            α := min(α, expectiminimax(child, depth-1))
//    else if we are to play at node
//        // Return value of maximum-valued child node
//        let α := -∞
//        foreach child of node
//            α := max(α, expectiminimax(child, depth-1))
//    else if random event at node
//        // Return weighted average of all child nodes' values
//        let α := 0
//        foreach child of node
//            α := α + (Probability[child] * expectiminimax(child, depth-1))
    return alpha;
    }
    
    public double runMCTS(Node node, int depth) {
      double alpha = 0;
//    if node is a terminal node or depth = 0
//        return the heuristic value of node
//    if the adversary is to play at node
//        // Return value of minimum-valued child node
//        let α := +∞
//        foreach child of node
//            α := min(α, expectiminimax(child, depth-1))
//    else if we are to play at node
//        // Return value of maximum-valued child node
//        let α := -∞
//        foreach child of node
//            α := max(α, expectiminimax(child, depth-1))
//    else if random event at node
//        // Return weighted average of all child nodes' values
//        let α := 0
//        foreach child of node
//            α := α + (Probability[child] * expectiminimax(child, depth-1))
    return alpha;
    }
}
