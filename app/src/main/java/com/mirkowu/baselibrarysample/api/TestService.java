package com.mirkowu.baselibrarysample.api;


import com.mirkowu.baselibrarysample.bean.GoodsBean;
import com.softgarden.baselibrary.network.BaseBean;

import java.util.List;

import io.reactivex.Observable;
import retrofit2.http.POST;
import retrofit2.http.Url;


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

    @POST
    Observable<BaseBean<List<GoodsBean>>> getData(@Url String url/*@Field("is_new") int is_new*/);

}
