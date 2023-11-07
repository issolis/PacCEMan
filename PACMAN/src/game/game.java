package game;

import java.awt.Color;
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

    client client = new client();


    game(JFrame window, int width, int height){
    //---------General Configurations--------//
        this.window=window;
        gamePanel = new JPanel();     
        gamePanel.setLayout(null);
        gamePanel.setBounds(0,0, width, height);
        gamePanel.setVisible(true);
        gamePanel.setBackground(Color.BLUE);
    //--------------------------------------//
    
        ground = new JPanel(); 
        ground.setLayout(null);
        ground.setBounds(39,35, 360,360);
        ground.setVisible(true);
        ground.setBackground(Color.BLACK);

        pacManJLabel = new JLabel(); 
        pacManJLabel.setIcon(new ImageIcon("PACMAN\\src\\resources\\image2.png"));
        pacManJLabel.setBounds(24,24,24,24); 
        pacManJLabel.setVisible(true); 
        ground.add(pacManJLabel); 
    
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

        direcction();
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
    
    void move (){
        Timer timer = new Timer(200, new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                if (right && checkBlockedPosition(1)){
                    Point pacManLocation = pacManJLabel.getLocation();
                    pacManJLabel.setBounds(pacManLocation.x+24, pacManLocation.y, 24,24); 

                    int pacX = pacManLocation.x/24; 
                    int pacY = pacManLocation.y/24;

                    blockMatrix[pacY][pacX].setIcon(new ImageIcon("PACMAN\\src\\resources\\image-1.png"));
                }
                else if(left && checkBlockedPosition(2)){
                    Point pacManLocation = pacManJLabel.getLocation();
                    pacManJLabel.setBounds(pacManLocation.x-24, pacManLocation.y, 24,24); 

                    int pacX = pacManLocation.x/24; 
                    int pacY = pacManLocation.y/24;

                    blockMatrix[pacY][pacX].setIcon(new ImageIcon("PACMAN\\src\\resources\\image-1.png"));

                }
                else if(up && checkBlockedPosition(3)){
                    Point pacManLocation = pacManJLabel.getLocation();
                    pacManJLabel.setBounds(pacManLocation.x, pacManLocation.y-24, 24,24); 

                    int pacX = pacManLocation.x/24; 
                    int pacY = pacManLocation.y/24;

                    blockMatrix[pacY][pacX].setIcon(new ImageIcon("PACMAN\\src\\resources\\image-1.png"));

                }
                else if(down && checkBlockedPosition(4)){
                    Point pacManLocation = pacManJLabel.getLocation();
                    pacManJLabel.setBounds(pacManLocation.x, pacManLocation.y+24, 24,24); 

                    int pacX = pacManLocation.x/24; 
                    int pacY = pacManLocation.y/24;

                    blockMatrix[pacY][pacX].setIcon(new ImageIcon("PACMAN\\src\\resources\\image-1.png"));
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
            this.client.sendMessage("R " +pacY+ " "+ pacX);
            if (this.client.response.contentEquals("false"))
                return false; 
        }
        if (direction == 2){
            this.client.sendMessage("L " +pacY+ " "+ pacX);
            if (this.client.response.contentEquals("false"))
                return false; 
        }
        if (direction == 3){
            this.client.sendMessage("U " +pacY+ " "+ pacX);
            if (this.client.response.contentEquals("false"))
                return false; 
        }
        if (direction == 4){
            this.client.sendMessage("D " +pacY+ " "+ pacX);
            if (this.client.response.contentEquals("false"))
                return false; 
        }
        
        return true; 
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
