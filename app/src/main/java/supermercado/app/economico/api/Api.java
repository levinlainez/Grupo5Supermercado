package supermercado.app.economico.api;

public class Api {


    private static final String URL_BASE = "https://supereconomico.online/";
    private static final String ROOT_URL = URL_BASE + "views/json-movil/Api.php?apicall=";

    //usuarios
    public static final String URL_CREATE_USUARIO = ROOT_URL + "createusuario";
    public static final String URL_READ_USUARIOS = ROOT_URL + "readusuarios";
    public static final String URL_LOGIN_USUARIO = ROOT_URL + "loginusuario";

    //categoria
    public static final String URL_CREATE_CATEGORIA = ROOT_URL + "createcategoria";
    public static final String URL_READ_CATEGORIA = ROOT_URL + "readcategorias";
    public static final String URL_UPDATE_CATEGORIA = ROOT_URL + "updatecategoria";
    public static final String URL_DELETE_CATEGORIA = ROOT_URL + "deletecategoria&id=";

    //productos
    public static final String URL_READ_PRODUCTOS = ROOT_URL + "readproductos";
    public static final String URL_DELETE_PRODUCTO = ROOT_URL + "deleteproducto&id=";

    //ventas
    public static final String URL_CREATE_VENTAS = ROOT_URL + "createventa";
    public static final String URL_READ_VENTAS = ROOT_URL + "readventas";
    public static final String URL_READ_VENTAS_ESPECIFICAS = ROOT_URL + "readventasespecificas&usuario=";

    //galeria
    public static final String GALERIA = URL_BASE + "backend/";

    //request codes
    public static final int CODE_GET_REQUEST = 1024;
    public static final int CODE_POST_REQUEST = 1025;

}
