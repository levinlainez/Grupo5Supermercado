package supermercado.app.economico.models;

import io.realm.RealmObject;
import io.realm.annotations.PrimaryKey;
import io.realm.annotations.Required;
import supermercado.app.economico.utils.MyApp;

public class Deseos extends RealmObject{
    @PrimaryKey
    private int id;
    @Required
    private String producto;
    @Required
    private String imagen;
    private int ideWeb;
    private double costo;

    public Deseos(){}

    public Deseos(String producto, String imagen, int ideWeb, Double costo){
        this.id = MyApp.DeseoID.incrementAndGet();
        this.producto = producto;
        this.imagen = imagen;
        this.ideWeb = ideWeb;
        this.costo = costo;
    }

    public int getId() {
        return id;
    }

    public String getProducto() {
        return producto;
    }

    public void setProducto(String producto) {
        this.producto = producto;
    }

    public String getImagen() {
        return imagen;
    }

    public int getIdeWeb() {
        return ideWeb;
    }

    public void setIdeWeb(int ideWeb) {
        this.ideWeb = ideWeb;
    }

    public void setImagen(String imagen) {
        this.imagen = imagen;
    }

    public double getCosto() {
        return costo;
    }

    public void setCosto(double costo) {
        this.costo = costo;
    }
}
