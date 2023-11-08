package ghost;

import java.awt.Point;

import javax.swing.ImageIcon;
import javax.swing.JLabel;
import javax.swing.JPanel;

public abstract class object {

    JPanel parent; 
    public JLabel objectLabel; 
    ImageIcon image; 
    String type; 

    public object(JPanel parent){
        this.parent = parent; 
        objectLabel = new JLabel();
    } 

    public abstract void move(int xPos, int yPos); 
    public abstract void setImage();
     public abstract void setImage(String path); 
    public abstract void addLabel(); 
    public abstract Point getLocation(); 
    public abstract void deleteObject();

}
