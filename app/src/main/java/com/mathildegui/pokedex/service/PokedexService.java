package com.mathildegui.pokedex.service;

import com.mathildegui.pokedex.bean.Pokemon;

import java.util.List;

import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Path;
import retrofit2.http.Url;

/**
 * @author mathilde on 18/05/16.
 */
public interface PokedexService {

    @GET("pokemon/")
    Call<ResponseBody> listPokemons();
    //Call<List<Pokemon>> listPokemons(@Path("pokeId") String pokeId);

    @GET
    Call<ResponseBody> getStringFromUrl(@Url String url);

    @GET("pokemon/{pokeId}")
    Call<Pokemon> getPokemon(@Path("pokeId") String pokeId);

    @GET
    Call<Pokemon> getPokemonFromUrl(@Url String url);
}
