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
import supermercado.app.economico.models.Usuarios;

public class UsuariosAdapter extends RecyclerView.Adapter<UsuariosAdapter.ViewHolder>{

    private List<Usuarios> usuarios;
    private int layout;
    private OnClickListener listener;

    private Context context;

    public UsuariosAdapter(List<Usuarios> usuarios, int layout, OnClickListener listener) {
        this.usuarios = usuarios;
        this.layout = layout;
        this.listener = listener;
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
        holder.bind(usuarios.get(position), listener);
    }

    @Override
    public int getItemCount() {
        return usuarios.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder{

        private TextView usuario;
        private TextView role;
        private ImageView imagen;

        public ViewHolder(View v) {
            super(v);
            usuario = (TextView)itemView.findViewById( R.id.textViewUsuarioNombre);
            role = (TextView)itemView.findViewById( R.id.textViewRole);
            imagen = (ImageView) itemView.findViewById(R.id.imageViewUsuario);
        }

        public void bind(final Usuarios usuarios, final OnClickListener listener){

            usuario.setText(usuarios.getUsuario());
            role.setText(usuarios.getRole());

            if(usuarios.getRole().toString().equals("administrador")){
                Picasso.get().load(R.drawable.administrador).fit().into(imagen);
            }else{
                Picasso.get().load(R.drawable.cliente).fit().into(imagen);
            }

            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    listener.onItemClick(usuarios, getAdapterPosition());
                }
            });

        }
    }

    public interface OnClickListener{
        void onItemClick(Usuarios usuarios, int position);
    }

}
