package com.study.zhai.playandroid.utils.udp;

import com.study.zhai.playandroid.utils.ConstantUtil;

import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;

public class SendSingleUdp extends Thread {

    private byte[] by;
    private DatagramSocket socket;
    private DatagramPacket packet;

    public SendSingleUdp(String data) {
        by = data.getBytes();
    }

    @Override
    public void run() {
        super.run();
        try {
            socket = new DatagramSocket();
            InetAddress address = InetAddress.getByName(ConstantUtil.IP_SINGLE_UDP);
            packet = new DatagramPacket(by, by.length, address, ConstantUtil.SEND_UDP_PORT);
            socket.send(packet);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
