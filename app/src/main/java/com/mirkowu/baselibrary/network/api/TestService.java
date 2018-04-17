package com.mirkowu.baselibrary.network.api;


import com.mirkowu.baselibrary.bean.TestBean;
import com.mirkowu.baselibrary.network.BaseBean;

import io.reactivex.Observable;
import retrofit2.http.Field;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.POST;


public interface TestService {


    /**
     * 测试接口
     *
     * @param is_new {String}(可选)是否用新接口 1 是,0 否
     * @return
     */
    @FormUrlEncoded
    @POST(HostUrl.LOGIN_APP_LOGIN)
    Observable<BaseBean<TestBean>> getData(@Field("is_new") int is_new);

}
