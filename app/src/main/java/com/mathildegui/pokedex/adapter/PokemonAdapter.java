package com.mathildegui.pokedex.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.mathildegui.pokedex.R;
import com.mathildegui.pokedex.bean.Pokemon;
import com.squareup.picasso.Picasso;

import java.util.List;

/**
 * @author mathilde on 19/05/16.
 */
public class PokemonAdapter extends
        RecyclerView.Adapter<PokemonAdapter.ViewHolder>{

    private List<Pokemon> list;
    private Context context;

    public PokemonAdapter(Context context, List<Pokemon> list) {
        this.list    = list;
        this.context = context;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.adapter_pokemon, parent, false);
        return new ViewHolder(v);
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        holder.tv.setText(list.get(position).name);
        Picasso
                .with(context)
                .load("http://pokeapi.co/media/img/"+list.get(position).id+".png")
                .fit()
                .error(R.mipmap.ic_launcher)
                .placeholder(R.mipmap.ic_launcher)
                .into(holder.iv);

    }

    @Override
    public int getItemCount() {
        return list.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {

        public TextView tv;
        public ImageView iv;

        public ViewHolder(View itemView) {
            super(itemView);
            tv = (TextView)itemView.findViewById(R.id.poke_adapter_tv);
            iv = (ImageView)itemView.findViewById(R.id.poke_adapter_iv);
        }
    }
}
