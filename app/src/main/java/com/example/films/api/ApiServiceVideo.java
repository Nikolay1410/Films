package com.example.films.api;

import com.example.films.pojo.Video;

import io.reactivex.Observable;
import retrofit2.http.GET;
import retrofit2.http.Path;
import retrofit2.http.Query;

public interface ApiServiceVideo {
    @GET("{id}/videos?api_key=bc28c1ca4bea349819d6edc94a40b1e7")
    Observable<Video> getVideos(@Path("id") int id, @Query("language") String lang);
}
