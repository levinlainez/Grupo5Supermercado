package supermercado.app.economico.activities;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.AsyncTask;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.squareup.picasso.Picasso;

import java.util.HashMap;

import io.realm.Realm;
import io.realm.RealmChangeListener;
import io.realm.RealmResults;
import supermercado.app.economico.R;
import supermercado.app.economico.api.Api;
import supermercado.app.economico.api.RequestHandler;
import supermercado.app.economico.models.Carrito;
import supermercado.app.economico.models.Deseos;
import supermercado.app.economico.utils.DateTime;
import supermercado.app.economico.utils.SharedPrefManager;

public class VerProductoActivity extends AppCompatActivity implements View.OnClickListener,
        RealmChangeListener<RealmResults<Deseos>>{

    private TextView titulo;
    private TextView descripcion;
    private TextView precio;
    private TextView contenido;

    private ImageView imagen;
    private ImageView imagenDeseo;

    private FloatingActionButton fabCompra;


    private MenuItem loginMenuItem;
    private MenuItem signInMenuItem;
    private MenuItem carritoMenuItem;
    private MenuItem administrativoMenuItem;
    private MenuItem perfilMenuItem;
    private MenuItem salirMenuItem;

    private int keyID;
    private String keyTitulo;
    private String keyImagen;
    private double keycosto;

    private boolean verificarDeseo = false;

    private Realm realm;
    private RealmResults<Deseos> deseoList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_ver_producto);
        setToolbar();

        realm = Realm.getDefaultInstance();
        deseoList = realm.where(Deseos.class).findAll();
        deseoList.addChangeListener(this);

        Bundle bundle = getIntent().getExtras();
        inicializaciones();

        keyID = bundle.getInt("id");
        keyTitulo = bundle.getString("titulo");
        keyImagen = bundle.getString("imagen");
        keycosto = bundle.getDouble("precio");

        titulo.setText(keyTitulo);
        descripcion.setText(bundle.getString("descripcion"));
        precio.setText("precio: $" + keycosto);
        contenido.setText(bundle.getString("contenido"));

        fabCompra.setOnClickListener(this);
        imagenDeseo.setOnClickListener(this);

        Picasso.get().load(R.drawable.deseo_apagado).into(imagenDeseo);

        for (int i = 0; i < deseoList.size(); i++){
            if(keyID == deseoList.get(i).getIdeWeb()){
                Picasso.get().load(R.drawable.deseo_activo).into(imagenDeseo);
                verificarDeseo = true;
            }
        }

        Picasso.get().load(Api.GALERIA+keyImagen).fit().into(imagen);
    }

    private void setToolbar(){
        Toolbar myToolbar = (Toolbar)findViewById(R.id.toolbarProductos);
        setSupportActionBar(myToolbar);
    }

    public void inicializaciones(){
        titulo = (TextView)findViewById(R.id.textViewVerTitulo);
        descripcion = (TextView)findViewById(R.id.textViewVerDescripcion);
        precio = (TextView)findViewById(R.id.textViewVerPrecio);
        contenido = (TextView)findViewById(R.id.textViewVerContenido);
        fabCompra = (FloatingActionButton)findViewById(R.id.fabComprarVerProducto);

        imagen = (ImageView)findViewById(R.id.imagenViewVerProducto);
        imagenDeseo = (ImageView)findViewById(R.id.imageViewDeseos);
    }

    private void agregarProductoAlCarrito(String producto, String imagen, Double costo){

        realm.beginTransaction();
        Carrito carrito = new Carrito(producto, imagen, costo);
        realm.copyToRealm(carrito);
        realm.commitTransaction();
    }

    private void agregarProductoADeseos(int id, String producto, String imagen, Double costo){

        realm.beginTransaction();
        Deseos deseos = new Deseos(producto, imagen, id, costo);
        realm.copyToRealm(deseos);
        realm.commitTransaction();

        Picasso.get().load(R.drawable.deseo_activo).fit().into(imagenDeseo);
        Toast.makeText(VerProductoActivity.this, "producto agregado a tu lista de deseo", Toast.LENGTH_SHORT).show();
    }

    private void createVenta(){
        String usuarioConversion = String.valueOf(SharedPrefManager.getmInstance(VerProductoActivity.this).getUser().getId());
        String titulo = keyTitulo;
        String imagen = keyImagen;
        String costoConversion = String.valueOf(keycosto);

        HashMap<String, String> params = new HashMap<>();
        params.put("usuario", usuarioConversion);
        params.put("producto", titulo);
        params.put("imagen", imagen);
        params.put("costos", costoConversion);
        params.put("fecha", DateTime.getDateTime());

        PerformNetworkRequest request = new PerformNetworkRequest(Api.URL_CREATE_VENTAS, params, Api.CODE_POST_REQUEST);
        request.execute();

        Toast.makeText(VerProductoActivity.this, "Compra Realizada", Toast.LENGTH_LONG).show();
    }

    private void showInforAlertCompra(){
        new AlertDialog.Builder(VerProductoActivity.this)
                .setTitle("Estas a punto de realizar una compra!!!")
                .setMessage("Si quieres comprar este producto haz Click en OK")
                .setPositiveButton("OK", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        createVenta();
                    }
                })
                .setNegativeButton("CANCEL", null)
                .show();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.action_menu_ver_producto, menu);
        this.loginMenuItem = menu.findItem(R.id.loginActionBar);
        this.signInMenuItem = menu.findItem(R.id.signInActionBar);
        this.carritoMenuItem = menu.findItem(R.id.verProductoActionBar);
        this.perfilMenuItem = menu.findItem(R.id.perfilActionBar);
        this.administrativoMenuItem = menu.findItem(R.id.administrativoActionBar);
        this.salirMenuItem = menu.findItem(R.id.logOutActionBar);

        if(SharedPrefManager.getmInstance(VerProductoActivity.this).isLoggedIn()){

            if(SharedPrefManager.getmInstance(VerProductoActivity.this).getUser().getRole().equals("administrador")){
                this.loginMenuItem.setVisible(false);
                this.signInMenuItem.setVisible(false);
                this.perfilMenuItem.setVisible(false);
                this.administrativoMenuItem.setVisible(true);
                this.salirMenuItem.setVisible(true);
                this.carritoMenuItem.setVisible(false);
            }else{
                this.loginMenuItem.setVisible(false);
                this.signInMenuItem.setVisible(false);
                this.perfilMenuItem.setVisible(true);
                this.administrativoMenuItem.setVisible(false);
                this.salirMenuItem.setVisible(true);
                this.carritoMenuItem.setVisible(true);
            }

        }else{
            this.loginMenuItem.setVisible(true);
            this.signInMenuItem.setVisible(true);
            this.perfilMenuItem.setVisible(false);
            this.administrativoMenuItem.setVisible(false);
            this.salirMenuItem.setVisible(false);
            this.carritoMenuItem.setVisible(true);
        }

        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()){
            case R.id.loginActionBar:
                startActivity(new Intent(VerProductoActivity.this, LoginActivity.class));
                return true;

            case R.id.signInActionBar:
                startActivity(new Intent(VerProductoActivity.this, RegistrarseActivity.class));
                return true;

            case R.id.logOutActionBar:
                SharedPrefManager.getmInstance(VerProductoActivity.this).logOut();
                return true;

            case R.id.administrativoActionBar:
                startActivity(new Intent(VerProductoActivity.this, AdministradorActivity.class));
                return true;

            case R.id.perfilActionBar:
                startActivity(new Intent(VerProductoActivity.this, PerfilActivity.class));
                return true;
            case R.id.verProductoActionBar:
                if(SharedPrefManager.getmInstance(VerProductoActivity.this).isLoggedIn()
                        && SharedPrefManager.getmInstance(VerProductoActivity.this).getUser().getRole().equals("cliente")){
                    agregarProductoAlCarrito(keyTitulo, keyImagen, keycosto);
                    Toast.makeText(VerProductoActivity.this, "Agregado al carrito", Toast.LENGTH_LONG).show();
                }else{
                    Toast.makeText(VerProductoActivity.this, "estas como administrador", Toast.LENGTH_LONG).show();
                }
                return true;

            default:
                return super.onOptionsItemSelected(item);
        }

    }

    @Override
    public void onClick(View view) {

        switch (view.getId()){
            case R.id.fabComprarVerProducto:
                if(SharedPrefManager.getmInstance(VerProductoActivity.this).isLoggedIn()
                        && SharedPrefManager.getmInstance(VerProductoActivity.this).getUser().getRole().equals("cliente")){
                    showInforAlertCompra();
                }else{
                    Toast.makeText(VerProductoActivity.this, "Estas como administrador", Toast.LENGTH_LONG).show();
                }
                break;
            case R.id.imageViewDeseos:
                if(SharedPrefManager.getmInstance(VerProductoActivity.this).isLoggedIn()
                        && SharedPrefManager.getmInstance(VerProductoActivity.this).getUser().getRole().equals("cliente")){
                    if (verificarDeseo){
                        startActivity(new Intent(VerProductoActivity.this, PerfilActivity.class));
                    }else{
                        agregarProductoADeseos(keyID, keyTitulo, keyImagen, keycosto);
                    }
                }else{
                    Toast.makeText(VerProductoActivity.this, "Estas como administrador", Toast.LENGTH_LONG).show();
                }
                break;
        }
    }

    @Override
    public void onChange(RealmResults<Deseos> deseos) {}

    //clase interna para realizar la solicitud de red extendiendo un AsyncTask
    private class PerformNetworkRequest extends AsyncTask<Void, Void, String> {

        //la url donde nececitamos enviar la solicitud
        String url;

        //parametros
        HashMap<String, String> params;

        //el codigo de solicitud para definir si se trata de un GET o POST
        int requestCode;

        //contructor para inicializar los valores
        PerformNetworkRequest(String url, HashMap<String, String> params, int requestCode){
            this.url = url;
            this.params = params;
            this.requestCode = requestCode;
        }

        //la operacion de red se realizará en segundo plano
        @Override
        protected String doInBackground(Void... voids) {

            RequestHandler requestHandler = new RequestHandler();

            if(requestCode == Api.CODE_POST_REQUEST)
                return requestHandler.sendPostRequest(url, params);

            if ((requestCode == Api.CODE_GET_REQUEST))
                return requestHandler.sendGetRequest(url);

            return null;
        }
    }
}
