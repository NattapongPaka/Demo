package com.example.nattapongpaka.demo.manager;

import com.example.nattapongpaka.demo.util.Constance;
import com.example.nattapongpaka.demo.util.ToStringConverterFactory;

import okhttp3.OkHttpClient;
import retrofit2.Retrofit;
import retrofit2.adapter.rxjava.RxJavaCallAdapterFactory;
import retrofit2.converter.gson.GsonConverterFactory;

/**
 * Created by nattapongpaka on 3/22/2017 AD.
 */

public class HttpManager {
    private static HttpManager instatance;

    public static HttpManager getInstance() {
        if (instatance == null) {
            instatance = new HttpManager();
        }
        return instatance;
    }

    private ApiService service;
    private Retrofit retrofit;

    private HttpManager() {
        retrofit = new Retrofit.Builder()
                .baseUrl(Constance.url + "/")
                .addCallAdapterFactory(RxJavaCallAdapterFactory.create())
                .addConverterFactory(GsonConverterFactory.create())     //Converter to gson
                .addConverterFactory(new ToStringConverterFactory())    //Converter to string
                //.client(customClient())
                .build();
        service = retrofit.create(ApiService.class);
    }

//    private Gson gsonBuilder() {
//        return new GsonBuilder()
//                .setExclusionStrategies(new ExclusionStrategy() {
//                    @Override
//                    public boolean shouldSkipField(FieldAttributes f) {
//                        return f.getDeclaringClass().equals(RealmObject.class);
//                    }
//
//                    @Override
//                    public boolean shouldSkipClass(Class<?> clazz) {
//                        return false;
//                    }
//                })
//                .registerTypeAdapter(new TypeToken<RealmList<RealmString>>() {}.getType(), new RealmStringDeserializer())
//                .create();
//    }

    private OkHttpClient customClient() {
        //HttpLoggingInterceptor interceptor = initInterceptor();

        OkHttpClient.Builder okHttpClient = new OkHttpClient.Builder();
        //okHttpClient.addNetworkInterceptor(new StethoInterceptor());
        okHttpClient.retryOnConnectionFailure(true);
        okHttpClient.followRedirects(true);
        okHttpClient.followSslRedirects(true);

        //okHttpClient.addInterceptor(interceptor);
        return okHttpClient.build();

//        HttpLoggingInterceptor interceptor = new HttpLoggingInterceptor();
//        interceptor.setLevel(BuildConfig.DEBUG ? HttpLoggingInterceptor.Level.BODY : HttpLoggingInterceptor.Level.NONE);
//        return new OkHttpClient.Builder().addInterceptor(interceptor).build();
    }

//    private HttpLoggingInterceptor initInterceptor() {
//        return new HttpLoggingInterceptor().setLevel(HttpLoggingInterceptor.Level.BODY);
//    }

    public ApiService getService() {
        return service;
    }

}
