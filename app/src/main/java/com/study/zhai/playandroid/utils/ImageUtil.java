package com.study.zhai.playandroid.utils;

import android.graphics.Bitmap;

/**
 * @author zhaixiaofan
 * @date 2019/1/19 12:45 PM
 */
public class ImageUtil {

    /**
     * 裁剪bitmap
     *
     * @param bitmap
     * @return
     */
    public static synchronized Bitmap cropSquareBitmap(Bitmap bitmap) {
        return cropSquareBitmap(bitmap, Integer.MAX_VALUE);
    }

    /**
     * 从中间截取一个正方形
     *
     * @param bitmap
     * @param maxWH
     * @return
     */
    public static synchronized Bitmap cropSquareBitmap(Bitmap bitmap, int maxWH) {
        int w = bitmap.getWidth(); // 得到图片的宽，高
        int h = bitmap.getHeight();
        int cropWidth = Math.min(maxWH, Math.min(w, h));
        return Bitmap.createBitmap(bitmap, (bitmap.getWidth() - cropWidth) / 2, (bitmap.getHeight() - cropWidth) / 2, cropWidth, cropWidth);
    }
}
