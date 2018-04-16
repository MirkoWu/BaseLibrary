package com.mirkowu.baselibrary.network;


import com.softgarden.baselibrary.base.IBaseDisplay;

import io.reactivex.Observable;
import io.reactivex.ObservableSource;
import io.reactivex.ObservableTransformer;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.schedulers.Schedulers;


/**
 * 无网络加载的Transformer  类似数据库查询
 */
public class LocalTransformer<T> implements ObservableTransformer<T, T> {
    private IBaseDisplay mView;

    public LocalTransformer(IBaseDisplay mView) {
        if (mView == null) throw new RuntimeException("IBaseDisplay is not NULL");
        this.mView = mView;
    }

    @Override
    public ObservableSource<T> apply(Observable<T> upstream) {
        return upstream
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .doOnSubscribe(disposable -> mView.showProgressDialog())
                // .doOnTerminate(() -> mView.hideProgressDialog())
                .doFinally(() -> mView.hideProgressDialog())
                .compose(mView.bindToLifecycle());

    }

}
