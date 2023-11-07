package clientSocket; 

import java.io.*;
import java.net.*;

public class client {

    public String response; 
    public client(){
        response = ""; 
    }

    public void sendMessage(String message){
        String serverAddress = "127.0.0.1";
        int serverPort = 12345; 
        
        try (Socket socket = new Socket(serverAddress, serverPort);
            PrintWriter out = new PrintWriter(socket.getOutputStream(), true);
            BufferedReader in = new BufferedReader(new InputStreamReader(socket.getInputStream()))) {
            out.println(message); 
            response = in.readLine(); 

        } catch (IOException e) {
            e.printStackTrace();
        }
    }
   
}
