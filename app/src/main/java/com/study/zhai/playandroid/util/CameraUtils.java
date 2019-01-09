package com.study.zhai.playandroid.util;

import android.hardware.Camera.Size;

import com.study.zhai.playandroid.log.LogUtils;

import java.util.Collections;
import java.util.Comparator;
import java.util.List;

public class CameraUtils {

    private static final String TAG = "CameraUtils";

    /**
     * 根据比例得到合适的尺寸的最大尺寸
     */
    public static Size getProperSize4Ratio(List<Size> sizeList, float displayRatio) {
        LogUtils.e(TAG, "displayRatio = " + displayRatio);
        Collections.sort(sizeList, new SizeL2hComparator());
        for (Size size : sizeList) {
            LogUtils.e(TAG, "width = " + size.width +"  height = " +size.height);
        }
        Size result = null;
        for (Size size : sizeList) {
            float curRatio = ((float) size.width) / size.height;
            if (curRatio == displayRatio) {
                result = size;
            }
        }

        if (null == result) {
            for (Size size : sizeList) {
                float curRatio = ((float) size.width) / size.height;
                if (curRatio == 4f / 3) {
                    result = size;
                }
            }
        }
        LogUtils.e(TAG, "result = " +result);
        return result;
    }

    /**
     * 根据宽度得到最大合适的尺寸
     * @param sizeList
     * @param Width
     * @return
     */
    public static Size getMaxSize4Width(List<Size> sizeList, int Width) {
        // 先对传进来的size列表进行排序
        Collections.sort(sizeList, new SizeL2hComparator());
        Size result = null;
        for (Size size : sizeList) {
            if (size.height == Width) {
                result = size;
            }
        }
        return result;
    }

    /**
     * 获取支持的最大尺寸
     */
    public static Size getMaxSize(List<Size> sizeList) {
        // 先对传进来的size列表进行排序
        Collections.sort(sizeList, new SizeL2hComparator());
        Size result = null;
        if(sizeList != null && !sizeList.isEmpty()){
            result = sizeList.get(sizeList.size() - 1);
        }
        return result;
    }

    /**
     * 从小到大排序
     */
    private static class SizeL2hComparator implements Comparator<Size> {
        @Override
        public int compare(Size size1, Size size2) {
            if (size1.width < size2.width) {
                return -1;
            }else if (size1.width > size2.width) {
                return 1;
            }
            return 0;
        }
    }

    public static int getRecorderRotation(int cameraId){
        android.hardware.Camera.CameraInfo info = new                 android.hardware.Camera.CameraInfo();
        android.hardware.Camera.getCameraInfo(cameraId, info);
        return info.orientation;
    }

}
