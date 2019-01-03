package com.mirkowu.baselibrarysample.app;

import android.content.Context;

import com.softgarden.baselibrary.BaseApplication;

/**
 * @author by DELL
 * @date on 2018/2/6
 * @describe
 */

public class App extends BaseApplication{

    @Override
    protected void attachBaseContext(Context base) {
        super.attachBaseContext(base);
       // MultiDex.install(this);
    }

    @Override
    public void onCreate() {
        super.onCreate();
    }
}
