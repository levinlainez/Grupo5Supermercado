package supermercado.app.economico.adapters;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.squareup.picasso.Picasso;

import java.util.List;

import supermercado.app.economico.R;
import supermercado.app.economico.api.Api;
import supermercado.app.economico.models.Ventas;

public class VentasAdapter extends RecyclerView.Adapter<VentasAdapter.ViewHolder> {

    private List<Ventas> ventas;
    private int layout;

    private Context context;

    public VentasAdapter(List<Ventas> ventas, int layout){
        this.ventas = ventas;
        this.layout = layout;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {

        View v = LayoutInflater.from(parent.getContext()).inflate(layout, parent, false);
        ViewHolder vh = new ViewHolder(v);
        context = parent.getContext();
        return vh;
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        holder.bind(ventas.get(position));
    }

    @Override
    public int getItemCount() { return ventas.size();}

    public class ViewHolder extends RecyclerView.ViewHolder{

        private TextView usuarios;
        private TextView productos;
        private TextView costos;
        private TextView fechas;
        private ImageView imagen;

        public ViewHolder(View v){
            super(v);

            usuarios = (TextView)itemView.findViewById(R.id.textViewUsuarioVentas);
            productos = (TextView)itemView.findViewById(R.id.textViewProductoVentas);
            costos = (TextView)itemView.findViewById(R.id.textViewCostoVentas);
            fechas = (TextView)itemView.findViewById(R.id.textViewFechaVentas);
            imagen = (ImageView)itemView.findViewById(R.id.imagenViewVentas);
        }

        public void bind(final Ventas ventas){

            String precio = String.valueOf(ventas.getCosto());

            usuarios.setText(ventas.getUsuario());
            productos.setText(ventas.getProductos());
            costos.setText(precio);
            fechas.setText(ventas.getFecha());

            Picasso.get().load(Api.GALERIA + ventas.getRuta()).fit().into(imagen);
        }
    }
}