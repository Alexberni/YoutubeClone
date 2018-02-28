/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package cristotube;

import Models.Usuario;
import cristotube.Controllers.Controller;
import java.io.*;
import java.net.Socket;
import java.util.Base64;
 
public class CristoTubeProtocol {
    public Controller controller;
    private PrintWriter out;
    private BufferedReader in;
    public Usuario currentUser = new Usuario();
    private CristoTubeServer server;
    public Socket socket;
    public int idVideoSending = -4;
    public CristoTubeProtocol(Controller controller, Socket socket, CristoTubeServer server) throws IOException{
        this.server = server;
        this.controller = controller;
        this.socket = socket;       
        this.out = new PrintWriter(socket.getOutputStream(), true);
        this.in = new BufferedReader(
                new InputStreamReader(
                    socket.getInputStream()));
    }
    
    public String processInput(String theInput) throws IOException {
        String theOutput = null;    
        if(theInput != null){
          
            String[] parts = theInput.split("#");

            if(parts[1].equals("REGISTER")){
                if(this.controller.registerUser(theInput)){
                    theOutput = "PROTOCOLCRISTOTUBE1.0#OK#REGISTERED";
                }
                else
                    theOutput = "PROTOCOLCRISTOTUBE1.0#ERROR#CANT_REGISTER";               
            }
            
            else if(parts[1].equals("LOGIN")){
                if(this.controller.getLogin(theInput)){
                    theOutput = "PROTOCOLCRISTOTUBE1.0#OK#USER_LOGGED#" + this.controller.getLogData(); 
                    this.currentUser = this.controller.getCurrentUser(parts[2]);
                }
                else
                    theOutput = "PROTOCOLCRISTOTUBE1.0#ERROR#BAD_LOGIN";               
            }
            
            else if(parts[2].equals("GET_ALL")){                  
                String cadena = this.controller.getAllVideos();
                this.currentUser = this.controller.getCurrentUser(parts[1]);
                String fullOutPut = "PROTOCOLCRISTOTUBE1.0#GET_ALL_RESPONSE#" + this.currentUser.getLogin() + cadena;
                theOutput = fullOutPut;
                }
            
            else if(parts[1].equals("DELETE_VIDEO")){     
                    this.controller.deleteVideo(Integer.parseInt(parts[2]));
                    this.server.serverCutStream(parts[2]);
                    theOutput = "vacio";
                }           
            
            else if(parts[2].equals("GETVIDEO")){
                this.currentUser = this.controller.getCurrentUser(parts[1]);
                theOutput = "vacio"; //Esto es solo para mi, no se envia.
                String path = this.controller.getVideo(Integer.parseInt(parts[3]));
                
                File tempFile = new File(path);
                               
                out.println("PROTOCOLCRISTOTUBE1.0#OK#VIDEO_FOUND#"+parts[3]+"#"+tempFile.length()+"#1024");
                idVideoSending = Integer.parseInt(parts[3]);
                theInput = in.readLine();
                System.out.println(theInput);
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
                        encodedString = Base64.getEncoder().encodeToString(buffer);                 
                        out.println("PROTOCOLCRISTOTUBE1.0#OK#"+parts[3]+"#" + encodedString);
                    }

                } catch (IOException e) {
                    System.out.println("No se encuentra el archivo de video");
                }
                idVideoSending = -4;                
                inputStream.close();
                System.out.println("Video Transmitido Satisfactoriamente");                
               } 
            
            else if(parts[1].equals("VIDEO_UP")){
                this.currentUser = this.controller.getCurrentUser(parts[5]);
                theOutput = "vacio"; //Esto es solo para mi, no se envia.
                this.controller.uploadVideo(parts[6], parts[7], "users/" + this.currentUser.getId() + "/videos/" + parts[6] + ".mp4", this.currentUser.getId());
                out.println("PROTOCOLCRISTOTUBE1.0#OK#VIDEO_UP#PREPARED_TO_RECEIVE#" + parts[2]);                 
                int length = Integer.parseInt(parts[2]);                   
                String[] parts2 = theInput.split("#");               
                byte[] bytes;
                byte[] total_bytes;
                boolean exit = true;
                File file = new File("users/" + this.currentUser.getId() + "/videos/" + parts[6] + ".mp4");
                FileOutputStream outp = new FileOutputStream(file);
                ByteArrayOutputStream output = new ByteArrayOutputStream(); 
                          
                //theInput = in.readLine();
                System.out.println(theInput);    
                
                while((theInput = in.readLine()) != null && exit){
                    parts2 = theInput.split("#");
                    System.out.println(theInput);
                    String encoded = parts2[2];
                    bytes = java.util.Base64.getDecoder().decode(encoded.getBytes()); 
                    outp.write(bytes);                          
                    if(file.length() >= length){
                        System.out.println("Saliendo video");
                        exit = false;
                    } 
                    else{
                        //theInput = in.readLine();  
                        //parts2 = theInput.split("#");
                    }
                }                              
                try {      
                    outp.close();
                    System.out.println("Video Transmitido Satisfactoriamente");
                } catch (Exception e) {
                }           
                
            }
            
            
            }                                       
        
        return theOutput;
    }
}