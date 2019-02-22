package com.study.zhai.playandroid.api;

import java.io.IOException;

import okhttp3.Interceptor;
import okhttp3.Request;
import okhttp3.Response;

/**
 * 有网络时的策略
 *
 *     onlineCacheTime表示缓存过期时间，单位是秒，如果设置为0表示每次请求都直接从网络获取数据，
 *     非0则表示在该时间内读取缓存，超过则再从网络获取
 */
public class NetCacheInterceptor implements Interceptor {

    @Override
    public Response intercept(Chain chain) throws IOException {
        Request request = chain.request();
        Response response = chain.proceed(request);
        int onlineCacheTime = 30;
        return response.newBuilder()
                .header("Cache-Control", "public, max-age="+onlineCacheTime)
                .removeHeader("Pragma")
                .build();
    }
}
