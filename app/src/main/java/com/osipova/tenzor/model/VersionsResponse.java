package com.osipova.tenzor.model;

import java.util.List;

/**
 * Created by Osipova Ekaterina on 24.01.2016.
 */
public class VersionsResponse {
    String header;
    List<Version> body;

    public String getHeader() {
        return header;
    }

    public List<Version> getBody() {
        return body;
    }
}
