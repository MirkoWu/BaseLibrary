package com.mirkowu.baselibrarysample.socketUtil;

import com.softgarden.baselibrary.utils.L;
import com.softgarden.baselibrary.utils.StringUtil;

import java.io.InputStream;
import java.io.OutputStream;
import java.net.InetSocketAddress;
import java.net.Socket;
import java.net.SocketAddress;

/**
 * @author by DELL
 * @date on 2018/1/15
 * @describe
 */

public class TCPClient implements ISocket {

    public static final int TIME_OUT = 3000;//连接时间

    private int length = 0;
    private int port;
    private String host;
    private Socket socket;
    private OutputStream outputStream;
    private InputStream inputStream;
    private byte[] recBuffer = new byte[1024];//读取的buffer

    public TCPClient(String host, int port) throws Exception {
        this.host = host;
        this.port = port;
        socket = new Socket();
        socket.setReuseAddress(true);//复用端口
        socket.setKeepAlive(true);
        //    socket.setSoTimeout(TIME_OUT);//读写超时
        SocketAddress socketAddress = new InetSocketAddress(host, port);
        socket.connect(socketAddress, TIME_OUT);//连接socket

        outputStream = socket.getOutputStream();
        inputStream = socket.getInputStream();

    }


    @Override
    public void send(byte[] data) throws Exception {
        L.d("发送：" + new String(data) + "\n16进制：" + StringUtil.byte2Hexstr(data));
        outputStream.write(data);
        outputStream.flush();
    }

    @Override
    public byte[] receive() throws Exception {
        if ((length = inputStream.read(recBuffer)) != -1) {
            byte[] rec = sublist(recBuffer, 0, length);
            L.e("接收：" + new String(rec) + "\n16进制：" + StringUtil.byte2Hexstr(rec));
            return rec;
        }
        return null;


//        if (inputStream.available() <= 0) return null;
//        byte[] rec = new byte[inputStream.available()];
//        inputStream.read(rec);


    }

    public static byte[] sublist(byte[] data, int start, int end) {
        if (data == null || data.length < start) return null;
        byte[] bytes = new byte[Math.min(data.length, end) - start];
        System.arraycopy(data, start, bytes, 0, bytes.length);
        return bytes;
    }

    @Override
    public void close() throws Exception {
//        socket.shutdownInput();
//        socket.shutdownOutput();
        // inputStream.close();
        outputStream.close();
        socket.close();
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
