package com.mirkowu.baselibrary.network;


import android.app.Activity;

import com.mirkowu.baselibrary.utils.NetworkUtil;
import com.softgarden.baselibrary.base.BaseActivity;

import io.reactivex.Observable;
import io.reactivex.ObservableSource;
import io.reactivex.ObservableTransformer;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.exceptions.Exceptions;
import io.reactivex.schedulers.Schedulers;


/**
 * Created by Lightwave on 2016/6/28.
 */
public class NetworkTransformer<T> implements ObservableTransformer<BaseBean<T>, T> {
    private BaseActivity activity;

    public NetworkTransformer(Activity activity) {
        if (activity instanceof BaseActivity)
            this.activity = (BaseActivity) activity;
        else {
            throw Exceptions.propagate(new RuntimeException("activity is not instanceof BaseActivity"));
        }
    }

    @Override
    public ObservableSource<T> apply(Observable<BaseBean<T>> upstream) {
        return upstream
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .doOnSubscribe(disposable -> {
                    if (!NetworkUtil.isConnected(activity)) {
                        disposable.dispose();
                        NetworkUtil.showNoNetWorkDialog(activity);
                    } else {
                        activity.showProgressDialog();
                    }
                })
                .doFinally(() -> activity.hideProgressDialog())
                .map(baseBean -> {
                    if (baseBean.status == 1) {
                        return baseBean;
                    } else {
                        if (baseBean.status == -1) {
                            activity.showReLoginDialog();
                        }
                        throw Exceptions.propagate(new ApiException(baseBean.status, baseBean.info));
                    }
                })
                .map(baseBean -> baseBean.data)
                .compose(activity.bindToLifecycle());

    }

}
