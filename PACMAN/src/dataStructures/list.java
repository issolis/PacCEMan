package dataStructures;

public class list {
    node head; 
    public list(){
        head = null; 
    }

    public void insertElement(int id){
        node newNode = new node(id); 
        if(head == null){
            head = newNode;
        }
        else{
            node current = head;
            while (current.next!=null) 
                current = current.next;
            current.next = newNode;  
        }
    }

    public void show(){
        node current = head; 
        while(current!=null){
            System.out.println(current.id);
            current = current.next;
        }
    }

}
