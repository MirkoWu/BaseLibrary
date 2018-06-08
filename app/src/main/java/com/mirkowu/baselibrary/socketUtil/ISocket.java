package com.mirkowu.baselibrary.socketUtil;

/**
 * @author by DELL
 * @date on 2018/1/15
 * @describe
 */

public interface ISocket {
    enum SocketType{TCP,UDP}

    void send(byte[] data) throws Exception;

    byte[] receive() throws Exception;

    void close() throws Exception;

    boolean isClosed();

    boolean isConnected();

    String getHost();

    int getPort();
}
