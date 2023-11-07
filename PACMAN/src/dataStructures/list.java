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

    public void convertStringToList(String str){
        int len = str.length();
        int pos = 0;
        for (int i=0; i<len; i++){
            if (str.charAt(i)==' '){
                insertElement(Integer.parseInt(str.substring(pos, i)));
                pos = i +1; 
            }
            if (i == len -1){
                 insertElement(Integer.parseInt(str.substring(pos, i+1)));
            }

        }
    }
    public void deleteFirst (){
        if (head == null)
            return; 
        head = head.next;
    }

    public void show(){
        node current = head; 
        while(current!=null){
            System.out.println(current.id);
            current = current.next;
        }
    }


}
