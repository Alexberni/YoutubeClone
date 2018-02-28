/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Controllers;

import ConnectionClasses.ConnectedTransmision;
import ConnectionClasses.UploadVideo;
import ConnectionClasses.VideoStream;
import GUIs.InstantInputDialog;
import Models.Video;
import java.io.File;
import java.util.ArrayList;
import javax.swing.JTable;
import javax.swing.table.DefaultTableModel;

/**
 *
 * @author Alex
 */
public class Controller {
    public ConnectedTransmision con;
    
    public Controller(ConnectedTransmision con){
        this.con = con;
    }
    
    public ArrayList<Video> parseVideos(String serverIput){
        ArrayList<Video> videos = new ArrayList<>();
        String[] fromServerCut = serverIput.split("#");
        for(int i = 3; i < fromServerCut.length; i++){
            String[] userCut = fromServerCut[i].split("@");
            for(int o = 1; o < userCut.length; o++){
                String[] videoCut = userCut[o].split("&");
                videos.add(new Video(videoCut[1], videoCut[2], Integer.parseInt(videoCut[0]), userCut[0]));
            }
        }
        return videos;
    }
    
    public void updateVideoTable(ArrayList<Video> videos){
        
        DefaultTableModel model = (DefaultTableModel) this.con.videoTable.getModel();
        int rowCount = model.getRowCount();
        for (int i = rowCount - 1; i >= 0; i--) {
            model.removeRow(i);
        }
        for(Video video : videos){
            String data1 = video.getTitle();
            String data2 = video.getDesc();
            String data3 = video.getIdOwner();
            Object[] row = { data1, data2, data3};
            model.addRow(row);         
        }

    }

    public void uploadVideo(String ruta){
        File tempFile = new File(ruta);
        InstantInputDialog dialog = new InstantInputDialog("titulo");
        InstantInputDialog dialog2 = new InstantInputDialog("desc");
        dialog.main();
        dialog2.main();
        Video video = new Video(dialog.data, dialog2.data, ruta); 
        UploadVideo up = new UploadVideo(video, tempFile.length(), tempFile, this.con.user);
        up.thr.start();
        //con.uploadVideo(video, tempFile.length(), tempFile);
    }
    
    public void loadVideo(int id) {
        VideoStream stream = new VideoStream("PROTOCOLCRISTOTUBE1.0#" + this.con.user + "#GETVIDEO#" + id ,this.con.user);
        stream.thr.start();
        //this.con.userOutput("PROTOCOLCRISTOTUBE1.0#" + this.con.user + "#GETVIDEO#" + id);
    }
    
    public void getAll() {
        this.con.userOutput("PROTOCOLCRISTOTUBE1.0#"+ this.con.user + " #GET_ALL");
    }
    
    public void deleteVideo(String title) {
        this.con.userOutput("PROTOCOLCRISTOTUBE1.0#DELETE_VIDEO#" + this.getVideoId(title));
    }
    
    public void getVideo(String titulo){
        ArrayList<Video> videos = this.con.getVideos();
        for(Video video : videos){
            if(video.getTitle().equals(titulo)){
                this.loadVideo(video.getId());
            }
            
        }
    }
    
    public int getVideoId(String titulo){
        int id = 0;
        ArrayList<Video> videos = this.con.getVideos();
        for(Video video : videos){
            if(video.getTitle().equals(titulo)){
                id = video.getId();
            }           
        }
        return id;
    }
    
}
