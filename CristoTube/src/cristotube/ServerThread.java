/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package cristotube;


import Models.Usuario;
import cristotube.Controllers.Controller;
import java.net.*;
import java.io.*;
import java.util.ArrayList;
import java.util.logging.Level;
import java.util.logging.Logger;



public class ServerThread extends Thread {
    public Socket socket = null;
    public Usuario currentUser = new Usuario();
    public Controller controller;
    public long onlineTime = 0;
    private ClientTimer timer;
    private ArrayList<ServerThread> clients;
    public PrintWriter out2;
    public BufferedReader in2;
    public CristoTubeProtocol ctp;
    
    public ServerThread(Socket socket, ArrayList<ServerThread> clients) {
        super("ServerThread");
        this.socket = socket; 
        this.timer = new ClientTimer(this);
        this.clients = clients;
        this.controller = new Controller(clients);
        System.out.println("creando socket");
    }
    
    @Override
    public void run() {
        this.timer.start();
        try (
            PrintWriter out = new PrintWriter(socket.getOutputStream(), true);
            BufferedReader in = new BufferedReader(
                new InputStreamReader(
                    socket.getInputStream()));
        ) {
            out2 = out;
            in2 = in;
            
            String inputLine, outputLine;                  
            ctp = new CristoTubeProtocol(this.controller, this.socket);            

            while ((inputLine = in.readLine()) != null) {
                System.out.println(inputLine);
                outputLine = ctp.processInput(inputLine); 
                this.currentUser = ctp.currentUser;  
                if(outputLine != null){
                    if(!outputLine.equals("vacio"))
                        out.println(outputLine);
                    //System.out.println(outputLine);                            
                    }
                }
            
            } catch (FileNotFoundException ex) {          
            Logger.getLogger(ServerThread.class.getName()).log(Level.SEVERE, null, ex);
        } catch (IOException ex) {
            Logger.getLogger(ServerThread.class.getName()).log(Level.SEVERE, null, ex);
        }        
            System.out.println("Transmision Correcta");
            this.timer.stop();    
            this.clients.remove(this.clients.indexOf(this));
            
        try {
            socket.close();
        } catch (IOException ex) {
            Logger.getLogger(ServerThread.class.getName()).log(Level.SEVERE, null, ex);
        }
             
        } 
    }

