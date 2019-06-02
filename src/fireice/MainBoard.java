/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package fireice;

import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.Label;
import java.awt.MouseInfo;
import java.awt.TextArea;
import java.awt.image.BufferedImage;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileWriter;
import java.io.IOException;
import java.util.Scanner;
import java.util.logging.Level;
import java.util.logging.Logger;
import javafx.scene.control.ComboBox;
import javax.imageio.ImageIO;
import javax.swing.Icon;
import javax.swing.ImageIcon;
import javax.swing.JCheckBox;
import javax.swing.JComboBox;
import javax.swing.JLabel;

/**
 *
 * @author giang
 */
public final class MainBoard extends javax.swing.JFrame {

    /**
     * Creates new form MainBoard
     */
    Board boardGame;
    final BufferedImage redBlock;
    final BufferedImage blueBlock;
    //final BufferedImage redBlockHover;
    //final BufferedImage blueBlockHover;
    final BufferedImage bgImg;
    final BufferedImage blankTile;
    final BufferedImage labelImg;
    String fileName;
    int humanWin = 0;
    int AIWin = 0;
    boolean isHumanRed;
    boolean isHumanPlayFirst;

    boolean boardUpdate = false;
    int nSize = 0;
    int sX = -1;
    int sY = -1;
    int UNIT = 64;

    Node MiniMax;

    TextArea txtLogMove;
    BufferedWriter writer;
    int countGame = 0;

    public MainBoard(String fileName, boolean isHumanRed, boolean isHumanPlayFirst) throws FileNotFoundException, IOException {
        initComponents();
        this.fileName = fileName;
        txtLogMove = new TextArea("");
        readBoardConfiguration(fileName, isHumanRed, isHumanPlayFirst);
        redBlock = ImageIO.read(new File("img/r64.png"));
        blueBlock = ImageIO.read(new File("img/b64.png"));
        //redBlockHover = ImageIO.read(new File("img/r64_hover.png"));
        //blueBlockHover = ImageIO.read(new File("img/b64_hover.png"));
        blankTile = ImageIO.read(new File("img/blankTile.png"));
        bgImg = ImageIO.read(new File("img/bg3.png"));
        labelImg = ImageIO.read(new File("img/blankLabel.png"));
        this.nSize = this.boardGame.nSize;
        this.setSize(nSize * UNIT + marginX * 3 + 142, nSize * UNIT + (marginY * 2));
        // init label
        int dx = nSize * UNIT + marginX * 2;
        int dy = 1 * UNIT;

        this.isHumanRed = isHumanRed;
        this.isHumanPlayFirst = isHumanPlayFirst;

        if (!isHumanRed) {
            this.lbGameHumanInfor.setForeground(new Color(1, 187, 234));
            this.lbGameHumanInfor.setText("Human-BLUE 0");

            this.lbGameAIInfor.setForeground(new Color(255, 33, 66));
            this.lbGameAIInfor.setText("AI-RED 0");
        }

        txtLogMove.setEditable(false);

        txtLogMove.setLocation(dx, dy + 64);
        txtLogMove.setSize(142, (this.nSize - 3) * UNIT);

        selectAlg.setLocation(dx, dx - UNIT);
        selectAlg.setSize(142, 32);
        btnNewGame.setLocation(dx, 15);
        btnNewGame.setSize(142, 32);
        this.drawIndex.setSelected(true);

        lbStatus.setLocation(5, dx - 32);
        lbStatus.setSize(dx + 142, 32);

        this.add(txtLogMove);

        selectAlg.removeAllItems();
        selectAlg.addItem("AlphaBeta2a");
        selectAlg.addItem("AlphaBeta2");
        selectAlg.addItem("AlphaBeta1");
        selectAlg.addItem("MCTS-Like");
        selectAlg.setSelectedIndex(0);

        lbGameHumanInfor.setLocation(80, 10);
        lbGameAIInfor.setLocation(200, 10);
        lbPlayerTurn.setLocation(5, 10);
        lbGameHumanInfor.setSize(110, 12);
        lbGameAIInfor.setSize(110, 12);
        lbPlayerTurn.setSize(100, 12);

        this.paint(this.getGraphics());
    }

    public void readBoardConfiguration(String fileName, boolean isHumanRed, boolean isHumanPlayFirst) throws FileNotFoundException {
        // read file
        // pass the path to the file as a parameter 

        File file = new File(fileName);
        Scanner sc = new Scanner(file);
        String path = file.getAbsolutePath();
        System.out.println(path);

        int n = Integer.parseInt(sc.nextLine());

        String config = "";
        while (sc.hasNextLine()) {
            config += (sc.nextLine());
        }

        config = config.replace("\n", "").replace(" ", "");

        System.out.println(config.length());

        System.out.println(config);
        this.boardGame = new Board(n, config, isHumanRed, isHumanPlayFirst);

        if (isHumanRed) {
            this.lbPlayerTurn.setForeground(Color.RED);
            this.lbPlayerTurn.setText("Red Turn");
        } else {
            this.lbPlayerTurn.setForeground(new Color(1, 187, 234));
            this.lbPlayerTurn.setText("Blue Turn");
        }

        if (isHumanRed) {
            this.lbGameAIInfor.setText("- " + AIWin + " AI-BLUE");
            this.lbGameHumanInfor.setText("Human-RED " + humanWin);
        } else {
            this.lbGameAIInfor.setText("- " + AIWin + " AI-RED");
            this.lbGameHumanInfor.setText("Human-BLUE " + humanWin);
        }

        txtLogMove.setText("");
        try {
            writer = new BufferedWriter(new FileWriter("logMove" + countGame + ".txt"));
        } catch (IOException ex) {
            Logger.getLogger(MainBoard.class.getName()).log(Level.SEVERE, null, ex);
        }
        // AI move first
        if (this.boardGame.currentPlayer == this.boardGame.AIPlayer) {
            getAIMove();
        }
        this.nSize = this.boardGame.nSize;
        this.setSize(nSize * UNIT + marginX * 3 + 142, nSize * UNIT + (marginY * 2));

    }

    int marginX = 32;
    int marginY = 32;

    public void paint(Graphics g) {
        super.paint(g);

        //g.drawImage(bgImg, 0, 40, nSize*UNIT + marginX*3 + 142  , nSize*UNIT + marginY*2,this);
        g.setFont(new Font("TimesRoman", Font.PLAIN, 8));
        g.setColor(Color.black);
        if (this.boardGame != null) {
            int n = this.boardGame.nSize;

            for (int i = 0; i < n; i++) {
                for (int j = 0; j < n; j++) {
                    int dx = i * 64 + marginX;
                    int dy = j * 64 + marginY;
                    g.drawImage(blankTile, dx, dy, null);
                    if (this.boardGame.board[j][i] == TileType.BLUE) {
                        g.drawImage(blueBlock, dx, dy, null);
                        if (this.drawIndex.isSelected()) {
                            g.drawString("(" + j + "," + i + ")", dx + 15, dy + 15);
                        }
                    } else if (this.boardGame.board[j][i] == TileType.RED) {
                        g.drawImage(redBlock, dx, dy, null);
                        if (this.drawIndex.isSelected()) {
                            g.drawString("(" + j + "," + i + ")", dx + 15, dy + 15);
                        }
                    }

                }
            }

        }
//        if (!(sX < 0 || sY < 0 || sX >= this.boardGame.nSize || sY >= this.boardGame.nSize)) {
//            int dx = sX * 64 + marginX;
//            int dy = sY * 64 + marginY;
//            if (this.boardGame.board[sX][sY] == TileType.RED) {
//                g.drawImage(redBlockHover, dx, dy, null);
//            } else if (this.boardGame.board[sX][sY] == TileType.BLUE) {
//                g.drawImage(blueBlockHover, dx, dy, null);
//            }
//        }

        // set fopnt
        g.setFont(new Font("default", Font.PLAIN, 32));

        int dx = nSize * UNIT + marginX * 2;
        int dy = 1 * UNIT;
        g.drawImage(labelImg, dx, dy, null);
        //draw scre
        g.setColor(Color.red);
        g.drawString(this.boardGame.redScore + "", dx + 55, dy + 45);
        g.drawImage(labelImg, dx, (this.nSize - 1) * UNIT, null);
        g.setColor(new Color(1, 187, 234));
        g.drawString(this.boardGame.blueScore + "", dx + 55, (this.nSize - 1) * UNIT + 45);

        g.setFont(new Font("default", Font.PLAIN, 50));
        dx = (this.nSize / 2) * UNIT;

        if (this.boardGame.isDraw()) {
            g.setColor(Color.green);
            g.drawString(gameResult, dx - 30, 50 + dx);
        } else if (this.boardGame.winner == TileType.RED) {
            g.setColor(Color.red);
            g.drawString(gameResult, dx - 30, 50 + dx);
        } else if (this.boardGame.winner == TileType.BLUE) {
            g.setColor(new Color(1, 187, 234));
            g.drawString(gameResult, dx - 30, 50 + dx);
        }

    }

    /**
     * This method is called from within the constructor to initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is always
     * regenerated by the Form Editor.
     */
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        lbPlayerTurn = new javax.swing.JLabel();
        btnNewGame = new javax.swing.JButton();
        lbGameAIInfor = new javax.swing.JLabel();
        lbGameHumanInfor = new javax.swing.JLabel();
        lbStatus = new javax.swing.JLabel();
        selectAlg = new javax.swing.JComboBox<>();
        drawIndex = new javax.swing.JCheckBox();

        setDefaultCloseOperation(javax.swing.WindowConstants.EXIT_ON_CLOSE);
        setTitle("FireIce v1.0");
        setResizable(false);
        addMouseMotionListener(new java.awt.event.MouseMotionAdapter() {
            public void mouseMoved(java.awt.event.MouseEvent evt) {
                formMouseMoved(evt);
            }
        });
        addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseReleased(java.awt.event.MouseEvent evt) {
                formMouseReleased(evt);
            }
        });
        addWindowListener(new java.awt.event.WindowAdapter() {
            public void windowClosing(java.awt.event.WindowEvent evt) {
                formWindowClosing(evt);
            }
        });
        getContentPane().setLayout(null);

        lbPlayerTurn.setText("Red Turn");
        getContentPane().add(lbPlayerTurn);
        lbPlayerTurn.setBounds(12, 13, 62, 17);

        btnNewGame.setText("New Game");
        btnNewGame.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnNewGameActionPerformed(evt);
            }
        });
        getContentPane().add(btnNewGame);
        btnNewGame.setBounds(657, 12, 88, 29);

        lbGameAIInfor.setFont(new java.awt.Font("Ubuntu", 1, 15)); // NOI18N
        lbGameAIInfor.setForeground(new java.awt.Color(1, 187, 234));
        lbGameAIInfor.setText("- 0 AI-BLUE");
        getContentPane().add(lbGameAIInfor);
        lbGameAIInfor.setBounds(196, 12, 82, 18);

        lbGameHumanInfor.setFont(new java.awt.Font("Ubuntu", 1, 15)); // NOI18N
        lbGameHumanInfor.setForeground(new java.awt.Color(255, 33, 66));
        lbGameHumanInfor.setText("Human-RED 0");
        getContentPane().add(lbGameHumanInfor);
        lbGameHumanInfor.setBounds(92, 12, 98, 18);

        lbStatus.setBackground(java.awt.SystemColor.menu);
        lbStatus.setText("      Win:  0%  | Thinking Time: 0ms");
        getContentPane().add(lbStatus);
        lbStatus.setBounds(0, 743, 666, 26);

        selectAlg.setModel(new javax.swing.DefaultComboBoxModel<>(new String[] { "Item 1", "Item 2", "Item 3", "Item 4" }));
        selectAlg.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                selectAlgActionPerformed(evt);
            }
        });
        getContentPane().add(selectAlg);
        selectAlg.setBounds(678, 743, 122, 27);

        drawIndex.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                drawIndexActionPerformed(evt);
            }
        });
        getContentPane().add(drawIndex);
        drawIndex.setBounds(12, 36, 22, 24);

        pack();
        setLocationRelativeTo(null);
    }// </editor-fold>//GEN-END:initComponents

    private void formMouseMoved(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_formMouseMoved
        // TODO add your handling code here:
        double mouseX = MouseInfo.getPointerInfo().getLocation().x - this.getLocationOnScreen().x;

        double mouseY = MouseInfo.getPointerInfo().getLocation().y - this.getLocationOnScreen().y;
        sY = (int) (mouseX - marginX) / 64;
        sX = (int) (mouseY - marginY) / 64;

//     /   this.paint(this.getGraphics());

    }//GEN-LAST:event_formMouseMoved
    String gameResult = "";
    private void formMouseReleased(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_formMouseReleased

        if (this.boardGame.isDraw() || this.boardGame.winner != TileType.EMPTY) {
            this.paint(this.getGraphics());
            return;
        }

        if (!(this.boardGame.isValidMove(sX, sY))) {
            System.out.println("Invalid Move");
            lbStatus.setText("Invalid Move!!!!");
            this.paint(this.getGraphics());
            return;
        }

        try {
            // System.out.println("Thinking Time: " + estimatedTime + "ms");
            writer.write(this.boardGame.currentPlayer + " (" + sX + ", " + sY + ")\n");
        } catch (IOException ex) {
            Logger.getLogger(MainBoard.class.getName()).log(Level.SEVERE, null, ex);
        }

        this.boardGame.takeTurn(sX, sY);
         System.out.println("Reward: " + this.boardGame.reward);
        //moveHuman++;

        System.out.println("Player move: " + sX + " " + sY);

        txtLogMove.setText(txtLogMove.getText() + "HM: " + sX + " " + sY + "\n");
        if (this.boardGame.winner != TileType.EMPTY) {
            if (this.boardGame.isDraw()) {
                gameResult = "DRAW";
            } else if (this.boardGame.winner == this.boardGame.HumanPlayer) {
                humanWin++;
                gameResult = "HUMAN WIN";
            } else if (this.boardGame.winner == this.boardGame.AIPlayer) {
                gameResult = "AI WIN";
                AIWin++;
            }

            this.paint(this.getGraphics());
            return;
        }
        this.paint(this.getGraphics());

        // AI turn
        getAIMove();
        if (this.boardGame.winner != TileType.EMPTY) {
            if (this.boardGame.isDraw()) {
                gameResult = "DRAW";
            } else if (this.boardGame.winner == this.boardGame.HumanPlayer) {
                humanWin++;
                gameResult = "HUMAN WIN";
            } else if (this.boardGame.winner == this.boardGame.AIPlayer) {
                gameResult = "AI WIN";
                AIWin++;
            }
        }

        this.paint(this.getGraphics());
    }//GEN-LAST:event_formMouseReleased

    String selectedAlgorithm = "AlphaBeta2a";

    void getAIMove() {
        this.lbStatus.setText("    Thinking...");
        this.paint(this.getGraphics());
        long startTime = System.currentTimeMillis();
        MiniMax = new Node(this.boardGame);
        MiniMax.rootPlayer = this.boardGame.currentPlayer;

        int maxDepth = 15;
        int remainingStone = this.boardGame.blueScore + this.boardGame.redScore;
        int sqrtSize = (int) (Math.sqrt(remainingStone));

        if (selectedAlgorithm.equals("AlphaBeta1")) {
            System.out.println("AlphaBeta1 is calculating...");
            if (remainingStone > 70) {
                maxDepth = 5;
            } else if (remainingStone >= 32) {
                maxDepth = 7;
            } else if (remainingStone > 20) {
                maxDepth = 9;
            }

            Node.runAlphaBeta(MiniMax, 3, -299999999, 299999999, true);
            System.out.println("AB1 Score root: " + MiniMax.value + " move: " + MiniMax.moveX + " " + MiniMax.moveY + " child " + MiniMax.nChild);
         //   MiniMax = new Node(this.boardGame);
        //    MiniMax.rootPlayer = this.boardGame.currentPlayer;
          //  Node.runMinimax(MiniMax, maxDepth, true);
         //   System.out.println("MinMax1 Score root: " + MiniMax.value + " move: " + MiniMax.moveX + " " + MiniMax.moveY + " child " + MiniMax.nChild);

        } else if (selectedAlgorithm.equals("AlphaBeta2")) {
            System.out.println("AlphaBeta2 is calculating...");
           if (remainingStone > 70) {
                maxDepth = 3;
            } else if (remainingStone >= 32) {
                maxDepth = 5;
            } else if (remainingStone > 20) {
                maxDepth = 7;
            }

            Node.runAlphaBeta2(MiniMax, maxDepth, -299999999, 299999999, true);
            System.out.println("AB2 Score root: " + MiniMax.value + " move: " + MiniMax.moveX + " " + MiniMax.moveY + " child " + MiniMax.nChild);
        } else if (selectedAlgorithm.equals("AlphaBeta2a")) {
            System.out.println("AlphaBeta2a is calculating...");
//
//              if (remainingStone > 70) {
//                maxDepth = 5;
//            } else if (remainingStone >= 32) {
//                maxDepth = 7;
//            } else if (remainingStone > 20) {
//                maxDepth = 9;
//            }
                if (remainingStone > 50) {
                    maxDepth = 3;
                } else if (remainingStone >= 32) {
                    maxDepth = 5;
                } else if (remainingStone > 20) {
                    maxDepth = 7;
                }

           Node.runAlphaBeta2a(MiniMax, maxDepth, -299999999, 299999999, true);
           System.out.println("AB2a Score root: " + MiniMax.value + " move: " + MiniMax.moveX + " " + MiniMax.moveY + " child " + MiniMax.nChild);
           MiniMax = new Node(this.boardGame);
            MiniMax.rootPlayer = this.boardGame.currentPlayer;
             Node.runMinimax2(MiniMax, maxDepth, true);
            System.out.println("MinMax2 Score root: " + MiniMax.value + " move: " + MiniMax.moveX + " " + MiniMax.moveY + " child " + MiniMax.nChild);
        } else if (selectedAlgorithm.equals("MCTS-Like")) {
            //MCTS
            if (remainingStone >= 32) {
                maxDepth = 3;
            } else if (remainingStone > 20) {
                maxDepth = 5;
            }

            System.out.println("MCTS-Like is calculating with depth  " + maxDepth + "...");

            Node.runMCTS(MiniMax, maxDepth, true);
            System.out.println("done MCTS");
        }

        sX = MiniMax.moveX;
        sY = MiniMax.moveY;
        System.out.println("AI move: " + sX + " " + sY);
        txtLogMove.setText(txtLogMove.getText() + " AI:  " + sX + " " + sY + "\n");

        try {
            // System.out.println("Thinking Time: " + estimatedTime + "ms");
            writer.write(this.boardGame.currentPlayer + " (" + sX + ", " + sY + ")\n");
        } catch (IOException ex) {
            Logger.getLogger(MainBoard.class.getName()).log(Level.SEVERE, null, ex);
        }

        this.boardGame.takeTurn(sX, sY);
       
        //moveAI++;

        if (this.boardGame.currentPlayer == TileType.RED) {
            this.lbPlayerTurn.setForeground(Color.RED);
            this.lbPlayerTurn.setText("Red Turn");
        } else {
            this.lbPlayerTurn.setForeground(new Color(1, 187, 234));
            this.lbPlayerTurn.setText("Blue Turn");
        }

//// check game Result
//        if (this.boardGame.winner != TileType.EMPTY) {
//             if (this.boardGame.isDraw())
//                 gameResult = "DRAW";
//            else if (this.boardGame.winner == this.boardGame.HumanPlayer) {
//                humanWin++;
//                gameResult = "HUMAN WIN";
//            } else if (this.boardGame.winner == this.boardGame.AIPlayer) {
//                gameResult = "AI WIN";
//                AIWin++;
//            }
//            
//            this.paint(this.getGraphics());
//            return;
//        }
        long estimatedTime = System.currentTimeMillis() - startTime;
//        
//       System.out.println(remainingStone + "," + maxDepth +"," + estimatedTime);
        this.lbStatus.setText("    Win: " + Math.round(Math.abs(MiniMax.chance * 100)) + "%  | Thinking Time: " + estimatedTime + "ms | Depth: " + maxDepth + " | Nodes: " + MiniMax.nChild + "");

    }

    private void btnNewGameActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnNewGameActionPerformed
//       this.dispose();
        try {
            // TODO add your handling code here:
            // create new game
            try {
                // TODO add your handling code here:
                if (this.boardGame.isDraw()) {
                    writer.write("Winner: DRAW\n");
                } else {
                    writer.write("Winner: " + this.boardGame.winner + "\n");
                }
                this.writer.close();
            } catch (IOException ex) {
                Logger.getLogger(MainBoard.class.getName()).log(Level.SEVERE, null, ex);
            }
            countGame++;

            readBoardConfiguration(this.fileName, isHumanRed, isHumanPlayFirst);
            this.paint(this.getGraphics());
        } catch (FileNotFoundException ex) {
            Logger.getLogger(MainBoard.class.getName()).log(Level.SEVERE, null, ex);
        }
    }//GEN-LAST:event_btnNewGameActionPerformed

    private void formWindowClosing(java.awt.event.WindowEvent evt) {//GEN-FIRST:event_formWindowClosing
        try {
            // TODO add your handling code here:
            if (this.boardGame.isDraw()) {
                writer.write("Winner: DRAW\n");
            } else {
                writer.write("Winner: " + this.boardGame.winner + "\n");
            }
            this.writer.close();
        } catch (IOException ex) {
            Logger.getLogger(MainBoard.class.getName()).log(Level.SEVERE, null, ex);
        }
    }//GEN-LAST:event_formWindowClosing

    private void drawIndexActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_drawIndexActionPerformed
        // TODO add your handling code here:
        this.paint(this.getGraphics());
    }//GEN-LAST:event_drawIndexActionPerformed

    private void selectAlgActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_selectAlgActionPerformed
        // TODO add your handling code here:
        if (this.selectAlg.getSelectedItem() == null) {
            return;
        }
        this.selectedAlgorithm = this.selectAlg.getSelectedItem().toString();
    }//GEN-LAST:event_selectAlgActionPerformed

    /**
     * @param args the command line arguments
     */
    public static void main(String args[]) {
        /* Set the Nimbus look and feel */
        //<editor-fold defaultstate="collapsed" desc=" Look and feel setting code (optional) ">
        /* If Nimbus (introduced in Java SE 6) is not available, stay with the default look and feel.
         * For details see http://download.oracle.com/javase/tutorial/uiswing/lookandfeel/plaf.html 
         */
        try {
            for (javax.swing.UIManager.LookAndFeelInfo info : javax.swing.UIManager.getInstalledLookAndFeels()) {
                if ("Nimbus".equals(info.getName())) {
                    javax.swing.UIManager.setLookAndFeel(info.getClassName());
                    break;
                }
            }
        } catch (ClassNotFoundException ex) {
            java.util.logging.Logger.getLogger(MainBoard.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (InstantiationException ex) {
            java.util.logging.Logger.getLogger(MainBoard.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (IllegalAccessException ex) {
            java.util.logging.Logger.getLogger(MainBoard.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (javax.swing.UnsupportedLookAndFeelException ex) {
            java.util.logging.Logger.getLogger(MainBoard.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        }
        //</editor-fold>

        /* Create and display the form */
        java.awt.EventQueue.invokeLater(new Runnable() {
            public void run() {

                MainBoard boardGameForm = null;
                try {

                    int size = 10; //Integer.parseInt(args[0]);
                    //System.out.println("b"+size+".txt");
                    boardGameForm = new MainBoard("boards/b" + size + "a.txt", true, true);
                } catch (IOException ex) {
                    Logger.getLogger(MainBoard.class.getName()).log(Level.SEVERE, null, ex);
                }

                boardGameForm.setVisible(true);

            }
        });
    }

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JButton btnNewGame;
    private javax.swing.JCheckBox drawIndex;
    private javax.swing.JLabel lbGameAIInfor;
    private javax.swing.JLabel lbGameHumanInfor;
    private javax.swing.JLabel lbPlayerTurn;
    private javax.swing.JLabel lbStatus;
    private javax.swing.JComboBox<String> selectAlg;
    // End of variables declaration//GEN-END:variables
}
