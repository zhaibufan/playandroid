package com.study.zhai.playandroid.utils;

import android.content.res.Resources;
import android.text.TextUtils;
import android.util.DisplayMetrics;
import android.webkit.MimeTypeMap;

public class ApplicationUtils {

    /**
     * 根据本地文件路径获取MIME类型 一般用于上传文件时的MediaType类型
     *
     *      使用：MediaType mediaType = MediaType.parse(getMimeType(file.getAbsolutePath()));
     *
     * @param filePath
     * @return
     */
    public static String getMimeType(String filePath) {
        String extension = MimeTypeMap.getFileExtensionFromUrl(filePath);
        if (TextUtils.isEmpty(extension)) {
            return "file/*";
        }
        String mimeType = MimeTypeMap.getSingleton().getMimeTypeFromExtension(extension);
        if (!TextUtils.isEmpty(mimeType)) {
            return mimeType;
        }
        return "file/*";
    }

    public static float getScreenHeight() {
        DisplayMetrics dm = Resources.getSystem().getDisplayMetrics();
        return dm.heightPixels;
    }

    public static float getScreenWidth() {
        DisplayMetrics dm = Resources.getSystem().getDisplayMetrics();
        return dm.widthPixels;
    }
}
