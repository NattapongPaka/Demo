package com.example.nattapongpaka.demo.presenter;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.provider.MediaStore;

import com.example.nattapongpaka.demo.manager.HttpManager;
import com.example.nattapongpaka.demo.model.PhotoDaos;
import com.example.nattapongpaka.demo.presenter.View.IMainView;
import com.example.nattapongpaka.demo.util.Constance;
import com.example.nattapongpaka.demo.util.LogUtil;
import com.example.nattapongpaka.demo.util.PreferenceUtil;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import me.iwf.photopicker.PhotoPicker;
import rx.Observable;
import rx.Observer;
import rx.Subscriber;
import rx.Subscription;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;

/**
 * Created by nattapongpaka on 3/25/2017 AD.
 */

public class MainPresenter {

    private IMainView iMainView;
    private Activity activity;
    private Subscription subscription;

    public MainPresenter(Activity activity, IMainView iMainView) {
        this.activity = activity;
        this.iMainView = iMainView;
    }

    private Gson gson = new Gson();

    public void stop(){
        if(subscription != null && !subscription.isUnsubscribed()){
            subscription.unsubscribe();
        }
    }

    public void pushPhotoDaosCache(final List<PhotoDaos> photoDaosList) {
        subscription = Observable.create(new Observable.OnSubscribe<String>() {
            @Override
            public void call(Subscriber<? super String> subscriber) {
                String result = gson.toJson(photoDaosList);
                LogUtil.D("Result cache %s",result);
                subscriber.onNext(result);
                subscriber.onCompleted();
            }
        }).subscribeOn(Schedulers.newThread())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Observer<String>() {
                    @Override
                    public void onCompleted() {

                    }

                    @Override
                    public void onError(Throwable e) {
                        e.printStackTrace();
                    }

                    @Override
                    public void onNext(String s) {
                        PreferenceUtil.getInstance().setSharedPreference(Constance.KEY_PHOTO, s);
                    }
                });
    }

    public void getPhotoDaosCache() {
       subscription= Observable.create(new Observable.OnSubscribe<List<PhotoDaos>>() {
            @Override
            public void call(Subscriber<? super List<PhotoDaos>> subscriber) {
                String jObject = (String) PreferenceUtil.getInstance().getValue(Constance.KEY_PHOTO);
                Type type = new TypeToken<List<PhotoDaos>>() {}.getType();
                List<PhotoDaos> photoDaosList = gson.fromJson(jObject, type);
                subscriber.onNext(photoDaosList);
                subscriber.onCompleted();
            }
        }).subscribeOn(Schedulers.newThread())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Observer<List<PhotoDaos>>() {
                    @Override
                    public void onCompleted() {

                    }

                    @Override
                    public void onError(Throwable e) {
                        e.printStackTrace();
                    }

                    @Override
                    public void onNext(List<PhotoDaos> photoDaosList) {
                        if (photoDaosList != null && !photoDaosList.isEmpty()) {
                            iMainView.onLoadPhotosFromCache(photoDaosList);
                        } else {
                            fatchImageFromServer();
                        }
                    }
                });
    }

    public void fatchImageFromServer() {
       subscription = HttpManager.getInstance().getService().getPhoto()
               .subscribeOn(Schedulers.newThread())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Observer<List<PhotoDaos>>() {
                    @Override
                    public void onCompleted() {

                    }

                    @Override
                    public void onError(Throwable e) {
                        e.printStackTrace();
                    }

                    @Override
                    public void onNext(List<PhotoDaos> photoDaosList) {
                        if (photoDaosList != null && !photoDaosList.isEmpty()) {
                            pushPhotoDaosCache(photoDaosList);
                            iMainView.onLoadPhotosFromServerSuccess(photoDaosList);
                        }
                    }
                });
    }

    public void getOriginalImagePath() {
        String[] projection = {MediaStore.Images.Media.DATA};
        Cursor cursor = activity.managedQuery(MediaStore.Images.Media.EXTERNAL_CONTENT_URI, projection, null, null, null);
        int column_index_data = cursor.getColumnIndexOrThrow(MediaStore.Images.Media.DATA);
        cursor.moveToLast();
        String path = cursor.getString(column_index_data);
        iMainView.onLoadPhotosFromTakePhoto(path);
    }

    public void getImageFromGallery(Intent data) {
        ArrayList<String> photoList = data.getStringArrayListExtra(PhotoPicker.KEY_SELECTED_PHOTOS);
        List<PhotoDaos> photoDaosList = new ArrayList<>();
        for (String path : photoList) {
            //LogUtil.D("Gallery Path %s", path);
            photoDaosList.add(generatePhotoDaos(path));
        }
        iMainView.onLoadPhotosFromSelectGallery(photoDaosList);
    }

    public PhotoDaos generatePhotoDaos(String path) {
        PhotoDaos photoDaos = new PhotoDaos();
        photoDaos.setUrl(path);
        //photoDaos.setAlbumId("AlbumID");
        //photoDaos.setId("");
        photoDaos.setThumbnailUrl("");
        photoDaos.setTitle("");
        return photoDaos;
    }
}
