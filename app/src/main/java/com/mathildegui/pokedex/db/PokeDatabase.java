package com.mathildegui.pokedex.db;

import com.raizlabs.android.dbflow.annotation.Database;

/**
 * @author mathilde on 21/05/16.
 */
@Database(name = PokeDatabase.NAME, version = PokeDatabase.VERSION)
public class PokeDatabase {

    public static final String NAME = "PokeDatabase"; // we will add the .db extension
    public static final int VERSION = 1;
}
