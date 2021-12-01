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
import supermercado.app.economico.models.Deseos;

public class DeseoAdapter extends RecyclerView.Adapter<DeseoAdapter.ViewHolder> {

    private List<Deseos> deseosList;
    private int layout;
    private OnClickListener listener;
    private OnLongClickListener listenerLong;

    private Context context;

    public DeseoAdapter(List<Deseos> deseosList, int layout, OnClickListener listener, OnLongClickListener listenerLong){
        this.deseosList = deseosList;
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
        holder.bind(deseosList.get(position), listener, listenerLong);
    }

    @Override
    public int getItemCount() { return deseosList.size();}

    public class ViewHolder extends RecyclerView.ViewHolder{

        private TextView titulo;
        private TextView precio;
        private ImageView imagen;

        public ViewHolder(View v){
            super(v);

            titulo = (TextView)itemView.findViewById(R.id.textViewTituloDeseos);
            precio = (TextView)itemView.findViewById(R.id.textViewPrecioDeseos);
            imagen = (ImageView)itemView.findViewById(R.id.imageViewDeseo);
        }

        public void bind(final Deseos deseos, final OnClickListener listener, final OnLongClickListener listenerLong){

            titulo.setText(deseos.getProducto());
            String precioConvercion = String.valueOf(deseos.getCosto());
            precio.setText(precioConvercion);

            Picasso.get().load(Api.GALERIA + deseos.getImagen()).fit().into(imagen);

            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    listener.onItemClick(deseos, getAdapterPosition());
                }
            });

            itemView.setOnLongClickListener(new View.OnLongClickListener() {
                @Override
                public boolean onLongClick(View v) {

                    listenerLong.onItemClick(deseos, getAdapterPosition());

                    return true;
                }
            });
        }
    }

    public interface OnClickListener{
        void onItemClick(Deseos deseos, int position);
    }

    public interface OnLongClickListener{
        void onItemClick(Deseos deseos, int position);
    }
}
