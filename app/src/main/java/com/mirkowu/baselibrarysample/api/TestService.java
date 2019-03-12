package com.mirkowu.baselibrarysample.api;


import com.mirkowu.baselibrarysample.base.GankBaseBean;
import com.mirkowu.baselibrarysample.bean.ImageBean;
import com.softgarden.baselibrary.network.BaseBean;

import java.util.List;

import io.reactivex.Observable;
import retrofit2.http.GET;
import retrofit2.http.POST;
import retrofit2.http.Path;
import retrofit2.http.Url;


public interface TestService {


    /**
     * 测试接口
     *
     * @return
     */

    //接口有参数时 要写上该注解
    // @FormUrlEncoded
    @POST(HostUrl.HOST_Gank_Image)
    Observable<BaseBean<List<ImageBean>>> getData(/*@Field("is_new") int is_new*/);

    /**
     * gank 图片列表
     *
     * @param page
     * @param pageSize
     * @return
     */
    @GET(HostUrl.HOST_Gank_Image)
    Observable<GankBaseBean<List<ImageBean>>> getImages(@Path("page") int page, @Path("pageSize") int pageSize);

    @POST
    Observable<BaseBean<List<ImageBean>>> getData(@Url String url/*@Field("is_new") int is_new*/);

}
