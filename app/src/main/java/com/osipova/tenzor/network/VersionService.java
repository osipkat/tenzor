package com.osipova.tenzor.network;

import com.osipova.tenzor.model.VersionsResponse;

import retrofit.Call;
import retrofit.http.GET;

/**
 * Created by Osipova Ekaterina on 24.01.2016.
 */
public interface VersionService {

    @GET("/s/2kwni1y9iclkmjj/server_data.txt")
    Call<VersionsResponse> getVersions();
}
