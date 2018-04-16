package com.mirkowu.baselibrary.network.api;

/**
 * @author by DELL
 * @date on 2017/12/22
 * @describe
 */

public interface HostUrl {
    /**
     * app接口调试
     * http://47.52.5.170:9002/zhuawawaApp/
     * 文件上传地址
     * http://47.52.5.170:9002/zhuawawaFile/
     * 后台管理地址
     * http://47.52.5.170:9002/zhuawawa/
     * 文件访问地址
     * http://47.52.5.170:9001/zhuawawaMedia/
     */
    String HOST_URL = "http://47.52.5.170:9002/zhuawawaApp/";

    /*** 登录模块 */
    String LOGIN_APP_LOGIN = "login/appLogin";//登录接口


}
