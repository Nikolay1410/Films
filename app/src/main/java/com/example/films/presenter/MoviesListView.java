package com.example.films.presenter;

import com.example.films.pojo.ReviewsResult;
import com.example.films.pojo.VideoResult;

import java.util.List;

public interface MoviesListView {
    void showDataVideo(List<VideoResult> videoResults);
    void showDataReviews(List<ReviewsResult> reviewsResults);
    void showError(String error);
}
