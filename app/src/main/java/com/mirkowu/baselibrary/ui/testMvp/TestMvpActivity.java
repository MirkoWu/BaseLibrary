package com.mirkowu.baselibrary.ui.testMvp;

import android.content.Context;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;

import com.mirkowu.baselibrary.R;
import com.mirkowu.baselibrary.base.ToolbarActivity;
import com.mirkowu.baselibrary.ui.MainActivity;
import com.mirkowu.basetoolbar.BaseToolbar;

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
        return builder.setTitle("MVP模板").addRightText("普通的Activity",v -> MainActivity.start(this));
    }

    @Override
    public TestMvpPresenter createPresenter() {
        return new TestMvpPresenter();
    }

    @Override
    protected void initialize() {

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


}
