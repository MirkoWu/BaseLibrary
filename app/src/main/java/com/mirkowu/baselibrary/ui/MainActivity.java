package com.mirkowu.baselibrary.ui;

import android.graphics.Color;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.widget.TextView;

import com.mirkowu.baselibrary.R;
import com.mirkowu.baselibrary.base.ToolbarActivity;
import com.mirkowu.baselibrary.utils.KeyBoardUtil;
import com.softgarden.baselibrary.utils.DisplayUtil;
import com.softgarden.baselibrary.utils.BaseSPManager;
import com.softgarden.baselibrary.widget.BaseToolbar;

public class MainActivity extends ToolbarActivity {


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
                .addRightImage(R.mipmap.share, v -> startActivity(MainActivity.class))
                .setSplitLine(Color.parseColor("#E4DFE1"), DisplayUtil.dip2px(this, 5))
                .setTitleTextColor(Color.BLACK)
                .setTitle(R.string.app_name);
    }

    @Override
    protected void initialize() {
        TextView tv = findViewById(R.id.tvText);
        tv.setText("+" + Math.random() * 10);
        KeyBoardUtil keyboardPatch = new KeyBoardUtil(this, findViewById(R.id.edt));
        keyboardPatch.register();
    }

}
