package com.mirkowu.baselibrary.ui;

import android.support.annotation.NonNull;
import android.support.annotation.Nullable;

import com.mirkowu.baselibrary.R;
import com.mirkowu.baselibrary.base.ToolbarActivity;
import com.softgarden.baselibrary.widget.BaseToolbar;

public class MainActivity extends ToolbarActivity {


    @Override
    protected int getLayoutId() {
        return R.layout.activity_main;
    }

    @Override
    protected void initialize() {

    }

    @Nullable
    @Override
    protected BaseToolbar.Builder setToolbar(@NonNull BaseToolbar.Builder builder) {
        return builder.setTitle(R.string.app_name);
    }
}
