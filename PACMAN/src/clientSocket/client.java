package clientSocket; 

import java.io.*;
import java.net.*;

public class client {
    int serverPort;
    public String response; 
    public boolean allowConnection; 
    public client(int serverPort){
        response = ""; 
        this.serverPort = serverPort;
        allowConnection = true; 
    }

    public void sendMessage(String message){
        if (allowConnection){
            String serverAddress = "127.0.0.1";
            
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
   
}
