package ghost;

import java.awt.Point;

import javax.swing.ImageIcon;
import javax.swing.JPanel;

public class fruit extends object {

    public fruit(JPanel parent) {
        super(parent);
    }

   @Override
    public void move(int xPos, int yPos) {
        objectLabel.setBounds(xPos, yPos, 24, 24);
    }

    @Override
    public void setImage() {
        String path = ""; 
        path = "PACMAN\\src\\resources\\image3.jpg"; 
        image = new ImageIcon(path);
        objectLabel.setIcon(image); 
    }
    @Override
    public void setImage(String path) {
        image = new ImageIcon(path);
        objectLabel.setIcon(image); 
        objectLabel.repaint(); 
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

    @Override
    public void setVisible(boolean flag) {
        // TODO Auto-generated method stub
        throw new UnsupportedOperationException("Unimplemented method 'setVisible'");
    }
    
    
}
