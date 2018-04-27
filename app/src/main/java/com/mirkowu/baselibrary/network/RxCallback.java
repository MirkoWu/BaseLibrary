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
    public void onNext(T t) {
        onSuccess(t);
        onFinish();
    }

    @Override
    public void onError(Throwable e) {
        if (e instanceof ConnectException) {
            // ToastUtil.s(R.string.network_connection_failed);
        } else if (e instanceof UnknownHostException) {
            // ToastUtil.s(R.string.network_request_failed);
        } else if (e instanceof SocketTimeoutException) {
            //ToastUtil.s(R.string.network_connection_timeout);
        } else if (e instanceof JsonParseException) {
            //ToastUtil.s(R.string.json_failed);
            // e.printStackTrace();
        } else if (e instanceof RxJava2NullException) {//RxJava2不能发送null
            onSuccess(null);
        } else if (e instanceof ApiException) {
            // mView.showError(e);//有需要的话可以 发送给View层处理异常

            //通用的Api异常处理
            ApiException apiException = (ApiException) e;
            if (apiException.getStatus() == -1) {//token过期 这里不用管前面已处理

            } else {
                // mView.showError(e);//有需要的话可以 发送给View层处理异常

                ToastUtil.s(e.getMessage());
                e.printStackTrace();
            }
        } else {
            ToastUtil.s(e.getMessage());
            e.printStackTrace();
        }
        onFinish();
    }

    @Override
    public void onComplete() {
        onFinish();
    }

    @Override
    public void onStart() {

    }

    @Override
    public void onFinish() {

    }
}
