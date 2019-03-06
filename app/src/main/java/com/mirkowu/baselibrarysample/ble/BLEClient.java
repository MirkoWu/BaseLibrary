package com.mirkowu.baselibrarysample.ble;

import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.content.BroadcastReceiver;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.ServiceConnection;
import android.content.pm.PackageManager;
import android.os.Build;
import android.os.Handler;
import android.os.IBinder;
import android.os.Looper;

import com.softgarden.baselibrary.utils.L;

import java.util.UUID;

/**
 * @author by DELL
 * @date on 2018/6/27
 * @describe 本工具只适用BLE设备，非BLE传统蓝牙设备（手机，电脑等）不适用
 */
public class BLEClient {
    public static final String TAG = "BLEClient";

    private static BLEClient bleClient;
    private BluetoothAdapter mBluetoothAdapter;


    BLEService mBLEService;
    BroadcastReceiver mGattUpdateReceiver;
    private boolean isConnected;//蓝牙是否已连接
    private boolean mScanning;//是否正在扫描
    private BluetoothAdapter.LeScanCallback mLeScanCallback;
    private static final int SCAN_TIME = 10000;
    private String mBleAddress;


    private Handler mHandler = new Handler(Looper.getMainLooper());
    private OnServiceConnectListener onServiceConnectListener;

    private final ServiceConnection mServiceConnection = new ServiceConnection() {
        @Override
        public void onServiceConnected(ComponentName componentName, IBinder service) {
            mBLEService = ((BLEService.LocalBinder) service).getService();

            if (!mBLEService.initialize()) {
                L.e(TAG, "Unable to initialize Bluetooth");
                return;
            }
            L.d(TAG, "mBLEService is okay");

            if (onServiceConnectListener != null) {
                onServiceConnectListener.onServiceConnected();
            }

            // Automatically connects to the device upon successful start-up initialization.
            //mBLEService.connect(mDeviceAddress);
            // connect(mBleAddress);
        }

        @Override
        public void onServiceDisconnected(ComponentName componentName) {
            if (onServiceConnectListener != null) {
                onServiceConnectListener.onServiceDisconnected();
            }
            mBLEService = null;
        }
    };

    /**
     * @return
     */
    public static BLEClient getInstance() {
        if (bleClient == null) {
            bleClient = new BLEClient();
        }
        return bleClient;
    }

    private BLEClient() {
        mBluetoothAdapter = BluetoothAdapter.getDefaultAdapter();
    }

    /**
     * 需要全程（即多界面）监听 的不用每个界面都绑定再解绑服务 退出蓝牙场景时再解绑
     *
     * @param context
     * @param onServiceConnectListener 连接服务的状态 监听回调 成功 或 失败
     * @return
     */
    public boolean bindService(Context context, OnServiceConnectListener onServiceConnectListener) {
        this.onServiceConnectListener = onServiceConnectListener;
        Intent gattServiceIntent = new Intent(context, BLEService.class);
        boolean bindResult = context.bindService(gattServiceIntent, mServiceConnection, Context.BIND_AUTO_CREATE);
        L.d(TAG, "Try to bindService=" + bindResult);
        return bindResult;
    }

    public void unbindService(Context context) {
        close();
        context.unbindService(mServiceConnection);
    }

    /**
     * 开始监听数据变化
     *
     * @param context
     * @param gattUpdateReceiver     广播
     * @param gattUpdateIntentFilter 广播事件Action 过滤
     * @return
     */
    public void startMonitor(Context context, BroadcastReceiver gattUpdateReceiver, IntentFilter gattUpdateIntentFilter) {
        this.mGattUpdateReceiver = gattUpdateReceiver;
        context.registerReceiver(mGattUpdateReceiver, gattUpdateIntentFilter);
    }

    public void stopMonitor(Context context) {
        context.unregisterReceiver(mGattUpdateReceiver);
    }


    /**
     * Intent 过滤
     *
     * @return
     */
    public static IntentFilter makeGattUpdateIntentFilter() {
        final IntentFilter intentFilter = new IntentFilter();
        intentFilter.addAction(BLEService.ACTION_GATT_CONNECTED);
        intentFilter.addAction(BLEService.ACTION_GATT_DISCONNECTED);
        intentFilter.addAction(BLEService.ACTION_GATT_SERVICES_DISCOVERED);
        intentFilter.addAction(BLEService.ACTION_DATA_AVAILABLE);
        // intentFilter.addAction(BLEService.ACTION_BOND_STATE_CHANGED);//配对状态
        intentFilter.addAction(BluetoothDevice.ACTION_UUID);
        return intentFilter;
    }

    /**
     * 开启蓝牙
     *
     * @return
     */
    public boolean enable() {
        if (mBluetoothAdapter != null) {
            return mBluetoothAdapter.enable();
        }
        return false;
    }

    /**
     * 蓝牙是否开启
     *
     * @return
     */
    public boolean isEnable() {
        if (mBluetoothAdapter != null) {
            return mBluetoothAdapter.isEnabled();
        }
        return false;
    }


    /**
     * 判断设备是否支持BLE
     *
     * @param context
     * @return
     */
    public boolean isSupportBLE(Context context) {
        // android版本>=4.3 才支持BLE
        if (Build.VERSION.SDK_INT <= Build.VERSION_CODES.JELLY_BEAN_MR1 ||
                !context.getPackageManager().hasSystemFeature(PackageManager.FEATURE_BLUETOOTH_LE)) {
            return false;
        }

        mBluetoothAdapter = BluetoothAdapter.getDefaultAdapter();
        if (mBluetoothAdapter == null) {
            return false;
        }
        return true;
    }


    /**
     * 扫描BLE设备（手机不属于ble，要搜索手机要调用startDiscovery()
     */
    public boolean startLeScan(BluetoothAdapter.LeScanCallback callback) {
        return startLeScan(null, callback);
    }

    public boolean startLeScan(final UUID[] serviceUuids, BluetoothAdapter.LeScanCallback callback) {
        if (mBluetoothAdapter == null) {
            return false;
        }
        if (callback == null) {
            throw new NullPointerException("LeScanCallback is NULL");
        }
        mLeScanCallback = callback;

        if (mScanning) {
            return false;
        }

        mHandler.postDelayed(new Runnable() {
            @Override
            public void run() {
                stopLeScan();
            }
        }, SCAN_TIME);

        mScanning = true;
        //F000E0FF-0451-4000-B000-000000000000
        return mBluetoothAdapter.startLeScan(serviceUuids, mLeScanCallback);
    }

    public void stopLeScan() {
        if (mScanning && mBluetoothAdapter != null && mLeScanCallback != null) {
            mScanning = false;
            mBluetoothAdapter.stopLeScan(mLeScanCallback);
        }
    }


    /**
     * 连接BLE设备
     */
    public void connect(String mBleAddress) {
        connect(mBleAddress, false);
    }

    /**
     * 连接BLE设备
     *
     * @param mBleAddress
     * @param autoConnect 是否自动连接
     */
    public void connect(String mBleAddress, boolean autoConnect) {
        this.mBleAddress = mBleAddress;
        if (mBLEService != null) {
            boolean result = mBLEService.connect(mBleAddress, autoConnect);
            L.d(TAG, "Connect request result=" + result);
        } else {
            L.e(TAG, "Connect  failed mBLEService ==null");
        }
    }

    /**
     * 发送指令
     */
    public void sendData(byte[] data) {
        if (mBLEService != null) {
            mBLEService.writeValue(data);
            L.d(TAG, "disconnectBle" + new String(data));
        }
    }

    /**
     * 断开蓝牙连接
     */
    public void disconnectBle() {
        if (mBLEService != null) {
            mBLEService.disconnect();
        }
        L.d(TAG, "disconnectBle");
    }

    /**
     * 关闭蓝牙服务
     */
    public void close() {
        if (mBLEService != null) {
            mBLEService.close();
            mBLEService = null;
        }
        L.d(TAG, "close");
    }
}
