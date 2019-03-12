package com.mirkowu.baselibrarysample.ui.testRefresh;

import com.mirkowu.baselibrarysample.api.GankNetworkTransformer;
import com.mirkowu.baselibrarysample.api.RetrofitClient;
import com.mirkowu.baselibrarysample.bean.ImageBean;
import com.softgarden.baselibrary.base.BasePresenter;
import com.softgarden.baselibrary.network.NetworkTransformer;

import java.util.List;

import io.reactivex.Observable;
import io.reactivex.functions.Consumer;

/**
 * @author by DELL
 * @date on 2018/4/12
 * @describe
 */
public class TestRefreshPresenter extends BasePresenter {


//    public void getData() {
//        RetrofitClient.getTestService()
//                .getData()
//                .compose(new NetworkTransformer<>(this))
//                .subscribe(new RxCallback<List<ImageBean>>() {
//                    @Override
//                    public void onSuccess(@Nullable List<ImageBean> data) {
//                        mView.getData(data);
//                    }
//                });
//    }

    public Observable<List<ImageBean>> getData2(int page,int pageSize) {
        return RetrofitClient.getTestService()
                .getImages(page,pageSize)
                .compose(new GankNetworkTransformer<>(this));
//                .subscribe(new RxCallback<List<ImageBean>>() {
//                    @Override
//                    public void onSuccess(@Nullable List<ImageBean> data) {
//                        mView.getData(data);
//                    }
//                });
    }

    public void getData3() {
        RetrofitClient.getTestService()
                .getData()
                .compose(new NetworkTransformer<>(this))
                .subscribe(new Consumer<List<ImageBean>>() {
                    @Override
                    public void accept(List<ImageBean> goodsBeans) throws Exception {

                    }
                }, throwable -> showError(throwable));
//                .subscribe(new RxCallback<List<ImageBean>>() {
//                    @Override
//                    public void onSuccess(@Nullable List<ImageBean> data) {
//                        mView.getData(data);
//                    }
//                });
    }


}
