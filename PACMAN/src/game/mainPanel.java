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
        start.setBounds(width / 2 - 75, height / 2 + 70, 150, 70);
        Font customFont = new Font("Arial", Font.BOLD, 16);
        start.setFont(customFont);
        start.setVisible(true);

        ImageIcon pacManImage = new ImageIcon("PACMAN\\src\\resources\\inicioRedimensionadoSinFondo.gif");

        JLabel pacManLabel = new JLabel();
        pacManLabel.setBounds(width / 9, 0, width, 100);
        pacManLabel.setIcon(pacManImage);

        //--------------------------Action Listiners--------------------------//
        start.addMouseListener(new MouseAdapter() {
            @Override
            public void mousePressed(MouseEvent e) {
                mainPanel.setVisible(false);
                new game(window, width, height); 
            }
        });
        //-------------------------------------------------------------------//

        mainPanel.add(pacManLabel);
        mainPanel.add(bgLabel); 
        mainPanel.add(start);
        
        
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
