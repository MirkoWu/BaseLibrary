package com.mirkowu.baselibrarysample.socketUtil;

/**
 * @author by DELL
 * @date on 2018/1/15
 * @describe
 */

public interface ISocket {

    /*** 发送数据 */
    void send(byte[] data) throws Exception;

    /*** 接收数据 */
    byte[] receive() throws Exception;

    /*** 关闭socket */
    void close() throws Exception;

    boolean isClosed();

    boolean isConnected();

    String getHost();

    int getPort();
}
