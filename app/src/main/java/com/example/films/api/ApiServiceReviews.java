package com.example.films.api;

import com.example.films.pojo.Reviews;

import io.reactivex.Observable;
import retrofit2.http.GET;
import retrofit2.http.Path;
import retrofit2.http.Query;

public interface ApiServiceReviews {
    @GET("{id}/reviews?api_key=bc28c1ca4bea349819d6edc94a40b1e7")
    Observable<Reviews> getReview(@Path("id") int id, @Query("language") String lang);
}
