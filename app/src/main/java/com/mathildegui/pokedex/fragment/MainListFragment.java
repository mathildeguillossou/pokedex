package com.mathildegui.pokedex.fragment;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Environment;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.mathildegui.pokedex.R;
import com.mathildegui.pokedex.adapter.PokemonAdapter;
import com.mathildegui.pokedex.bean.Pokemon;
import com.mathildegui.pokedex.service.PokedexService;
import com.mathildegui.pokedex.utils.Constants;
import com.raizlabs.android.dbflow.sql.language.SQLite;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.nio.channels.FileChannel;
import java.util.ArrayList;
import java.util.List;

import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.GsonConverterFactory;
import retrofit2.Response;
import retrofit2.Retrofit;

/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * {@link MainListFragment.OnFragmentInteractionListener} interface
 * to handle interaction events.
 * Use the {@link MainListFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class MainListFragment extends Fragment {
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    private String mParam1;
    private String mParam2;

    private OnFragmentInteractionListener mListener;

    public MainListFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @return A new instance of fragment MainListFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static MainListFragment newInstance() {
        return new MainListFragment();
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mParam1 = getArguments().getString(ARG_PARAM1);
            mParam2 = getArguments().getString(ARG_PARAM2);
        }
    }

    private String uri = "http://pokeapi.co/api/v1/sprite/";
    private RecyclerView mRecyclerView;
    private RecyclerView.Adapter mAdapter;
    private RecyclerView.LayoutManager mLayoutManager;
    private List<Pokemon> mPokemons;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View v = inflater.inflate(R.layout.fragment_main_list, container, false);
        mRecyclerView = (RecyclerView) v.findViewById(R.id.recyclerview);

        mPokemons = new ArrayList<>();

        // use this setting to improve performance if you know that changes
        // in content do not change the layout size of the RecyclerView
        mRecyclerView.setHasFixedSize(true);

        // use a linear layout manager
        mLayoutManager = new LinearLayoutManager(getActivity());
        mRecyclerView.setLayoutManager(mLayoutManager);

        // specify an adapter (see also next example)
        mAdapter = new PokemonAdapter(getContext(), mPokemons);
        mRecyclerView.setAdapter(mAdapter);


        getPokemonList();
        return v;
    }

    private void getPokemonList() {
        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(Constants.URL_MAIN)
                .addConverterFactory(GsonConverterFactory.create())
                .build();

        final PokedexService service = retrofit.create(PokedexService.class);
        exportDatabse("PokeDatabase.db");

        List<Pokemon> pokemons = SQLite.select()
                .from(Pokemon.class)
                .queryList();
        mPokemons.addAll(pokemons);
        //if(pokemons.size() == 0) {
            getNext(service, Constants.BASE_URL_API);
        //}
    }

    private void getNext(final PokedexService service , String url) {
        Call<ResponseBody> call = service.getStringFromUrl(url);
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
                            Log.d("id", jsonPokemon.toString());

                            String url = jsonPokemon.getString("url");
                            final  String name = jsonPokemon.getString("name");

                            String[] arr = url.split("/");

                            Pokemon p = new Pokemon();
                            p.name    = name;
                            p.pokeId  = Integer.parseInt(arr[arr.length -1]);
                            p.save();
                            new PokemonBitmapAsync().execute(p);

                            mPokemons.add(p);

                            if (i == jsonarray.length() - 1) {
                                if (next != null) {
                                    getNext(service, next);
                                }
                            }
                        }
                    }
                    mAdapter.notifyDataSetChanged();
                } catch (IOException | JSONException e) {
                    e.printStackTrace();
                }
            }

            @Override
            public void onFailure(Call<ResponseBody> call, Throwable t) {

            }
        });
    }

    public static class PokemonBitmapAsync extends AsyncTask<Pokemon, Void, Void> {

        @Override
        protected Void doInBackground(Pokemon... pokemons) {
            Log.d("THE_URL::", "http://pokeapi.co/media/img/" + pokemons[0].pokeId+".png");

            try {
                pokemons[0].image = BitmapFactory.decodeStream(new URL("http://pokeapi.co/media/img/" + pokemons[0].pokeId+".png").openStream());
                pokemons[0].update();
            } catch (IOException e) {
                e.printStackTrace();
            }
            return null;
        }
    }

    public void exportDatabse(String databaseName) {
        try {
            File sd = Environment.getExternalStorageDirectory();
            File data = Environment.getDataDirectory();

            Log.d("sd.canWrite", sd.canWrite() +"");

            if (sd.canWrite()) {
                String currentDBPath = "//data//"+getActivity().getPackageName()+"//databases//"+databaseName+"";
                String backupDBPath = "backupname.db";
                File currentDB = new File(data, currentDBPath);
                File backupDB = new File("/sdcard/", backupDBPath);
                Log.d("PATH_BK",backupDB.getAbsolutePath() + "");

                if (currentDB.exists()) {
                    FileChannel src = new FileInputStream(currentDB).getChannel();
                    FileChannel dst = new FileOutputStream(backupDB).getChannel();
                    dst.transferFrom(src, 0, src.size());
                    src.close();
                    dst.close();
                }
            }
        } catch (Exception e) {

        }
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        if (context instanceof OnFragmentInteractionListener) {
            mListener = (OnFragmentInteractionListener) context;
        } else {
            throw new RuntimeException(context.toString()
                    + " must implement OnFragmentInteractionListener");
        }
    }

    @Override
    public void onDetach() {
        super.onDetach();
        mListener = null;
    }

    /**
     * This interface must be implemented by activities that contain this
     * fragment to allow an interaction in this fragment to be communicated
     * to the activity and potentially other fragments contained in that
     * activity.
     * <p/>
     * See the Android Training lesson <a href=
     * "http://developer.android.com/training/basics/fragments/communicating.html"
     * >Communicating with Other Fragments</a> for more information.
     */
    public interface OnFragmentInteractionListener {
        void onFragmentInteraction(String pokeId);
    }
}
