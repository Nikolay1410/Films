package com.example.films.presenter;

import com.example.films.api.ApiFactoryReviews;
import com.example.films.api.ApiServiceReviews;

import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.CompositeDisposable;
import io.reactivex.disposables.Disposable;
import io.reactivex.schedulers.Schedulers;

public class ReviewListPresenter {
    private CompositeDisposable compositeDisposable;
    private final MoviesListView view;

    public ReviewListPresenter(MoviesListView view) {
        this.view = view;
    }

    public void loadReview(int id, String lang) {
        ApiFactoryReviews apiFactoryReviews = ApiFactoryReviews.getInstance();
        ApiServiceReviews apiServiceReviews = apiFactoryReviews.getReviews();
        compositeDisposable = new CompositeDisposable();
        Disposable disposable = apiServiceReviews.getReview(id, lang)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(reviews -> view.showDataReviews(reviews.getReviewsResults()), throwable -> view.showError(throwable.getMessage()));
        compositeDisposable.add(compositeDisposable);
    }
    public void disposeDisposable(){
        if (compositeDisposable!=null){
            compositeDisposable.dispose();
        }
    }
}
