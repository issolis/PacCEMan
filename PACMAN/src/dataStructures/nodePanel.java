package dataStructures;
import ghost.*;

public class nodePanel {
    public nodePanel next; 
    public object element; 
    int id; 

    nodePanel(object element, int id){
        this.element = element;
        this.id = id; 
    }

}
