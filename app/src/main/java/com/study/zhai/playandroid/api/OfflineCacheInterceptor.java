package com.study.zhai.playandroid.api;

import com.study.zhai.playandroid.MyApplication;
import com.study.zhai.playandroid.utils.NetUtils;

import java.io.IOException;

import okhttp3.Interceptor;
import okhttp3.Request;
import okhttp3.Response;

/**
 * 没有网络时读取缓存数据
 *
 *    .header("Cache-Control", "public, only-if-cached, max-stale=" + offlineCacheTime)和
 *    .cacheControl(new CacheControl
 *               .Builder()
 *               .maxStale(60,TimeUnit.SECONDS)
 *               .onlyIfCached()
 *               .build())
 *    两种方式结果是一样的，写法不同
 *
 *
 *    没有网络时对Response的设置是无效的，只能对request进行缓存设置
 *
 */
public class OfflineCacheInterceptor implements Interceptor {
    @Override
    public Response intercept(Chain chain) throws IOException {
        Request request = chain.request();
        if (NetUtils.getNetWorkState(MyApplication.getInstance()) == NetUtils.NETWORK_NONE ) {
            int offlineCacheTime = 60 * 60 * 24 * 7;//离线的时候的缓存的过期时间设为一周
            request = request.newBuilder()
                    .header("Cache-Control", "public, only-if-cached, max-stale=" + offlineCacheTime)
                    .build();
        }
        return chain.proceed(request);
    }
}
