package com.example.nattapongpaka.demo.util;

import android.content.Context;

/**
 * Created by PONG on 1/19/16 AD.
 */
public class Contextor {

    private Context mContext;
    private static Contextor instantce;

    public static Contextor getInstance(){
        synchronized (Contextor.class) {
            if (instantce == null) {
                instantce = new Contextor();
            }
            return instantce;
        }
    }

    public void init(Context context){
        mContext = context;
    }
    public Context getContext(){
        return mContext;
    }
}
