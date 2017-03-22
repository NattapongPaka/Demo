package com.example.nattapongpaka.demo.manager;

import com.example.nattapongpaka.demo.PhotoDaos;

import java.util.List;

import retrofit2.http.GET;
import rx.Observable;

/**
 * Created by nattapongpaka on 3/22/2017 AD.
 */

public interface ApiService {

    @GET("albums/1/photos")
    Observable<List<PhotoDaos>> getPhoto();
}
