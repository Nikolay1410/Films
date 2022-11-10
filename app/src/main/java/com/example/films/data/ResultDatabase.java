package com.example.films.data;

import android.content.Context;

import androidx.room.Database;
import androidx.room.Room;
import androidx.room.RoomDatabase;

import com.example.films.pojo.Result;

@Database(entities = {Result.class, FavouriteMovie.class}, version = 6, exportSchema = false)
public abstract class ResultDatabase extends RoomDatabase {
    private static final String DB_NAME = "results.db";
    private static ResultDatabase database;

    private static final Object LOCK = new Object();

    public static ResultDatabase getInstance(Context context){
        synchronized (LOCK){
            if (database == null) {
                database = Room.databaseBuilder(context, ResultDatabase.class, DB_NAME).fallbackToDestructiveMigration().build();
            }
        }
        return database;
    }
    public abstract ResultDao resultDao();
}
