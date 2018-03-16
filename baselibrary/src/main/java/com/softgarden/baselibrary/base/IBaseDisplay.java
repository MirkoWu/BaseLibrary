package com.softgarden.baselibrary.base;

import android.content.Context;

import com.trello.rxlifecycle2.LifecycleTransformer;

/**
 * 用于Activity Fragment 界面交互
 */
public interface IBaseDisplay {
    Context getContext();

    void showProgressDialog();

    void hideProgressDialog();

    void showReLoginDialog();

    void showError(Throwable t);

    void changeDayNightMode(boolean isNightMode);

    <T> LifecycleTransformer<T> bindToLifecycle();
}
