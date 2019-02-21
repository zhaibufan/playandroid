package com.study.zhai.playandroid.utils.udp;

import android.content.Context;
import android.net.wifi.WifiInfo;
import android.net.wifi.WifiManager;
import android.os.Handler;
import android.os.Message;
import android.util.Log;

import com.study.zhai.playandroid.utils.ConstantUtil;

import java.net.DatagramPacket;
import java.net.InetAddress;
import java.net.MulticastSocket;

import static android.content.Context.WIFI_SERVICE;

/**
 * 接收UDP多播
 */
public class ReceiveGroupUdp extends Thread {

    private static final String TAG = "ReceiveGroupUdp";
    MulticastSocket ms = null;
    DatagramPacket dp;
    Handler handler;
    Context context;
    /**
     * 接收多播的锁对象，因为手机默认情况下是不会接收多播的，所以调用MulticastLock对象的acquire方法，
     * 获取到组播锁，相应的，用完组播，为了不浪费电力，要调用MulticastLock的release方法释放锁
     */
    WifiManager.MulticastLock lock;

    public ReceiveGroupUdp(Handler handler, Context context) {
        this.handler = handler;
        this.context = context;

        WifiManager manager = (WifiManager) context.getSystemService(WIFI_SERVICE);
        lock = manager.createMulticastLock("udpservice");
    }
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
            Log.e(TAG, "接收组播");
            try {
                dp = new DatagramPacket(data, data.length);
                if (ms != null) {
                    lock.acquire();
                    ms.receive(dp);
                }
            } catch (Exception e) {
                e.printStackTrace();
            }

            if (dp.getAddress() != null) {
                final String quest_ip = dp.getAddress().toString();
                String host_ip = getLocalHostIp();

                Log.e(TAG, "quest_ip = " + quest_ip +" host_ip = " +host_ip);

                /* 若udp包的ip地址 是 本机的ip地址的话，丢掉这个包(不处理)*/

                if( (!host_ip.equals(""))  && host_ip.equals(quest_ip.substring(1)) ) {
                    continue;
                }

                final String codeString = new String(dp.getData(), 0, dp.getLength());

                msg = new Message();
                msg.what = 0X003;
                information = "收到来自: \n" + quest_ip.substring(1) + "\n" +"的udp请求\n"
                        + "请求内容: " + codeString + "\n\n";
                msg.obj = information;
                handler.sendMessage(msg);
            }
            lock.release();
        }
    }

    private String getLocalHostIp() {
        //获取wifi服务
        WifiManager wifiManager = (WifiManager) context.getSystemService(WIFI_SERVICE);
        //判断wifi是否开启
        if (!wifiManager.isWifiEnabled()) {
            wifiManager.setWifiEnabled(true);
        }
        WifiInfo wifiInfo = wifiManager.getConnectionInfo();
        int ipAddress = wifiInfo.getIpAddress();
        Log.e(TAG,"ipAddress = " + ipAddress+"");
        return String.valueOf(ipAddress);
    }
}
