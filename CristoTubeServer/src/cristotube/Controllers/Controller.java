package cristotube.Controllers;

import Interfaces.MysqlConn;
import Models.Usuario;
import Models.Video;
import cristotube.ServerThread;
import java.io.File;
import java.nio.file.Files;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author Alex Benavides
 */
public class Controller {
    private Connection conn;
    String[] logedData = new String[5];
    public Usuario user = new Usuario();
    
    public Controller(){
    }
    
    public boolean registerUser(String cadena){
        boolean registered = false;
        Statement stmt= null;         
        String[] parts=cadena.split("#");
        String dni=parts[2];      
        String nombre = parts[3];
        String apellido1=parts[4]; 
        String apellido2=parts[5];
        String login=parts[6];
        String password=parts[7];         
        String query="INSERT INTO usuarios (DNI,NAME,LASTNAME1,LASTNAME2,LOGIN,PASSWORD) VALUES ('"+dni+"','"+nombre+"','"+apellido1+"','"+apellido2+"','"+login+"','"+password+"')";           
        try {         
            this.conn = MysqlConn.getConn();
            stmt = conn.createStatement();
            PreparedStatement pps=conn.prepareStatement(query);
            pps.executeUpdate();
            this.conn.close();   
            registered = true;
        } catch (SQLException ex) {
           ex.printStackTrace();
        }      
        return registered;
    }
    
    
    public boolean getLogin(String cadena){  
        //RECONSTRUIR CON 
        String[] parts=cadena.split("#");
        String login=parts[2];
        String password=parts[3];
        boolean found = false;
        String query="SELECT * FROM usuarios WHERE LOGIN = '"+login+"'AND PASSWORD= '"+password+"'";
        Statement stmt= null;       
        try {
            this.conn = MysqlConn.getConn();
            stmt = conn.createStatement();
        
            ResultSet rs = stmt.executeQuery(query);
           
            while(rs.next()){              
                String first = rs.getString("login");
                String last = rs.getString("password");
                if(first.equals(login) && last.equals(password)){                   
                    found=true;
                    this.logedData[0] = rs.getString("dni");
                    this.logedData[1] = rs.getString("name");
                    this.logedData[2] = rs.getString("lastname1");
                    this.logedData[3] = rs.getString("lastname2");
                    this.logedData[4] = first;
                    this.user = new Usuario(Integer.parseInt(rs.getString("id")), this.logedData[0], this.logedData[1], this.logedData[2], this.logedData[3], this.logedData[4], last);
                }
            }
            this.conn.close();            
        } catch (SQLException ex) {
            Logger.getLogger(Controller.class.getName()).log(Level.SEVERE, null, ex);
        }
        return found;
    }
    
    public ArrayList<Usuario> getAllUsers(){ 
        String query="SELECT * FROM usuarios";
        Statement stmt= null;
        ArrayList<Usuario> usuarios = new ArrayList<>();
        String cadena;
        try {
            this.conn = MysqlConn.getConn();
            stmt = conn.createStatement();
            ResultSet rs = stmt.executeQuery(query);
            String currentUser = null;
            while(rs.next()){                         
                Usuario usuario = new Usuario(Integer.parseInt(rs.getString("id")), rs.getString("dni"), rs.getString("name"), rs.getString("lastname1"), rs.getString("lastname2"), rs.getString("login"), rs.getString("password"));
                usuarios.add(usuario);
            }
            
            this.conn.close();            
        } catch (SQLException ex) {
            Logger.getLogger(Controller.class.getName()).log(Level.SEVERE, null, ex);
        }              
        return usuarios;  
    
    }
    
    public String getAllVideos(){
        String query="SELECT * FROM videos";
        Statement stmt= null;
        ArrayList<Video> videos = new ArrayList<>();
        ArrayList<Usuario> usuarios = new ArrayList<>();
        String cadena = "";
        try {
            this.conn = MysqlConn.getConn();
            stmt = conn.createStatement();
            ResultSet rs = stmt.executeQuery(query);
            String currentUser = null;
            while(rs.next()){                         
                Video video = new Video(Integer.parseInt(rs.getString("idvideo")), Integer.parseInt(rs.getString("idusuario")), rs.getString("rutaserver"), rs.getString("titulo"), rs.getString("descripcion"));
                videos.add(video);
            }           
            this.conn.close();            
        } catch (SQLException ex) {
            Logger.getLogger(Controller.class.getName()).log(Level.SEVERE, null, ex);
        }   
        usuarios = this.getAllUsers();
        for(Usuario usuario : usuarios){
            int id = usuario.getId();
            for(Video video : videos){
                if(video.getIdUsuario() == id){
                    usuario.getVideos().add(video);
                }
            }
        }
        
        for(Usuario usuario : usuarios){
            cadena = cadena + usuario.getAllPreparedString();
        }

        return cadena;
       
    }

    
    public String getLogData() {
        return this.logedData[0] + "#" + this.logedData[1] + "#" + this.logedData[2] + "#" + this.logedData[3] + "#" + this.logedData[4];
    }

    public String getVideo(int id) {
        String query="SELECT * FROM videos WHERE IDVIDEO = " + id;
        Statement stmt= null;
        String datos = null;
        try {
            this.conn = MysqlConn.getConn();
            stmt = conn.createStatement();
        
            ResultSet rs = stmt.executeQuery(query);
           
            while(rs.next()){              
                datos = rs.getString("rutaserver");
            }
            this.conn.close();            
        } catch (SQLException ex) {
            Logger.getLogger(Controller.class.getName()).log(Level.SEVERE, null, ex);
        }
        return datos;      
    }
    
    public Usuario getCurrentUser(String userLogin){ 
        //RECONSTRUIR CON
        String query="SELECT * FROM usuarios WHERE LOGIN = '"+userLogin+"'";
        Statement stmt= null;       
        try {
            this.conn = MysqlConn.getConn();
            stmt = conn.createStatement();
        
            ResultSet rs = stmt.executeQuery(query);       
            rs.next();
            String first = rs.getString("login");
            String last = rs.getString("password");
                    this.logedData[0] = rs.getString("dni");
                    this.logedData[1] = rs.getString("name");
                    this.logedData[2] = rs.getString("lastname1");
                    this.logedData[3] = rs.getString("lastname2");
                    this.logedData[4] = first;
                    this.user = new Usuario(Integer.parseInt(rs.getString("id")), this.logedData[0], this.logedData[1], this.logedData[2], this.logedData[3], this.logedData[4], last);
            this.conn.close();            
        
        }catch (SQLException ex) {
            Logger.getLogger(Controller.class.getName()).log(Level.SEVERE, null, ex);
        }     
        
        return this.user;
    }

    public void uploadVideo(String titulo, String desc, String ruta, int id) {
        boolean registered = false;
        Statement stmt= null;              
        String query="INSERT INTO videos (IDUSUARIO,RUTASERVER,TITULO,DESCRIPCION) VALUES ('"+ id +"','"+ruta+"','"+titulo+"','"+desc+"')";           
        try {         
            this.conn = MysqlConn.getConn();
            stmt = conn.createStatement();
            PreparedStatement pps=conn.prepareStatement(query);
            pps.executeUpdate();
            this.conn.close();   
            registered = true;
        } catch (SQLException ex) {
           ex.printStackTrace();
        }          
    }
    
    public void deleteVideo(int id) {    
        File file = new File(this.getVideo(id));
        if(file.exists())
            file.delete();
        String consulta = "DELETE FROM videos where idvideo LIKE "+id+";";
        Statement stmt= null;
        String datos = null;
        try {
            this.conn = MysqlConn.getConn();
            stmt = conn.createStatement();
            PreparedStatement pps=conn.prepareStatement(consulta);
            pps.executeUpdate();         
            this.conn.close();            
        } catch (SQLException ex) {
            Logger.getLogger(Controller.class.getName()).log(Level.SEVERE, null, ex);
        }  
    }
       
}
