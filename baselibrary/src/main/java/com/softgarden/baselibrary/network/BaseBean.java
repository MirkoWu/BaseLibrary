package com.softgarden.baselibrary.network;

/**
 * Created by Administrator on 2016/11/11 0011.
 */

public class BaseBean<T> {
    /**
     * 状态码
     * 0  获取失败
     * 1  获取成功
     */
    public int status;

    /**
     * 错误信息 ,成功则返回空
     */
    public String info;
    /**
     * 时间
     */
    public long time;

    /**
     * 数据
     */
    public T data;


    @Override
    public String toString() {
        return "BaseBean{" +
                "code=" + status +
                ", msg='" + info + '\'' +
                ", time=" + time +
                ", data=" + data +
                '}';
    }
}
