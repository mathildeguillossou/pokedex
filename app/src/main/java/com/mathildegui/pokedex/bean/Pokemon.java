package com.mathildegui.pokedex.bean;

/**
 * @author mathilde on 18/05/16.
 */
public class Pokemon {

    public long id;
    public String name;

    public Pokemon(long id, String name) {
        this.id   = id;
        this.name = name;
    }

    @Override
    public String toString() {
        return "Pokemon{" +
                "id=" + id +
                ", name='" + name + '\'' +
                '}';
    }
}
