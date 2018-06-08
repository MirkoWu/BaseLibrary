package com.mirkowu.baselibrary.socketUtil;

import com.softgarden.baselibrary.utils.L;

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

    public static final int TIME_OUT = 600;//连接时间

    private int port;
    private String host;
    private Socket socket;
    private OutputStream outputStream;
    private InputStream inputStream;

    public TCPClient(String host, int port) throws Exception {
        this.host = host;
        this.port = port;
        // socket = new Socket(host, port);
        socket = new Socket();
        socket.setReuseAddress(true);//复用端口
        socket.setKeepAlive(true);

        socket.setSoTimeout(TIME_OUT);
        SocketAddress socketAddress = new InetSocketAddress(host, port);
        socket.connect(socketAddress, TIME_OUT);

        outputStream = socket.getOutputStream();
        inputStream = socket.getInputStream();
    }


    @Override
    public void send(byte[] data) throws Exception {
        L.d(new String(data));
        outputStream.write(data);
        outputStream.flush();
    }

    @Override
    public byte[] receive() throws Exception {
        if (inputStream.available() <= 0) return null;


        byte[] rec = new byte[inputStream.available()];
        inputStream.read(rec);

//        ByteArrayOutputStream outSteam = new ByteArrayOutputStream();
//        int length;
//        while ((length = inputStream.read(recBuffer)) != -1) {
//            outSteam.write(recBuffer, 0, length);
//        }
//        outSteam.close();
//        byte[] rec = outSteam.toByteArray();


        L.d(new String(rec));
        return rec;
    }

    @Override
    public void close() throws Exception {
        //  socket.shutdownInput();
        // socket.shutdownOutput();
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
