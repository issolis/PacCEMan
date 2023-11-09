package dataStructures;
import object.*;

public class listPanel {
    nodePanel head; 

    public listPanel(){
        head = null; 
    }

    public void insert(object element, int id){
        nodePanel newNode = new nodePanel(element, id); 
        if (head == null)
            head = newNode;
        else{
            nodePanel current = head;
            while (current.next!=null) 
                current = current.next;
            current.next = newNode; 
        }
    }
    public nodePanel getElementByID(int id){
        nodePanel current = head;
        while (current!=null) {
            if (current.id == id)
                return current; 
            current = current.next;
        }
        return null; 
    }
    public void deleteElement (nodePanel element){
        nodePanel current = head;
        nodePanel before = head;
        while (current!=null) {
            if (current == element){
                if (head == current)
                    head = head.next;
                else
                     before.next = current.next; 
            }
            current = current.next;
        }
    }
}
