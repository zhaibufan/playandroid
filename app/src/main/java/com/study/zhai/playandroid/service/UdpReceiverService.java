package com.study.zhai.playandroid.service;

import android.app.Service;
import android.content.Intent;
import android.net.wifi.WifiInfo;
import android.net.wifi.WifiManager;
import android.os.Handler;
import android.os.IBinder;
import android.os.Message;
import android.util.Log;

import com.study.zhai.playandroid.MyApplication;
import com.study.zhai.playandroid.log.LogUtils;
import com.study.zhai.playandroid.utils.ConstantUtil;
import com.study.zhai.playandroid.utils.ZToast;

import java.io.BufferedWriter;
import java.io.IOException;
import java.io.OutputStream;
import java.io.PrintWriter;
import java.net.DatagramPacket;
import java.net.InetAddress;
import java.net.MulticastSocket;
import java.net.Socket;

public class UdpReceiverService extends Service {

    private static final String TAG = "UdpReceiverService";
    private static Socket socket;
    private WifiManager.MulticastLock lock;
    MulticastSocket ms = null;
    DatagramPacket dp;

    static String targetIp;

    Handler handler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            ZToast.showToast(MyApplication.getInstance(), String.valueOf(msg.obj));
        }
    };

    @Override
    public void onCreate() {
        super.onCreate();
        LogUtils.e(TAG, "onCreate");
        WifiManager manager = (WifiManager) this.getSystemService(WIFI_SERVICE);
        lock = manager.createMulticastLock("udpservice");
    }

    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        LogUtils.e(TAG, "onStartCommand");
        new Thread(new Runnable() {
            @Override
            public void run() {
                Message msg;
                String information;

                byte[] data = new byte[1024];
                try {
                    InetAddress groupAddress = InetAddress.getByName(ConstantUtil.IP_GROUP_UDP);
                    ms = new MulticastSocket(ConstantUtil.SEND_UDP_PORT);
                    ms.joinGroup(groupAddress);
                } catch (Exception e) {
                    e.printStackTrace();
                }
                while (true) {
                    try {
                        dp = new DatagramPacket(data, data.length);
                        if (ms != null) {
                            lock.acquire();
                            ms.receive(dp);
                        }
                    } catch (Exception e) {
                        LogUtils.e(TAG, "error = " + e.getMessage());
                        e.printStackTrace();
                    }

                    if (dp.getAddress() != null) {
                        String quest_ip = dp.getAddress().toString();
                        String host_ip = getLocalHostIp();

                        /* 若udp包的ip地址 是 本机的ip地址的话，丢掉这个包(不处理)*/
                        if( (!host_ip.equals(""))  && host_ip.equals(quest_ip.substring(1)) ) {
                            continue;
                        }

                        //测试是否收到udp消息
                        String codeString = new String(dp.getData(), 0, dp.getLength());
                        msg = new Message();
                        msg.what = 0X005;
                        information = "收到来自: \n" + quest_ip.substring(1) + "\n" +"的udp请求\n"
                                + "请求内容: " + codeString + "\n\n";
                        msg.obj = information;
                        handler.sendMessage(msg);

                        // 建立tcp连接
                        targetIp = dp.getAddress().toString().substring(1);
                        connectTcp(targetIp, ConstantUtil.SEND_UDP_PORT);
                    }
                    lock.release();
                }
            }
        }).start();

        return super.onStartCommand(intent, flags, startId);
    }

    /**
     * 建立tcp连接
     *
     * @param targetIp
     * @param port
     */
    private void connectTcp(String targetIp, int port) {
        try {
            if (socket == null) {
                socket = new Socket(targetIp, port);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**
     * 发送消息到服务端
     * @param msg
     */
    public static void sendMsg(String msg) {
        if (socket == null) {
            return;
        }

        //实现多次发送数据到服务端
        if (socket.isOutputShutdown()) {
            try {
                socket = new Socket(targetIp, ConstantUtil.SEND_UDP_PORT);
            } catch (IOException e) {
                e.printStackTrace();
            }
        }

        OutputStream outputStream = null;
        try {
            outputStream = socket.getOutputStream();
            PrintWriter pw = new PrintWriter(outputStream);
            BufferedWriter bw=new BufferedWriter(pw);
            bw.write(msg);
            bw.flush();
            socket.shutdownOutput();

        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            try {
                if (outputStream != null) {
                    outputStream.close();
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    private String getLocalHostIp() {
        //获取wifi服务
        WifiManager wifiManager = (WifiManager) this.getSystemService(WIFI_SERVICE);
        //判断wifi是否开启
        if (!wifiManager.isWifiEnabled()) {
            wifiManager.setWifiEnabled(true);
        }
        WifiInfo wifiInfo = wifiManager.getConnectionInfo();
        int ipAddress = wifiInfo.getIpAddress();
        Log.e(TAG,"ipAddress = " + ipAddress+"");
        return String.valueOf(ipAddress);
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        stopSelf();
    }
}
