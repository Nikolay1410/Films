package com.example.films.presenter;

import com.example.films.api.ApiFactoryVideo;
import com.example.films.api.ApiServiceVideo;

import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.CompositeDisposable;
import io.reactivex.disposables.Disposable;
import io.reactivex.schedulers.Schedulers;

public class VideoListPresenter {
    private CompositeDisposable compositeDisposable;
    private final MoviesListView view;

    public VideoListPresenter(MoviesListView view) {
        this.view = view;
    }

    public void loadVideo(int id, String lang){
        ApiFactoryVideo apiFactoryVideo = ApiFactoryVideo.getVideo();
        ApiServiceVideo apiServiceVideo = apiFactoryVideo.getApiServiceVideo();
        compositeDisposable = new CompositeDisposable();
        Disposable disposableVideo = apiServiceVideo.getVideos( id, lang)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(video -> view.showDataVideo(video.getVideoResults()), throwable -> view.showError(throwable.getMessage()));
        compositeDisposable.add(disposableVideo);
    }
    public void disposeDisposable(){
        if (compositeDisposable!=null){
            compositeDisposable.dispose();
        }
    }
}
