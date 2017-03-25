package com.example.nattapongpaka.demo.util;

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;


/**
 * Created by Noth on 23/6/2559.
 */
public class NetworkUtils {

    private Context mContext;
    private static NetworkUtils networkInstance;

    public static NetworkUtils getInstance() {
        if (networkInstance == null) {
            networkInstance = new NetworkUtils();
        }
        return networkInstance;
    }

    private NetworkUtils() {
        this.mContext = Contextor.getInstance().getContext();
    }

//    private boolean hasInternetAccess(Context c){
//        TelephonyManager tm = (TelephonyManager) c.getSystemService(Context.TELEPHONY_SERVICE);
//        if(isConnected() && tm.getDataState() == TelephonyManager.DATA_CONNECTED)
//            return true;
//        else
//            return false;
//    }

//    private boolean isConnected(){
//        ConnectivityManager cm = (ConnectivityManager) mContext.getSystemService(Context.CONNECTIVITY_SERVICE);
//        //NetworkInfo info = cm.getActiveNetworkInfo();
//
//        //For 3G check
//        boolean is3g = cm.getNetworkInfo(ConnectivityManager.TYPE_MOBILE).isConnectedOrConnecting();
//        //For WiFi Check
//        boolean isWifi = cm.getNetworkInfo(ConnectivityManager.TYPE_WIFI).isConnectedOrConnecting();
//
//        //return (info != null && info.isConnected());
//
//        return is3g || isWifi;
//    }

    private boolean isConnected() {
        ConnectivityManager cm = (ConnectivityManager) mContext.getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo wifi = cm.getNetworkInfo(ConnectivityManager.TYPE_WIFI);
        NetworkInfo mobile = cm.getNetworkInfo(ConnectivityManager.TYPE_MOBILE);
        if (wifi.isAvailable() && wifi.getDetailedState() == NetworkInfo.DetailedState.CONNECTED) {
            return true;
        } else if (mobile.isAvailable() && mobile.getDetailedState() == NetworkInfo.DetailedState.CONNECTED) {
            return true;
        } else {
            return false;
        }
    }

    public boolean isNetworkConnected() {
        return isConnected();
    }


}
