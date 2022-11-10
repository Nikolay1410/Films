package com.example.films;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.films.adapters.MovieAdapter;
import com.example.films.data.FavouriteMovie;
import com.example.films.data.ResultViewModal;
import com.example.films.pojo.Result;
import com.example.films.screens.employees.EmployeesListActivity;

import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

public class FavouriteActivity extends AppCompatActivity {
    private MovieAdapter adapter;

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.main_menu, menu);
        return super.onCreateOptionsMenu(menu);
    }

    @SuppressLint("NonConstantResourceId")
    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        int id = item.getItemId();
        switch (id){
            case R.id.itemMain:
                Intent intentMain = new Intent(this, EmployeesListActivity.class);
                startActivity(intentMain);
                break;
            case R.id.itemFavourite:
                Intent intentFavourite = new Intent(this, FavouriteActivity.class);
                startActivity(intentFavourite);
                break;
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_favourite);
        String lang = Locale.getDefault().getLanguage();
        int page = EmployeesListActivity.page;
        RecyclerView recyclerViewFavouriteMovies = findViewById(R.id.recyclerViewFavouriteMovies);
        adapter = new MovieAdapter();
        adapter.setMovies(new ArrayList<>());
        recyclerViewFavouriteMovies.setLayoutManager(new GridLayoutManager(this, 2));
        recyclerViewFavouriteMovies.setAdapter(adapter);
        ResultViewModal viewModal = new ViewModelProvider(this).get(ResultViewModal.class);
        LiveData<List<FavouriteMovie>> favoriteResult = viewModal.getFavouriteMovies();
        favoriteResult.observe(this, favouriteResult -> {
            if(favouriteResult!=null) {
                List<Result> results = new ArrayList<>(favouriteResult);
                adapter.setMovies(results);
            }
        });
        adapter.setOnPosterClickListener(position -> {
            Result result = adapter.getMovies().get(position);
            Intent intent = new Intent(FavouriteActivity.this, DetailActivity.class);
            intent.putExtra("id", result.getId());
            startActivity(intent);
        });
        viewModal.loadData(page,null, lang);
    }
}