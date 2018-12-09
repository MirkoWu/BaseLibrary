package com.mirkowu.baselibrary.api;


import com.mirkowu.baselibrary.bean.GoodsBean;
import com.softgarden.baselibrary.network.BaseBean;

import java.util.List;

import io.reactivex.Observable;
import retrofit2.http.POST;


public interface TestService {


    /**
     * 测试接口
     *
     * @return
     */

    //接口有参数时 要写上该注解
    // @FormUrlEncoded
    @POST(HostUrl.HOST_GOODSLIST)
    Observable<BaseBean<List<GoodsBean>>> getData(/*@Field("is_new") int is_new*/);

}
