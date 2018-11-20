package com.mirkowu.baselibrary.network;

import com.google.gson.JsonParseException;
import com.softgarden.baselibrary.base.IBaseDisplay;
import com.softgarden.baselibrary.utils.ToastUtil;

import java.net.ConnectException;
import java.net.SocketTimeoutException;
import java.net.UnknownHostException;

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

    public RxCallback(IBaseDisplay mView) {
        this.mView = mView;
    }


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
    public void onError(Throwable e) {

        if (e instanceof ConnectException) {
            ToastUtil.s("服务器连接失败");
        } else if (e instanceof UnknownHostException) {
            ToastUtil.s("请求失败");
        } else if (e instanceof SocketTimeoutException) {
            ToastUtil.s("请求超时");
        } else if (e instanceof JsonParseException) {
            ToastUtil.s("数据解析失败");
            e.printStackTrace();
        } else if (e instanceof RxJava2NullException) {//RxJava2不能发送null
            onSuccess(null);
        } else if (e instanceof ApiException) {
            // mView.showError(e);//有需要的话可以 发送给View层处理异常

            //通用的Api异常处理
            ApiException apiException = (ApiException) e;
            onApiException(apiException);

        } else {
            ToastUtil.s(e.getMessage());
            e.printStackTrace();
        }
        onFinish();
    }

    @Override
    public void onApiException(ApiException e) {
        //默认 需要根据业务逻辑 处理了
        ToastUtil.s(e.getMessage());
        e.printStackTrace();
    }

    @Override
    public void onComplete() {
        onFinish();
    }


    @Override
    public void onFinish() {
        if (mView != null) {
            mView.onRequestFinish();
        }
    }
}
