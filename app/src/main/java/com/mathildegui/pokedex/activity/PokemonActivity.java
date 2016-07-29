package com.mathildegui.pokedex.activity;

import android.net.Uri;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import com.mathildegui.pokedex.R;
import com.mathildegui.pokedex.fragment.PokemonFragment;

public class PokemonActivity extends AppCompatActivity implements PokemonFragment.OnFragmentInteractionListener {

    ViewPager vp;
    PokemonPagerAdapter pagerAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_pokemon);

        pagerAdapter =
                new PokemonPagerAdapter(
                        getSupportFragmentManager());
        vp = (ViewPager) findViewById(R.id.pager);
        vp.setAdapter(pagerAdapter);
    }

    @Override
    public void onFragmentInteraction(Uri uri) {

    }


    public class PokemonPagerAdapter extends FragmentStatePagerAdapter {

        public PokemonPagerAdapter(FragmentManager fm) {
            super(fm);
        }

        @Override
        public Fragment getItem(int position) {
            return new PokemonFragment();
        }

        @Override
        public int getCount() {
            return 5;
        }
    }
}
