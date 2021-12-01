package owl.app.catalogo.adapters;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.squareup.picasso.Picasso;

import org.w3c.dom.Text;

import java.util.List;

import owl.app.catalogo.R;
import owl.app.catalogo.api.Api;
import owl.app.catalogo.models.Productos;

public class ProductosAdapter extends RecyclerView.Adapter<ProductosAdapter.ViewHolder> {

    private List<Productos> productos;
    private int layout;
    private OnClickListener listener;
    private OnLongClickListener listenerLong;

    private Context context;

    public ProductosAdapter(List<Productos> productos, int layout, OnClickListener listener, OnLongClickListener listenerLong){
        this.productos = productos;
        this.layout = layout;
        this.listener = listener;
        this.listenerLong = listenerLong;
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
        holder.bind(productos.get(position), listener, listenerLong);
    }

    @Override
    public int getItemCount() { return productos.size();}

    public class ViewHolder extends RecyclerView.ViewHolder{

        private TextView titulo;
        private TextView descripcion;
        private TextView precio;
        private ImageView imagen;

        public ViewHolder(View v){
            super(v);

            titulo = (TextView)itemView.findViewById(R.id.textViewTitulo);
            descripcion = (TextView)itemView.findViewById(R.id.textViewDescripcion);
            precio = (TextView)itemView.findViewById(R.id.textViewPrecio);
            imagen = (ImageView)itemView.findViewById(R.id.imagenViewProducto);
        }

        public void bind(final Productos productos, final OnClickListener listener, final OnLongClickListener listenerLong){

            titulo.setText(productos.getTitulo());
            descripcion.setText(productos.getDescripcion());

            String precioConvercion = String.valueOf(productos.getPrecio());

            precio.setText(precioConvercion);

            Picasso.get().load(Api.GALERIA + productos.getImagen()).fit().into(imagen);

            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    listener.onItemClick(productos, getAdapterPosition());
                }
            });

            itemView.setOnLongClickListener(new View.OnLongClickListener() {
                @Override
                public boolean onLongClick(View v) {

                    listenerLong.onItemClick(productos, getAdapterPosition());

                    return true;
                }
            });
        }
    }

    public interface OnClickListener{
        void onItemClick(Productos productos, int position);
    }

    public interface OnLongClickListener{
        void onItemClick(Productos productos, int position);
    }
}
