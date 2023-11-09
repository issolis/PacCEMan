package game;

import java.awt.Color;
import java.awt.Font;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;

public class mainPanel { 
    JButton start;
    JButton matchButton;
    JPanel  mainPanel; 

    public mainPanel(JFrame window, int width, int height) {

        mainPanel = new JPanel(); 
        mainPanel.setLayout(null);
        mainPanel.setBounds(0,0, width, height);
        mainPanel.setVisible(true);
        mainPanel.setBackground(Color.BLACK);

        ImageIcon bgImage = new ImageIcon("PACMAN\\src\\resources\\bg.jpg"); 

        JLabel bgLabel = new JLabel();
        bgLabel.setBounds(0, 0, width, height);
        bgLabel.setIcon(bgImage);


        start = new JButton("   Play    ");
        start.setBounds(width / 2 - 175, height / 2 + 70, 150, 70);
        Font customFont = new Font("Arial", Font.BOLD, 16);
        start.setFont(customFont);
        start.setVisible(true);

        matchButton = new JButton("   Join    ");
        matchButton.setBounds(width / 2 +25, height / 2 + 70, 150, 70);
        matchButton.setFont(customFont);
        matchButton.setVisible(true);

        ImageIcon pacManImage = new ImageIcon("PACMAN\\src\\resources\\inicioRedimensionadoSinFondo.gif");

        JLabel pacManLabel = new JLabel();
        pacManLabel.setBounds(width / 9, 0, width, 100);
        pacManLabel.setIcon(pacManImage);

        //--------------------------Action Listiners--------------------------//
        start.addMouseListener(new MouseAdapter() {
            @Override
            public void mousePressed(MouseEvent e) {
                mainPanel.setVisible(false);
                new game(window,mainPanel,  width, height, 1); 
            }
        });

        matchButton.addMouseListener(new MouseAdapter() {
            @Override
            public void mousePressed(MouseEvent e) {
                mainPanel.setVisible(false);
                new game(window,mainPanel,  width, height, 1,2); 
            }
        });
        //-------------------------------------------------------------------//

        mainPanel.add(pacManLabel);
        mainPanel.add(bgLabel); 
        mainPanel.add(start);
        mainPanel.add(matchButton);
        
        
        window.add(mainPanel);
        window.setVisible(true);
    }

    public static void main(String[] args) {
        JFrame window;
        window = new JFrame("Pac Man");
        window.setSize(450, 450);
        window.setLayout(null);
        window.setLocationRelativeTo(null);
        window.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
       
        Integer width = window.getWidth();
        Integer height = window.getHeight();

        new mainPanel(window, width, height); 
    
    }
}
