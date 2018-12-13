package com.study.zhai.playandroid.api;

import com.google.gson.JsonObject;

import io.reactivex.Observable;
import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.http.Field;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.GET;
import retrofit2.http.POST;
import retrofit2.http.Path;
import retrofit2.http.Query;
import retrofit2.http.Streaming;
import retrofit2.http.Url;

/**
 * retrofit api 接口类
 *
 * @packageName: cn.white.ymc.wanandroidmaster.model.api
 * @fileName: ApiServer
 * @date: 2018/7/24  10:45
 * @author: ymc
 * @QQ:745612618
 */

public interface ApiService {
    /**
     * 主页
     */
    @GET("article/list/{page}/json")
    Observable<JsonObject> getArticleList(@Path("page") int num);

    @Streaming //大文件时要加不然会OOM
    @GET
    Observable<ResponseBody> downloadFile(@Url String fileUrl);

}
