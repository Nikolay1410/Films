package com.example.films.api;


import com.example.films.pojo.Example;

import io.reactivex.Observable;
import retrofit2.http.GET;
import retrofit2.http.Query;

public interface ApiService {

    @GET("movie?api_key=bc28c1ca4bea349819d6edc94a40b1e7&vote_count.gte=2000")
    Observable<Example> getExample(@Query("page") int page, @Query("sort_by") String sortBy, @Query("language") String lang);
}
