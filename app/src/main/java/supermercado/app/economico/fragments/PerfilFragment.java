package supermercado.app.economico.fragments;


import android.content.DialogInterface;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.Fragment;
import android.support.v7.app.AlertDialog;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;


import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import io.realm.Realm;
import io.realm.RealmResults;
import supermercado.app.economico.R;
import supermercado.app.economico.api.Api;
import supermercado.app.economico.api.RequestHandler;
import supermercado.app.economico.models.Carrito;
import supermercado.app.economico.models.Deseos;
import supermercado.app.economico.models.Ventas;
import supermercado.app.economico.utils.DateTime;
import supermercado.app.economico.utils.SharedPrefManager;

/**
 * A simple {@link Fragment} subclass.
 */
public class PerfilFragment extends Fragment implements View.OnClickListener{

    private View view;

    private TextView perfilNombre;
    private TextView montoTotalProducto;
    private TextView cantidadTotalProducto;
    private TextView contarCarrito;
    private TextView contarDeseos;

    private FloatingActionButton fabCarrito;
    private FloatingActionButton fabDeseo;

    private List<Ventas> ventasList;

    private Realm realm;
    private RealmResults<Carrito> carritoList;
    private RealmResults<Deseos> deseoList;

    public PerfilFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        view = inflater.inflate(R.layout.fragment_perfil, container, false);
        inicializaciones();
        ventasList = new ArrayList<>();

        perfilNombre.setText(SharedPrefManager.getmInstance(getContext()).getUser().getUsuario());
        contarCarrito.setText(contadorProductos(true));
        contarDeseos.setText(contadorProductos(false));

        fabCarrito.setOnClickListener(this);
        fabDeseo.setOnClickListener(this);

        readVentas(SharedPrefManager.getmInstance(getContext()).getUser().getId());
        return view;
    }

    public void inicializaciones(){
        perfilNombre = (TextView)view.findViewById(R.id.textViewPerfilNombre);
        montoTotalProducto = (TextView)view.findViewById(R.id.textViewPerfilMontoProductos);
        cantidadTotalProducto = (TextView)view.findViewById(R.id.textViewPerfilCantidadProductos);
        contarCarrito = (TextView)view.findViewById(R.id.textViewCarritoCantidad);
        contarDeseos = (TextView)view.findViewById(R.id.textViewDeseoCantidad);
        realm = Realm.getDefaultInstance();
        carritoList = realm.where(Carrito.class).findAll();
        deseoList = realm.where(Deseos.class).findAll();

        fabCarrito = (FloatingActionButton)view.findViewById(R.id.fabPerfilCarrito);
        fabDeseo = (FloatingActionButton)view.findViewById(R.id.fabPerfilDeseo);
    }

    public String contadorProductosVendidos(int ventas){
        String resultado = (ventas == 1) ? "producto":"productos";
        String texto = ventas + " " + resultado;
        return texto;
    }

    public String monotoTotal(List<Ventas> ventas){

        double montoTotal = 0;
        for (int i = 0; i < ventas.size(); i++) {
            montoTotal += ventas.get(i).getCosto();
        }

        String resultado = (ventas.size() < 1) ? "0 pesos" : montoTotal + " pesos";
        return resultado;
    }

    private void createVenta(String productos, String imagenes, double costos){
        String usuarioConversion = String.valueOf(SharedPrefManager.getmInstance(getContext()).getUser().getId());
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
        ventasList.clear();
    }

    private void readVentas(int id){
        PerformNetworkRequest request = new PerformNetworkRequest(Api.URL_READ_VENTAS_ESPECIFICAS + id,
                null, Api.CODE_GET_REQUEST);
        request.execute();
    }

    public String contadorProductos(boolean verificar){
        String resultado;
        String texto;
        if(verificar){
            resultado = (carritoList.size() == 1) ? "producto":"productos";
            texto = carritoList.size() + " " + resultado;
        }else{
            resultado = (deseoList.size() == 1) ? "producto":"productos";
            texto = deseoList.size() + " " + resultado;

        }
        return texto;
    }

    private String costoTotal(boolean verificar){
        double total = 0;
        if(verificar){
            for (int position = 0; position < carritoList.size(); position++){
                total += carritoList.get(position).getCosto();
            }
        }else{
            for (int position = 0; position < deseoList.size(); position++) {
                total += deseoList.get(position).getCosto();
            }
        }
        return String.valueOf(total);
    }

    private void showInforAlertComprarAll(final boolean verificar){
        new AlertDialog.Builder(getContext())
                .setTitle("Todos los productos serán comprados!!!")
                .setMessage("el costo será de: $" + costoTotal(verificar) + " pesos, quieres continuar?")
                .setPositiveButton("OK", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {

                        if(verificar){
                            for(int position = 0; position < carritoList.size(); position++){
                                createVenta(carritoList.get(position).getProducto(),
                                        carritoList.get(position).getImagen(),
                                        carritoList.get(position).getCosto());
                            }
                        }else{
                            for(int position = 0; position < deseoList.size(); position++){
                                createVenta(deseoList.get(position).getProducto(),
                                        deseoList.get(position).getImagen(),
                                        deseoList.get(position).getCosto());
                            }
                        }

                        Toast.makeText(getContext(), "compra realizada", Toast.LENGTH_LONG).show();

                        if(verificar){
                            realm.beginTransaction();
                            carritoList.deleteAllFromRealm();
                            realm.commitTransaction();
                            contarCarrito.setText("0 productos");
                        }else{
                            realm.beginTransaction();
                            deseoList.deleteAllFromRealm();
                            realm.commitTransaction();
                            contarDeseos.setText("0 productos");
                        }
                    }
                })
                .setNegativeButton("CANCEL", null)
                .show();
    }

    private void refreshContenidoList(JSONArray contenido) throws JSONException {
        //limpiar las noticias anteriores
        ventasList.clear();

        //recorrer todos los elementos de la matriz json
        //del json que recibimos la respuesta

        for(int i = 0; i < contenido.length(); i++){
            //obtener el json de nuestros productos
            JSONObject obj = contenido.getJSONObject(i);

            //añadiendo los productos de nuestro json a la clase productos
            ventasList.add(new Ventas(
                    obj.getInt("id"),
                    obj.getString("usuario"),
                    obj.getString("producto"),
                    obj.getString("imagen"),
                    obj.getDouble("costo"),
                    obj.getString("fecha")
            ));
        }

        Toast.makeText(getContext(), "total: " + ventasList.size(), Toast.LENGTH_LONG).show();

        cantidadTotalProducto.setText(contadorProductosVendidos(ventasList.size()));
        montoTotalProducto.setText(monotoTotal(ventasList));
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.fabPerfilCarrito:
                if (carritoList.isEmpty())
                    Toast.makeText(getContext(), "no hay nada en tu carrito", Toast.LENGTH_SHORT).show();
                else
                    showInforAlertComprarAll(true);
                break;
            case R.id.fabPerfilDeseo:
                if (deseoList.isEmpty())
                    Toast.makeText(getContext(), "no hay nada en tu lista de deseos", Toast.LENGTH_SHORT).show();
                else
                    showInforAlertComprarAll(false);
                break;
        }
    }

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

        //este metodo dará la respuesta de la petición

        @Override
        protected void onPostExecute(String s) {
            super.onPostExecute(s);
            try {
                JSONObject object = new JSONObject(s);
                if(!object.getBoolean("error")){
                    //refrescar la lista despues de cada operación
                    //para que obtengamos una lista actualizada
                    refreshContenidoList(object.getJSONArray("contenido"));
                }
            }catch (JSONException e){
                e.printStackTrace();
            }
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
