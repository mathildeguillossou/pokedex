package com.mathildegui.pokedex.bean;

import android.graphics.Bitmap;

import com.mathildegui.pokedex.db.PokeDatabase;
import com.raizlabs.android.dbflow.annotation.Column;
import com.raizlabs.android.dbflow.annotation.PrimaryKey;
import com.raizlabs.android.dbflow.annotation.Table;
import com.raizlabs.android.dbflow.data.Blob;
import com.raizlabs.android.dbflow.structure.BaseModel;

/**
 * @author mathilde on 18/05/16.
 */
@Table(database = PokeDatabase.class)
public class Pokemon extends BaseModel {
    @Column
    @PrimaryKey(autoincrement = true)
    public long id;
    @Column
    public String name;
    @Column
    public int pokeId;

    //public String image;
    /*@Column
    public Blob image;*/

    @Column(typeConverter = ImageConverter.class)
    public Bitmap image;

    public Pokemon() { }

    @Override
    public String toString() {
        return "Pokemon{" +
                "id=" + id  +
                ", name='"  + name  + '\'' +
                ", image='" + image + '\'' +
                '}';
    }
}
