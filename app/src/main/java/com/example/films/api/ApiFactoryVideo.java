package com.example.films.api;

import com.jakewharton.retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory;

import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class ApiFactoryVideo {
    public static ApiFactoryVideo apiFactoryVideo;
    public static Retrofit retrofitVideo;
    private static final String BASE_VIDEO_URL = " https://api.themoviedb.org/3/movie/";

    private ApiFactoryVideo(){
        retrofitVideo = new Retrofit.Builder()
                .addConverterFactory(GsonConverterFactory.create())
                .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
                .baseUrl(BASE_VIDEO_URL)
                .build();
    }
    public static ApiFactoryVideo getVideo(){
        if (apiFactoryVideo == null){
            apiFactoryVideo = new ApiFactoryVideo();
        }
        return apiFactoryVideo;
    }
    public ApiServiceVideo getApiServiceVideo(){
        return retrofitVideo.create(ApiServiceVideo.class);
    }
}
