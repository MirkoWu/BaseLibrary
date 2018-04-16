package com.mirkowu.baselibrary.ui;

import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;

import com.mirkowu.baselibrary.R;
import com.mirkowu.baselibrary.base.ToolbarActivity;
import com.mirkowu.basetoolbar.BaseToolbar;
import com.softgarden.baselibrary.utils.BaseSPManager;
import com.softgarden.baselibrary.utils.DisplayUtil;

public class MainActivity extends ToolbarActivity {
    public static void start(Context context) {
        Intent starter = new Intent(context, MainActivity.class);
//        starter.putExtra();
        context.startActivity(starter);
    }


    @Override
    protected int getLayoutId() {
        return R.layout.activity_main;
    }


    @Nullable
    @Override
    protected BaseToolbar.Builder setToolbar(@NonNull BaseToolbar.Builder builder) {
        return builder
                // .setStatusBarColor(Color.GRAY)
                .addLeftText("语言切换", v -> {
                    BaseSPManager.setEnglish(!BaseSPManager.isEnglish());
                    reload();
                })
                .addRightText("日夜切换", v -> {
                    BaseSPManager.setNightMode(!BaseSPManager.isNightMode());
                    reload();
                })
                .addRightImage(R.mipmap.share, v -> startActivity(TestToolbarActivity.class))
                .setBottomDivider(Color.parseColor("#E4DFE1"), DisplayUtil.dip2px(this, 5))
                .setTitleTextColor(Color.BLACK)
                .setTitle(R.string.app_name);
    }

    @Override
    protected void initialize() {

    }

}
