package com.softgarden.baselibrary.network;

import com.softgarden.baselibrary.base.IBaseDisplay;

import io.reactivex.disposables.Disposable;

/**
 * 可直接订阅RxJava 的 Observer 继承类  已实现功能有：
 * 1.对应请求流程，可进行相应的操作
 * 2.捕捉异常，并进行后续操作
 * 3.解决RxJava2不能发生null的问题，当数据传回了Null 则主动进行捕捉并处理
 */

public abstract class RxCallback<T> implements Callback<T> {
    private IBaseDisplay mView;

    public RxCallback() {

    }

//    public RxCallback(IBaseDisplay mView) {
//        this.mView = mView;
//    }


    @Override
    public void onSubscribe(Disposable d) {
        onStart();
    }

    @Override
    public void onStart() {

    }

    @Override
    public void onNext(T t) {
        onSuccess(t);
        onFinish();
    }

    @Override
    public void onError(Throwable t) {
        if (t instanceof RxJava2NullException) {//RxJava2不能发送null
            onSuccess(null);
        } else if (t instanceof ApiException) {
            //通用的Api异常处理
            ApiException apiException = (ApiException) t;
            onApiException(apiException);
        } else {
//            ToastUtil.s(t.getMessage());
//            t.printStackTrace();
        }

    }

    @Override
    public void onApiException(ApiException e) {
        //默认 需要根据业务逻辑 处理了
//        ToastUtil.s(e.getMessage());
//        e.printStackTrace();
    }

    @Override
    public void onComplete() {
        onFinish();
    }


    @Override
    public void onFinish() {

    }
}
