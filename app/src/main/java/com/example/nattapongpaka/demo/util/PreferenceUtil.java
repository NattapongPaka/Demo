package com.example.nattapongpaka.demo.util;

import android.content.Context;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;

import com.google.gson.Gson;

import java.util.Map;


/**
 * Created by Noth on 7/10/2559.
 */

public class PreferenceUtil {
    private static PreferenceUtil instance;

    /**
     * All KEY
     */
    public static final String KEY_START_ACTIVITY = "START_ACTIVITY";


    public synchronized static PreferenceUtil getInstance() {
        if (instance == null) {
            instance = new PreferenceUtil();
        }
        return instance;
    }

    public synchronized void setSharedPreferenceEmpty(String key){
        SharedPreferences.Editor editor = getSharedPreference().edit();
        editor.putString(key,null);
        editor.apply();
    }

    public synchronized void removeAllSharedPreference(){
        SharedPreferences sharedPreferences = getSharedPreference();
        sharedPreferences.edit().clear().apply();
    }

    public synchronized void removeSharedPreference(String key){
        SharedPreferences sharedPreferences = getSharedPreference();
        if(sharedPreferences.contains(key)) {
            SharedPreferences.Editor editor = sharedPreferences.edit();
            editor.remove(key);
            editor.apply();
        }
    }

    public synchronized <T> void setSharedPreference(String key, T value) {
        SharedPreferences.Editor editor = getSharedPreference().edit();
        if (value instanceof Integer) {
            editor.putInt(key, (Integer) value);
        } else if (value instanceof Boolean) {
            editor.putBoolean(key, (Boolean) value);
        } else if (value instanceof Float) {
            editor.putFloat(key, (Float) value);
        } else if (value instanceof Long) {
            editor.putLong(key, (Long) value);
        } else if (value instanceof String) {
            editor.putString(key, (String) value);
        }
        editor.apply();
    }

//    public synchronized Object getSharedPreferenceValue(String key) {
//        Map<String, ?> all = getSharedPreference().getAll();
//        if (all.get(key) instanceof String) {
//            return getSharedPreference().getString(key, "");
//        } else if (all.get(key) instanceof Integer) {
//            return getSharedPreference().getInt(key, -1);
//        } else if (all.get(key) instanceof Float) {
//            return getSharedPreference().getFloat(key, -1.0f);
//        } else if (all.get(key) instanceof Boolean) {
//            return getSharedPreference().getBoolean(key, false);
//        }
//        return null;
//    }

    public synchronized Object getValue(String key) {
        Map<String, ?> all = getSharedPreference().getAll();
        if (all.get(key) instanceof String) {
            return getSharedPreference().getString(key, "");
        } else if (all.get(key) instanceof Integer) {
            return getSharedPreference().getInt(key, -1);
        } else if (all.get(key) instanceof Float) {
            return getSharedPreference().getFloat(key, -1.0f);
        } else if (all.get(key) instanceof Boolean) {
            return getSharedPreference().getBoolean(key, false);
        }
        return null;
    }



    public synchronized void setSharedPreferenceObject(Object key, Object value) {
        String serializedObject = new Gson().toJson(value);
        SharedPreferences.Editor editor = getSharedPreference().edit();
        editor.putString(String.valueOf(key), serializedObject);
        editor.apply();
    }

    public synchronized Object getSharedPreferenceObject(Object key, Class<?> value) {
        String result = getSharedPreference().getString(String.valueOf(key), null);
        if (result != null && !result.isEmpty()) {
            Gson gson = new Gson();
            return gson.fromJson(result, value);
        }
        return null;
    }

    public synchronized boolean checkDuplicateLocalChatID(Object key) {
        SharedPreferences sharedPreferences = getSharedPreference();
        return sharedPreferences.contains(String.valueOf(key));
    }

    public synchronized boolean checkKey(String key) {
        return getSharedPreference().contains(key);
    }

//    public synchronized void removeSharedPreference(Object key) {
//        SharedPreferences.Editor editor = getSharedPreference().edit();
//        editor.remove(String.valueOf(key));
//        editor.apply();
//    }

    private synchronized SharedPreferences getSharedPreference() {
        return PreferenceManager.getDefaultSharedPreferences(Contextor.getInstance().getContext());
    }

    public synchronized void setOnSharedPreferenceChangeListener(SharedPreferences.OnSharedPreferenceChangeListener onSharedPreferenceChangeListener) {
        SharedPreferences sharedPreferences = getSharedPreference();
        sharedPreferences.registerOnSharedPreferenceChangeListener(onSharedPreferenceChangeListener);
    }

    public void firstTimeAskingPermission(Context context, String permission, boolean isFirstTime) {
        SharedPreferences sharedPreference = getSharedPreference();
        sharedPreference.edit().putBoolean(permission, isFirstTime).apply();
    }

    public boolean isFirstTimeAskingPermission(Context context, String permission) {
        return getSharedPreference().getBoolean(permission, true);
    }
}
