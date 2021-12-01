package supermercado.app.economico.fragments;


import android.os.Bundle;
import android.support.design.widget.Snackbar;
import android.support.v4.app.Fragment;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import io.realm.Realm;
import io.realm.RealmChangeListener;
import io.realm.RealmResults;
import supermercado.app.economico.R;
import supermercado.app.economico.adapters.DeseoAdapter;
import supermercado.app.economico.models.Deseos;

/**
 * A simple {@link Fragment} subclass.
 */
public class DeseosFragment extends Fragment implements RealmChangeListener<RealmResults<Deseos>>{

    private RecyclerView mRecyclerView;
    private RecyclerView.Adapter mAdapter;
    private RecyclerView.LayoutManager mLayoutManager;

    private Realm realm;
    private RealmResults<Deseos> deseosList;


    public DeseosFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view =  inflater.inflate(R.layout.fragment_deseos, container, false);

        mRecyclerView = (RecyclerView)view.findViewById(R.id.recyclerViewDeseo);

        realm = Realm.getDefaultInstance();
        deseosList = realm.where(Deseos.class).findAll();
        deseosList.addChangeListener(this);

        //crear el adaptador y configurar en la vista nuestra lista de informacion que llega en el formato json
        mLayoutManager = new LinearLayoutManager(getContext());

        mAdapter = new DeseoAdapter(deseosList, R.layout.card_view_deseos, new DeseoAdapter.OnClickListener() {
            @Override
            public void onItemClick(Deseos deseos, int position) {
                Toast.makeText(getActivity(), "id: " + deseos.getId()
                + "\nTitulo: " + deseos.getProducto() + "\nImagen: " + deseos.getImagen()
                        + "\nCosto: " + deseos.getCosto(), Toast.LENGTH_LONG).show();
            }
        }, new DeseoAdapter.OnLongClickListener() {
            @Override
            public void onItemClick(final Deseos deseos, int position) {
                final String nombre = deseos.getProducto();
                Snackbar.make(getView(), "Quieres eliminar el producto: " + nombre
                        + " de tu lista de deseos?", Snackbar.LENGTH_LONG).setAction("ELIMINAR", new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        realm.beginTransaction();
                        deseos.deleteFromRealm();
                        realm.commitTransaction();

                        Snackbar.make(getView(), "producto: " + nombre
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

        return view;
    }

    @Override
    public void onChange(RealmResults<Deseos> deseos) {
        mAdapter.notifyDataSetChanged();
    }
}
