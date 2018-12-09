package com.mirkowu.baselibrary.ui;

import android.content.Context;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.TextView;

import com.mirkowu.baselibrary.R;
import com.mirkowu.baselibrary.dataBinding.BaseBindActivity;
import com.mirkowu.baselibrary.bean.GoodsBean;
import com.mirkowu.baselibrary.databinding.ActivityScreenAdapterBinding;
import com.mirkowu.baselibrary.ui.testRefresh.TestRefreshPresenter;
import com.mirkowu.basetoolbar.BaseToolbar;
import com.softgarden.baselibrary.utils.FileUtil;
import com.softgarden.baselibrary.utils.L;
import com.softgarden.baselibrary.utils.ScreenUtil;

import java.util.List;

public class ScreenAdapterActivity extends BaseBindActivity<TestRefreshPresenter, ActivityScreenAdapterBinding>   {

    public static void start(Context context) {
        Intent starter = new Intent(context, ScreenAdapterActivity.class);
//	    starter.putExtra( );
        context.startActivity(starter);
    }

    @Override
    protected int getLayoutId() {
        return R.layout.activity_screen_adapter;
    }

    @Override
    protected void initialize() {


        binder.mToolbar.setTitle("这是DataBinding示例");



        L.d("getAppRootDir=" + FileUtil.getAppRootDir().getAbsolutePath());
        L.d("getStatusBarHeight=" + ScreenUtil.getStatusBarHeight(this));

        L.d("getActionBarHeight= " + ScreenUtil.getActionBarHeight(this));


        TextView tvPhoneHint = findViewById(R.id.tvPhoneHint);
        tvPhoneHint.setTextSize(12);

        Toolbar mToolbar = findViewById(R.id.mToolbar);
        mToolbar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                L.d("getActionBarHeight= " + mToolbar.getHeight());
                //          L.d("BaseToolbar= " + getToolbar().getHeight());
            }
        });
    }

    protected BaseToolbar.Builder setToolbar(@NonNull BaseToolbar.Builder builder) {
        //  return builder.setTitle("测试屏幕适配");
        return null;
    }


    public void getData(List<GoodsBean> bean) {

    }
}
