package com.example.films.data;

import androidx.lifecycle.LiveData;
import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.OnConflictStrategy;
import androidx.room.Query;

import com.example.films.pojo.Result;

import java.util.List;

@Dao
public interface ResultDao {
    @Query("SELECT * FROM result")
    LiveData<List<Result>> getAllResult();

    @Query("SELECT * FROM favorite_result")
    LiveData<List<FavouriteMovie>> getAllFavoriteResult();

    @Query("SELECT * FROM result WHERE id == :resultId")
    Result getResultById(int resultId);

    @Query("SELECT * FROM favorite_result WHERE id == :resultFavoriteId")
    FavouriteMovie getFavoriteResultById(int resultFavoriteId);

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    void insertListResult(List<Result> results);

    @Query("DELETE FROM result")
    void deleteAllResults();

    @Insert
    void insertResult(Result result);

    @Insert
    void insertFavoriteResult(FavouriteMovie favouriteResult);

    @Delete
    void deleteFavoriteResult(FavouriteMovie favouriteResult);
}
