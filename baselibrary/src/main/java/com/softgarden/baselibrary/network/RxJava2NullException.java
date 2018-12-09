package com.softgarden.baselibrary.network;

/**
 * Created by MirkoWu on 2017/5/25 0025.
 */

public class RxJava2NullException extends RuntimeException {

    /**
     * RxJava2不能发生null ,但数据有时会返回null ,
     * so,这里主动抛出异常并进行主动捕捉，将数据通过Callback正确返回
     */

    public RxJava2NullException() {
        super("RxJava2 cannot send null");
    }
}
