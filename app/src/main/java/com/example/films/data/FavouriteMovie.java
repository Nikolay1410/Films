package com.example.films.data;

import androidx.room.Entity;
import androidx.room.Ignore;

import com.example.films.pojo.Result;
@Entity(tableName = "favorite_result")
public class FavouriteMovie extends Result{
    public FavouriteMovie(int uniqueId, boolean adult, String backdropPath, int id, String originalLanguage, String originalTitle, String overview, double popularity, String posterPath, String releaseDate, String title, boolean video, double voteAverage, int voteCount) {
        super(uniqueId, adult, backdropPath, id, originalLanguage, originalTitle, overview, popularity, posterPath, releaseDate, title, video, voteAverage, voteCount);
    }
    @Ignore
    public FavouriteMovie(Result result){
        super(result.getUniqueId(), result.isAdult(), result.getBackdropPath(), result.getId(), result.getOriginalLanguage(), result.getOriginalTitle(),result.getOverview(), result.getPopularity(), result.getPosterPath(), result.getReleaseDate(), result.getTitle(), result.isVideo(), result.getVoteAverage(), result.getVoteCount());
    }
}
