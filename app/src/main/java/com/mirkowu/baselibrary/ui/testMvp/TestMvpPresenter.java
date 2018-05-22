package com.mirkowu.baselibrary.ui.testMvp;

import com.mirkowu.baselibrary.bean.GoodsBean;
import com.mirkowu.baselibrary.network.NetworkTransformer;
import com.mirkowu.baselibrary.network.RetrofitClient;
import com.mirkowu.baselibrary.network.RxCallback;
import com.softgarden.baselibrary.base.BasePresenter;

import java.util.List;

/**
 * @author by DELL
 * @date on 2018/4/12
 * @describe
 */
public class TestMvpPresenter extends BasePresenter<TestMvpContract.Display> implements TestMvpContract.Presenter {

    @Override
    public void getIndexData() {
//         RetrofitClient.getTestService()
//                .getData()
//                .compose(new NetworkTransformer<>(mView))
//                .subscribe(new RxCallback<List<GoodsBean>>() {
//                    @Override
//                    public void onSuccess(@Nullable List<GoodsBean> data) {
//                        mView.getIndexData(data);
//                    }
//
//                });
    }

    @Override
    public void switchOnOff() {
//        RetrofitClient.getHomeService()
//                .switchOnOff()
//                .compose(new NetworkTransformer<>(mView))
//                .subscribe(new RxCallback<String>() {
//                    @Override
//                    public void onSuccess(@Nullable String data) {
//                        mView.switchOnOff(data);
//                    }
//                });
    }

    @Override
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
