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
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.Scanner;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.imageio.ImageIO;
import javax.swing.Icon;
import javax.swing.ImageIcon;
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
    final BufferedImage redBlockHover;
    final BufferedImage blueBlockHover;
    final BufferedImage bgImg;
    final BufferedImage blankTile;
    final BufferedImage labelImg;
    String fileName ;
    int humanWin = 0;
    int AIWin = 0;

    boolean boardUpdate = false;
    int nSize = 0;
    int sX = -1;
    int sY = -1;
    int UNIT = 64;

    Node MiniMax;

    public MainBoard(String fileName, boolean isHumanRed, boolean isHumanPlayFirst) throws FileNotFoundException, IOException {
        initComponents();
        this.fileName = fileName;
        readBoardConfiguration(fileName);
        redBlock = ImageIO.read(new File("img/r64.png"));
        blueBlock = ImageIO.read(new File("img/b64.png"));
        redBlockHover = ImageIO.read(new File("img/r64_hover.png"));
        blueBlockHover = ImageIO.read(new File("img/b64_hover.png"));
        blankTile = ImageIO.read(new File("img/blankTile.png"));
        bgImg = ImageIO.read(new File("img/bg3.png"));
        labelImg = ImageIO.read(new File("img/blankLabel.png"));
        this.nSize = this.boardGame.nSize;
        this.setSize(nSize * UNIT + marginX * 3 + 142, nSize * UNIT + (marginY *2));
        // init label
        int dx = nSize * UNIT + marginX * 2;
        int dy = 1 * UNIT;
        
        if (!isHumanRed){
            this.lbGameHumanInfor.setForeground(new Color(1,187,234));
            this.lbGameHumanInfor.setText("Human-BLUE 0");
            
            this.lbGameAIInfor.setForeground(new Color(255,33,66));
            this.lbGameAIInfor.setText("AI-RED 0");
        }
     

        // draw background once
        // MiniMax = new Node(this.boardGame);
    }

    public void readBoardConfiguration(String fileName) throws FileNotFoundException {
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
        this.boardGame = new Board(n, config);
        
        this.lbPlayerTurn.setText("Red Turn");
        this.lbGameAIInfor.setText("- "+AIWin+" AI-BLUE");
        this.lbGameHumanInfor.setText("Human-RED "+humanWin);
        
          
        // AI move first
        if(this.boardGame.currentPlayer== this.boardGame.AIPlayer) {
            getAIMove();
        }
        this.nSize = this.boardGame.nSize;
        this.setSize(nSize * UNIT + marginX * 3 + 142, nSize * UNIT + (marginY *2));
     }

    int marginX = 32;
    int marginY = 32;

    public void paint(Graphics g) {
        super.paint(g);

        //g.drawImage(bgImg, 0, 40, nSize*UNIT + marginX*3 + 142  , nSize*UNIT + marginY*2,this);
        if (this.boardGame != null) {
            int n = this.boardGame.nSize;

            for (int i = 0; i < n; i++) {
                for (int j = 0; j < n; j++) {
                    int dx = i * 64 + marginX;
                    int dy = j * 64 + marginY;
                    g.drawImage(blankTile, dx, dy, null);
                    if (this.boardGame.board[i][j] == TileType.BLUE) {
                        g.drawImage(blueBlock, dx, dy, null);
                    } else if (this.boardGame.board[i][j] == TileType.RED) {
                        g.drawImage(redBlock, dx, dy, null);
                    }

                }
            }

        }
        if (!(sX < 0 || sY < 0 || sX >= this.boardGame.nSize || sY >= this.boardGame.nSize)) {
            int dx = sX * 64 + marginX;
            int dy = sY * 64 + marginY;
            if (this.boardGame.board[sX][sY] == TileType.RED) {
                g.drawImage(redBlockHover, dx, dy, null);
            } else if (this.boardGame.board[sX][sY] == TileType.BLUE) {
                g.drawImage(blueBlockHover, dx, dy, null);
            }
        }

        // set fopnt
        g.setFont(new Font("default", Font.PLAIN, 32));

        int dx = nSize * UNIT + marginX * 2;
        int dy = 1 * UNIT;
        g.drawImage(labelImg, dx, dy, null);
        //draw scre
        g.setColor(Color.red);
        g.drawString(this.boardGame.redScore + "", dx + 55, dy + 45);
        g.drawImage(labelImg, dx, (this.nSize - 1) * UNIT, null);
        g.setColor(Color.blue);
        g.drawString(  this.boardGame.blueScore + "", dx + 55, (this.nSize - 1) * UNIT + 45);

        g.setFont(new Font("default", Font.PLAIN, 50));
        dx = (this.nSize / 2) * UNIT;
        if (this.boardGame.winner == TileType.RED) {
            g.setColor(Color.red);
            g.drawString(gameResult, dx-30, 50+dx);
        } else if (this.boardGame.winner == TileType.BLUE) {
            g.setColor(Color.blue);
            g.drawString(gameResult, dx-30, 50+dx);
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

        lbPlayerTurn.setText("Red Turn");

        btnNewGame.setText("New Game");
        btnNewGame.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnNewGameActionPerformed(evt);
            }
        });

        lbGameAIInfor.setFont(new java.awt.Font("Ubuntu", 1, 15)); // NOI18N
        lbGameAIInfor.setForeground(new java.awt.Color(1, 187, 234));
        lbGameAIInfor.setText("- 0 AI-BLUE");

        lbGameHumanInfor.setFont(new java.awt.Font("Ubuntu", 1, 15)); // NOI18N
        lbGameHumanInfor.setForeground(new java.awt.Color(255, 33, 66));
        lbGameHumanInfor.setText("Human-RED 0");

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(lbPlayerTurn)
                .addGap(18, 18, 18)
                .addComponent(lbGameHumanInfor)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(lbGameAIInfor)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, 379, Short.MAX_VALUE)
                .addComponent(btnNewGame)
                .addGap(55, 55, 55))
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                        .addComponent(lbPlayerTurn)
                        .addComponent(lbGameAIInfor)
                        .addComponent(lbGameHumanInfor))
                    .addComponent(btnNewGame))
                .addContainerGap(729, Short.MAX_VALUE))
        );

        pack();
        setLocationRelativeTo(null);
    }// </editor-fold>//GEN-END:initComponents

    private void formMouseMoved(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_formMouseMoved
        // TODO add your handling code here:
        double mouseX = MouseInfo.getPointerInfo().getLocation().x - this.getLocationOnScreen().x;

        double mouseY = MouseInfo.getPointerInfo().getLocation().y - this.getLocationOnScreen().y;
        sX = (int) (mouseX - marginX) / 64;
        sY = (int) (mouseY - marginY) / 64;

        this.paint(this.getGraphics());
        

    }//GEN-LAST:event_formMouseMoved
    String gameResult = "";
    private void formMouseReleased(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_formMouseReleased
       

            if (!(this.boardGame.isValidMove(sX, sY))) {
                System.out.println("Invalid Move");
                return;
            }

            this.boardGame.takeTurn(sX, sY);
            //moveHuman++;

            System.out.println("Player move: " + sX + " " + sY);
            if (this.boardGame.winner != TileType.EMPTY) {
                if (this.boardGame.winner == this.boardGame.HumanPlayer) {
                    humanWin++;
                    gameResult = "HUMAN WIN";
                } else {
                    gameResult = "AI WIN";
                    AIWin++;
                }
                return;
            }

            this.paint(this.getGraphics());
//            try {
//                Thread.sleep(1000);
//            } catch (InterruptedException ex) {
//                Logger.getLogger(MainBoard.class.getName()).log(Level.SEVERE, null, ex);
//            }

            // AI turn
            getAIMove();
            
    }//GEN-LAST:event_formMouseReleased

    void getAIMove(){
        long startTime = System.currentTimeMillis();
            MiniMax = new Node(this.boardGame);
            MiniMax.rootPlayer = this.boardGame.currentPlayer;
            
            int maxDepth = 15;
            int remainingStone = this.boardGame.blueScore+this.boardGame.redScore;
            int sqrtSize = (int) ( Math.sqrt(remainingStone));
            // FIX IT LATER
            switch (sqrtSize) {
                case 10:
                case 9: 
                case 8:{maxDepth = 3; break;}
                case 7:{maxDepth = 3; break;}
                case 6: {maxDepth = 5; break;}
                case 5:{maxDepth = 7; break;}
            }
            
            System.out.println(remainingStone+ " " + sqrtSize + " " + maxDepth );
            Node.runMinimax(MiniMax, maxDepth , true);
            sX = MiniMax.moveX;
            sY = MiniMax.moveY;
            System.out.println("AI move: " + sX + " " + sY);
            System.out.println("Score Root: " + (MiniMax.value));
            this.boardGame.takeTurn(sX, sY);
            //moveAI++;
            
            if (this.boardGame.currentPlayer==TileType.RED) {
                this.lbPlayerTurn.setText("Red Turn");
            } else {
                this.lbPlayerTurn.setText("Blue Turn");
            }

// check game Result
            if (this.boardGame.winner != TileType.EMPTY) {
                if (this.boardGame.winner == this.boardGame.HumanPlayer) {
                    gameResult = "HUMAN WIN";
                    humanWin++;
                } else {
                    gameResult = "AI WIN";
                    AIWin++;
                }
                return;
            }
            long estimatedTime = System.currentTimeMillis() - startTime;
            System.out.println("Thinking Time: " + estimatedTime +"ms");
            this.paint(this.getGraphics());
//        try {
//            Thread.sleep(1000);
//        } catch (InterruptedException ex) {
//            Logger.getLogger(MainBoard.class.getName()).log(Level.SEVERE, null, ex);
//        }

    }
    
    private void btnNewGameActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnNewGameActionPerformed
        try {
            // TODO add your handling code here:
            // create new game
            readBoardConfiguration(this.fileName);
        } catch (FileNotFoundException ex) {
            Logger.getLogger(MainBoard.class.getName()).log(Level.SEVERE, null, ex);
        }
    }//GEN-LAST:event_btnNewGameActionPerformed

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

                    int size = 4; //Integer.parseInt(args[0]);
                    //System.out.println("b"+size+".txt");
                    boardGameForm = new MainBoard("b" + size + ".txt",true,true);
                } catch (IOException ex) {
                    Logger.getLogger(MainBoard.class.getName()).log(Level.SEVERE, null, ex);
                }

                boardGameForm.setVisible(true);

            }
        });
    }

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JButton btnNewGame;
    private javax.swing.JLabel lbGameAIInfor;
    private javax.swing.JLabel lbGameHumanInfor;
    private javax.swing.JLabel lbPlayerTurn;
    // End of variables declaration//GEN-END:variables
}