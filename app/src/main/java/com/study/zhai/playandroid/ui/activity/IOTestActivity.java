package com.study.zhai.playandroid.ui.activity;

import android.os.Environment;
import android.view.View;

import com.study.zhai.playandroid.R;
import com.study.zhai.playandroid.base.BaseActivity;
import com.study.zhai.playandroid.log.LogUtils;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;

public class IOTestActivity extends BaseActivity {

    File file;

    @Override
    public int getLayoutId() {
        return R.layout.io_test;
    }

    @Override
    public void initView() {

    }
    public void write(View view) {
//        writeMsg("你好 哈哈哈哈");
        writeMsg2("一次写入一行");
    }

    public void read(View view) {
//        readInputStream();
        readInputStream2();
    }

    @Override
    public void initData() {
        String path = Environment.getExternalStorageDirectory() + "/iotest";
        file = new File(path);
        if (!file.exists()) {
            try {
                boolean newFile = file.createNewFile();
                LogUtils.d("newFile = " + newFile);
            } catch (IOException e) {
                e.printStackTrace();
            }
        }

    }

    private void readInputStream() {
        FileInputStream fis = null;
        try {
            fis = new FileInputStream(file);
            byte[] buffer = new byte[1024];
            StringBuilder sb = new StringBuilder();
            int data;
            while ((data = fis.read(buffer)) != -1) {
                String str = new String(buffer, 0, data);
                LogUtils.d("str = " +str);
                sb.append(str).append("-");
            }
            LogUtils.e("end------------");
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            try {
                if (fis != null) {
                    fis.close();
                }
            } catch (IOException e) {

            }
        }
    }

    private void readInputStream2() {
        FileReader fr = null;
        try {
            fr = new FileReader(file);
            BufferedReader br = new BufferedReader(fr);
            String line;
            while ((line = br.readLine()) != null) {
                LogUtils.e("line = " + line);
            }
            LogUtils.e("end------------");
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
        try {
            if (fr != null) {
                fr.close();
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
    }

    private void writeMsg(String msg) {
        FileOutputStream fos = null;
        try {
            fos = new FileOutputStream(file, true);
            byte[] bytes = msg.getBytes();
            fos.write(bytes);
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            try {
                if (fos != null) {
                    fos.close();
                }
            } catch (IOException e) {

            }
        }
    }

    private void writeMsg2(String msg) {
        FileWriter fw = null;
        BufferedWriter bw = null;
        try {
            fw = new FileWriter(file, true);
            bw = new BufferedWriter(fw);
            bw.write(msg);
            bw.flush(); //刷新 没有这句代码写入失败
        } catch (IOException e) {
            e.printStackTrace();
            LogUtils.e("write error --->" + e.getMessage());
        } finally {
            try {
                if (fw != null) {
                    fw.close();
                }
            } catch (IOException e) {
                e.printStackTrace();
                LogUtils.e("write error --->" + e.getMessage());
            }
        }
    }
}
