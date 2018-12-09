package com.softgarden.baselibrary.base;


import android.content.Context;

import com.softgarden.baselibrary.network.ApiException;
import com.trello.rxlifecycle2.LifecycleTransformer;

/**
 * 引入 IBaseDisplay mView  控制生命周期
 */
public class BasePresenter implements IBasePresenter, IBaseDisplay {

    protected IBaseDisplay mView;


    @Override
    public void attachView(IBaseDisplay display) {
        this.mView = display;
    }

    @Override
    public void detachView() {
        this.mView = null;
    }


    @Override
    public Context getContext() {
        return mView.getContext();
    }

    @Override
    public BaseActivity getBaseActivity() {
        return mView.getBaseActivity();
    }

    @Override
    public void showProgressDialog() {
        mView.showProgressDialog();
    }

    @Override
    public void showProgressDialog(CharSequence message) {
        mView.showProgressDialog(message);
    }

    @Override
    public void hideProgressDialog() {
        mView.hideProgressDialog();
    }

    @Override
    public void showError(Throwable t) {
        mView.showError(t);
    }

    @Override
    public void onApiException(ApiException e) {
        mView.onApiException(e);
    }

    @Override
    public void onRequestFinish() {
        mView.onRequestFinish();
    }

    @Override
    public void changeDayNightMode(boolean isNightMode) {
        mView.changeDayNightMode(isNightMode);
    }

    @Override
    public <T> LifecycleTransformer<T> bindToLifecycle() {
        return mView.bindToLifecycle();
    }
}
