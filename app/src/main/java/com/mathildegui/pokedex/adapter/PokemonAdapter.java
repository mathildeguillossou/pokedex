package com.mathildegui.pokedex.adapter;

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
        RecyclerView.Adapter<PokemonAdapter.ViewHolder>{

    private List<Pokemon> list;

    public PokemonAdapter(List<Pokemon> list) {
        this.list = list;
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
