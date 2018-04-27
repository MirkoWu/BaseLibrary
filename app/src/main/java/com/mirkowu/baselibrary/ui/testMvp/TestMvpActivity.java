package com.mirkowu.baselibrary.ui.testMvp;

import android.content.Context;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.view.View;

import com.mirkowu.baselibrary.R;
import com.mirkowu.baselibrary.base.ToolbarActivity;
import com.mirkowu.basetoolbar.BaseToolbar;
import com.softgarden.baselibrary.utils.ToastUtil;

/**
 * 1.这里要泛型 要记得写上当前Presenter  要继承view的接口Contract.Display
 */
public class TestMvpActivity extends ToolbarActivity<TestMvpPresenter> implements TestMvpContract.Display {

    public static void start(Context context) {
        Intent starter = new Intent(context, TestMvpActivity.class);
//        starter.putExtra();
        context.startActivity(starter);
    }

    @Override
    protected int getLayoutId() {
        return R.layout.activity_test_mvp;
    }


    @Nullable
    @Override
    protected BaseToolbar.Builder setToolbar(@NonNull BaseToolbar.Builder builder) {
        return builder.setTitle("MVP模板");
    }


    /**
     * 2.要重写该方法 传入当前的Presenter
     * @return
     */
    @Override
    public TestMvpPresenter createPresenter() {
        return new TestMvpPresenter();
    }

    @Override
    protected void initialize() {
        /**
         * 3.调用
         */
        getPresenter().getIndexData();
        getPresenter().switchBluetooth();
        getPresenter().switchOnOff();
    }

    @Override
    public void getIndexData(String bean) {

    }

    @Override
    public void switchOnOff(String bean) {

    }

    @Override
    public void switchBluetooth(String bean) {

    }


    public void onClick(View view) {
        ToastUtil.s("测试吐司");
    }
}
