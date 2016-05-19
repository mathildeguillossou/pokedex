package com.mathildegui.pokedex.bean;

/**
 * @author mathilde on 18/05/16.
 */
public class Pokemon {

    public long id;
    public String name;
    public String image;

    public Pokemon(long id, String name, String image) {
        this.id    = id;
        this.name  = name;
        this.image = image;
    }

    @Override
    public String toString() {
        return "Pokemon{" +
                "id=" + id +
                ", name='" + name + '\'' +
                ", image='" + image + '\'' +
                '}';
    }
}
