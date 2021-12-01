package supermercado.app.economico.models;

public class Usuarios {

    private int id;
    private String usuario;
    private String password;
    private String role;
    private String mail;

    public Usuarios(int id, String usuario, String password, String role, String mail){
        setId(id);
        setUsuario(usuario);
        setPassword(password);
        setRole(role);
        setMail(mail);
    }

    // fn + alt + ins ó click derecho
    public int getId() {
        return id;
    }

    private void setId(int id) {
        this.id = id;
    }

    public String getUsuario() {
        return usuario;
    }

    private void setUsuario(String usuario) {
        this.usuario = usuario;
    }

    public String getPassword() {
        return password;
    }

    private void setPassword(String password) {
        this.password = password;
    }

    public String getRole() {
        return role;
    }

    private void setRole(String role) {
        this.role = role;
    }

    public String getMail() {
        return mail;
    }

    private void setMail(String mail) {
        this.mail = mail;
    }
}
