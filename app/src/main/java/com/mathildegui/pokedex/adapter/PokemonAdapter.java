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

import java.util.List;

/**
 * @author mathilde on 19/05/16.
 */
public class PokemonAdapter extends
        RecyclerView.Adapter<PokemonAdapter.ViewHolder> {

    private List<Pokemon> list;
    private Context mContext;

    public PokemonAdapter(Context context, List<Pokemon> list) {
        this.list    = list;
        this.mContext = context;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.adapter_pokemon, parent, false);
        return new ViewHolder(v);
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        Pokemon pokemon = list.get(position);
        holder.tv.setText(pokemon.name);

        if(pokemon.image != null) {
            holder.iv.setImageBitmap(pokemon.image);
        } else {
            holder.iv.setImageResource(R.mipmap.ic_launcher);
        }

        /*int id = (mContext.getResources().getIdentifier("poke_"+pokemon.pokeId , "drawable", mContext.getPackageName()) == 0)?
                R.mipmap.ic_launcher :
                mContext.getResources().getIdentifier("poke_"+pokemon.pokeId , "drawable", mContext.getPackageName());
        holder.iv.setImageResource(+id);*/
/*
        //FIXME : Add images in local for better performances
        Picasso
                .with(mContext)
                .load("http://pokeapi.co/media/img/"+list.get(position).id+".png")
                .fit()
                .error(R.mipmap.ic_launcher)
                .placeholder(R.mipmap.ic_launcher)
                .into(holder.iv);*/
    }

    @Override
    public int getItemCount() {
        return list.size();
    }

    public Pokemon getPokemon(int position) {
        return list.get(position);
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
