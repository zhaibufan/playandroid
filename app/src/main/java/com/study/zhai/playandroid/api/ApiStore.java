package com.study.zhai.playandroid.api;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.study.zhai.playandroid.BuildConfig;
import com.study.zhai.playandroid.MyApplication;
import com.study.zhai.playandroid.api.cookie.CookiesManager;
import com.study.zhai.playandroid.log.httplog.HttpLoggingInterceptorM;
import com.study.zhai.playandroid.log.httplog.LogInterceptor;
import com.study.zhai.playandroid.utils.ConstantUtil;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.security.KeyStore;
import java.security.SecureRandom;
import java.security.cert.CertificateFactory;

import javax.net.ssl.SSLContext;
import javax.net.ssl.SSLSocketFactory;
import javax.net.ssl.TrustManagerFactory;

import okhttp3.Cache;
import okhttp3.OkHttpClient;
import okio.Buffer;
import retrofit2.Retrofit;
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory;
import retrofit2.converter.gson.GsonConverterFactory;

/**
 * api retrofit 工具类
 *
 * @packageName: cn.white.ymc.wanandroidmaster.model
 * @fileName: ApiStore
 * @date: 2018/7/23  16:30
 * @author: ymc
 * @QQ:745612618
 */

public class ApiStore {

    private static Retrofit retrofit;

    public static String baseUrl = ConstantUtil.BASE_URL;

    public static <T> T createApi(Class<T> service){
        return retrofit.create(service);
    }

    static {
        createProxy();
    }

    /**
     * 创建 retrofit 客户端
     */
    private static void createProxy() {

        Gson gson = new GsonBuilder().setDateFormat("yyyy.MM.dd HH:mm:ss").create();

        //设置缓存路径
        File httpCacheDirectory = new File(MyApplication.getInstance().getCacheDir(), "responses");
        //设置缓存 10M
        Cache cache = new Cache(httpCacheDirectory, 10 * 1024 * 1024);

        // 创建拦截器实列，用来输出请求与响应的日志
        HttpLoggingInterceptorM interceptor = new HttpLoggingInterceptorM(new LogInterceptor());
        interceptor.setLevel(HttpLoggingInterceptorM.Level.BODY);

        OkHttpClient.Builder builder = new OkHttpClient().newBuilder()
                .cookieJar(new CookiesManager())
                .addInterceptor(new CommonParamsInterceptor())
                .addInterceptor(new OfflineCacheInterceptor()) //应用拦截器，优先于网络拦截器执行
                .addNetworkInterceptor(new NetCacheInterceptor()) //网络拦截器，只有有网时才会调用执行该拦截器
                .cache(cache);
        if (BuildConfig.DEBUG) {
            builder.addInterceptor(interceptor);
        }

        SSLSocketFactory sslSocketFactory = getSSLSocketFactory(new Buffer().writeUtf8(ConstantUtil.SSL_KEY).inputStream(),
                new Buffer().writeUtf8(ConstantUtil.MIDDLE_KEY).inputStream());
        if (sslSocketFactory != null) {
            //builder.sslSocketFactory(sslSocketFactory);
        }
        retrofit = new Retrofit.Builder()
                .baseUrl(baseUrl)
                .addConverterFactory(GsonConverterFactory.create(gson))
                .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
                .client(builder.build())
                .build();
    }

    /**
     *  ssl 工厂类
     * @param certificates certificates
     * @return SSLSocketFactory
     */
    private static SSLSocketFactory getSSLSocketFactory(InputStream... certificates) {
        try {
            CertificateFactory certificateFactory = CertificateFactory.getInstance("X.509");
            KeyStore keyStore = KeyStore.getInstance(KeyStore.getDefaultType());
            keyStore.load(null);
            int index = 0;
            for (InputStream certificate : certificates) {
                String certificateAlias = Integer.toString(index++);
                keyStore.setCertificateEntry(certificateAlias, certificateFactory.generateCertificate(certificate));

                try {
                    if (certificate != null){
                        certificate.close();
                    }
                } catch (IOException e) {
                }
            }

            SSLContext sslContext = SSLContext.getInstance("TLS");

            TrustManagerFactory trustManagerFactory =
                    TrustManagerFactory.getInstance(TrustManagerFactory.getDefaultAlgorithm());

            trustManagerFactory.init(keyStore);
            sslContext.init
                    (
                            null,
                            trustManagerFactory.getTrustManagers(),
                            new SecureRandom()
                    );
            return sslContext.getSocketFactory();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

}
