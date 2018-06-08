//package com.mirkowu.baselibrary.socketUtil;
//
//import android.content.Context;
//import android.net.wifi.WifiManager;
//import android.text.TextUtils;
//
//import com.google.gson.Gson;
//import com.google.gson.reflect.TypeToken;
//import com.softgarden.baselibrary.base.BaseActivity;
//import com.softgarden.baselibrary.utils.EmptyUtil;
//import com.softgarden.baselibrary.utils.L;
//import com.wci.homesmart.App;
//import com.wci.homesmart.Constants;
//import com.wci.homesmart.bean.TcpBean;
//import com.wci.homesmart.utils.SPManager;
//
//import org.json.JSONException;
//import org.json.JSONObject;
//
//import java.lang.reflect.Type;
//import java.util.HashMap;
//import java.util.concurrent.TimeUnit;
//
//import io.reactivex.Observable;
//import io.reactivex.disposables.Disposable;
//
///**
// * @author by DELL
// * @date on 2018/1/16
// * @describe
// */
//
//public class SocketUtil {
//    public static final String TAG = SocketUtil.class.getName();
//    public static final int TCP_PORT = 80;
//    public static final int PORT = 8000;
//    public static final String HOST = "224.0.0.1";
//    public static final String COMMOND_SEARCH_GATEWAYS = "SearchGateways";//udp搜索网关
//    public static final String COMMOND_CONNECT_SEND = "SearchGateway";//tcp发送链接指令
//    public static final String COMMOND_CONNECT_RESULT = "LanConnect";//链接结果
//    public static final String COMMOND_REFRESH_GATEWAY = "GatewayOnline";//刷新网关状态
//    public static final String RECEIVER_DEVICE_STATE = "DeviceState";//接收到网关返回设备信息
//    public static final String CONTROL_DEVICE = "ControlDevice";//发送控制指令
//    public static final String NET_REFRESH = "RefreshData";//收到该指令要进行网络刷新
//    public static final String GET_DEVICE_STATE = "GetDeviceState";//获取设备详情信息
//
//
//    private static boolean isConnectSocket = false;//是否还在连接
//    private String controlID;
//    private Disposable heartTask;
//
//
//    private BaseActivity activity;
//    //  private RxSocketManager.Callback callback;
//    private OnResultListener resultListener;
//
////    private static SocketUtil socketUtil;
////
////    public static SocketUtil getInstance() {
////        if (socketUtil == null) {
////            socketUtil = new SocketUtil();
////        }
////        return socketUtil;
////    }
////
////    public SocketUtil() {
////        controlID = SPManager.getControlID();
////        createTcpSocket();
////    }
//
////    public void with(BaseActivity iBaseDisplay) {
////        this.activity = iBaseDisplay;
////    }
////
////    public void setCallback(RxSocketManager.Callback callback) {
////        this.callback = callback;
////    }
//
//
//    public SocketUtil(BaseActivity iBaseDisplay, OnResultListener resultListener) {
//        this.activity = iBaseDisplay;
//        //  this.callback = callback;
//        this.resultListener = resultListener;
//        controlID = SPManager.getControlID();
//        // createTcpSocket();
//    }
//
//
//    private void createUdpSocket() {
//        controlID = SPManager.getControlID();//更新
//        WifiManager wifiManager = (WifiManager)
//                App.getInstance().getApplicationContext().getSystemService(Context.WIFI_SERVICE);
//        if (!wifiManager.isWifiEnabled()) return;//检测wifi是否打开
//        // RxSocketManager.getInstance().close();
//        activity.showProgressDialog();
//        RxSocketManager.getInstance()
//                .setClient(SocketUtil.HOST, SocketUtil.PORT)
//                .setClientType(ISocket.SocketType.UDP)
//                .setCallback(new RxSocketManager.Callback() {
//                    @Override
//                    public void onSucceed(byte[] data) {
//                        String response = new String(data);
//                        Type type = new TypeToken<HashMap<String, String>>() {
//                        }.getType();
//                        HashMap<String, String> map = new Gson().fromJson(response, type);
//                        // {"mac":"6001948B3E5C","ip":"192.168.0.130"}
//                        if (map != null && map.containsKey("mac") && map.containsKey("ip")) {
//                            activity.hideProgressDialog();
//
//                            String mac = map.get("mac");
//                            if (TextUtils.equals(controlID, mac)) {//mac 与当前中控id一致时
//                                String ip = map.get("ip");
//                                Constants.GateWayIp = ip;
//                                createTcpSocket();
//                            }
//                        }
//                    }
//
//                    @Override
//                    public void onFailed(Throwable t) {
//                        activity.hideProgressDialog();
//                        t.printStackTrace();
//                    }
//                })
//                .setSocketStatusListener(new RxSocketManager.OnSocketStatusListener() {
//                    @Override
//                    public void onConnected() {
//                        L.d(TAG, "UDP----onConnected ");
//                        activity.runOnUiThread(() -> activity.hideProgressDialog());
//                        RxSocketManager.getInstance().sendUDPData(SocketUtil.getGatewaysList());//获取mac地址和ip
//                    }
//
//                    @Override
//                    public void onConnectFailed() {
//                        L.d("UDP----onConnectFailed ");
//                        activity.runOnUiThread(() -> {
//                            //  ToastUtil.s(R.string.connect_device_failed);
//                            activity.hideProgressDialog();
//                        });
//                    }
//
//                    @Override
//                    public void onDisConnected() {
//                        L.d("UDP----onDisConnected ");
//                        activity.runOnUiThread(() -> {
//                            activity.hideProgressDialog();
////                if (!getActivity().isFinishing()) {
////                    ToastUtil.s(R.string.device_disconnect);
////                }
//                        });
//                    }
//                })
//                .build();
//    }
//
//
//    /**
//     * 创建TCP socket
//     */
//    public void createTcpSocket() {
//        controlID = SPManager.getControlID();//更新
//        if (isConnectSocket) return;
//        //  RxSocketManager.getInstance().close();
//        if (TextUtils.isEmpty(Constants.GateWayIp)) {//没有ip,就去获取ip
//            searchGateWays();
//            return;
//        }
//        WifiManager wifiManager = (WifiManager)
//                App.getInstance().getApplicationContext().getSystemService(Context.WIFI_SERVICE);
//        if (!wifiManager.isWifiEnabled()) return;//检测wifi是否打开
//        RxSocketManager.getInstance().close();
//        activity.showProgressDialog();
//        RxSocketManager.getInstance()
//                .setClient(Constants.GateWayIp, SocketUtil.TCP_PORT)
//                .setClientType(ISocket.SocketType.TCP)
//                .setCallback(new RxSocketManager.Callback() {
//                    @Override
//                    public void onSucceed(byte[] data) {
//                        activity.hideProgressDialog();
//                        String response = new String(data);
//                        try {
//                            JSONObject jsonObj = new JSONObject(response);
//                            String method = jsonObj.optString("method");
//
//                            if (!TextUtils.isEmpty(method)) {
//                                if (SocketUtil.RECEIVER_DEVICE_STATE.equalsIgnoreCase(method)) {//设备状态
//                                    isConnectSocket = true;
//                                    if (TextUtils.isEmpty(response)) return;
//                                    TcpBean resBean = new Gson().fromJson(response, TcpBean.class);
//                                    if (resultListener != null) {
//                                        resultListener.receiveDeviceState(resBean);
//                                    }
//                                } else {
//                                    Type type = new TypeToken<HashMap<String, String>>() {
//                                    }.getType();
//                                    HashMap<String, String> map = new Gson().fromJson(response, type);
//                                    if (SocketUtil.COMMOND_CONNECT_RESULT.equalsIgnoreCase(method)) {//连接结果
//                                        String idVal = map.get("idVal");
//                                        if (!TextUtils.isEmpty(idVal) && TextUtils.equals(controlID, idVal)) {
//                                            Constants.setLanConnectMode(true);//将模式切换到 局域网控制模式
//                                            createHeartData();
//                                            if (resultListener != null) {
//                                                resultListener.connectGateWaysSucceed(idVal);
//                                            }
//                                        }
//                                    } else if (SocketUtil.NET_REFRESH.equalsIgnoreCase(method)) {//网络刷新
//                                        isConnectSocket = true;
//                                        Constants.setLanConnectMode(true);
//                                        if (resultListener != null) {
//                                            resultListener.onNetworkRefresh();
//                                        }
//                                    }
//                                }
//                            }
//                        } catch (JSONException e) {
//                            this.onFailed(e);
//                        }
//                    }
//
//                    @Override
//                    public void onFailed(Throwable t) {
//                        activity.hideProgressDialog();
//                        isConnectSocket = false;
//                        Constants.setLanConnectMode(false);
//                        t.printStackTrace();
//                        if (resultListener != null) {
//                            resultListener.onFailed(t);
//                        }
//                    }
//                })
//                .setSocketStatusListener(new RxSocketManager.OnSocketStatusListener() {
//                    @Override
//                    public void onConnected() {
//                        L.d(TAG, "TCP-----onConnected ");
//                        isConnectSocket = true;
//
//                        activity.runOnUiThread(() -> activity.hideProgressDialog());
//                        sendTCPCommand(SocketUtil.getLinkGateway(controlID));
//                    }
//
//                    @Override
//                    public void onConnectFailed() {
//                        L.d(TAG, "TCP-----onConnectFailed ");
//                        isConnectSocket = false;
//
//                        activity.runOnUiThread(() -> {
//                            activity.hideProgressDialog();
//                            //     ToastUtil.s(R.string.connect_device_failed);
//                        });
//                    }
//
//                    @Override
//                    public void onDisConnected() {
//                        L.d("TCP-----onDisConnected ");
//                        isConnectSocket = false;
//                        L.d("isConnectSocket == " + isConnectSocket);
//
//                        Constants.setLanConnectMode(false);
//                        if (!activity.isFinishing()) {
//                            activity.runOnUiThread(() -> {
//                                //    ToastUtil.s(R.string.device_disconnect);
//                                activity.hideProgressDialog();
//                                if (resultListener != null) {
//                                    resultListener.onDisconnected();
//                                }
//                            });
//                        }
//
//                    }
//                })
//                .build();
//    }
//
//    public interface OnResultListener {
//        void connectGateWaysSucceed(String mac);
//
//        void receiveDeviceState(TcpBean bean);
//
//        void onNetworkRefresh();
//
//        void onFailed(Throwable t);
//
//        void onDisconnected();
//    }
//
//    public abstract static class OnSimpleResultListener implements OnResultListener {
//
//        @Override
//        public void connectGateWaysSucceed(String mac) {
//
//        }
//
//        @Override
//        public void onFailed(Throwable t) {
//
//        }
//
//        @Override
//        public void onDisconnected() {
//
//        }
//    }
//
//    /**
//     * 搜索链接设备
//     *
//     * @param jsonObject
//     */
//    public void sendTCPCommand(JSONObject jsonObject) {
//        sendTCPCommand(jsonObject, true);
//    }
//
//    /**
//     * 搜索链接设备
//     *
//     * @param jsonObject
//     */
//    public void sendTCPCommand(JSONObject jsonObject, boolean isTimeout) {
//        L.d("isConnectSocket == " + isConnectSocket);
//        if (isConnectSocket) {
//            RxSocketManager.getInstance().send(jsonObject, isTimeout);
//        } else {
//            if (EmptyUtil.isNotEmpty(Constants.GateWayIp)) {
//                createTcpSocket();//尝试连接
//            } else {
//                //没有开启局域网 则需要从头开始
//                searchGateWays();
//            }
//        }
//    }
//
//
//    public void onRefresh() {
//        L.d("isConnectSocket == " + isConnectSocket);
//
//        sendTCPCommand(getRefreshGateway(controlID));//手动刷新网关在线状态
//    }
//
//    public void onResume() {
//        controlID = SPManager.getControlID();//更新
//        if (!isConnectSocket) {
//            if (EmptyUtil.isNotEmpty(Constants.GateWayIp)) {
//                createTcpSocket();
//            } else {
//                searchGateWays();
//            }
//        }
//    }
//
//    public void onPause() {
//        isConnectSocket = false;
//        Constants.setLanConnectMode(false);
//        RxSocketManager.getInstance().close();
//    }
//
//    public boolean isConnectSocket() {
//        return isConnectSocket;
//    }
//
//    /**
//     * 第一次进入 或者 当网关改变时 要重新搜索网关
//     */
//    public void searchGateWays() {
//        if (!Constants.isLanConnectMode()) {
//            createUdpSocket();
//        }
//    }
//
//    /**
//     * 控制设备开关
//     *
//     * @param type
//     * @param eid
//     * @param value
//     */
//    public void sendDeviceCommand(int type, String eid, int value) {
//        TcpBean bean = new TcpBean(SocketUtil.CONTROL_DEVICE, type, eid, value);
//        RxSocketManager.getInstance().send(bean);
//    }
//
//    /**
//     * 获取设备状态的指令
//     *
//     * @param type
//     * @param eid
//     */
//    public void getDeviceState(int type, String eid) {
//        TcpBean bean = new TcpBean(SocketUtil.GET_DEVICE_STATE, type, eid, 0);
//        RxSocketManager.getInstance().send(bean);
//    }
//
//    /**
//     * 发送心跳包
//     */
//    public void createHeartData() {
//        if (isConnectSocket) {
//            if (heartTask != null && !heartTask.isDisposed()) heartTask.dispose();
//            heartTask = Observable.interval(0, 5, TimeUnit.SECONDS)
//                    .compose(activity.bindToLifecycle())
//                    .subscribe(aLong -> {
//                        if (isConnectSocket) {
//                            sendTCPCommand(SocketUtil.getHeart(), false);
//                        } else {
//                            if (heartTask != null && !heartTask.isDisposed()) {
//                                heartTask.dispose();
//                            }
//                        }
//                    });
//        }
//    }
//
//
//    /**
//     * 获取wifi列表的请求
//     *
//     * @return
//     */
//    public static JSONObject getWifiList() {
//        JSONObject jsonObject = new JSONObject();
//        try {
//            jsonObject.put("method", "getWifi");
//        } catch (JSONException e) {
//            e.printStackTrace();
//        }
//        return jsonObject;
//    }
//
//    /**
//     * 设置wifi密码的请求
//     *
//     * @param name     SSID
//     * @param password 密码
//     * @param sec      当ssid为隐藏时，必须输入加密类型 OPEN/WEP/WPA/WPA2/FT_PSK/EAP
//     * @return
//     */
//    public static JSONObject getSetWifi(String name, String password, String sec) {
//        JSONObject jsonObject = new JSONObject();
//        try {
//            jsonObject.put("method", "setWifi");
//            jsonObject.put("name", name);
//            jsonObject.put("password", password);
//            if (EmptyUtil.isNotEmpty(sec)) jsonObject.put("sec", sec);
//        } catch (JSONException e) {
//            e.printStackTrace();
//        }
//        return jsonObject;
//    }
//
//
//    /**
//     * 连接wifi请求
//     *
//     * @return
//     */
//    public static JSONObject getConnectWifiList() {
//        JSONObject jsonObject = new JSONObject();
//        try {
//            jsonObject.put("method", "getConnect");
//        } catch (JSONException e) {
//            e.printStackTrace();
//        }
//        return jsonObject;
//    }
//
//    /**
//     * 获取网关列表
//     *
//     * @return
//     */
//    public static JSONObject getGatewaysList() {
//        JSONObject jsonObject = new JSONObject();
//        try {
//            jsonObject.put("method", "SearchGateways");
//        } catch (JSONException e) {
//            e.printStackTrace();
//        }
//        return jsonObject;
//    }
//
//    /**
//     * 尝试连接网关
//     *
//     * @param mac
//     * @return
//     */
//    public static JSONObject getLinkGateway(String mac) {
//        JSONObject jsonObject = new JSONObject();
//        try {
//            jsonObject.put("method", "SearchGateway");
//            jsonObject.put("mac", mac);
//        } catch (JSONException e) {
//            e.printStackTrace();
//        }
//        return jsonObject;
//    }
//
//    /**
//     * 刷新网关状态
//     *
//     * @param mac
//     * @return
//     */
//    public static JSONObject getRefreshGateway(String mac) {
//        JSONObject jsonObject = new JSONObject();
//        try {
//            jsonObject.put("method", "GetGatewayState");
//            jsonObject.put("mac", mac);
//        } catch (JSONException e) {
//            e.printStackTrace();
//        }
//        return jsonObject;
//    }
//
//    /**
//     * 发送心跳包
//     *
//     * @return
//     */
//    public static JSONObject getHeart() {
//        JSONObject jsonObject = new JSONObject();
//        try {
//            jsonObject.put("method", "HB");
//        } catch (JSONException e) {
//            e.printStackTrace();
//        }
//        return jsonObject;
//    }
//
//
//}
