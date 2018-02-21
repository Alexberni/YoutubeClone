/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Models;

import java.util.ArrayList;

/**
 *
 * @author Alex
 */
public class Usuario {

    private int id;
    private String dni;
    private String name;
    private String lastname1;
    private String lastname2;
    private String login;
    private String password;    
    private ArrayList<Video> videos = new ArrayList<>();
    
    public Usuario(int id, String dni, String name, String lastname1, String lastname2, String login, String password){
        this.id = id;
        this.dni = dni;
        this.name = name;
        this.lastname1 = lastname1;
        this.lastname2 = lastname2;
        this.login = login;
        this.password = password;
    }
    
    public Usuario(){
        this.login = "Unknown User";
    }
    
    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getDni() {
        return dni;
    }

    public void setDni(String dni) {
        this.dni = dni;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getLastname1() {
        return lastname1;
    }

    public void setLastname1(String lastname1) {
        this.lastname1 = lastname1;
    }

    public String getLastname2() {
        return lastname2;
    }

    public void setLastname2(String lastname2) {
        this.lastname2 = lastname2;
    }

    public String getLogin() {
        return login;
    }

    public void setLogin(String login) {
        this.login = login;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }
    
    public ArrayList<Video> getVideos() {
        return videos;
    }

    public void setVideos(ArrayList<Video> videos) {
        this.videos = videos;
    }
    
    public String getAllPreparedString(){
        String cadena = "#" + this.login;
        for(Video video : this.videos){
            cadena = cadena + "@" + video.getId() + "&" + video.getTitulo() + "&" + video.getDescripcion();
        }
        return cadena;
    }
}
