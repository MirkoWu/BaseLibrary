package com.mirkowu.baselibrarysample.ui.testMvp;

import com.mirkowu.baselibrarysample.api.RetrofitClient;
import com.mirkowu.baselibrarysample.bean.GoodsBean;
import com.softgarden.baselibrary.base.BasePresenter;
import com.softgarden.baselibrary.network.NetworkTransformer;

import java.util.List;

import io.reactivex.Observable;

/**
 * @author by DELL
 * @date on 2018/4/12
 * @describe
 */
public class TestMvpPresenter extends BasePresenter {


    public Observable<List<GoodsBean>> getIndexData() {
        return RetrofitClient.getTestService()
                .getData()
                .compose(new NetworkTransformer<>(this));
//                .subscribe(new RxCallback<List<GoodsBean>>() {
//                    @Override
//                    public void onSuccess(@Nullable List<GoodsBean> data) {
//                        mView.getIndexData(data);
//                    }
//
//                });
    }


//    public void switchOnOff() {
//      return;  RetrofitClient.getHomeService()
//                .switchOnOff()
//                .compose(new NetworkTransformer<>(mView));
//                .subscribe(new RxCallback<String>() {
//                    @Override
//                    public void onSuccess(@Nullable String data) {
//                        mView.switchOnOff(data);
//                    }
//                });
//    }


    public void switchBluetooth() {
//        RetrofitClient.getHomeService()
//                .switchBluetooth()
//                .compose(new NetworkTransformer<>(mView))
//                .subscribe(new RxCallback<String>() {
//                    @Override
//                    public void onSuccess(@Nullable String data) {
//                        mView.switchBluetooth(data);
//                    }
//                });

    }
}
