package game;

import java.awt.Color;
import java.awt.Font;
import java.awt.Point;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.util.Random;

import javax.swing.Timer;
import clientSocket.client;
import javax.swing.ImageIcon;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import ghost.*;
import dataStructures.list;

public class game {
    JPanel gamePanel; 
    JPanel ground = new JPanel();  
    JFrame window;
    JLabel blockMatrix[][];
    JLabel pacManJLabel; 
    int matrix[][]; 
   

    boolean up    = false; 
    boolean down  = false; 
    boolean left  = false;
    boolean right = false; 

    client moveClient = new client(12345);
    client generalClient = new client(12345);
    client ghostOneClient = new client(12345);  
    client ghostTwoClient = new client(12345);  
    client ghostThreeClient = new client(12345);  
    client ghostFourClient = new client(12345); 
    client fruitClient = new client(12345);

    JLabel pointsLb;
    String points = "0";

    JLabel heartLb; 
    JLabel lifesLb;

    object ghostOne = new ghost ("1", ground); 
    object ghostTwo = new ghost ("2", ground); 
    object ghostThree = new ghost ("3", ground); 
    object ghostFour = new ghost ("4", ground); 
    list ghostOneRoute = new list();
    list ghostTwoRoute = new list();
    list ghostThreeRoute = new list();
    list ghostFourRoute = new list();




    game(JFrame window, int width, int height){
    //---------General Configurations--------//
        this.window=window;
        gamePanel = new JPanel();     
        gamePanel.setLayout(null);
        gamePanel.setBounds(0,0, width, height);
        gamePanel.setVisible(true);
        gamePanel.setBackground(Color.BLUE);
   
    
        
        ground.setLayout(null);
        ground.setBounds(39,35, 360,360);
        ground.setVisible(true);
        ground.setBackground(Color.BLACK);

        heartLb = new JLabel(); 
        heartLb.setIcon(new ImageIcon("PACMAN\\src\\resources\\heart (2).png")); 
        heartLb.setBounds(40,5, 24, 21); 
        heartLb.setVisible(true);
        gamePanel.add(heartLb); 

        lifesLb = new JLabel();
        lifesLb.setBounds(width / 2 - 160, 3, 50, 24);
        lifesLb.setText("0");
        lifesLb.setVisible(true); 
        gamePanel.add(lifesLb); 
   

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

        
        ghostOne.setImage();
        ghostOne.addLabel();
        ghostOne.move(336, 336);

        ghostTwo.setImage();
        ghostTwo.addLabel();
        ghostTwo.move(312, 336);

        ghostThree.setImage();
        ghostThree.addLabel();
        ghostThree.move(288, 336);

        ghostFour.setImage();
        ghostFour.addLabel();
        ghostFour.move(264, 336);

        

    //-------------Score Label-----------------//
    

        pointsLb = new JLabel("Score: 0");
        pointsLb.setBounds(width / 2 - 30, 0, 250, 24);
        pointsLb.setVisible(true);
        gamePanel.add(pointsLb);
        
        Font font = new Font("Arial", Font.BOLD, 16); 
        pointsLb.setFont(font);
        lifesLb.setFont(font); 


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
        showLife();
        move(); 
        moveGhostOne();
        moveGhostTwo();
        moveGhostThree();
        moveGhostFour();
        fruitGenerator();

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
    
    //----------------------------------------//
    ////////////////////////////////////////////
    ////////////Player functionalities/////////
    ///////////////////////////////////////////
    
    void showLife(){
        Timer timer = new Timer(200, new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                generalClient.sendMessage("o "+ points);
                lifesLb.setText(generalClient.response);
            }
        });
        timer.start();
    }
    
    void showPoints(){
        Timer timer = new Timer(200, new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                pointsLb.setText("Score :"+ points);
            }
        });
        timer.start();
    }

    void fruitGenerator(){
        Timer timer = new Timer(10000, new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                generateFruit();
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

                    checkPoints(pacX+1, pacY); 

                }
                else if(left && checkBlockedPosition(2)){
                    Point pacManLocation = pacManJLabel.getLocation();
                    pacManJLabel.setBounds(pacManLocation.x-24, pacManLocation.y, 24,24); 

                    int pacX = pacManLocation.x/24; 
                    int pacY = pacManLocation.y/24;

                    checkPoints(pacX-1, pacY); 

                }
                else if(up && checkBlockedPosition(3)){
                    Point pacManLocation = pacManJLabel.getLocation();
                    pacManJLabel.setBounds(pacManLocation.x, pacManLocation.y-24, 24,24); 

                    int pacX = pacManLocation.x/24; 
                    int pacY = pacManLocation.y/24;

                    checkPoints(pacX, pacY-1); 

                }
                else if(down && checkBlockedPosition(4)){
                    Point pacManLocation = pacManJLabel.getLocation();
                    pacManJLabel.setBounds(pacManLocation.x, pacManLocation.y+24, 24,24); 

                    int pacX = pacManLocation.x/24; 
                    int pacY = pacManLocation.y/24;

                    checkPoints(pacX, pacY+1); 
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
                up    = true; 
                down  = false; 
                left  = false; 
                right = false;
            }
            
            if(e.getKeyCode() == KeyEvent.VK_S){
                up    = false; 
                down  = true; 
                left  = false; 
                right = false;
            }
            
            if(e.getKeyCode() == KeyEvent.VK_D){
                up    = false; 
                down  = false; 
                left  = false; 
                right = true;
            }
            
            if(e.getKeyCode() == KeyEvent.VK_A){
                
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
        this.generalClient.sendMessage("S " +pacY+ " "+ pacX);
        if (!this.generalClient.response.contentEquals("empty")){
            blockMatrix[pacY][pacX].setIcon(new ImageIcon("PACMAN\\src\\resources\\image-1.png"));
            points = generalClient.response; 
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
    
    //-_-_-_-_-_-_-_-_-_-_-_-_-_-_-_-_-_-_-_-_-//
    //--_-_-_-_-_Logique des cerises-_-_-_-_--_//
    //-_-_-_-_-_-_-_-_-_-_-_-_-_-_-_-_-_-_-_-_-//

    private void generateFruit(){
        boolean posFoundF =  false;

        //System.out.println("A fruit will be generated !!");
        while (posFoundF == false) {
            int randomID = genRandNum(0, 224);
            int xCoord = convertX(randomID);
            int yCoord = convertY(randomID);
            if(checkAvailablePos(yCoord, xCoord)){
                posFoundF = true;
            }
        }
    }

    public int genRandNum(int min, int max) {
        Random rand = new Random();
        int randomID = rand.nextInt(max - min + 1) + min;
        //System.out.println("Rand num ! " + Integer.toString(randomID));
        return randomID; 
    }
    
    private int convertY(int id){
        int posY = id/15;
        return posY;
    }

    private int convertX(int id){
        int posX = (id%15);
        return posX; 
    }

    private boolean checkAvailablePos(int coordX, int coordY){
        if(matrix[coordY][coordX] != 1){
            this.fruitClient.sendMessage("F " + coordY + " " + coordX);
            if (!this.fruitClient.response.contentEquals("empty")){
                blockMatrix[coordY][coordX].setIcon(new ImageIcon("PACMAN\\src\\resources\\image3.jpg"));
                //System.out.println("New fruit added in cords" + Integer.toString(coordY) + Integer.toString(coordX));
                return true;
            }
        }
        return false; 
    }
    
    //----------------------------------------//
    //----------------------------------------//
    ////////////////////////////////////////////
    ////////////Ghost functionalities//////////
    //////////////////////////////////////////

    int convertToId(int X, int Y){
        return X%15+Y*15; 
    }

    void moveGhostOne(){
        Timer timer = new Timer(300, new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                Point ghostLocation = ghostOne.getLocation(); 
                
                int ghostX  = ghostLocation.x/24; 
                int ghostY  = ghostLocation.y/24; 
                int ghostID = convertToId(ghostX, ghostY); 
                //System.out.println(ghostID); 
                ghostOneClient.sendMessage("g "+ghostID);
                if (ghostOneClient.response.contentEquals("true")){
                    Point pacManLocation = pacManJLabel.getLocation(); 

                    int pacX = pacManLocation.x/24;
                    int pacY = pacManLocation.y/24;
                    int pacManID = convertToId(pacX, pacY);

                    if (pacManID != ghostID){

                        ghostOneClient.sendMessage("p 1 "+ghostID+" "+pacManID);
                        ghostOneRoute.convertStringToList(ghostOneClient.response);
                        //System.out.println(ghostOneClient.response);
                    }
                }else{
                    if(ghostOneRoute.head!= null){
                        int id = ghostOneRoute.head.id; 
                        int newX = id%15*24; 
                        int newY = id/15*24; 

                        //System.out.println(newX+" "+ newY);
                        ghostOne.move(newX, newY);
                        ghostOneRoute.head = ghostOneRoute.head.next; 
                    }
                }
            }
        });
        timer.start();
    }

    void moveGhostTwo(){

        Timer timer = new Timer(350, new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                Point ghostLocation = ghostTwo.getLocation(); 
                
                int ghostX  = ghostLocation.x/24; 
                int ghostY  = ghostLocation.y/24; 
                int ghostID = convertToId(ghostX, ghostY); 
                //System.out.println(ghostID); 
                ghostTwoClient.sendMessage("G "+ghostID);
                if (ghostTwoClient.response.contentEquals("true")){
                    Point pacManLocation = pacManJLabel.getLocation(); 

                    int pacX = pacManLocation.x/24;
                    int pacY = pacManLocation.y/24;
                    
                    int pacManID = convertToId(pacX, pacY);

                    if (pacManID != ghostID){

                        ghostTwoClient.sendMessage("p 2 "+ghostID+" "+pacManID);
                        ghostTwoRoute.convertStringToList(ghostTwoClient.response);
                        //System.out.println(ghostOneClient.response);
                    }
                }else{
                    if(ghostTwoRoute.head!= null){
                        int id = ghostTwoRoute.head.id; 
                        int newX = id%15*24; 
                        int newY = id/15*24; 

                        //System.out.println(newX+" "+ newY);
                        ghostTwo.move(newX, newY);
                        ghostTwoRoute.head = ghostTwoRoute.head.next; 
                    }
                }
            }
        });
        timer.start();

    }

    void moveGhostThree(){

        Timer timer = new Timer(350, new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                Point ghostLocation = ghostThree.getLocation(); 
                
                int ghostX  = ghostLocation.x/24; 
                int ghostY  = ghostLocation.y/24; 
                int ghostID = convertToId(ghostX, ghostY); 
                System.out.println(ghostID); 
                ghostThreeClient.sendMessage("h "+ghostID);
                System.out.println(ghostID); 
                if (ghostThreeClient.response.contentEquals("true")){
                    Point pacManLocation = pacManJLabel.getLocation(); 

                    int pacX = pacManLocation.x/24;
                    int pacY = pacManLocation.y/24;
                    
                    int pacManID = convertToId(pacX, pacY);

                    if (pacManID != ghostID){
                        ghostThreeClient.sendMessage("p 3 "+ghostID+" "+pacManID);
                        ghostThreeRoute.convertStringToList(ghostThreeClient.response);
                      
                    }
                }else{
                    if(ghostThreeRoute.head!= null){
                        int id = ghostThreeRoute.head.id; 
                        int newX = id%15*24; 
                        int newY = id/15*24; 

                        System.out.println(newX+" "+ newY);
                        ghostThree.move(newX, newY);
                        ghostThreeRoute.head = ghostThreeRoute.head.next; 
                    }
                }
            }
        });
        timer.start();

    }

     void moveGhostFour(){

        Timer timer = new Timer(250, new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                Point ghostLocation = ghostFour.getLocation(); 
                
                int ghostX  = ghostLocation.x/24; 
                int ghostY  = ghostLocation.y/24; 
                int ghostID = convertToId(ghostX, ghostY); 
                System.out.println(ghostID); 
                ghostFourClient.sendMessage("H "+ghostID);
                System.out.println(ghostFourClient.response); 
                if (ghostFourClient.response.contentEquals("true")){
                    Point pacManLocation = pacManJLabel.getLocation(); 

                    int pacX = pacManLocation.x/24;
                    int pacY = pacManLocation.y/24;
                    
                    int pacManID = convertToId(pacX, pacY);

                    if (pacManID != ghostID){
                        ghostFourClient.sendMessage("p 4 "+ghostID+" "+pacManID);
                        ghostFourRoute.convertStringToList(ghostFourClient.response);
                        System.out.println(ghostFourClient.response); 
                    }
                }else{
                    if(ghostFourRoute.head!= null){
                        int id = ghostFourRoute.head.id; 
                        int newX = id%15*24; 
                        int newY = id/15*24; 

                        System.out.println(newX+" "+ newY);
                        ghostFour.move(newX, newY);
                        ghostFourRoute.head = ghostFourRoute.head.next; 
                    }
                }
            }
        });
        timer.start();

    }
}
