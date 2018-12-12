package com.study.zhai.playandroid.api;

import com.google.gson.Gson;
import com.study.zhai.playandroid.util.ConstantUtil;

import okhttp3.OkHttpClient;
import retrofit2.Retrofit;
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory;
import retrofit2.converter.gson.GsonConverterFactory;

public class ApiStore {

    private static Retrofit retrofit;
    public static String baseUrl = ConstantUtil.BASE_URL;

    private ApiStore(){};

    public static<T> T createApi(Class<T> service) {
        return retrofit.create(service);
    }

    static {
        createProxy();
    }
    public static void createProxy(){

        OkHttpClient.Builder builder = new OkHttpClient().newBuilder();

        retrofit = new Retrofit.Builder()
                .baseUrl(baseUrl)
                .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
                .addConverterFactory(GsonConverterFactory.create(new Gson()))
                .client(builder.build())
                .build();
    }
}
