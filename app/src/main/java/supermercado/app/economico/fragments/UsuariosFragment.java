package supermercado.app.economico.fragments;


import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import supermercado.app.economico.R;
import supermercado.app.economico.activities.VentasEspecificasActivity;
import supermercado.app.economico.adapters.UsuariosAdapter;
import supermercado.app.economico.api.Api;
import supermercado.app.economico.api.RequestHandler;
import supermercado.app.economico.models.Usuarios;

/**
 * A simple {@link Fragment} subclass.
 */
public class UsuariosFragment extends Fragment {

    List<Usuarios> usuariosList;

    private RecyclerView mRecyclerView;
    private RecyclerView.Adapter mAdapter;
    private RecyclerView.LayoutManager mLayoutManager;


    public UsuariosFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_usuarios, container, false);

        mRecyclerView = (RecyclerView)view.findViewById(R.id.recyclerViewUsuarios);
        usuariosList = new ArrayList<>();
        readUsuarios();

        return view;
    }

    private void readUsuarios(){
        PerformNetworkRequest request = new PerformNetworkRequest(Api.URL_READ_USUARIOS, null, Api.CODE_GET_REQUEST);
        request.execute();
    }

    private void refreshContenidoList(JSONArray contenido) throws JSONException {
        //limpiar las noticias anteriores
        usuariosList.clear();

        //recorrer todos los elementos de la matriz json
        //del json que recibimos la respuesta

        for(int i = 0; i < contenido.length(); i++){
            //obtener el json de nuestros productos
            JSONObject obj = contenido.getJSONObject(i);

            //añadiendo los productos de nuestro json a la clase productos
            usuariosList.add(new Usuarios(
                    obj.getInt("id"),
                    obj.getString("usuario"),
                    obj.getString("password"),
                    obj.getString("role"),
                    obj.getString("mail")
            ));
        }

        //crear el adaptador y configurar en la vista nuestra lista de informacion que llega en el formato json
        mLayoutManager = new LinearLayoutManager(getActivity());

        mAdapter = new UsuariosAdapter(usuariosList, R.layout.card_view_usuarios, new UsuariosAdapter.OnClickListener() {
            @Override
            public void onItemClick(Usuarios usuarios, int position) {

                if(usuarios.getRole().equals("administrador")){
                    Toast.makeText(getContext(), "eres administrador", Toast.LENGTH_SHORT).show();

                }else{
                    Intent intent = new Intent(getContext(), VentasEspecificasActivity.class);
                    intent.putExtra("id", usuarios.getId());
                    startActivity(intent);
                }
                }
        });

        mRecyclerView.setHasFixedSize(true);
        mRecyclerView.setItemAnimator(new DefaultItemAnimator());

        mRecyclerView.setLayoutManager(mLayoutManager);
        mRecyclerView.setAdapter(mAdapter);

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
