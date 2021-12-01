package supermercado.app.economico.activities;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.AsyncTask;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.Toast;

import java.util.HashMap;

import io.realm.Realm;
import io.realm.RealmChangeListener;
import io.realm.RealmResults;
import supermercado.app.economico.R;
import supermercado.app.economico.adapters.CarritoAdapter;
import supermercado.app.economico.api.Api;
import supermercado.app.economico.api.RequestHandler;
import supermercado.app.economico.models.Carrito;
import supermercado.app.economico.utils.DateTime;
import supermercado.app.economico.utils.SharedPrefManager;

public class CarritoActivity extends AppCompatActivity implements RealmChangeListener<RealmResults<Carrito>>,
        View.OnClickListener{

    private RecyclerView mRecyclerView;
    private RecyclerView.Adapter mAdapter;
    private RecyclerView.LayoutManager mLayoutManager;

    private Realm realm;
    private RealmResults<Carrito> carritoList;

    private FloatingActionButton fab;

    private MenuItem loginMenuItem;
    private MenuItem signInMenuItem;
    private MenuItem perfilMenuItem;
    private MenuItem salirMenuItem;

    private Toolbar myToolbar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_carrito);

        setToolbar();

        mRecyclerView = (RecyclerView)findViewById(R.id.recyclerViewCarrito);
        fab = (FloatingActionButton)findViewById(R.id.fabCarrito);

        if(SharedPrefManager.getmInstance(CarritoActivity.this).isLoggedIn()){
            fab.setImageResource(R.mipmap.ic_ventas_on);
        }else {
            fab.setImageResource(R.mipmap.ic_venta_off);
        }

        fab.setOnClickListener(this);
        setHideShowFAB();

        realm = Realm.getDefaultInstance();
        carritoList = realm.where(Carrito.class).findAll();
        carritoList.addChangeListener(this);

        //crear el adaptador y configurar en la vista nuestra lista de informacion que llega en el formato json
        mLayoutManager = new LinearLayoutManager(this);

        mAdapter = new CarritoAdapter(carritoList, R.layout.card_view_carrito, new CarritoAdapter.OnClickListener() {
            @Override
            public void onItemClick(Carrito carrito, int position) {
                Toast.makeText(CarritoActivity.this, "el precio: " + carrito.getCosto(), Toast.LENGTH_LONG).show();
            }
        }, new CarritoAdapter.OnLongClickListener() {
            @Override
            public void onItemClick(final Carrito carrito, int position) {
                final String nombre = carrito.getProducto();
                Snackbar.make(findViewById(R.id.CarritoActivityID), "Quieres eliminar el producto: " + nombre
                        + " de tu carrito?", Snackbar.LENGTH_LONG).setAction("ELIMINAR", new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        realm.beginTransaction();
                        carrito.deleteFromRealm();
                        realm.commitTransaction();

                        Snackbar.make(findViewById(R.id.CarritoActivityID), "producto: " + nombre
                                + " retirado correctamente", Snackbar.LENGTH_LONG).show();
                    }
                }).setActionTextColor(getResources().getColor(R.color.colorPrimaryDark))
                        .show();
            }
        });

        mRecyclerView.setHasFixedSize(true);
        mRecyclerView.setItemAnimator(new DefaultItemAnimator());

        mRecyclerView.setLayoutManager(mLayoutManager);
        mRecyclerView.setAdapter(mAdapter);
    }

    private void setToolbar(){
        myToolbar = (android.support.v7.widget.Toolbar)findViewById(R.id.toolbarCarritoMenu);
        setSupportActionBar(myToolbar);
    }

    private void setHideShowFAB(){
        mRecyclerView.addOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrolled(RecyclerView recyclerView, int dx, int dy) {
                if(dy > 0){
                    fab.hide();
                }else if (dy < 0){
                    fab.show();
                }
            }
        });
    }

    private void createVenta(String productos, String imagenes, double costos){
        String usuarioConversion = String.valueOf(SharedPrefManager.getmInstance(CarritoActivity.this).getUser().getId());
        String titulo = productos;
        String imagen = imagenes;
        String costoConversion = String.valueOf(costos);

        HashMap<String, String> params = new HashMap<>();
        params.put("usuario", usuarioConversion);
        params.put("producto", titulo);
        params.put("imagen", imagen);
        params.put("costos", costoConversion);
        params.put("fecha", DateTime.getDateTime());

        PerformNetworkRequest request = new PerformNetworkRequest(Api.URL_CREATE_VENTAS, params, Api.CODE_POST_REQUEST);
        request.execute();
    }

    private String costoTotal(){
        double total = 0;
        for(int position = 0; position < carritoList.size(); position++){
            total += carritoList.get(position).getCosto();
        }

        return String.valueOf(total);
    }

    private void showInforAlertCompra(){
        new AlertDialog.Builder(CarritoActivity.this)
                .setTitle("Todos los productos serán comprados!!!")
                .setMessage("El costo será de" + costoTotal() + " pesos, quieres continuar")
                .setPositiveButton("OK", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {

                        for(int position = 0; position < carritoList.size(); position++){
                            createVenta(carritoList.get(position).getProducto(),
                                    carritoList.get(position).getImagen(),
                                    carritoList.get(position).getCosto());
                        }

                        Toast.makeText(CarritoActivity.this, "Compras Realizadas", Toast.LENGTH_LONG).show();

                        realm.beginTransaction();
                        realm.deleteAll();
                        realm.commitTransaction();
                    }
                })
                .setNegativeButton("CANCEL", null)
                .show();
    }

    private void showInforAlertDelete(){
        new AlertDialog.Builder(CarritoActivity.this)
                .setTitle("Eliminar todo el carrito?")
                .setMessage("Todos los productos que fueron agregados serán eliminados y " +
                        "no podrás deshacer esta acción, ¿Quieres continuar?")
                .setPositiveButton("OK", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        realm.beginTransaction();
                        realm.deleteAll();
                        realm.commitTransaction();
                    }
                })
                .setNegativeButton("CANCEL", null)
                .show();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.action_menu_carrito, menu);
        this.loginMenuItem = menu.findItem(R.id.loginActionBarCarrito);
        this.signInMenuItem = menu.findItem(R.id.signInActionBarCarrito);
        this.perfilMenuItem = menu.findItem(R.id.perfilActionBarCarrito);
        this.salirMenuItem = menu.findItem(R.id.logOutActionBarCarrito);

        if(SharedPrefManager.getmInstance(CarritoActivity.this).isLoggedIn()){

            if(SharedPrefManager.getmInstance(CarritoActivity.this).getUser().getRole().equals("administrador")){
                this.loginMenuItem.setVisible(false);
                this.signInMenuItem.setVisible(false);
                this.perfilMenuItem.setVisible(false);
                this.salirMenuItem.setVisible(true);
            }else{
                this.loginMenuItem.setVisible(false);
                this.signInMenuItem.setVisible(false);
                this.perfilMenuItem.setVisible(true);
                this.salirMenuItem.setVisible(true);
            }

        }else{
            this.loginMenuItem.setVisible(true);
            this.signInMenuItem.setVisible(true);
            this.perfilMenuItem.setVisible(false);
            this.salirMenuItem.setVisible(false);
        }

        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()){
            case R.id.loginActionBarCarrito:
                startActivity(new Intent(CarritoActivity.this, LoginActivity.class));
                return true;
            case R.id.signInActionBarCarrito:
                startActivity(new Intent(CarritoActivity.this, RegistrarseActivity.class));
                return true;
            case R.id.logOutActionBarCarrito:
                SharedPrefManager.getmInstance(CarritoActivity.this).logOut();
                return true;
            case R.id.perfilActionBarCarrito:
                startActivity(new Intent(CarritoActivity.this, PerfilActivity.class));
                return true;
            case R.id.allDeleteActionBarCarrito:
                showInforAlertDelete();
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    @Override
    public void onClick(View v) {
        if(SharedPrefManager.getmInstance(CarritoActivity.this).isLoggedIn()
                && SharedPrefManager.getmInstance(CarritoActivity.this).getUser().getRole().equals("cliente")
                && carritoList.size() > 0){
            showInforAlertCompra();
        }else{
            Toast.makeText(CarritoActivity.this, "logueate", Toast.LENGTH_LONG).show();
        }
    }

    @Override
    public void onChange(RealmResults<Carrito> carritos) { mAdapter.notifyDataSetChanged(); }

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
