package com.example.films.screens.employees;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.util.DisplayMetrics;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.ProgressBar;
import android.widget.Switch;
import android.widget.TextView;
import android.widget.Toast;

import com.example.films.DetailActivity;
import com.example.films.FavouriteActivity;
import com.example.films.R;
import com.example.films.adapters.MovieAdapter;
import com.example.films.data.ResultViewModal;
import com.example.films.pojo.Result;
import java.util.ArrayList;
import java.util.Locale;

public class EmployeesListActivity extends AppCompatActivity {
    private TextView textViewTopRated;
    private TextView textViewPopularity;
    @SuppressLint("UseSwitchCompatOrMaterialCode")
    private Switch switchSort;
    private MovieAdapter adapter;
    private ResultViewModal viewModal;
    @SuppressLint("StaticFieldLeak")
    public static ProgressBar progressBarLoading;
    public static int page = 1;
    private static String methodOfSort = "vote_average.desc";
    private static String lang;
    public static boolean isLoading = false;

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

    private int getColumnCount(){
        DisplayMetrics displayMetrics = new DisplayMetrics();
        getWindowManager().getDefaultDisplay().getMetrics(displayMetrics);
        int width = (int) (displayMetrics.widthPixels / displayMetrics.density);
        return Math.max(width / 185, 2);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        lang = Locale.getDefault().getLanguage();
        switchSort = findViewById(R.id.switchSort);
        textViewTopRated = findViewById(R.id.textViewTopRated);
        textViewPopularity = findViewById(R.id.textViewPopularity);
        RecyclerView recyclerViewPosters = findViewById(R.id.recyclerViewPosters);
        progressBarLoading = findViewById(R.id.progressBarLoading);
        adapter = new MovieAdapter();
        adapter.setMovies(new ArrayList<>());
        recyclerViewPosters.setLayoutManager(new GridLayoutManager(this, getColumnCount()));
        recyclerViewPosters.setAdapter(adapter);
        switchSort.setChecked(true);
        viewModal = new ViewModelProvider(this).get(ResultViewModal.class);
        viewModal.getResults().observe(this, results -> adapter.setMovies(results));

        viewModal.getErrors().observe(this, throwable -> {
            if (throwable != null) {
                Toast.makeText(EmployeesListActivity.this, "Error:" + throwable.getMessage(), Toast.LENGTH_SHORT).show();
                progressBarLoading.setVisibility(View.INVISIBLE);
                viewModal.clearErrors();
            }
        });
        switchSort.setOnCheckedChangeListener((buttonView, isChecked) -> {
            page = 1;
            if (isChecked){
                textViewTopRated.setTextColor(getResources().getColor(R.color.teal_200));
                textViewPopularity.setTextColor(getResources().getColor(R.color.white));
            }else {
                textViewTopRated.setTextColor(getResources().getColor(R.color.white));
                textViewPopularity.setTextColor(getResources().getColor(R.color.teal_200));
            }
           setMethodOfSort(isChecked);
        });
        switchSort.setChecked(false);
        adapter.setOnPosterClickListener(position -> {
            Result result = adapter.getMovies().get(position);
            Intent intent = new Intent(EmployeesListActivity.this, DetailActivity.class);
            intent.putExtra("id", result.getId());
            startActivity(intent);
        });
        adapter.setOnReachEndListener(() -> {
           if (!isLoading) {
               page++;
               viewModal.loadData(page, methodOfSort, lang);
           }
        });

        textViewPopularity.setOnClickListener(v -> switchSort.setChecked(false));
        textViewTopRated.setOnClickListener(v -> switchSort.setChecked(true));
    }

    private void setMethodOfSort(boolean isTopRated){
        if (isTopRated){
            methodOfSort = "vote_average.desc";
        } else {
            methodOfSort = "popularity.desc";
        }
        viewModal.loadData(page, methodOfSort, lang);
    }



}