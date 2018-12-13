package com.study.zhai.playandroid.util;

import android.os.Environment;
import android.text.TextUtils;
import android.util.Log;

import com.study.zhai.playandroid.base.BasePre;

import java.io.File;

public class DownloadUtils {

    private static final String TAG = "";
    private static final String PATH_CHALLENGE_VIDEO = Environment.getExternalStorageDirectory() + "/DownloadFile";
    private String mFilePath; //下载到本地的视频路径
    private File mFile;

    public void downloadFile(String url, BasePre pre) {
        String name = url;
        File dir = new File(PATH_CHALLENGE_VIDEO);
        if (dir != null) {
            if (!dir.exists() || !dir.isDirectory()) {
                dir.mkdir();
            }
            int i = name.lastIndexOf('/');//一定是找最后一个'/'出现的位置
            if (i != -1) {
                name = name.substring(i);
                mFilePath = PATH_CHALLENGE_VIDEO + name;
            }
        }

        if (TextUtils.isEmpty(mFilePath)) {
            Log.e(TAG, "downloadVideo: 存储路径为空了");
            return;
        }
        mFile = new File(mFilePath);
        if (mFile == null ) {
            Log.e(TAG, "downloadVideo: 文件路径为空了");
            return;
        }
        if (mFile.exists()) {

        }

    }
}
