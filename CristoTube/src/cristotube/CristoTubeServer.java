package cristotube;

import java.io.IOException;
import java.net.ServerSocket;
import java.util.ArrayList;
import java.util.logging.Level;
import java.util.logging.Logger;

public class CristoTubeServer extends Thread{
    public ArrayList<ServerThread> clients = new ArrayList<>();
    private ServerSocket serverSocket;
    private int port = 8;
    private boolean listening;
    
    public CristoTubeServer(int port){
        this.port = port;
    }
    
    public void stopServer(){
        this.listening = false;
        try {
            serverSocket.close();
        } catch (IOException ex) {
            Logger.getLogger(CristoTubeServer.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
    
    public void run() { 
        this.listening = true;
         
        try {
            this.serverSocket = new ServerSocket(this.port);
            while (this.listening) {
               this.clients.add(new ServerThread(serverSocket.accept(), this.clients));
               this.clients.get(this.clients.size()-1).start();
            }
        } catch (IOException e) {
            System.err.println("Could not listen on port " + this.port);
            System.exit(-1);
        }
    }
}
