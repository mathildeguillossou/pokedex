package com.mathildegui.pokedex.activity;

import android.os.Bundle;

import com.mathildegui.pokedex.R;
import com.mathildegui.pokedex.fragment.MainListFragment;

public class MainActivity extends BaseActivity implements MainListFragment.OnFragmentInteractionListener {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        if(findViewById(R.id.container) != null) {
            getSupportFragmentManager()
                    .beginTransaction()
                    .add(R.id.container, MainListFragment.newInstance())
                    .addToBackStack(null).commit();
        }
    }

    @Override
    public void onFragmentInteraction(String pokeId) {

    }
}
