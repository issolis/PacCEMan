package ghost;

import java.awt.Point;

import javax.swing.ImageIcon;
import javax.swing.JPanel;

public class ghost extends object {

    public ghost(String type, JPanel parent) {
        super(type, parent);
        
    }
    @Override
    public void move(int xPos, int yPos) {
        objectLabel.setBounds(xPos, yPos, 24, 24);
    }

    @Override
    public void setImage() {
        String path = ""; 

        if (type.contentEquals("1"))
            path = "PACMAN\\src\\resources\\ghost1.png";
        else if (type.contentEquals("1"))
            path = "PACMAN\\src\\resources\\ghost2.png"; 
        else if (type.contentEquals("1"))
            path = "PACMAN\\src\\resources\\ghost3.png"; 
        else if (type.contentEquals("1"))
            path = "PACMAN\\src\\resources\\ghost4.png"; 


        image = new ImageIcon(path);
        objectLabel.setIcon(image); 
    }

    @Override
    public void addLabel() {
        objectLabel.setVisible(true);
        parent.add(objectLabel); 
    }
    @Override
    public Point getLocation() {
        Point ghostLocation = objectLabel.getLocation(); 
        return ghostLocation;
    }

    @Override
    public void deleteObject() {
        objectLabel.setVisible(false);
    }
    
}
