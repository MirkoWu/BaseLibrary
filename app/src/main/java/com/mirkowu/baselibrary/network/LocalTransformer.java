package com.mirkowu.baselibrary.network;


import android.app.Activity;

import com.softgarden.baselibrary.base.BaseActivity;

import io.reactivex.Observable;
import io.reactivex.ObservableSource;
import io.reactivex.ObservableTransformer;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.Disposable;
import io.reactivex.exceptions.Exceptions;
import io.reactivex.functions.Action;
import io.reactivex.functions.Consumer;
import io.reactivex.schedulers.Schedulers;


/**
 * Created by Lightwave on 2016/6/28.
 */
public class LocalTransformer<T> implements ObservableTransformer<T, T> {
    private BaseActivity activity;

    public LocalTransformer(Activity activity) {
        if (activity instanceof BaseActivity)
            this.activity = (BaseActivity) activity;
        else {
            throw Exceptions.propagate(new RuntimeException("activity is not instanceof BaseActivity"));
        }
    }

    @Override
    public ObservableSource<T> apply(Observable<T> upstream) {
        return upstream
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .doOnSubscribe(new Consumer<Disposable>() {
                    @Override
                    public void accept(Disposable disposable) throws Exception {
                        activity.showProgressDialog();
                    }
                })
                .doOnTerminate(new Action() {
                    @Override
                    public void run() throws Exception {
                        activity.hideProgressDialog();
                    }
                })
                .doFinally(new Action() {
                    @Override
                    public void run() throws Exception {
                        activity.hideProgressDialog();
                    }
                })
                .compose(activity.<T>bindToLifecycle());

    }

}
