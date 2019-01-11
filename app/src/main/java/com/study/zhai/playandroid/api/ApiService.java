package com.study.zhai.playandroid.api;

import com.google.gson.JsonObject;

import io.reactivex.Observable;
import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import okhttp3.ResponseBody;
import retrofit2.http.GET;
import retrofit2.http.Multipart;
import retrofit2.http.POST;
import retrofit2.http.Part;
import retrofit2.http.Path;
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

    /**
     * 文件上传示例
     * 可传递普通参数及文件参数
     * 普通参数需使用RequestBody进行封装，将其值封装进RequestBody中
     *
     * @param userName 普通参数,通过RequestBody进行封装，
     * @param filePart 封装了文件Content-type(文件类型)、文件名称及路径的 Part
     * @Part("userName") 中的userName相当于普通参数的key
     */
    @Multipart
    @POST()
    Observable<String> uploadFile(@Part("userName") RequestBody userName, @Part MultipartBody.Part filePart);

    /*
    uploadFile的使用：
    RequestBody userParamBody = RequestBody.create(null, "zhangsan");
    String fileType = FileUtil.getMimeType(file.getAbsolutePath());
    MediaType mediaType = MediaType.parse(fileType);
    RequestBody fileParamBody = RequestBody.create(mediaType, file);
    MultipartBody.Part filePart = MultipartBody.Part.createFormData("userAvatar", file.getName(), fileParamBody);
    Observable<String> observable = BaseServiceUtil.createService(ApiService.class).uploadFile(userParamBody, filePart);
    */

}
