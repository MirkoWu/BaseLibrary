package com.mirkowu.baselibrarysample.ui;

import android.os.Bundle;
import android.support.annotation.Nullable;

import com.mirkowu.baselibrarysample.R;
import com.mirkowu.baselibrarysample.bean.GoodsBean;
import com.softgarden.baselibrary.network.NetworkTransformer;
import com.mirkowu.baselibrarysample.api.RetrofitClient;
import com.softgarden.baselibrary.network.RxCallback;
import com.softgarden.baselibrary.base.BaseLazyFragment;

import java.util.List;

/**
 * @author by DELL
 * @date on 2018/2/24
 * @describe
 */

public class TestFragment extends BaseLazyFragment {

    public static TestFragment newInstance() {
        
        Bundle args = new Bundle();
        
        TestFragment fragment = new TestFragment();
        fragment.setArguments(args);
        return fragment;
    }
    @Override
    protected int getLayoutId() {
        return R.layout.activity_test_toolbar;
    }


    @Override
    protected void initEventAndData() {

    }

    @Override
    protected void lazyLoad() {
//        loadData();

    }

    private void loadData() {
        RetrofitClient.getTestService()
                .getData()
                .compose(new NetworkTransformer<>(this))
                .subscribe(new RxCallback<List<GoodsBean>>() {
                    @Override
                    public void onSuccess(@Nullable List<GoodsBean> data) {

                    }
                });
    }


}
