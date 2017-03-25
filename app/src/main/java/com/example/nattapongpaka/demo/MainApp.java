package com.example.nattapongpaka.demo;

import android.app.Application;

import com.example.nattapongpaka.demo.util.Contextor;

/**
 * Created by nattapongpaka on 3/23/2017 AD.
 */

public class MainApp extends Application {



    @Override
    public void onCreate() {
        super.onCreate();
        Contextor.getInstance().init(getApplicationContext());
    }

}
