package ConnectionClasses;

/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
import Controllers.Controller;
import Models.Video;
import GUIs.ReproductorVideoGUI;
import java.net.*;
import java.io.*;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.Base64;
import java.util.Date;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.JTable;




public class ConnectedTransmision implements Runnable{
    public Thread thr;
    public Boolean connected = false;
    public Boolean open = true;
    public JTable videoTable;
    private Socket kkSocket;
    public ArrayList<Video> videos = new ArrayList<>();
    public String user;
    private String outPut = null;
    public Controller controller;
    
    public ConnectedTransmision(String cadena){
        this.thr = new Thread(this, "connection");
        this.connected = false;
        this.outPut = cadena;
        this.connectSocket();
        this.controller = new Controller(this);
    }
    
    public ConnectedTransmision(){
        this.thr = new Thread(this, "connection");
        this.connected = false;
        this.connectSocket();
        this.controller = new Controller(this);
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
            
            //Pre Login
            while((fromServer = in.readLine()) != null){
                    System.out.println("Server: " + fromServer);
                    String[] fromServerCut = fromServer.split("#");
                    if(fromServerCut[0].equals("PROTOCOLCRISTOTUBE1.0")){           
                        if (((fromServerCut[1] + fromServerCut[2]).equals("OKREGISTERED"))){
                          System.out.println("SUCCESS_Registrado Correctamente"); 
                          this.thr.stop();
                        }   
                        else if(((fromServerCut[1] + fromServerCut[2]).equals("ERRORCANT_REGISTER"))){
                            System.out.println("ERROR_Fallo al registrar");
                            this.thr.stop();
                        }
                        else if(((fromServerCut[1] + fromServerCut[2]).equals("OKUSER_LOGGED"))){                     
                            System.out.println("SUCCESS_Logueado");
                            this.connected = true;
                            this.user = fromServerCut[7];
                            new File("users/" + this.user + "/videos/").mkdirs();
                            break;
                        }
                        else if(((fromServerCut[1] + fromServerCut[2]).equals("ERRORBAD_LOGIN"))){
                            System.out.println("ERROR_Fallo al loguear");
                            this.connected = false;
                        }   
                    }  
                    
            }            
            
            while(this.videoTable == null){this.thr.sleep(2);} //Bucle de espera a que se le asigne un jtable
            out.println("PROTOCOLCRISTOTUBE1.0#"+ this.user + " #GET_ALL");
            
            //Post Login
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
                //}                                                                      
            }
            
        } catch (UnknownHostException e) {
            System.out.println("Don't know about host ");


        } catch (IOException e) {
              System.out.println("Couldn't get I/O for the connection to ");


        } catch (InterruptedException ex) {
            Logger.getLogger(ConnectedTransmision.class.getName()).log(Level.SEVERE, null, ex);
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
    
     public void uploadVideo(Video video, long total_size, File tempFile){
        try {
            PrintWriter out = new PrintWriter(kkSocket.getOutputStream(), true);
            BufferedReader in = new BufferedReader(
            new InputStreamReader(kkSocket.getInputStream()));
            out.println("PROTOCOLCRISTOTUBE1.0#VIDEO_UP#" + total_size + "#" + 1024 + "#METADATOS_VIDEO#" + this.user + "#"+ video.getTitle() + "#" + video.getDesc());           
            //String fromServer = in.readLine();           
            String encodedString = null;
            InputStream inputStream = null;
            try {
                inputStream = new FileInputStream(tempFile);
            } catch (Exception e) {
            }
            byte[] buffer = new byte[1024];
            int bytesRead;
            ByteArrayOutputStream output = new ByteArrayOutputStream();
            try {
                while ((bytesRead = inputStream.read(buffer)) != -1) {
                    output.write(buffer);
                    encodedString = Base64.getEncoder().encodeToString(buffer);
                    out.println("PROTOCOLCRISTOTUBE1.0#OK#" + encodedString);
                }

            } catch (IOException e) {
                e.printStackTrace();
            }                
            inputStream.close();
            System.out.println("Video Transmitido Satisfactoriamente");
            
            
            
        } catch (IOException ex) {
            Logger.getLogger(ConnectedTransmision.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
    
    private void connectSocket(){
        String hostName = "localhost";
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