/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Models;

/**
 *
 * @author Alex
 */
public class Video {

    public String getRuta() {
        return ruta;
    }

    public void setRuta(String ruta) {
        this.ruta = ruta;
    }
    String title;
    String desc;
    int id;   
    String idOwner;
    String ruta;

    public Video(String title, String desc, int id, String idOwner){
        this.title = title;
        this.desc = desc;
        this.id = id;
        this.idOwner = idOwner;
    }
    
    public Video(String title, String desc, String idOwner, String ruta){
        this.title = title;
        this.desc = desc;
        this.idOwner = idOwner;
        this.ruta = ruta;
    }
    
    public Video(String title, String desc, String idOwner){
        this.title = title;
        this.desc = desc;
        this.id = id;
        this.idOwner = idOwner;
    }
    
    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getDesc() {
        return desc;
    }

    public void setDesc(String Desc) {
        this.desc = Desc;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getIdOwner() {
        return idOwner;
    }

    public void setIdOwner(String idOwner) {
        this.idOwner = idOwner;
    }
    
    
}
