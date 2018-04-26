package com.mirkowu.baselibrary.network;


import com.mirkowu.baselibrary.utils.NetworkUtil;
import com.softgarden.baselibrary.base.IBaseDisplay;

import io.reactivex.Observable;
import io.reactivex.ObservableSource;
import io.reactivex.ObservableTransformer;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.schedulers.Schedulers;


/**
 * RxJava2 转换器 用于网络加载数据 已实现功能有：
 * <p>
 * 1.检测有无网络
 * 2.加载网络时显示加载框 结束是隐藏
 * 3.控制RxJava生命周期，防止内存泄漏
 */
public class NetworkTransformer<T> implements ObservableTransformer<BaseBean<T>, T> {
    private IBaseDisplay mView;

    public NetworkTransformer(IBaseDisplay mView) {
        if (mView == null) throw new RuntimeException("IBaseDisplay is not NULL");
        this.mView = mView;
    }

    @Override
    public ObservableSource<T> apply(Observable<BaseBean<T>> upstream) {
        return upstream
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .doOnSubscribe(disposable -> {
                    if (!NetworkUtil.isConnected(mView.getContext())) {
                        NetworkUtil.showNoNetWorkDialog(mView.getContext());
                        disposable.dispose();
                    } else {
                        mView.showProgressDialog();
                    }
                })
                .doFinally(() -> mView.hideProgressDialog())
                .map(baseBean -> {
                    if (baseBean.status == 1) {
                        return baseBean;
                    } else {
//                        if (baseBean.code == -1) {
//                            mView.showReLoginDialog();
//                        }
                        throw new ApiException(baseBean.status, baseBean.info);
                    }
                })
                .map(tBaseBean -> {
                    if (tBaseBean.data != null) return tBaseBean.data;
                    //返回空数据时 抛出一个异常让CallBack处理
                    throw new RxJava2NullException();
                })
                .compose(mView.bindToLifecycle());

    }

}
