package com.mirkowu.baselibrary;

import android.graphics.Color;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;

import com.mirkowu.baselibrary.base.ToolbarActivity;
import com.mirkowu.baselibrary.network.BaseBean;
import com.mirkowu.baselibrary.network.NetworkTransformer;
import com.mirkowu.baselibrary.network.RetrofitManager;
import com.mirkowu.baselibrary.network.RxCallback;
import com.softgarden.baselibrary.utils.ToastUtil;
import com.softgarden.baselibrary.widget.BaseToolbar;

public class TestToolbarActivity extends ToolbarActivity {
    @Nullable
    @Override
    protected BaseToolbar.Builder setToolbar(@NonNull BaseToolbar.Builder builder) {
        return builder
                .setStatusBarColor(Color.GRAY)
                .addLeftText("左菜单", v -> ToastUtil.s("左菜单"))
                .addRightText("右菜单", v -> ToastUtil.s("右菜单"))
                .addRightImage(R.mipmap.share, v -> startActivity(MainActivity.class))
                .setTitle("标题标题");
    }

    @Override
    protected int getLayoutId() {
        return R.layout.activity_test_toolbar;

    }

    @Override
    protected void initialize() {
        getToolbar().getRightView(0).setBackgroundColor(Color.GRAY);

        loadData();


    }

    private void loadData() {
        RetrofitManager.getLoginService()
                .loginThridParty(1, "", "", "")
                .compose(new NetworkTransformer(this))
                .subscribe(new RxCallback<BaseBean<String>>() {
                    @Override
                    public void onSuccess(@Nullable BaseBean<String> data) {

                    }
                });
    }


}
