/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ConnectionClasses;

import Controllers.Controller;
import GUIs.ReproductorVideoGUI;
import Models.Video;
import java.io.BufferedReader;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;
import java.net.UnknownHostException;
import java.util.ArrayList;
import java.util.Base64;
import java.util.Date;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.JTable;

public class VideoStream implements Runnable{
    public Thread thr;
    public Boolean connected = false;
    public Boolean open = true;
    public JTable videoTable;
    private Socket kkSocket;
    public ArrayList<Video> videos = new ArrayList<>();
    public String user = "Unkown";
    private String outPut = null;
    public Controller controller;
    
    public VideoStream(String cadena, String usu){
        this.thr = new Thread(this, "connection");
        this.connected = false;
        this.outPut = cadena;
        this.user = usu;
        this.connectSocket();
    }
    
    public VideoStream(){
        this.thr = new Thread(this, "connection");
        this.connected = false;
        this.connectSocket();
    }
    
    @Override
    public  void run() {
    try (
            PrintWriter out = new PrintWriter(kkSocket.getOutputStream(), true);
            BufferedReader in = new BufferedReader(
                new InputStreamReader(kkSocket.getInputStream()));
        ) {
            BufferedReader stdIn =
                new BufferedReader(new InputStreamReader(System.in));
            
            String fromServer;
            String fromUser = this.outPut;
            
            if (fromUser != null) {
                 System.out.println("User: " + fromUser);
                 out.println(fromUser);
            }
            
            while((fromServer = in.readLine()) != null){ //(fromServer = in.readLine()) != null
                System.out.println("Server: " + fromServer);
                String[] fromServerCut = fromServer.split("#");
                //if(fromServerCut[0].equals("PROTOCOLCRISTOTUBE1.0")){                                 
                    if(((fromServerCut[1].equals("GET_ALL_RESPONSE")))){                     
                        this.videos = controller.parseVideos(fromServer);
                        controller.updateVideoTable(videos);
                    }
                    else if(fromServerCut[3].equals("DELETED")){
                        out.println("PROTOCOLCRISTOTUBE1.0#"+ this.user + " #GET_ALL");                       
                        fromServer = in.readLine();
                        System.out.println("Server: " + fromServer);
                        fromServerCut = fromServer.split("#");
                        this.videos = controller.parseVideos(fromServer);
                        this.controller.updateVideoTable(videos);
                        System.out.println(fromServerCut[2]);
                        out.println("PROTOCOLCRISTOTUBE1.0#" + this.user + "#DELETE#" + fromServerCut[2] + "#BROADCASTING#OK");
                    }
                    else if(fromServerCut[2].equals("VIDEO_FOUND")){ 
                        out.println("PROTOCOLCRISTOTUBE1.0#OK#" + this.user + "#PREPARED_TO_RECEIVE#" + fromServerCut[4]);
                        int length_total = Integer.parseInt(fromServerCut[4]);
                        
                        int length = (int) ((length_total/1024)*0.015);
                        
                        if (length < 500){
                            length = 500;
                        }
                        else
                            length = 500;
                        
                        fromServer = in.readLine();
                        
                        System.out.println(fromServer);
                        try{
                            fromServerCut = fromServer.split("#");
                            byte[] bytes;
                            byte[] total_bytes;
                            boolean exit = true;
                            int j = 0;
                            File file = new File("users/" + this.user + "/videos/" + new Date().getTime()/60 + ".mp4");
                            FileOutputStream outp = new FileOutputStream(file);
                            //ByteArrayOutputStream output = new ByteArrayOutputStream(); 
                            do{
                                System.out.println(fromServer);
                                if(!fromServerCut[3].equals("DELETE")){
                                    String encoded = fromServerCut[3];
                                    bytes = java.util.Base64.getDecoder().decode(encoded.getBytes()); 
                                    //output.write(bytes);
                                    outp.write(bytes);
                                    //System.out.println(file.length() +"   "+ length_total);
                                    if(file.length() >= length_total){
                                        exit = false;
                                    } 
                                    else{
                                        fromServer = in.readLine();  
                                        fromServerCut = fromServer.split("#");
                                    }
                                    if (j == length){
                                        ReproductorVideoGUI repro = new ReproductorVideoGUI(file);
                                        repro.show();
                                    }
                                    j++;
                                    //this.thr.sleep(30);
                                    //total_bytes = output.toByteArray();
                                    //outp.write(total_bytes);
                                }
                                else{
                                    exit = false;
                                    this.controller.updateVideoTable(videos);
                                    out.println("PROTOCOLCRISTOTUBE1.0#" + this.user + "#DELETE#" + fromServerCut[2] + "#BROADCASTING#OK");
                                }
                                }while(exit);                                  
                            try {      
                                //total_bytes = output.toByteArray();
                                //outp.write(total_bytes);
                                outp.close();
                                System.out.println("Video Transmitido Satisfactoriamente");
                            } catch (Exception e) {}
                        }
                        catch(Exception e){
                            System.out.println("Fichero de video no encontrado");
                        }
                } 
                //}                                                                      
            }
            
        } catch (UnknownHostException e) {
            System.out.println("Don't know about host ");


        } catch (IOException e) {
              System.out.println("Couldn't get I/O for the connection to ");


        }   
    }

     public void userOutput(String nextOut){
        try {
            PrintWriter out = new PrintWriter(kkSocket.getOutputStream(), true);
            out.println(nextOut);
        } catch (IOException ex) {
            Logger.getLogger(ConnectedTransmision.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
    
    private void connectSocket(){
        String hostName = "59.19.19.65";
        int portNumber = 4444;
        try {
            this.kkSocket = new Socket(hostName, portNumber);
        } catch (IOException ex) {
            Logger.getLogger(ConnectedTransmision.class.getName()).log(Level.SEVERE, null, ex);
            System.out.println("Error connection I/O " + hostName);
        }
    
    }
    
    public ArrayList<Video> getVideos(){
        return this.videos;
    }
    
    private void FileOutPutStream(File file) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }
    
    
}
