package com.mirkowu.baselibrarysample.socketUtil;

import android.content.Context;
import android.net.wifi.WifiManager;

import com.softgarden.baselibrary.BaseApplication;
import com.softgarden.baselibrary.utils.L;

import java.net.DatagramPacket;
import java.net.InetAddress;
import java.net.InetSocketAddress;
import java.net.MulticastSocket;

/**
 * @author by DELL
 * @date on 2018/1/15
 * @describe
 */

public class UDPClient implements ISocket {

    public static final int TIME_OUT = 3000;
    public static final int TTL = 4;

    private int port;
    private String host;
    private DatagramPacket packet;
    private MulticastSocket socket;
    private InetAddress destAddress;
    private WifiManager wifiManager;
    private WifiManager.MulticastLock multicastLock;

    public UDPClient(String host, int port) throws Exception {
        this.host = host;
        this.port = port;
        allowMultiCast();
        destAddress = InetAddress.getByName(host);
        if (!destAddress.isMulticastAddress()) {// 检测该地址是否是多播地址
            return;
        }
        socket = new MulticastSocket(null);

        socket.setReuseAddress(true);//复用端口
        socket.setBroadcast(true);
        // socket.setTimeToLive(TTL);
        socket.bind(new InetSocketAddress(port));
        socket.joinGroup(destAddress);
    }

    private void allowMultiCast() {
        wifiManager = (WifiManager) BaseApplication.getInstance().getApplicationContext().getSystemService(Context.WIFI_SERVICE);
        multicastLock = wifiManager.createMulticastLock("findDevices");
        multicastLock.acquire();

    }

    @Override
    public void send(byte[] data) throws Exception {
        L.d(new String(data));
        DatagramPacket dp = new DatagramPacket(data, data.length, destAddress, port);
        socket.send(dp);
    }

    @Override
    public byte[] receive() throws Exception {
        DatagramPacket packet = getReceivePacket();

        socket.receive(packet);
        byte[] rec = sublist(packet.getData(), packet.getOffset(), packet.getLength());
        L.d(new String(rec));
        return rec;
    }

    @Override
    public void close() throws Exception {
        socket.close();
    }

    private DatagramPacket getReceivePacket() {
        if (packet == null) {
            byte data[] = new byte[2048];
            packet = new DatagramPacket(data, data.length);
        }
        return packet;
    }

    public static byte[] sublist(byte[] data, int start, int end) {
        if (data == null || data.length < start) return null;
        byte[] bytes = new byte[Math.min(data.length, end) - start];
        System.arraycopy(data, start, bytes, 0, bytes.length);
        return bytes;
    }

    @Override
    public boolean isClosed() {
        return socket.isClosed();
    }

    @Override
    public boolean isConnected() {
        return socket.isConnected();
    }

    @Override
    public String getHost() {
        return host;
    }

    @Override
    public int getPort() {
        return port;
    }
}
