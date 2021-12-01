package supermercado.app.economico.models;

public class Categorias {

    private int id;
    private String titulo;

    public Categorias(int id, String titulo){
        setId(id);
        setString(titulo);
    }

    public int getId(){
        return this.id;
    }

    private void setId(int id){
        this.id = id;
    }

    public String getTitulo(){
        return this.titulo;
    }

    private void setString(String titulo){
        this.titulo = titulo;
    }

}
