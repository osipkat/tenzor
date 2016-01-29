package com.osipova.tenzor.model;

import org.parceler.Parcel;

/**
 * Created by Osipova Ekaterina on 24.01.2016.
 */
@Parcel
public class Version {
    int id;
    String name;
    String version;
    String image;

    public int getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public String getVersion() {
        return version;
    }

    public String getImage() {
        return image;
    }
}
