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
    String LOGIN_GET_CAPTCHA = "login/getCaptcha";//获取验证码
    String LOGIN_CHECK_CAPTCHA = "login/checkCaptcha";//校验验证码
    String LOGIN_REGISTER = "login/appEnroll";//注册用户
    String LOGIN_RESET_PASSWORD = "login/resetPassword";//忘记密码
    String LOGIN_UPDATE_PWD = "login/updatePwd";//修改密码

    /*** 首页模块 */
    String HOME_BANNER = "homepage/findBannerInfo";//获取轮播图信息
    String HOME_COLUMN = "homepage/findColumnInfo";//获取栏目信息
    String HOME_GET_PRODUCT = "homepage/findProductInfo";//获取产品信息

    /*** 个人中心模块 */
    String USER_INFO = "personal/findPersonalInfo";//获取个人信息
    String USER_INFO_DETAIL = "personal/findPersonalDetailsInfo";//获取个人信息详情
    String USER_INFO_UPDATE = "personal/updatePersonalInfo";//修改个人信息
    String USER_ADDRESSLIST = "personal/findUserAddressInfo";//收货地址列表
    String USER_ADDRESS_UPDATE = "personal/addOrUpdateUserAddressInfo";//.添加或修改收货地址
    String USER_ADDRESS_DEL = "personal/deleteUserAddressInfo";//.删除收货地址
    String USER_ADDRESS_REGIONINFO = "personal/findRegionInfo";//.获取省，市，区信息

    /*** 更多模块 */
    String MORE_SUBMIT_FEEDBACK = "more/submitFeedbackInfo";//提交用户反馈信息

}
