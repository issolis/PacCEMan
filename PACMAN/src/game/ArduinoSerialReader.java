package game;

import com.fazecast.jSerialComm.*;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.LinkedBlockingQueue;

public class ArduinoSerialReader extends Thread {
    boolean allowConnection = true; 
    SerialPort[] ports;
    SerialPort arduinoPort;
    private BlockingQueue<String> messageQueue = new LinkedBlockingQueue<>();
    private volatile boolean detener = false;



    ArduinoSerialReader (){
        this.ports = SerialPort.getCommPorts();
        this.arduinoPort = ports[0];
        this.arduinoPort.openPort();
        this.arduinoPort.setComPortTimeouts(SerialPort.TIMEOUT_READ_BLOCKING, 100, 0);
    }

    public void run(){
        while (!detener) {
            byte[] buffer = new byte[1];
            int bytesRead = arduinoPort.readBytes(buffer, buffer.length);// Read 1 byte from the Arduino

            // Process the received data
            String receivedData = new String(buffer, 0, bytesRead);
            //System.out.println("Received: " + receivedData);
            // Add your game logic here based on the received data (e.g., update the game state).

            if(!allowConnection){
                break; }
            try {
                messageQueue.put(receivedData);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        } 
    }

    public void detenerHilo() {
        detener = true;
    }

    public String getNextMessage() {
        try {
            // Block until a message is available in the queue
            //tring msg = messageQueue.take();
            //messageQueue.clear();
            return messageQueue.take();
        } catch (InterruptedException e) {
            e.printStackTrace();
            return null;
        }
    }

    public void ReadSignals() {

        /* if (this.ports.length == 0) {
            System.out.println("No serial ports found.");
            return;
        } */

        while (!detener) {
            byte[] buffer = new byte[1];
            arduinoPort.readBytes(buffer, 1); // Read 1 byte from the Arduino

            // Process the received data
            String receivedData = new String(buffer);
            System.out.println("Received: " + receivedData);
            // Add your game logic here based on the received data (e.g., update the game state).
        }
    }
}
