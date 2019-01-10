package com.study.zhai.playandroid.api;

import com.study.zhai.playandroid.MyApplication;
import com.study.zhai.playandroid.utils.NetUtils;

import java.io.IOException;

import okhttp3.CacheControl;
import okhttp3.Interceptor;
import okhttp3.Request;
import okhttp3.Response;

/**
 * 设置http缓存的拦截器
 */
public class HttpCacheInterceptor implements Interceptor {

    @Override
    public Response intercept(Chain chain) throws IOException {
        int netWorkState = NetUtils.getNetWorkState(MyApplication.getInstance());
        Request request = chain.request();
        if (netWorkState == NetUtils.NETWORK_NONE) {
            request = request.newBuilder()
                    .cacheControl(CacheControl.FORCE_CACHE) //等价于.header("Cache-Control", "public, only-if-cached, max-stale=" + Integer.MAX_VALUE)
                    .build();
        }
        Response response = chain.proceed(request);
        if (netWorkState == NetUtils.NETWORK_NONE) {
            int maxStale = 60 * 60 * 24 * 28; // 无网络时，设置超时为4周
            response.newBuilder()
                    .header("Cache-Control", "public, only-if-cached, max-stale=" + maxStale)
                    .removeHeader("Pragma")
                    .build();
        } else {
            int maxAge = 0 * 60;
            response.newBuilder()
                    .header("Cache-Control", "public, max-age=" + maxAge)
                    .removeHeader("Pragma")// 清除头信息，因为服务器如果不支持，会返回一些干扰信息，不清除下面无法生效
                    .build();
        }
        return response;
    }
}
