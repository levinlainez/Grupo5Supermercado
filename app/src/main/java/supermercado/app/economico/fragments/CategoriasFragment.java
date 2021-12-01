package supermercado.app.economico.fragments;


import android.os.AsyncTask;
import android.os.Bundle;
import android.support.design.widget.Snackbar;
import android.support.v4.app.Fragment;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import supermercado.app.economico.R;
import supermercado.app.economico.adapters.CategoriasAdapter;
import supermercado.app.economico.api.Api;
import supermercado.app.economico.api.RequestHandler;
import supermercado.app.economico.models.Categorias;

/**
 * A simple {@link Fragment} subclass.
 */
public class CategoriasFragment extends Fragment implements View.OnClickListener{

    private RecyclerView mRecyclerView;
    private RecyclerView.Adapter mAdapter;
    private RecyclerView.LayoutManager mlayoutManager;

    private EditText titulo;
    private Button boton;

    private boolean isUpdate = false;

    private List<Categorias> categoriasList;

    private int ide;

    public CategoriasFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_categorias, container, false);

        mRecyclerView = (RecyclerView)view.findViewById(R.id.recyclerViewCategoria);
        titulo = (EditText)view.findViewById(R.id.editTextAddUpdateCategorias);
        boton = (Button)view.findViewById(R.id.buttonaddUpdateCategoria);
        boton.setOnClickListener(this);
        boton.setText(getString(R.string.agregar_label));

        categoriasList = new ArrayList<>();
        readCategoria();

        return view;
    }

    public void createCategoria(){
        String titulos = titulo.getText().toString().trim();

        if(TextUtils.isEmpty(titulos)){
            titulo.setError(getString(R.string.escribe_un_titulo_label));
            titulo.requestFocus();
            return;
        }

        HashMap<String, String> params = new HashMap<>();
        params.put("titulo", titulos);

        PerformNetworkRequest request = new PerformNetworkRequest(Api.URL_CREATE_CATEGORIA, params, Api.CODE_POST_REQUEST);
        request.execute();

        Snackbar.make(getView(), "haz agregado categoria: " + titulos + " correctamente!!!", Snackbar.LENGTH_LONG).show();
    }

    public void readCategoria(){
        PerformNetworkRequest request = new PerformNetworkRequest(Api.URL_READ_CATEGORIA, null, Api.CODE_GET_REQUEST);
        request.execute();
    }

    public void updateCategoria(int id){
        String identificador = String.valueOf(id);
        String titulos = titulo.getText().toString().trim();

        if(TextUtils.isEmpty(titulos)){
            titulo.setError(getString(R.string.escribe_un_titulo_label));
            titulo.requestFocus();
            return;
        }

        HashMap<String, String> params = new HashMap<>();
        params.put("id", identificador);
        params.put("titulo", titulos);

        PerformNetworkRequest request = new PerformNetworkRequest(Api.URL_UPDATE_CATEGORIA, params, Api.CODE_POST_REQUEST);
        request.execute();

        isUpdate = false;
        boton.setText(getString(R.string.agregar_label));
        ide = 0;
        titulo.setText("");

        Snackbar.make(getView(), "haz aditado correctamente!!!", Snackbar.LENGTH_LONG).show();
    }

    public void deleteCategoria(int id){
        PerformNetworkRequest request = new PerformNetworkRequest(Api.URL_DELETE_CATEGORIA + id, null, Api.CODE_GET_REQUEST);
        request.execute();
    }

    private void refreshContenidoList(JSONArray contenido) throws JSONException{
        categoriasList.clear();

        for(int i = 0; i < contenido.length(); i++){
            JSONObject obj = contenido.getJSONObject(i);

            categoriasList.add(new Categorias(
                    obj.getInt("id"),
                    obj.getString("titulo")
            ));
        }

        //crar el adaptador y configurarlo en la vista de la lista

        mlayoutManager = new LinearLayoutManager(getContext());

        mAdapter = new CategoriasAdapter(categoriasList, R.layout.list_view_categorias, new CategoriasAdapter.OnClickListener() {
            @Override
            public void onItemClick(Categorias categorias, int position) {
                boton.setText(getString(R.string.editar_label));
                ide = categorias.getId();
                isUpdate = true;
                titulo.setText(categorias.getTitulo());

            }
        },
                new CategoriasAdapter.OnLongClickListener() {
                    @Override
                    public void onLongItemClick(final Categorias categorias, int position) {

                        Snackbar.make(getView(), "Quiere eliminar la categoria: " + categorias.getTitulo() + "?",
                                Snackbar.LENGTH_LONG).setAction("ELIMINAR", new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                deleteCategoria(categorias.getId());
                                Snackbar.make(getView(), "categoria " + categorias.getTitulo() + " eliminada correctamente",
                                        Snackbar.LENGTH_LONG).show();
                            }
                        }).setActionTextColor(getResources().getColor(R.color.colorPrimaryDark))
                                .show();


                    }
                });

        mRecyclerView.setHasFixedSize(true);
        mRecyclerView.setItemAnimator(new DefaultItemAnimator());

        mRecyclerView.setLayoutManager(mlayoutManager);
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

    @Override
    public void onClick(View v) {
        if(isUpdate){
            updateCategoria(ide);
        }else{
            createCategoria();
        }
    }
}
