package supermercado.app.economico.models;

public class Ventas {

    private int id;
    private String usuario;
    private String productos;
    private String ruta;
    private double costo;
    private String fecha;

    public Ventas(int id, String usuario, String productos, String ruta, double costo, String fecha) {
        this.id = id;
        this.usuario = usuario;
        this.productos = productos;
        this.ruta = ruta;
        this.costo = costo;
        this.fecha = fecha;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getUsuario() {
        return usuario;
    }

    public void setUsuario(String usuario) {
        this.usuario = usuario;
    }

    public String getProductos() {
        return productos;
    }

    public void setProductos(String productos) {
        this.productos = productos;
    }

    public String getRuta() {
        return ruta;
    }

    public void setRuta(String ruta) {
        this.ruta = ruta;
    }

    public double getCosto() {
        return costo;
    }

    public void setCosto(double costo) {
        this.costo = costo;
    }

    public String getFecha() {
        return fecha;
    }

    public void setFecha(String fecha) {
        this.fecha = fecha;
    }
}
