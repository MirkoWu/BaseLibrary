package com.mirkowu.baselibrary.socketUtil;

import com.softgarden.baselibrary.utils.L;

import org.json.JSONObject;

import java.net.SocketException;
import java.net.SocketTimeoutException;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.TimeUnit;

import io.reactivex.Observable;
import io.reactivex.ObservableOnSubscribe;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.Disposable;
import io.reactivex.exceptions.Exceptions;
import io.reactivex.schedulers.Schedulers;


/**
 * @author by DELL
 * @date on 2018/1/15
 * @describe
 */

public class RxSocketManager {
    private static final String TAG = RxSocketManager.class.getSimpleName();

    private static RxSocketManager rxSocketManager;
    private static ISocket socket;
    private Disposable monitorTask;
    private List<Disposable> timeOutTaskList;//超时
    private Callback callback;
    private long timeOut =1500;
    private String host;
    private int port;
    private OnSocketStatusListener onSocketStatusListener;
    private int tryCount = 0;//失败重试次数
    private int tryCreateCount;
    private ISocket.SocketType socketType = ISocket.SocketType.TCP;//默认是tcp

    private RxSocketManager() {
    }

    public static RxSocketManager getInstance() {
        if (rxSocketManager == null) rxSocketManager = new RxSocketManager();
        return rxSocketManager;
    }

    public RxSocketManager setClient(String host, int port) {
        this.host = host;
        this.port = port;
        return this;
    }

    public RxSocketManager setClientType(ISocket.SocketType socketType) {
        this.socketType = socketType;
        return this;
    }

    public RxSocketManager setTimeOut(long timeOut) {
        this.timeOut = timeOut;
        return this;
    }

    public RxSocketManager setTryCount(int tryCount) {
        this.tryCount = tryCount;
        return this;
    }

    public RxSocketManager setCallback(Callback callback) {
        this.callback = callback;
        return this;
    }

    public RxSocketManager setSocketStatusListener(OnSocketStatusListener onSocketStatusListener) {
        this.onSocketStatusListener = onSocketStatusListener;
        return this;
    }

    public RxSocketManager removeSocketStatusListener() {
        this.onSocketStatusListener = null;
        return this;
    }

    public void build() {
        initSocket();
    }

    private void initSocket() {
        tryCreateCount = tryCount;
        createSocket();
        timeOutTaskList = new ArrayList<>();
    }

    private void createSocket() {
        Observable.create((ObservableOnSubscribe<Boolean>) e -> {
            L.d(TAG, "initSocket  create TCPClient");
            switch (socketType) {
                case TCP:
                    socket = new TCPClient(host, port);
                    break;
                case UDP:
                    socket = new UDPClient(host, port);
                    break;
            }

            e.onNext(true);
            e.onComplete();
        })
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(aBoolean -> {
                    if (monitorTask == null || monitorTask.isDisposed()) {
                        monitorTask = createMonitorTask(callback);
                    }
                    if (socket != null && onSocketStatusListener != null) {
                        onSocketStatusListener.onConnected();
                    }

                }, throwable -> {
                    if (tryCreateCount == 0) {
                        throwable.printStackTrace();
                        tryCreateCount = tryCount;
                        L.d("tryCount" + (socket == null));
                        if (onSocketStatusListener != null) {
                            onSocketStatusListener.onConnectFailed();
                        }
                    } else {
                        tryCreateCount--;
                        createSocket();
                    }
                });
    }

    public Disposable sendUDPData(JSONObject jsonObject) {
        if (jsonObject != null) {
            return send(jsonObject.toString().getBytes(), false);
        } else {
            throw Exceptions.propagate(new Exception("send data is null"));
        }
    }

    public Disposable send(JSONObject jsonObject) {
        if (jsonObject != null) {
            return send(jsonObject.toString().getBytes(), true);
        } else {
            throw Exceptions.propagate(new Exception("send data is null"));
        }
    }

    public Disposable send(JSONObject jsonObject, boolean isTimeout) {
        if (jsonObject != null) {
            return send(jsonObject.toString().getBytes(), isTimeout);
        } else {
            throw Exceptions.propagate(new Exception("send data is null"));
        }
    }

//    public Disposable send(TcpBean bean) {
//        String jsonStr = new Gson().toJson(bean);
//        if (bean != null) {
//            return send(jsonStr.getBytes(), true);
//        } else {
//            throw Exceptions.propagate(new Exception("send data is null"));
//        }
//    }


    public synchronized Disposable send(byte[] data, boolean isTimeout) {
        return Observable.create((ObservableOnSubscribe<Boolean>) e -> {
            // L.d("send  isConnected" + socket.isConnected() + " isClosed" + socket.isClosed());
            if (socket != null) {
                if (socketType == ISocket.SocketType.TCP) {
                    if (isConnected()) {
                        socket.send(data);
                        e.onNext(true);
                    } else {
                        e.onNext(false);
                    }
                } else if (socketType == ISocket.SocketType.UDP) {
                    socket.send(data);
                    e.onNext(true);
                }

            } else e.onNext(false);
            e.onComplete();
        })
                .subscribeOn(Schedulers.newThread())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(aBoolean -> {
                            if (aBoolean) {
                                L.d(TAG, "Send Succeed...");
                                if (isTimeout) createTimeOutTask();
                            } else {
                                L.d(TAG, "Send Failed...");

                                if (callback != null) {
                                    callback.onFailed(new Throwable("Send Failed"));
                                }
                            }
                        },
                        throwable -> {
                            L.d(TAG, "Send Failed...");
                            if (callback != null) {
                                callback.onFailed(throwable);
                            }
                        });
    }

    private void createTimeOutTask() {
        Disposable disposable = Observable.timer(timeOut, TimeUnit.MILLISECONDS)
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(aLong -> {
                    if (onSocketStatusListener != null) {
                        onSocketStatusListener.onDisConnected();
                    }
                });
        timeOutTaskList.add(disposable);
    }

    private synchronized void disposeTimeOut() {
        if (timeOutTaskList.isEmpty()) return;
        Disposable disposable = timeOutTaskList.remove(0);
        if (disposable != null && !disposable.isDisposed()) {
            disposable.dispose();
        }
    }

    private Disposable createMonitorTask(Callback callback) {
        return Observable.create((ObservableOnSubscribe<byte[]>) e -> {
            L.d(TAG, "createMonitorTask");

            try {
                while (socket != null) {
                    byte[] rec = socket.receive();
                    if (rec == null || rec.length == 0) {
                        continue;
                    }
                    e.onNext(rec);
                }
            } catch (SocketTimeoutException e1) {
                if (onSocketStatusListener != null) {
                    onSocketStatusListener.onConnectFailed();
                }
            } catch (SocketException e2) {
                if (onSocketStatusListener != null) {
                    onSocketStatusListener.onDisConnected();
                }
                e2.printStackTrace();
            } catch (Exception e3) {
                if (onSocketStatusListener != null) {
                    onSocketStatusListener.onDisConnected();
                }
                e3.printStackTrace();
            }

        })
                .subscribeOn(Schedulers.computation())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(bytes -> {
                    if (callback != null) {
                        disposeTimeOut();
                        callback.onSucceed(bytes);

                    }
                    handlerData(bytes);
                }, throwable -> {
                    if (callback != null) {
                        callback.onFailed(throwable);
                    }
                });
    }

    private void handlerData(byte[] bytes) {

    }

    public void close() {
        try {
            if (socket != null) {
                socket.close();
                socket = null;
            }

            if (monitorTask != null && !monitorTask.isDisposed()) {
                monitorTask.dispose();
                monitorTask = null;
            }

            rxSocketManager = null;

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public boolean isConnected() {
        if (socket != null && !socket.isClosed() && socket.isConnected()) {
            return true;
        } else return false;
    }

    public interface OnSocketStatusListener {
        void onConnected();

        void onConnectFailed();

        void onDisConnected();
    }


    public interface Callback {

        void onSucceed(byte[] data);

        void onFailed(Throwable t);
    }

    public static abstract class SimpleCallback implements Callback {

        @Override
        public void onFailed(Throwable t) {

        }
    }

}
