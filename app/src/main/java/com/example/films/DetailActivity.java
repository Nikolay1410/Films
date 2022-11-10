package com.example.films;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.films.adapters.MovieAdapter;
import com.example.films.adapters.ReviewAdapter;
import com.example.films.adapters.TrailerAdapter;
import com.example.films.data.FavouriteMovie;
import com.example.films.data.ResultViewModal;
import com.example.films.pojo.Result;
import com.example.films.pojo.ReviewsResult;
import com.example.films.pojo.VideoResult;
import com.example.films.presenter.MoviesListView;
import com.example.films.presenter.ReviewListPresenter;
import com.example.films.presenter.VideoListPresenter;
import com.example.films.screens.employees.EmployeesListActivity;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.List;
import java.util.Locale;


public class DetailActivity extends AppCompatActivity implements MoviesListView {

    private Result result;
    private ImageView imageViewAddToFavourite;
    private FavouriteMovie favouriteMovie;

    private TrailerAdapter trailerAdapter;
    private ReviewAdapter reviewAdapter;
    private VideoListPresenter videoListPresenter;
    private ReviewListPresenter reviewListPresenter;

    private int id;

    private MovieAdapter adapter;
    private ResultViewModal viewModal;

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

    @SuppressLint("SetTextI18n")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detail);
        String lang = Locale.getDefault().getLanguage();
        int page = EmployeesListActivity.page;
        videoListPresenter = new VideoListPresenter(this);
        reviewListPresenter = new ReviewListPresenter(this);
        ImageView imageViewBigPoster = findViewById(R.id.imageViewBigPoster);
        TextView textViewTitle = findViewById(R.id.textViewTitle);
        TextView textViewOriginalTitle = findViewById(R.id.textViewOriginalTitle);
        TextView textViewRating = findViewById(R.id.textViewRating);
        TextView textViewRealiseDate = findViewById(R.id.textViewRealiseDate);
        TextView textViewOverview = findViewById(R.id.textViewOverview);
        imageViewAddToFavourite = findViewById(R.id.imageViewAddToFavourite);
        adapter = new MovieAdapter();
        adapter.setMovies(new ArrayList<>());
        Intent intent = getIntent();
        if(intent != null && intent.hasExtra("id")){
            id = intent.getIntExtra("id", -1);
        }else {finish();}
        viewModal = new ViewModelProvider(this).get(ResultViewModal.class);
        viewModal.getResults().observe(this, results -> adapter.setMovies(results));

        viewModal.loadData(page,null, lang);
        result = viewModal.getResultById(id);
        Picasso.get().load("https://image.tmdb.org/t/p/w780"+result.getPosterPath()).placeholder(R.drawable.video).into(imageViewBigPoster);
        textViewTitle.setText(result.getTitle());
        textViewOriginalTitle.setText(result.getOriginalTitle());
        textViewOverview.setText(result.getOverview());
        textViewRealiseDate.setText(result.getReleaseDate());
        textViewRating.setText(Double.toString(result.getVoteAverage()));
        RecyclerView recyclerViewTrailers = findViewById(R.id.recyclerViewTrailer);
        RecyclerView recyclerViewReviews = findViewById(R.id.recyclerViewReviews);
        trailerAdapter = new TrailerAdapter();
        trailerAdapter.setVideos(new ArrayList<>());
        trailerAdapter.setOnTrailerClickListener(url -> {
            Intent intentTrailer = new Intent(Intent.ACTION_VIEW, Uri.parse(url));
            startActivity(intentTrailer);
        });
        reviewAdapter = new ReviewAdapter();
        reviewAdapter.setReviews(new ArrayList<>());
        recyclerViewTrailers.setLayoutManager(new LinearLayoutManager(this));
        recyclerViewReviews.setLayoutManager(new LinearLayoutManager(this));
        videoListPresenter.loadVideo(id, lang);
        reviewListPresenter.loadReview(id, lang);
        recyclerViewTrailers.setAdapter(trailerAdapter);
        recyclerViewReviews.setAdapter(reviewAdapter);
        setFavorite();

        imageViewAddToFavourite.setOnClickListener(v -> {
            if (favouriteMovie == null){
                viewModal.insertFavoriteResult(new FavouriteMovie(result));
                Toast.makeText(DetailActivity.this, "Добавлено в избраное", Toast.LENGTH_SHORT).show();
            }else {
                viewModal.deleteFavoriteResult(favouriteMovie);
                Toast.makeText(DetailActivity.this, "Удалено из избраного", Toast.LENGTH_SHORT).show();
            }
            setFavorite();
        });
    }
    private void setFavorite(){
        favouriteMovie = viewModal.getFavoriteById(id);
        if(favouriteMovie==null){
            imageViewAddToFavourite.setImageResource(R.drawable.favourite_add_to);
        }else {
            imageViewAddToFavourite.setImageResource(R.drawable.favourite_remove);
        }
    }


    @Override
    protected void onDestroy() {
        videoListPresenter.disposeDisposable();
        reviewListPresenter.disposeDisposable();
        super.onDestroy();
    }

    @Override
    public void showDataVideo(List<VideoResult> videoResults) {
        trailerAdapter.setVideos(videoResults);
    }

    @Override
    public void showDataReviews(List<ReviewsResult> reviewsResults) {
        reviewAdapter.setReviews(reviewsResults);
    }

    @Override
    public void showError(String error) {
        Toast.makeText(this, error, Toast.LENGTH_SHORT).show();
    }
}