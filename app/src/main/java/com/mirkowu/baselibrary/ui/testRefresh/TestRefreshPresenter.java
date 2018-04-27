package com.mirkowu.baselibrary.ui.testRefresh;

import android.support.annotation.Nullable;

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
public class TestRefreshPresenter extends BasePresenter<TestRefreshContract.Display> implements TestRefreshContract.Presenter {


    @Override
    public void getData() {
        RetrofitClient.getTestService()
                .getData()
                .compose(new NetworkTransformer<>(mView))
                .subscribe(new RxCallback<List<GoodsBean>>() {
                    @Override
                    public void onSuccess(@Nullable List<GoodsBean> data) {
                        mView.getData(data);
                    }

                    @Override
                    public void onFinish() {
                        super.onFinish();
                        mView.onFinishRefresh();
                    }
                });
    }
}
