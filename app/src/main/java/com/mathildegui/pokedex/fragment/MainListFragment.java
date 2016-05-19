package com.mathildegui.pokedex.fragment;

import android.content.Context;
import android.net.Uri;
import android.os.Bundle;
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

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
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
        getNext(service, Constants.BASE_URL_API);
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
                            //String replaced = ms.replace("\\", "");

                            String url = jsonPokemon.getString("url");
                           final  String name = jsonPokemon.getString("name");

String[] arr = url.split("/");
                            Log.d("Array", arr[arr.length -1]);
                            mPokemons.add(new Pokemon(Integer.parseInt(arr[arr.length -1]), name, null));
                            /*Call<ResponseBody> callImage = service.getStringFromUrl(uri+arr[arr.length -1]);
                            callImage.enqueue(new Callback<ResponseBody>() {
                                @Override
                                public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                                    if(response.body() != null) {
                                        String responseString = null;
                                        try {
                                            responseString = response.body().string().replace('\"', '\'');
                                            JSONObject jsonobject = new JSONObject(responseString);
                                            String image = jsonobject.getString("image");
                                            Log.d("image", image);

                                            mPokemons.add(new Pokemon(it, name, image));

                                            //Log.d("POKEMON", url);
                                            Log.d("POKEMON NAME", name);
                                        } catch (IOException e) {
                                            e.printStackTrace();
                                        } catch (JSONException e) {
                                            e.printStackTrace();
                                        }
                                    }
                                }

                                @Override
                                public void onFailure(Call<ResponseBody> call, Throwable t) {

                                }
                            });*/

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
