package game;

import java.awt.Color;
import java.awt.Font;
import java.awt.Point;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import javax.swing.Timer;
import clientSocket.client;
import javax.swing.ImageIcon;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import ghost.*;

public class game {
    JPanel gamePanel; 
    JPanel ground; 
    JFrame window;
    JLabel blockMatrix[][];
    JLabel pacManJLabel; 
    int matrix[][]; 

    boolean up    = false; 
    boolean down  = false; 
    boolean left  = false;
    boolean right = false; 

    client moveClient = new client();
    client pointsClient = new client();

    JLabel pointsLb;

    String points = "0";


    object ghostOne; 


    game(JFrame window, int width, int height){
    //---------General Configurations--------//
        this.window=window;
        gamePanel = new JPanel();     
        gamePanel.setLayout(null);
        gamePanel.setBounds(0,0, width, height);
        gamePanel.setVisible(true);
        gamePanel.setBackground(Color.BLUE);
   
    
        ground = new JPanel(); 
        ground.setLayout(null);
        ground.setBounds(39,35, 360,360);
        ground.setVisible(true);
        ground.setBackground(Color.BLACK);
    //--------------------------------------//
    /////////////////////////////////////////
    ///////////////PAC MAN PLAYER////////////
    /////////////////////////////////////////
    //--------------------------------------//

        pacManJLabel = new JLabel(); 
        pacManJLabel.setIcon(new ImageIcon("PACMAN\\src\\resources\\image2.png"));
        pacManJLabel.setBounds(24,24,24,24); 
        pacManJLabel.setVisible(true); 
        ground.add(pacManJLabel); 

    //--------------------------------------//
    /////////////////////////////////////////
    ///////////////GHOSTS////////////////////
    /////////////////////////////////////////
    //--------------------------------------//

        ghostOne = new ghost ("1", ground); 
        ghostOne.setImage();
        ghostOne.addLabel();
        ghostOne.move(336, 336);

    //-------------Score Label-----------------//
    

        pointsLb = new JLabel("Score: 0");
        pointsLb.setBounds(width / 2 - 30, 0, 100, 24);
        pointsLb.setVisible(true);
        gamePanel.add(pointsLb);
        
        Font font = new Font("Arial", Font.BOLD, 16); 
        pointsLb.setFont(font);


     //--------------------------------------//
     //-------------Start game---------------//
    
        matrix = table();
        blockMatrix = new JLabel[15][15];  

        for (int i = 0; i < matrix.length; i++) {
            for (int j = 0; j < matrix.length; j++) {
                blockMatrix[i][j] = new JLabel();
                blockMatrix[i][j].setIcon(new ImageIcon("PACMAN\\src\\resources\\image" +  matrix[i][j]  + ".png"));
                blockMatrix[i][j].setBounds(j * 24, i * 24, 24, 24);
                blockMatrix[i][j].setVisible(true);
                ground.add(blockMatrix[i][j]);
            }
        }

        this.moveClient.sendMessage("N");
        direcction();
        showPoints();
        move(); 

        gamePanel.add(ground);
        window.add(gamePanel);
        window.setFocusable(true);
        window.requestFocusInWindow();
        window.setResizable(false); 
    }

    int [][] table(){

        int[][] maze = {
            {0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0},
            {0, 0, 0, 0, 1, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0},
            {0, 0, 1, 0, 1, 1, 1, 0, 1, 1, 1, 0, 1, 0, 0},
            {0, 0, 1, 0, 0, 0, 1, 0, 1, 0, 1, 0, 1, 0, 0},
            {0, 0, 1, 0, 1, 0, 1, 0, 0, 0, 1, 0, 1, 0, 0},
            {0, 0, 1, 1, 1, 0, 1, 1, 1, 0, 1, 1, 1, 0, 0},
            {0, 0, 0, 0, 0, 0, 0, 0, 1, 0, 0, 0, 0, 0, 0},
            {0, 1, 1, 0, 1, 1, 1, 0, 1, 1, 1, 1, 1, 0, 0},
            {0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0},
            {0, 0, 1, 1, 1, 1, 0, 1, 1, 1, 1, 1, 1, 0, 0},
            {0, 0, 0, 0, 0, 1, 0, 1, 0, 0, 0, 0, 0, 0, 0},
            {0, 1, 1, 1, 0, 1, 0, 1, 0, 1, 1, 1, 1, 1, 0},
            {0, 0, 0, 0, 0, 0, 0, 1, 0, 0, 0, 0, 0, 0, 0},
            {0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0},
            {0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0}
        };
        return maze; 
    }
    
    void showPoints(){
        Timer timer = new Timer(200, new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                pointsLb.setText("Score :"+ points);
            }
        });
        timer.start();
    }

    void move (){
        Timer timer = new Timer(200, new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                if (right && checkBlockedPosition(1)){
                    Point pacManLocation = pacManJLabel.getLocation();
                    pacManJLabel.setBounds(pacManLocation.x+24, pacManLocation.y, 24,24);

                    int pacX = pacManLocation.x/24; 
                    int pacY = pacManLocation.y/24;

                    checkPoints(pacX, pacY); 

                }
                else if(left && checkBlockedPosition(2)){
                    Point pacManLocation = pacManJLabel.getLocation();
                    pacManJLabel.setBounds(pacManLocation.x-24, pacManLocation.y, 24,24); 

                    int pacX = pacManLocation.x/24; 
                    int pacY = pacManLocation.y/24;

                    checkPoints(pacX, pacY); 

                }
                else if(up && checkBlockedPosition(3)){
                    Point pacManLocation = pacManJLabel.getLocation();
                    pacManJLabel.setBounds(pacManLocation.x, pacManLocation.y-24, 24,24); 

                    int pacX = pacManLocation.x/24; 
                    int pacY = pacManLocation.y/24;

                    checkPoints(pacX, pacY); 

                }
                else if(down && checkBlockedPosition(4)){
                    Point pacManLocation = pacManJLabel.getLocation();
                    pacManJLabel.setBounds(pacManLocation.x, pacManLocation.y+24, 24,24); 

                    int pacX = pacManLocation.x/24; 
                    int pacY = pacManLocation.y/24;

                    checkPoints(pacX, pacY); 
                }

            }

        });
        timer.start();
    }
   
    void direcction(){
        window.addKeyListener(new KeyListener() {

        @Override
        public void keyTyped(KeyEvent e) {
        }

        @Override
        public void keyPressed(KeyEvent e) {
            if(e.getKeyCode() == KeyEvent.VK_W){
                System.out.println("Arriba");
                up    = true; 
                down  = false; 
                left  = false; 
                right = false;
            }
            
            if(e.getKeyCode() == KeyEvent.VK_S){
                System.out.println("Abajo");
                up    = false; 
                down  = true; 
                left  = false; 
                right = false;
            }
            
            if(e.getKeyCode() == KeyEvent.VK_D){
                System.out.println("Derecha");
                up    = false; 
                down  = false; 
                left  = false; 
                right = true;
            }
            
            if(e.getKeyCode() == KeyEvent.VK_A){
                System.out.println("Izqueirda");
                up    = false; 
                down  = false; 
                left  = true; 
                right = false;
            }
        }

        @Override
        public void keyReleased(KeyEvent e) {
        
        }
            
        });
    }
    
    boolean checkBlockedPosition(int direction){
        Point pacManPoint = pacManJLabel.getLocation(); 

        int pacY = pacManPoint.x/24; 
        int pacX = pacManPoint.y/24;

        

        if (direction == 1){
            this.moveClient.sendMessage("R " +pacY+ " "+ pacX);
            if (this.moveClient.response.contentEquals("false"))
                return false; 
        }
        if (direction == 2){
            this.moveClient.sendMessage("L " +pacY+ " "+ pacX);
            if (this.moveClient.response.contentEquals("false"))
                return false; 
        }
        if (direction == 3){
            this.moveClient.sendMessage("U " +pacY+ " "+ pacX);
            if (this.moveClient.response.contentEquals("false"))
                return false; 
        }
        if (direction == 4){
            this.moveClient.sendMessage("D " +pacY+ " "+ pacX);
            if (this.moveClient.response.contentEquals("false"))
                return false; 
        }
        
        return true; 
    }
    
    void checkPoints(int pacX, int pacY){
        this.pointsClient.sendMessage("S " +pacY+ " "+ pacX);
        if (!this.pointsClient.response.contentEquals("empty")){
            blockMatrix[pacY][pacX].setIcon(new ImageIcon("PACMAN\\src\\resources\\image-1.png"));
            points = pointsClient.response; 
        }
    }

    void drawMatrix(){
        for (int i = 0; i < matrix.length; i++) {
            for (int j = 0; j < matrix.length; j++) {
                blockMatrix[i][j].setIcon(new ImageIcon("PACMANJAVA\\src\\resources\\image" + matrix[i][j] + ".png"));
                blockMatrix[i][j].setBounds(j * 24, i * 24, 24, 24);
            }
        }
    }

}
