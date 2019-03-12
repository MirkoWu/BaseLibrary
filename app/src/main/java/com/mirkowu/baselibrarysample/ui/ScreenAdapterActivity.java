package com.mirkowu.baselibrarysample.ui;

import android.content.Context;
import android.content.Intent;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.TextView;

import com.mirkowu.baselibrarysample.R;
import com.mirkowu.baselibrarysample.dataBinding.BaseBindActivity;
import com.mirkowu.baselibrarysample.databinding.ActivityScreenAdapterBinding;
import com.mirkowu.baselibrarysample.ui.testRefresh.TestRefreshPresenter;
import com.softgarden.baselibrary.utils.FileUtil;
import com.softgarden.baselibrary.utils.L;
import com.softgarden.baselibrary.utils.ScreenUtil;

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


}
