package ghost;

import java.awt.Point;

import javax.swing.ImageIcon;
import javax.swing.JLabel;
import javax.swing.JPanel;

public abstract class object {

    JPanel parent; 
    JLabel objectLabel; 
    ImageIcon image; 
    String type; 

    public object(String type, JPanel parent){
        this.parent = parent; 
        this.type = type; 
        objectLabel = new JLabel();
    } 

    public abstract void move(int xPos, int yPos); 
    public abstract void setImage(); 
    public abstract void addLabel(); 
    public abstract Point getLocation(); 
    public abstract void deleteObject();

    public Object getRoute() {
        return null;
    }
}
