package Models;

/**
 *
 * @author Alex Benavides
 */
public class Video {
    private int id;
    private int idUsuario;
    private String ruta;
    private String titulo;
    private String descripcion;
    
    public Video(int id, int idUsuario, String ruta, String titulo, String desc){
        this.id = id;
        this.idUsuario = idUsuario;
        this.ruta = ruta;
        this.titulo = titulo;
        this.descripcion = desc;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getIdUsuario() {
        return idUsuario;
    }

    public void setIdUsuario(int idUsuario) {
        this.idUsuario = idUsuario;
    }

    public String getRuta() {
        return ruta;
    }

    public void setRuta(String ruta) {
        this.ruta = ruta;
    }

    public String getTitulo() {
        return titulo;
    }

    public void setTitulo(String titulo) {
        this.titulo = titulo;
    }

    public String getDescripcion() {
        return descripcion;
    }

    public void setDescripcion(String descripcion) {
        this.descripcion = descripcion;
    }
   
}
