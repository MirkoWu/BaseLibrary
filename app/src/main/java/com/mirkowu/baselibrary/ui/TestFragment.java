package com.mirkowu.baselibrary.ui;

import android.support.annotation.Nullable;

import com.mirkowu.baselibrary.R;
import com.mirkowu.baselibrary.bean.TestBean;
import com.mirkowu.baselibrary.network.NetworkTransformer;
import com.mirkowu.baselibrary.network.RetrofitClient;
import com.mirkowu.baselibrary.network.RxCallback;
import com.softgarden.baselibrary.base.BaseLazyFragment;

/**
 * @author by DELL
 * @date on 2018/2/24
 * @describe
 */

public class TestFragment extends BaseLazyFragment {
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
//        loadData2();
//        loadData();
//        loadData2();
//        loadData();
//        loadData2();
//        loadData();
    }

    private void loadData() {
        RetrofitClient.getTestService()
                .getData(1)
                .compose(new NetworkTransformer<>(this))
                .subscribe(new RxCallback<TestBean>() {
                    @Override
                    public void onSuccess(@Nullable TestBean data) {

                    }
                });
    }


}
