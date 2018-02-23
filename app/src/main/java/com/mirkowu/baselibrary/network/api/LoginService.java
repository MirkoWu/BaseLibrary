package com.mirkowu.baselibrary.network.api;


import com.mirkowu.baselibrary.network.BaseBean;

import io.reactivex.Observable;
import retrofit2.http.Field;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.POST;


public interface LoginService {


    /**
     * 登录
     *
     * @param loginType （必须）登录类型，1：QQ, 2: 微信, 3：微博, 4：手机号
     * @param openid    （第三方登录）第三方登录时返回的唯一标识
     * @param name      （第三方登录）用户名
     * @param headerImg （第三方登录）用户头像
     * @return
     */
    @FormUrlEncoded
    @POST(HostUrl.LOGIN_APP_LOGIN)
    Observable<BaseBean<String>> loginThridParty(@Field("loginType") int loginType,
                                                 @Field("openid") String openid,
                                                 @Field("name") String name,
                                                 @Field("headerImg") String headerImg);

}
