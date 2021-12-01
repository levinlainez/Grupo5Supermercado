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

public class MisComprasAdapter extends RecyclerView.Adapter<MisComprasAdapter.ViewHolder> {

    private List<Ventas> ventas;
    private int layout;
    private int contadores = 1;

    private Context context;

    public MisComprasAdapter(List<Ventas> ventas, int layout){
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

        private TextView productos;
        private TextView costos;
        private TextView fechas;
        private ImageView imagen;
        private TextView contador;

        public ViewHolder(View v){
            super(v);

            productos = (TextView)itemView.findViewById(R.id.textViewProductoMisCompras);
            costos = (TextView)itemView.findViewById(R.id.textViewCostoMisCompras);
            fechas = (TextView)itemView.findViewById(R.id.textViewFechaMisCompras);
            imagen = (ImageView)itemView.findViewById(R.id.imagenViewMisCompras);
            contador = (TextView)itemView.findViewById(R.id.textViewContadorMisCompras);
        }

        public void bind(final Ventas ventas){

            String precio = String.valueOf(ventas.getCosto());

            productos.setText(ventas.getProductos());
            costos.setText(precio);
            fechas.setText(ventas.getFecha());

            String resultado = String.valueOf(contadores);
            contador.setText(resultado);
            contadores += 1;

            Picasso.get().load(Api.GALERIA + ventas.getRuta()).fit().into(imagen);
        }
    }
}
