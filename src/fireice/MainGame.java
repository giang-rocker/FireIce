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
public class MainGame {
    public static void main(String [] args) throws IOException, InterruptedException
	{
            
            ConfigForm config = new ConfigForm();
            config.setVisible(true);
            boolean isHumanPlayFirst = false;
            boolean isHumanRed = false;
            String fileName = "";
            
            while (config.closed == false) {
            isHumanPlayFirst = config.isHumanPlayFirst;
            isHumanRed= config.isHumanRed;
            fileName = config.selectedFile;
            
                Thread.sleep(200);
            
            }
           if (isHumanRed && isHumanPlayFirst)
                System.out.println("Human is Red &  Human Plays First");
           if (isHumanRed && !isHumanPlayFirst)
                System.out.println("Human is Red & AI plays First");
           if (!isHumanRed && isHumanPlayFirst)
                System.out.println("Human is Blue & Human Plays First");
           if (!isHumanRed && !isHumanPlayFirst)
                System.out.println("Human is Blue & AI Plays First");
           
           
            MainBoard mainBoard = new MainBoard("boards/"+fileName,isHumanRed,isHumanPlayFirst);
            mainBoard.setVisible(true);
            }
         
}
