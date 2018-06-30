package com.example.mymovies.utility;


import retrofit.RequestInterceptor;
import retrofit.RestAdapter;


public class MyApplication extends android.app.Application {

    public static final String TAG = MyApplication.class
            .getSimpleName();
    private static MyApplication mInstance;
    RestAdapter resadapter;



    @Override
    public void onCreate() {
        super.onCreate();
        mInstance = this;
        resadapter = new RestAdapter.Builder()
                .setEndpoint("http://api.themoviedb.org/3")
                .setRequestInterceptor(new RequestInterceptor() {
                    @Override
                    public void intercept(RequestFacade request) {
                        request.addEncodedQueryParam("api_key", "31f131c31e0d280cf0ff0cfe7dac0333");
                    }
                })
                .setLogLevel(RestAdapter.LogLevel.FULL)
                .build();





    }

    public static synchronized MyApplication getInstance() {
        return mInstance;
    }

    public RestAdapter getResadapter() {
        return resadapter;
    }
}
