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

/**
 *
 * @author Alex
 */
public class UploadVideo implements Runnable{
    public Thread thr;
    public Boolean connected = false;
    public Boolean open = true;
    public JTable videoTable;
    private Socket kkSocket;
    public ArrayList<Video> videos = new ArrayList<>();
    public String user = "juan";
    public Controller controller;
    private Video video;
    private long total_size;
    private File tempFile;
    
    public UploadVideo(Video video, long total_size, File tempFile, String user){
        this.thr = new Thread(this, "connection");
        this.video = video;
        this.total_size = total_size;
        this.tempFile = tempFile;
        this.connectSocket();
    }  
    
    @Override
    public  void run() {
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
        String hostName = "52.19.19.65";
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