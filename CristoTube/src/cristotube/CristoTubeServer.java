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
                this.clients.add(new ServerThread(serverSocket.accept(), this));
                this.serverAdd();
            }
        } catch (IOException e) {
            System.err.println("Could not listen on port " + this.port);
            System.exit(-1);
        }
    }
    
    public synchronized void serverAdd() throws IOException {
        this.clients.get(this.clients.size()-1).start();
    }
    
    public synchronized void serverDelete(ServerThread client) throws IOException {
        this.clients.remove(client);
    }
    
    public synchronized void serverCutStream(String idVideo) throws IOException {
        for(ServerThread client : this.clients){
            client.out2.println("PROTOCOLCRISTOTUBE1.0#BROADCAST#" + idVideo + "#DELETED");
            if(client.ctp.idVideoSending == Integer.parseInt(idVideo)){
                System.out.println("holaa");
                client.socket.close();
                clients.remove(client);
            }
        }
    }
    
}
