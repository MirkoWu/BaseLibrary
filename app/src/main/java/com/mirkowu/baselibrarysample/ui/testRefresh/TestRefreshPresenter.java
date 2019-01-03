package com.mirkowu.baselibrarysample.ui.testRefresh;

import com.mirkowu.baselibrarysample.bean.GoodsBean;
import com.softgarden.baselibrary.network.NetworkTransformer;
import com.mirkowu.baselibrarysample.api.RetrofitClient;
import com.softgarden.baselibrary.base.BasePresenter;

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
//                .subscribe(new RxCallback<List<GoodsBean>>() {
//                    @Override
//                    public void onSuccess(@Nullable List<GoodsBean> data) {
//                        mView.getData(data);
//                    }
//                });
//    }

    public Observable<List<GoodsBean>> getData2() {
        return RetrofitClient.getTestService()
                .getData()
                .compose(new NetworkTransformer<>(this));
//                .subscribe(new RxCallback<List<GoodsBean>>() {
//                    @Override
//                    public void onSuccess(@Nullable List<GoodsBean> data) {
//                        mView.getData(data);
//                    }
//                });
    }

    public void getData3() {
        RetrofitClient.getTestService()
                .getData()
                .compose(new NetworkTransformer<>(this))
                .subscribe(new Consumer<List<GoodsBean>>() {
                    @Override
                    public void accept(List<GoodsBean> goodsBeans) throws Exception {

                    }
                }, throwable -> showError(throwable));
//                .subscribe(new RxCallback<List<GoodsBean>>() {
//                    @Override
//                    public void onSuccess(@Nullable List<GoodsBean> data) {
//                        mView.getData(data);
//                    }
//                });
    }


}
