package com.mirkowu.baselibrary.network;

import com.google.gson.JsonParseException;
import com.softgarden.baselibrary.utils.ToastUtil;

import java.net.ConnectException;
import java.net.SocketTimeoutException;
import java.net.UnknownHostException;

import io.reactivex.disposables.Disposable;

/**
 * @author by DELL
 * @date on 2017/11/23
 * @describe
 */

public abstract class RxCallback<T> implements Callback<T> {

//    public RxCallback() {
//
//    }



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
          //  ToastUtil.s(R.string.network_request_failed);
        } else if (e instanceof SocketTimeoutException) {
          //  ToastUtil.s(R.string.network_connection_timeout);
        } else if (e instanceof JsonParseException) {
          //  ToastUtil.s(R.string.json_failed);
            e.printStackTrace();
        } else if (e instanceof NullPointerException) {//RxJava2不能发送null
            onSuccess(null);
            // e.printStackTrace();
        } else if (e instanceof ApiException) {
            ApiException apiException = (ApiException) e;
            if (apiException.getStatus() == -1) {//token过期 这里不用管前面已处理

            } else {
                ToastUtil.s(e.getMessage());
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
