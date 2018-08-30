package com.softgarden.baselibrary.base;

import android.content.Context;

import com.trello.rxlifecycle2.LifecycleTransformer;

/**
 * 用于Activity Fragment 界面交互
 */
public interface IBaseDisplay {
    Context getContext();

    BaseActivity getBaseActivity();

    void showProgressDialog();

    void showProgressDialog(CharSequence message);

    void hideProgressDialog();

    void showError(Throwable t);

    void onRequestFinish();

    void changeDayNightMode(boolean isNightMode);

    <T> LifecycleTransformer<T> bindToLifecycle();
}
