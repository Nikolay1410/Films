package com.example.films.api;


import com.jakewharton.retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory;

import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class ApiFactoryReviews {
    private static ApiFactoryReviews apiFactoryReviews;
    private static Retrofit retrofit;
    private static final String BASE_URL_REVIEW = "https://api.themoviedb.org/3/movie/";

    private ApiFactoryReviews(){
        retrofit = new Retrofit.Builder()
                .addConverterFactory(GsonConverterFactory.create())
                .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
                .baseUrl(BASE_URL_REVIEW)
                .build();


    }
    public static ApiFactoryReviews getInstance(){
        if (apiFactoryReviews ==null){
            apiFactoryReviews = new ApiFactoryReviews();
        }
        return apiFactoryReviews;
    }
    public ApiServiceReviews getReviews(){
        return retrofit.create(ApiServiceReviews.class);
    }
}
