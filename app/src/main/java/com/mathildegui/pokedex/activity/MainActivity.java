package com.mathildegui.pokedex.activity;

import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.util.Log;
import android.view.View;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;

import com.crashlytics.android.Crashlytics;
import com.google.gson.Gson;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import com.mathildegui.pokedex.R;
import com.mathildegui.pokedex.bean.Pokemon;
import com.mathildegui.pokedex.service.PokedexService;

import io.fabric.sdk.android.Fabric;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.util.List;

import okhttp3.MediaType;
import okhttp3.RequestBody;
import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Converter;
import retrofit2.GsonConverterFactory;
import retrofit2.Response;
import retrofit2.Retrofit;

public class MainActivity extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Fabric.with(this, new Crashlytics());
        setContentView(R.layout.activity_main);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.setDrawerListener(toggle);
        toggle.syncState();

        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);

        getPokemonList();
    }

    @Override
    public void onBackPressed() {
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        // Handle navigation view item clicks here.
        int id = item.getItemId();

        if (id == R.id.nav_camera) {
            // Handle the camera action
        } else if (id == R.id.nav_gallery) {

        } else if (id == R.id.nav_slideshow) {

        } else if (id == R.id.nav_manage) {

        } else if (id == R.id.nav_share) {

        } else if (id == R.id.nav_send) {

        }

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }

    private void getPokemonList() {
        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl("http://pokeapi.co/api/v2/")
                .addConverterFactory(GsonConverterFactory.create())
                .build();

        final PokedexService service = retrofit.create(PokedexService.class);
        //Call<ResponseBody> call = service.listPokemons();
        getNext(service, "http://pokeapi.co/api/v2/pokemon");



        /*call.enqueue(new Callback<ResponseBody>() {
            @Override
            public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {

                try {
                    String responseString = response.body().string().replace('\"', '\'');
                    JSONObject jsonobject = new JSONObject(responseString);
                    JSONArray jsonarray = jsonobject.getJSONArray("results");

                    for (int i = 0; i < jsonarray.length(); i++) {
                        JSONObject jsonPokemon =  jsonarray.getJSONObject(i);
                        //String replaced = ms.replace("\\", "");

String url = jsonPokemon.getString("url");
                        Log.d("POKEMON", url);
                        Call<Pokemon> pokemon = service.getPokemonFromUrl(url);
                        //Log.d("pokepoke", pokemon.execute().body().toString());
                        pokemon.enqueue(new Callback<Pokemon>() {
                            @Override
                            public void onResponse(Call<Pokemon> call, Response<Pokemon> response) {
                                Log.d("reponse poke", response.body().toString());
                            }

                            @Override
                            public void onFailure(Call<Pokemon> call, Throwable t) {

                            }
                        });
                    }
                } catch (IOException e) {
                    e.printStackTrace();
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }

            @Override
            public void onFailure(Call<ResponseBody> call, Throwable t) {
                Log.d("Fail", "URE");

            }
        });*/
    }

    private void getNext(final PokedexService service , String url) {
        Call<ResponseBody> call = service.listPokemonsFromUrl(url);
        call.enqueue(new Callback<ResponseBody>() {
            @Override
            public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                String responseString;
                try {

                    if(response.body() != null) {
                        responseString = response.body().string().replace('\"', '\'');
                        JSONObject jsonobject = new JSONObject(responseString);
                        String next = jsonobject.getString("next");
                        Log.d("NEXT", next);


                        JSONArray jsonarray = jsonobject.getJSONArray("results");

                        for (int i = 0; i < jsonarray.length(); i++) {
                            JSONObject jsonPokemon = jsonarray.getJSONObject(i);
                            //String replaced = ms.replace("\\", "");

                            String url = jsonPokemon.getString("url");
                            String name = jsonPokemon.getString("name");
                            Log.d("POKEMON", url);
                            Log.d("POKEMON NAME", name);
                        /*Call<Pokemon> pokemon = service.getPokemonFromUrl(url);
                        pokemon.enqueue(new Callback<Pokemon>() {
                            @Override
                            public void onResponse(Call<Pokemon> call, Response<Pokemon> response) {
                                Log.d("reponse poke", response.body().toString());
                            }

                            @Override
                            public void onFailure(Call<Pokemon> call, Throwable t) {

                            }
                        });*/

                            if (i == jsonarray.length() - 1) {
                                if (next != null) {
                                    getNext(service, next);
                                }
                            }
                        }
                    }
                } catch (IOException | JSONException e) {
                    e.printStackTrace();
                }
            }

            @Override
            public void onFailure(Call<ResponseBody> call, Throwable t) {

            }
        });
    }

}
