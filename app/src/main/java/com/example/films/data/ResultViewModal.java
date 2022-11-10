package com.example.films.data;

import android.app.Application;
import android.os.AsyncTask;
import android.view.View;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;

import com.example.films.adapters.MovieAdapter;
import com.example.films.api.ApiFactory;
import com.example.films.api.ApiService;
import com.example.films.pojo.Example;
import com.example.films.pojo.Result;
import com.example.films.screens.employees.EmployeesListActivity;

import java.util.List;
import java.util.concurrent.ExecutionException;
import io.reactivex.Observer;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.CompositeDisposable;
import io.reactivex.disposables.Disposable;
import io.reactivex.schedulers.Schedulers;

public class ResultViewModal extends AndroidViewModel {
    private CompositeDisposable compositeDisposable;
    private static ResultDatabase db;
    private final LiveData<List<Result>> results;
    private final LiveData<List<FavouriteMovie>> favouriteMovies;
    private final MutableLiveData<Throwable> errors;
    private MovieAdapter adapter;

    public ResultViewModal(@NonNull Application application) {
        super(application);
        db = ResultDatabase.getInstance(getApplication());
        results = db.resultDao().getAllResult();
        favouriteMovies = db.resultDao().getAllFavoriteResult();
        errors = new MutableLiveData<>();
    }

    public Result getResultById(int id){
        try {
            return new GetResultTask().execute(id).get();
        } catch (ExecutionException | InterruptedException e) {
            e.printStackTrace();
        }
        return null;
    }

    public FavouriteMovie getFavoriteById(int favoriteId){
        try {
            return new GetFavoriteIdTask().execute(favoriteId).get();
        } catch (ExecutionException | InterruptedException e) {
            e.printStackTrace();
        }
        return null;
    }

    public LiveData<List<Result>> getResults() {
        return results;
    }

    public LiveData<List<FavouriteMovie>> getFavouriteMovies() {return favouriteMovies;}

    public LiveData<Throwable> getErrors() {
        return errors;
    }

    public void clearErrors(){
        errors.setValue(null);
    }

    @SuppressWarnings("unchecked")
    private void insertListResult(List<Result> results){
        new InsertResultsTask().execute(results);
    }

    public void insertFavoriteResult(FavouriteMovie favouriteResult){
        new InsertFavoriteTask().execute(favouriteResult);
    }

    public void deleteFavoriteResult(FavouriteMovie favouriteResult){
        new DeleteFavoriteTask().execute(favouriteResult);
    }

    private void deleteAllResults(){
        new DeleteAllResultTask().execute();
    }

    private static class InsertFavoriteTask extends AsyncTask<FavouriteMovie, Void, Void>{
        @Override
        protected Void doInBackground(FavouriteMovie... favouriteMovies) {
            if (favouriteMovies!=null && favouriteMovies.length>0){
                db.resultDao().insertFavoriteResult(favouriteMovies[0]);
            }
            return null;
        }
    }

    private static class DeleteFavoriteTask extends AsyncTask<FavouriteMovie, Void, Void>{
        @Override
        protected Void doInBackground(FavouriteMovie... favouriteMovies) {
            if (favouriteMovies!=null && favouriteMovies.length>0){
                db.resultDao().deleteFavoriteResult(favouriteMovies[0]);
            }
            return null;
        }
    }

    private static class GetResultTask extends AsyncTask<Integer, View, Result>{
        @Override
        protected Result doInBackground(Integer... integers) {
            if (integers!=null && integers.length>0){
              return db.resultDao().getResultById(integers[0]);
            }
            return null;
        }
    }

    private static class GetFavoriteIdTask extends AsyncTask<Integer, View, FavouriteMovie>{
        @Override
        protected FavouriteMovie doInBackground(Integer... integers) {
            if (integers!=null && integers.length>0){
                return db.resultDao().getFavoriteResultById(integers[0]);
            }
            return null;
        }
    }

    private static class InsertResultsTask extends AsyncTask<List<Result>,Void,Void>{
        @SafeVarargs
        @Override
        protected final Void doInBackground(List<Result>... lists) {
            if (lists!= null && lists.length>0){
                db.resultDao().insertListResult(lists[0]);
            }
            return null;
        }
    }

    private static class DeleteAllResultTask extends AsyncTask<Void,Void,Void>{
        @Override
        protected Void doInBackground(Void... voids) {
            db.resultDao().deleteAllResults();
            return null;
        }
    }

    public void loadData(int page, String sortBy, String lang){
        ApiFactory apiFactory = ApiFactory.getInstance();
        ApiService apiService = apiFactory.getApiService();
        compositeDisposable = new CompositeDisposable();
        adapter = new MovieAdapter();
        apiService.getExample(page, sortBy, lang)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Observer<Example>() {
                    @Override
                    public void onSubscribe(Disposable d) {
                        EmployeesListActivity.isLoading =true;
                        EmployeesListActivity.progressBarLoading.setVisibility(View.VISIBLE);
                        compositeDisposable.add(d);
                    }

                    @Override
                    public void onNext(Example example) {
                        if (example.getResults()!=null&&!example.getResults().isEmpty()) {
                            if (page == 1) {
                                deleteAllResults();
                                adapter.clear();
                            }
                            insertListResult(example.getResults());

                        }
                    }

                    @Override
                    public void onError(Throwable e) {
                        errors.setValue(e);
                    }

                    @Override
                    public void onComplete() {
                        EmployeesListActivity.isLoading =false;
                        EmployeesListActivity.progressBarLoading.setVisibility(View.INVISIBLE);
                    }
                });



    }

    @Override
    protected void onCleared() {
        compositeDisposable.dispose();
        super.onCleared();
    }

}
