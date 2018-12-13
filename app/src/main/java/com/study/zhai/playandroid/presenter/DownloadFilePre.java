package com.study.zhai.playandroid.presenter;

import android.os.Environment;
import android.text.TextUtils;
import android.util.Log;

import com.study.zhai.playandroid.R;
import com.study.zhai.playandroid.api.ApiService;
import com.study.zhai.playandroid.api.ApiStore;
import com.study.zhai.playandroid.base.BasePre;
import com.study.zhai.playandroid.base.BasePresenter;
import com.study.zhai.playandroid.contract.DownloadContract;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

import io.reactivex.Observer;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.Disposable;
import io.reactivex.schedulers.Schedulers;
import okhttp3.ResponseBody;

public class DownloadFilePre extends BasePresenter<DownloadContract.View> implements DownloadContract.Presenter {

    private static final String TAG = "DownloadFilePre";
    private static final String PATH_CHALLENGE_VIDEO = Environment.getExternalStorageDirectory() + "/DownloadFile";
    private String mFilePath; //下载到本地的视频路径
    private File mFile;

    @Override
    public void downloadFile(String url) {

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
            if (isAttachView()) {
                mView.onFailure("文件路径不对");
            }
            return;
        }
        if (mFile.exists()) {
            if (isAttachView()) {
                mView.onFinish(mFilePath);
            }
        } else {
            try {
                mFile.createNewFile();
            } catch (IOException e) {
                e.printStackTrace();
            }
            requestNet(url);
        }
    }

    private void requestNet(String url) {
        ApiStore.createApi(ApiService.class)
                .downloadFile(url)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Observer<ResponseBody>() {
                    @Override
                    public void onSubscribe(Disposable d) {

                    }

                    @Override
                    public void onNext(final ResponseBody response) {
                        new Thread(new Runnable() {
                            @Override
                            public void run() {
                                writeFile2Disk(response, mFile);
                            }
                        }).start();
                    }

                    @Override
                    public void onError(Throwable e) {
                        mView.showError(e.toString());
                    }

                    @Override
                    public void onComplete() {

                    }
                });
    }

    private void writeFile2Disk(ResponseBody response, File mFile) {
        if (isAttachView()) {
            mView.onStartDown();
        }
        long currentLength = 0;
        long totalLength;
        OutputStream os = null;

        if (response == null) {
            mView.onFailure("资源错误！");
            return;
        }
        InputStream is = response.byteStream();
        totalLength = response.contentLength();

        try {
            os = new FileOutputStream(mFile);
            int len;
            byte[] buff = new byte[1024];
            while ((len = is.read(buff)) != -1) {
                os.write(buff, 0, len);
                currentLength += len;
                mView.onProgress((int) (100 * currentLength / totalLength));

                if ((int) (100 * currentLength / totalLength) == 100) {
                    mView.onFinish(mFilePath);
                }
            }
        } catch (Exception e) {
            mView.onFailure("IO Exception");
            e.printStackTrace();
        } finally {
            if (os != null) {
                try {
                    os.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
            if (is != null) {
                try {
                    is.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
    }
}
