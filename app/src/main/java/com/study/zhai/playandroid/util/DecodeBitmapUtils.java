package com.study.zhai.playandroid.util;

import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;

import com.study.zhai.playandroid.log.LogUtils;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;

/**
 * 解析并压缩图片
 */
public class DecodeBitmapUtils {

    private static final String TAG = "DecodeBitmapUtils";

    /**
     * 解析本地图片，生成bitmap对象并压缩
     *
     * @param pathName
     * @param targetWidth
     * @param targetHeight
     * @return
     */
    public static Bitmap compressBySize(String pathName, int targetWidth,
                                        int targetHeight) {
        BitmapFactory.Options options = new BitmapFactory.Options();
        options.inJustDecodeBounds = true;// 不去真的解析图片，只是获取图片的头部信息，包含宽高等；
        BitmapFactory.decodeFile(pathName,options);

        // 得到图片的宽度、高度；
        float imgWidth = options.outWidth;
        float imgHeight = options.outHeight;

        // 分别计算图片宽度、高度与目标宽度、高度的比例；取大于等于该比例的最小整数；
        int widthRatio = (int) Math.ceil(imgWidth / (float) targetWidth);
        int heightRatio = (int) Math.ceil(imgHeight / (float) targetHeight);
        options.inSampleSize = 1;
        LogUtils.d(TAG, "widthRatio="+widthRatio+"  heightRatio="+heightRatio);
        // 图片本身尺寸大于目标尺寸，就压缩
        if (widthRatio > 1 || widthRatio > 1) {
            if (widthRatio > heightRatio) {
                options.inSampleSize = widthRatio;
            } else {
                options.inSampleSize = heightRatio;
            }
        }
        LogUtils.d(TAG, "options.inSampleSize="+options.inSampleSize);
        //设置好缩放比例后，加载图片进内容；
        options.inJustDecodeBounds = false; // 这里一定要设置false
        Bitmap bitmap = BitmapFactory.decodeFile(pathName, options);
        LogUtils.d(TAG, "bitmap="+bitmap);
        return bitmap;
    }

    /**
     * 解析IO流生成bitmap对象并压缩
     *
     *          这里将InputStream转成byte数组，然后使用BitmapFactory.decodeByteArray方法读取图片。
     *          如果使用BitmapFactory.decodeStream由于操作了两次流，会有bug，详见http://www.jianshu.com/p/634fc6feb47b
     *
     * @param is
     * @param targetWidth
     * @param targetHeight
     * @return
     */
    public static Bitmap compressBySize(InputStream is, int targetWidth,
                                        int targetHeight) {
        try {
            byte[] bytes = toByteArray(is);
            BitmapFactory.Options options = new BitmapFactory.Options();
            options.inJustDecodeBounds = true;// 不去真的解析图片，只是获取图片的头部信息，包含宽高等
            BitmapFactory.decodeByteArray(bytes, 0, bytes.length, options);
            options.inPreferredConfig = Bitmap.Config.RGB_565;

            // 得到图片的宽度、高度；
            float imgWidth = options.outWidth;
            float imgHeight = options.outHeight;

            LogUtils.d(TAG, "targetWidth="+targetWidth+"  targetHeight="+targetHeight);
            LogUtils.d(TAG, "imgWidth="+imgWidth+"  imgHeight="+imgHeight);

            // 分别计算图片宽度、高度与目标宽度、高度的比例；取大于等于该比例的最小整数；
            int widthRatio = (int) Math.ceil(imgWidth / (float) targetWidth);
            int heightRatio = (int) Math.ceil(imgHeight / (float) targetHeight);
            options.inSampleSize = 1;

            // 如果尺寸接近则不压缩，否则进行比例压缩
            if (widthRatio > 1 || widthRatio > 1) {
                if (widthRatio > heightRatio) {
                    options.inSampleSize = widthRatio;
                } else {
                    options.inSampleSize = heightRatio;
                }
            }
            //设置好缩放比例后，加载图片进内容；
            options.inJustDecodeBounds = false; // 这里一定要设置false
            Bitmap bitmap = BitmapFactory.decodeByteArray(bytes, 0, bytes.length, options);
            LogUtils.d(TAG, "options.inSampleSize="+options.inSampleSize+" bitmap="+bitmap);
            return bitmap;
        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }

    /**io流转byte数组*/
    public static byte[] toByteArray(InputStream input) throws IOException {
        ByteArrayOutputStream output = new ByteArrayOutputStream();
        byte[] buffer = new byte[4096];
        int n = 0;
        while (-1 != (n = input.read(buffer))) {
            output.write(buffer, 0, n);
        }
        return output.toByteArray();
    }

    /**
     * 解析drawable下的资源图片生成bitmap对象并压缩
     *
     * @param resource
     * @param resId
     * @param targetWidth
     * @param targetHeight
     * @return
     */
    public static Bitmap compressBySize(Resources resource, int resId, int targetWidth,
                                        int targetHeight) {
        BitmapFactory.Options options = new BitmapFactory.Options();
        options.inJustDecodeBounds = true;// 不去真的解析图片，只是获取图片的头部信息，包含宽高等；
        BitmapFactory.decodeResource(resource, resId,options);

        // 得到图片的宽度、高度；
        float imgWidth = options.outWidth;
        float imgHeight = options.outHeight;

        // 分别计算图片宽度、高度与目标宽度、高度的比例；取大于等于该比例的最小整数；
        int widthRatio = (int) Math.ceil(imgWidth / (float) targetWidth);
        int heightRatio = (int) Math.ceil(imgHeight / (float) targetHeight);
        options.inSampleSize = 1;
        LogUtils.d(TAG, "widthRatio="+widthRatio+"  heightRatio="+heightRatio);
        // 图片本身尺寸大于目标尺寸，就压缩
        if (widthRatio > 1 || widthRatio > 1) {
            if (widthRatio > heightRatio) {
                options.inSampleSize = widthRatio;
            } else {
                options.inSampleSize = heightRatio;
            }
        }
        LogUtils.d(TAG, "options.inSampleSize="+options.inSampleSize);
        //设置好缩放比例后，加载图片进内容；
        options.inJustDecodeBounds = false; // 这里一定要设置false
        Bitmap bitmap = BitmapFactory.decodeResource(resource, resId,options);
        LogUtils.d(TAG, "bitmap="+bitmap);
        return bitmap;
    }
}
