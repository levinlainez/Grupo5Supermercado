package owl.app.catalogo.models;

public class Productos {

    private int id;
    private String titulo;
    private String descripcion;
    private String contenido;
    private String imagen;
    private double precio;
    private double calificacion;
    private int categoria;

    public Productos(int id, String titulo, String descripcion, String contenido, String imagen,
                     double precio, double calificacion, int categoria){

        this.id = id;
        this.titulo = titulo;
        this.descripcion = descripcion;
        this.contenido = contenido;
        this.imagen = imagen;
        this.precio = precio;
        this.calificacion = calificacion;
        this.categoria = categoria;
    }

    public int getId(){return id;}

    public String getTitulo(){return titulo;}

    public String getDescripcion(){return descripcion;}

    public String getContenido(){return contenido;}

    public String getImagen(){return imagen;}

    public double getPrecio(){return precio;}

    public double getCalificacion(){return calificacion;}

    public int getCategoria(){return categoria;}


}
