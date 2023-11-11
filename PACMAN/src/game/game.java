package game;

import java.awt.Color;
import java.awt.Font;
import java.awt.Point;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.util.Random;
import java.util.TimerTask;

import javax.swing.Timer;
import clientSocket.client;
import javax.swing.ImageIcon;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;

import dataStructures.list;
import dataStructures.listPanel;
import dataStructures.nodePanel;
import processData.*;  
import object.*;

public class game {
    JPanel gamePanel; 
    JPanel parent; 
    JPanel ground = new JPanel();  
    JFrame window;
    int width; 
    int height;
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
    client powerUpClient = new client(12345);
    client winnerClient = new client(12345);

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

    boolean ghostOneEated   = false;
    boolean ghostTwoEated   = false;
    boolean ghostThreeEated = false;
    boolean ghostFourEated  = false;
    boolean lose = false;



    listPanel fruitList = new listPanel(); 
    listPanel powerUpList = new listPanel(); 

    boolean againstPacMan = true;
    boolean attackPos = true;

    int type; 
    int typeView = 1;
    boolean hasLose = false; 

    ArduinoSerialReader arduinoReader = new ArduinoSerialReader();
    
    game(JFrame window, JPanel parent, int width, int height, int type, boolean match){
    //---------General Configurations--------//
        this.window =window;
        this.parent = parent; 
        this.width  =width;
        this.height = height;
        this.type = type; 

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
   
    if (match){

        
    //--------------------------------------//
    /////////////////////////////////////////
    ///////////////PAC MAN PLAYER////////////
    /////////////////////////////////////////
    //--------------------------------------//

        pacManJLabel = new JLabel(); 
        pacManJLabel.setIcon(new ImageIcon("PACMAN\\src\\resources\\image2.png"));
        pacManJLabel.setBounds(0,0,24,24); 
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
        }

        

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
    
        matrix = table(type);
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

        if (match){
            System.out.println(match);
            showPoints();
            showLife();
            this.moveClient.sendMessage("N "+type);    
            direcction();
            move(); 
            moveGhostOne();
            moveGhostTwo();
            moveGhostThree();
            moveGhostFour();
            checkCollision();
            powerUp();
            fruitGenerator();
            checkVictory();
            setOnlineMatrix();

            arduinoReader.start();
            ArduinoQueueChecker();
        }else{
            getOnlineMatrix();
        }

        gamePanel.add(ground);
        window.add(gamePanel);
        window.setFocusable(true);
        window.requestFocusInWindow();
        window.setResizable(false); 
    }

    
    
    
    int [][] table(int type){
        if (type==1){
            int maze [][] = {
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
        int maze[][] = {
            {0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0},
            {0, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 0},
            {0, 1, 0, 0, 0, 0, 1, 0, 1, 0, 0, 0, 0, 1, 0},
            {0, 1, 1, 1, 0, 1, 1, 0, 1, 1, 1, 0, 1, 1, 0},
            {0, 1, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0},
            {0, 1, 1, 1, 0, 1, 0, 0, 0, 1, 1, 0, 1, 1, 0},
            {0, 0, 0, 0, 0, 1, 0, 1, 0, 0, 0, 0, 0, 0, 0},
            {0, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 0},
            {0, 1, 0, 0, 1, 0, 1, 0, 1, 0, 0, 0, 0, 1, 0},
            {0, 1, 1, 0, 0, 0, 0, 0, 0, 0, 0, 1, 0, 1, 0},
            {0, 1, 0, 1, 0, 1, 0, 1, 0, 1, 0, 1, 0, 1, 0},
            {0, 1, 0, 0, 0, 1, 1, 1, 1, 0, 0, 0, 0, 1, 0},
            {0, 1, 1, 1, 1, 1, 0, 0, 0, 0, 1, 1, 1, 1, 0},
            {0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0},
            {0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0}
        };
        
        

        return  maze;
    }
    
    //----------------------------------------//
    ////////////////////////////////////////////
    ////////////Player functionalities/////////
    ///////////////////////////////////////////
    void checkVictory(){
        Timer timer = new Timer(200, new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                winnerClient.sendMessage("y ");
                if (winnerClient.response.contentEquals("won")){
                    if (type == 2)
                        lose();
                    else{
                        looseControler();
                        new game(window, parent, width, height, 2, true); 
                    }

                }

                    
            }
        });
        timer.start();
    }
    
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

    void checkCollision(){
        Timer timer = new Timer(50, new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                int g1ID = convertToId(ghostOne.getLocation().x/24, ghostOne.getLocation().y/24);
                int g2ID = convertToId(ghostTwo.getLocation().x/24, ghostTwo.getLocation().y/24);
                int g3ID = convertToId(ghostThree.getLocation().x/24, ghostThree.getLocation().y/24);
                int g4ID = convertToId(ghostFour.getLocation().x/24, ghostFour.getLocation().y/24);

                int pacManID = convertToId(pacManJLabel.getLocation().x/24,pacManJLabel.getLocation().y/24);
                if (againstPacMan){
                    if (attackPos){
                        generalClient.sendMessage("c "+ pacManID + " "+ g1ID + " "+ g2ID +" "+ g3ID + " "+ g4ID);
                        if (generalClient.response.contains("killed")){
                            hasLose = true;
                            lose();
                        }
                        else if (generalClient.response.contains("minusLife")){
                            attackPos = false; 
                            waitResponse();
                        }
                    }

                }else{
                    generalClient.sendMessage("C "+ pacManID + " "+ g1ID + " "+ g2ID +" "+ g3ID + " "+ g4ID);
                    String response = generalClient.response;
                    

                    if (response.contentEquals("g1") && !ghostOneEated){
                        ghostOneEated = true; 
                        ghostOne.objectLabel.setVisible(false);
                        
                    }
                    else if (response.contentEquals("g2") && !ghostTwoEated){
                        ghostTwoEated = true; 
                        ghostTwo.objectLabel.setVisible(false);
                    }
                    else if (response.contentEquals("g3") && !ghostTwoEated){
                        ghostThreeEated = true; 
                        ghostThree.objectLabel.setVisible(false);
                    }
                    else if (response.contentEquals("g4") && !ghostFourEated){
                        ghostFourEated = true; 
                        ghostFour.objectLabel.setVisible(false);
                    }
                    
                    
                }
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
            if (generalClient.allowConnection){
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
        }

        });
        timer.start();
    }
    
    void ArduinoQueueChecker(){
        Timer timer = new Timer(50, new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                // Check for messages from the ArduinoSerialReader
                String receivedMessage = arduinoReader.getNextMessage();
                //System.out.println("This is the message:" + receivedMessage);

                // Process the received message and print appropriate messages
                if (receivedMessage != null) {
                    switch (receivedMessage.trim()) {
                        case "1":
                            System.out.println("Arriba");
                            up    = true; 
                            down  = false; 
                            left  = false; 
                            right = false;
                            break;
                        case "2":
                            System.out.println("Abajo");
                            up    = false; 
                            down  = true; 
                            left  = false; 
                            right = false;
                            break;
                        case "3":
                            System.out.println("Derecha");
                            up    = false; 
                            down  = false; 
                            left  = false; 
                            right = true;
                            break;
                        case "4":
                            System.out.println("Izqueirda");
                            up    = false; 
                            down  = false; 
                            left  = true; 
                            right = false;
                            break;
                        // Add more cases for other signals
                        //default:
                            //System.out.println("Unknown signal: " + receivedMessage);
                    }
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
        if (generalClient.allowConnection)
        this.generalClient.sendMessage("S " +pacY+ " "+ pacX);

       
        if (!this.generalClient.response.contentEquals("empty") && !this.generalClient.response.contentEquals("pacManAttack")){
            nodePanel node = fruitList.getElementByID(convertToId(pacX, pacY)); 
            if (node == null)
                blockMatrix[pacY][pacX].setIcon(new ImageIcon("PACMAN\\src\\resources\\image-1.png"));
            else{
                node.element.setImage("PACMAN\\src\\resources\\image-1.png");
                node.element.addLabel();
                fruitList.deleteElement(node);
            }
            points = generalClient.response; 
        }
        else if(this.generalClient.response.contentEquals("pacManAttack")){

            nodePanel node = powerUpList.getElementByID(convertToId(pacX, pacY)); 
            if (node == null)
                blockMatrix[pacY][pacX].setIcon(new ImageIcon("PACMAN\\src\\resources\\image-1.png"));
            else{
                node.element.setImage("PACMAN\\src\\resources\\image-1.png");
                node.element.addLabel();
                powerUpList.deleteElement(node);
            }
            againstPacMan = false; 
            waitPacManAttack();
        }
       
        
    }
    
    void waitResponse(){
        java.util.Timer timer = new java.util.Timer();

        TimerTask tarea = new TimerTask() {
            @Override
            public void run() {
              attackPos = true; 
              againstPacMan = true; 
            }
        };
        timer.schedule(tarea, 5000);
    }
   
    void waitPacManAttack(){
        java.util.Timer timer = new java.util.Timer();

        TimerTask tarea = new TimerTask() {
            @Override
            public void run() {
                againstPacMan = true; 
                ghostOneEated =false;
                ghostTwoEated =false; 
                ghostThreeEated = false; 
                ghostFourEated =false;
                  
                ghostOne.setVisible(true);
                ghostTwo.setVisible(true);
                ghostThree.setVisible(true);
                ghostFour.setVisible(true);
                
            }
        };
        timer.schedule(tarea, 10000);
    }

    void looseControler(){
        ghostOneRoute.head = null; 
        ghostTwoRoute.head = null; 
        ghostThreeRoute.head = null; 
        ghostFourRoute.head =null; 

        ghostOne.move(336, 336);
        ghostTwo.move(312, 336);
        ghostThree.move(288, 336);
        ghostFour.move(264, 336);

        ghostOneClient.allowConnection = false; 
        ghostTwoClient.allowConnection = false;
        ghostThreeClient.allowConnection = false; 
        ghostFourClient.allowConnection = false; 
        moveClient.allowConnection = false; 
        generalClient.allowConnection = false; 
        fruitClient.allowConnection = false; 
        powerUpClient.allowConnection = false; 
        winnerClient.allowConnection = false; 
        gamePanel.setVisible(false);
        arduinoReader.allowConnection = false; 
        window.remove(gamePanel); 

        if (type==2 || hasLose){
        
        parent.setVisible(true);   
        
        }
        

        gamePanel = null; 
}
    
    void lose(){
    java.util.Timer timer = new java.util.Timer();

        TimerTask tarea = new TimerTask() {
            @Override
            public void run() {
                try {
                    looseControler();
                } catch (Exception e) {
                  
                }

            }
        };
        timer.schedule(tarea, 1500);
   }
   
    void powerUp(){
        Timer timer = new Timer(15000, new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                if (powerUpClient.allowConnection){
                    powerUpClient.sendMessage("q");
                    int powerID = Integer.parseInt(powerUpClient.response);
                    int coordX  = powerID%15; 
                    int coordY = powerID/15; 

                    blockMatrix[coordY][coordX].setIcon(new ImageIcon("PACMAN\\src\\resources\\image-1.png"));
                    object power = new powerUp(ground);
                    power.setImage();
                    power.addLabel();
                    power.move(coordX*24, coordY*24);
                    ground.setComponentZOrder(power.objectLabel, 1); 
                    powerUpList.insert(power, powerID); 
                }
                
            
            }
        });
        timer.start();
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
                blockMatrix[coordY][coordX].setIcon(new ImageIcon("PACMAN\\src\\resources\\image-1.jpg"));
               
                object fruit = new fruit(ground);
                fruit.setImage();
                fruit.addLabel();
                fruit.move(coordX*24, coordY*24);
                ground.setComponentZOrder(fruit.objectLabel, 0); 
                fruitList.insert(fruit, coordX%15+coordY*15);

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
                    if(ghostOneRoute.head!= null && !ghostOneEated){
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
                    if(ghostTwoRoute.head!= null && !ghostTwoEated){
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
                ghostThreeClient.sendMessage("h "+ghostID);
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
                    if(ghostThreeRoute.head!= null && !ghostThreeEated){
                        int id = ghostThreeRoute.head.id; 
                        int newX = id%15*24; 
                        int newY = id/15*24; 

                       // System.out.println(newX+" "+ newY);
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
                ghostFourClient.sendMessage("H "+ghostID);
                if (ghostFourClient.response.contentEquals("true")){
                    Point pacManLocation = pacManJLabel.getLocation(); 

                    int pacX = pacManLocation.x/24;
                    int pacY = pacManLocation.y/24;
                    
                    int pacManID = convertToId(pacX, pacY);

                    if (pacManID != ghostID){
                        ghostFourClient.sendMessage("p 4 "+ghostID+" "+pacManID);
                        ghostFourRoute.convertStringToList(ghostFourClient.response);
                    }
                }else{
                    if(ghostFourRoute.head!= null && !ghostFourEated){
                        int id = ghostFourRoute.head.id; 
                        int newX = id%15*24; 
                        int newY = id/15*24; 

                        //System.out.println(newX+" "+ newY);
                        ghostFour.move(newX, newY);
                        ghostFourRoute.head = ghostFourRoute.head.next; 
                    }
                }
            }
        });
        timer.start();

    }

    //////////////////////////////////////////////////////////////////////////////////////////////////////////
    //////////////////////////////////////////////////OBSERVER////////////////////////////////////////////////
    //////////////////////OBSERVER/////////////////////////////////////////////OBSERVER///////////////////////
    //////////////////////////////////////////////////////////////////////////////////////////////////////////
    ///OBSERVER////////////////////////////////OBSERVER///////////////////////////////////OBSERVER////////////
    //////////////////////////////////////////////////////////////////////////////////////////////////////////
    /////////////////////////OBSERVER///////////////////////////////////OBSERVER//////////////////////////////
    /////////////////////////////////////////OBSERVER/////////////////////////////////////////////////////////
    //////////////////////////////////////////////////////////////////////////////////////////////////////////


    
    client onlinePlayerClient = new client(12345);

     void drawMatrix(){
        for (int i = 0; i < matrix.length; i++) {
            for (int j = 0; j < matrix.length; j++) {
                blockMatrix[i][j].setIcon(new ImageIcon("PACMAN\\src\\onlineResources\\image" + matrix[i][j] + ".png"));
                blockMatrix[i][j].setBounds(j * 24, i * 24, 24, 24);
            }
        }
    }

    void setOnlineMatrix(){
        Timer timer = new Timer(200, new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                int g1ID = convertToId(ghostOne.getLocation().x/24, ghostOne.getLocation().y/24);
                int g2ID = convertToId(ghostTwo.getLocation().x/24, ghostTwo.getLocation().y/24);
                int g3ID = convertToId(ghostThree.getLocation().x/24, ghostThree.getLocation().y/24);
                int g4ID = convertToId(ghostFour.getLocation().x/24, ghostFour.getLocation().y/24);

                int pacManID = convertToId(pacManJLabel.getLocation().x/24,pacManJLabel.getLocation().y/24);
                onlinePlayerClient.sendMessage("i "+ pacManID + " " + g1ID + " " + g2ID + " " + g3ID + " " + g4ID  );

                //matrix = MatrixConverter.stringToMatrix(onlinePlayerClient.response); 
            }
        });
        timer.start();
    }
    
    void getOnlineMatrix(){
        Timer timer = new Timer(200, new ActionListener() {
            public void actionPerformed(ActionEvent e) {
            onlinePlayerClient.sendMessage("I " );
            matrix = MatrixConverter.stringToMatrix(onlinePlayerClient.response); 
            String row = ""; 
            System.out.println(onlinePlayerClient.response);
            for(int i = 0; i<matrix.length; i++){
                for (int j = 0; j<matrix.length; j++){
                    row = row + " "+matrix[i][j]; 
                }
                System.out.println(row);
                row = "";

            }
            drawMatrix();
            }
        });
        timer.start();
    }

   
}
