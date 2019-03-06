/*
 * Copyright (C) 2013 The Android Open Source Project
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.mirkowu.baselibrarysample.ble;

import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.bluetooth.BluetoothGatt;
import android.bluetooth.BluetoothGattCallback;
import android.bluetooth.BluetoothGattCharacteristic;
import android.bluetooth.BluetoothGattDescriptor;
import android.bluetooth.BluetoothGattService;
import android.bluetooth.BluetoothManager;
import android.bluetooth.BluetoothProfile;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.pm.PackageManager;
import android.os.Build;
import android.os.Handler;
import android.os.Looper;
import android.util.Log;

import com.softgarden.baselibrary.BaseApplication;

import java.util.List;
import java.util.UUID;

/**
 * Service for managing connection and data communication with a GATT server hosted on a
 * given Bluetooth LE device.
 */

/**
 * 如果用于全局管理连接的请使用BLEManager
 * 如果要和界面绑定 请使用BLEService+BLEClient
 * 只适用 BLE设备， 传统蓝牙设备（手机，电脑等）不适用
 */
public class BLEManager {
    private final static String TAG = BLEManager.class.getSimpleName();


    public final static String ACTION_GATT_CONNECTED =
            "com.example.bluetooth.le.ACTION_GATT_CONNECTED";
    public final static String ACTION_GATT_DISCONNECTED =
            "com.example.bluetooth.le.ACTION_GATT_DISCONNECTED";
    public final static String ACTION_GATT_SERVICES_DISCOVERED =
            "com.example.bluetooth.le.ACTION_GATT_SERVICES_DISCOVERED";
    public final static String ACTION_DATA_AVAILABLE =
            "com.example.bluetooth.le.ACTION_DATA_AVAILABLE";
    public final static String EXTRA_DATA =
            "com.example.bluetooth.le.EXTRA_DATA";
    public final static String ACTION_BOND_STATE_CHANGED = BluetoothDevice.ACTION_BOND_STATE_CHANGED;


    /***
     * 这里修改为自己想要搜索的服务
     */
    public final static UUID UUID_SERVICE =
            UUID.fromString("00001800-0000-1000-8000-00805f9b34fb");
    public final static UUID UUID_NOTIFY =
            UUID.fromString("00002a00-0000-1000-8000-00805f9b34fb");
    private static final int SCAN_TIME = 10000;

    private BluetoothManager mBluetoothManager;
    private BluetoothAdapter mBluetoothAdapter;
    private BluetoothGatt mBluetoothGatt;
    private BluetoothAdapter.LeScanCallback mLeScanCallback;
    private BluetoothGattCharacteristic mNotifyCharacteristic;
    private Handler mHandler = new Handler(Looper.getMainLooper());

    private boolean mConnected;//是否已经连接
    private boolean mScanning;//是否正在扫描
    private static volatile BLEManager bleManager;


    public static BLEManager getInstance() {
        if (bleManager == null) {
            synchronized (BLEManager.class) {
                if (bleManager == null) {
                    bleManager = new BLEManager();
                }
            }

        }
        return bleManager;
    }

    private BLEManager() {
        if (!initialize()) {
            Log.e(TAG, "BLEManager: 初始化失败");
        }
    }

    /**
     * 写入数据
     *
     * @param data
     */
    public void writeValue(byte[] data) {
        if (isConnected()) {
            mNotifyCharacteristic.setValue(data);
            mBluetoothGatt.writeCharacteristic(mNotifyCharacteristic);
        }else {
            Log.e(TAG, " writeValue: failed , UnConnected" );
        }
    }

    /**
     * 发现服务
     *
     * @param gattServices
     */
    public void findService(List<BluetoothGattService> gattServices) {
        Log.i(TAG, "BluetoothGattService Count is:" + gattServices.size());
        for (BluetoothGattService gattService : gattServices) {
            Log.i(TAG, "发现服务：" + gattService.getUuid().toString());
            //  Log.i(TAG, UUID_SERVICE.toString());
            if (gattService.getUuid().toString().equalsIgnoreCase(UUID_SERVICE.toString())) {//在各服务里找到需要的服务
                List<BluetoothGattCharacteristic> gattCharacteristics =
                        gattService.getCharacteristics();
                Log.i(TAG, "Characteristics Count is:" + gattCharacteristics.size());
                for (BluetoothGattCharacteristic gattCharacteristic : gattCharacteristics) {
                    Log.i(TAG, "特征值：" + gattCharacteristic.getUuid().toString());
                    if (gattCharacteristic.getUuid().toString().equalsIgnoreCase(UUID_NOTIFY.toString())) {//在各特征值里找到需要的特征值

                        //  Log.i(TAG, UUID_NOTIFY.toString());
                        mNotifyCharacteristic = gattCharacteristic;
                        setCharacteristicNotification(gattCharacteristic, true);//开启特征值
                        broadcastUpdate(ACTION_GATT_SERVICES_DISCOVERED);//已发现服务特征值，发送广播通知
                        return;
                    }
                }
            }
        }
    }


    /**
     * 蓝牙状态回调
     */
    private final BluetoothGattCallback mGattCallback = new BluetoothGattCallback() {
        @Override
        public void onConnectionStateChange(BluetoothGatt gatt, int status, int newState) {
            String intentAction;
            Log.i(TAG, "oldStatus=" + status + " NewStates=" + newState);
            if (status == BluetoothGatt.GATT_SUCCESS) {
                if (newState == BluetoothProfile.STATE_CONNECTED) {//连接成功
                    mConnected = true;
                    intentAction = ACTION_GATT_CONNECTED;

                    broadcastUpdate(intentAction);
                    Log.i(TAG, "Connected to GATT server.");
                    // Attempts to discover services after successful connection.
                    Log.i(TAG, "Attempting to start service discovery:" +
                            mBluetoothGatt.discoverServices());


                } else if (newState == BluetoothProfile.STATE_DISCONNECTED) {//断开连接
                    mConnected = false;
                    intentAction = ACTION_GATT_DISCONNECTED;
                    mBluetoothGatt.close();
                    mBluetoothGatt = null;
                    Log.i(TAG, "Disconnected from GATT server.");
                    broadcastUpdate(intentAction);
                }
            }
        }

        /**
         * 发现服务，在蓝牙连接的时候会调用
         * @param gatt
         * @param status
         */
        @Override
        public void onServicesDiscovered(BluetoothGatt gatt, int status) {
            if (status == BluetoothGatt.GATT_SUCCESS) {
                Log.w(TAG, "onServicesDiscovered received: " + status);
                findService(gatt.getServices());
            } else {
                if (mBluetoothGatt.getDevice().getUuids() == null)
                    Log.w(TAG, "onServicesDiscovered received: " + status);
            }
        }

        /**
         *   Characteristic 改变，数据接收会调用
         * @param gatt
         * @param characteristic
         */
        @Override
        public void onCharacteristicRead(BluetoothGatt gatt,
                                         BluetoothGattCharacteristic characteristic,
                                         int status) {
            if (status == BluetoothGatt.GATT_SUCCESS) {
                broadcastUpdate(ACTION_DATA_AVAILABLE, characteristic);
            }
        }

        /**
         *   Characteristic 改变，数据接收会调用
         * @param gatt
         * @param characteristic
         */
        @Override
        public void onCharacteristicChanged(BluetoothGatt gatt,
                                            BluetoothGattCharacteristic characteristic) {
            broadcastUpdate(ACTION_DATA_AVAILABLE, characteristic);
            Log.e(TAG, "OnCharacteristicWrite");
        }

        @Override
        public void onCharacteristicWrite(BluetoothGatt gatt, BluetoothGattCharacteristic characteristic,
                                          int status) {
            Log.e(TAG, "OnCharacteristicWrite");
        }

        @Override
        public void onDescriptorRead(BluetoothGatt gatt,
                                     BluetoothGattDescriptor bd,
                                     int status) {
            Log.e(TAG, "onDescriptorRead");
        }

        @Override
        public void onDescriptorWrite(BluetoothGatt gatt,
                                      BluetoothGattDescriptor bd,
                                      int status) {
            Log.e(TAG, "onDescriptorWrite");
        }

        @Override
        public void onReadRemoteRssi(BluetoothGatt gatt, int a, int b) {
            Log.e(TAG, "onReadRemoteRssi");
        }

        @Override
        public void onReliableWriteCompleted(BluetoothGatt gatt, int a) {
            Log.e(TAG, "onReliableWriteCompleted");
        }

    };

    /**
     * 发送广播通知
     *
     * @param action
     */
    private void broadcastUpdate(final String action) {
        final Intent intent = new Intent(action);
        broadcastUpdate(intent);
    }

    private void broadcastUpdate(Intent intent) {
        BaseApplication.getInstance().sendBroadcast(intent);
    }

    /**
     * 发送广播通知 并获取到读取的数据
     *
     * @param action
     * @param characteristic
     */
    private void broadcastUpdate(final String action,
                                 final BluetoothGattCharacteristic characteristic) {
        final Intent intent = new Intent(action);

        // This is special handling for the Heart Rate Measurement profile.  Data parsing is
        // carried out as per profile specifications:
        // http://developer.bluetooth.org/gatt/characteristics/Pages/CharacteristicViewer.aspx?u=org.bluetooth.characteristic.heart_rate_measurement.xml
        // For all other profiles, writes the data formatted in HEX.
        final byte[] data = characteristic.getValue();
        if (data != null && data.length > 0) {
            //final StringBuilder stringBuilder = new StringBuilder(data.length);
            //for(byte byteChar : data)
            //    stringBuilder.append(String.format("%02X ", byteChar));
            //intent.putExtra(EXTRA_DATA, new String(data) + "\n" + stringBuilder.toString());
            intent.putExtra(EXTRA_DATA, data);
        }
        broadcastUpdate(intent);
    }

//    public class LocalBinder extends Binder {
//        public BLEManager getService() {
//            return BLEManager.this;
//        }
//    }
//
//    private final IBinder mBinder = new LocalBinder();
//
//    @Override
//    public IBinder onBind(Intent intent) {
//        return mBinder;
//    }
//
//    @Override
//    public boolean onUnbind(Intent intent) {
//        // After using a given device, you should make sure that BluetoothGatt.close() is called
//        // such that resources are cleaned up properly.  In this particular example, close() is
//        // invoked when the UI is disconnected from the Service.
//        //取消绑定时，关闭蓝牙服务，释放资源
//        close();
//        return super.onUnbind(intent);
//    }


    /**
     * 初始化 蓝牙相关
     *
     * @return
     */
    public boolean initialize() {
        // For API level 18 and above, get a reference to BluetoothAdapter through
        // BluetoothManager.
        if (mBluetoothManager == null) {
            mBluetoothManager = (BluetoothManager) BaseApplication.getInstance().getSystemService(Context.BLUETOOTH_SERVICE);
            if (mBluetoothManager == null) {
                Log.e(TAG, "Unable to initialize BluetoothManager.");
                return false;
            }
        }

        mBluetoothAdapter = mBluetoothManager.getAdapter();
        if (mBluetoothAdapter == null) {
            Log.e(TAG, "Unable to obtain a BluetoothAdapter.");
            return false;
        }

        return true;
    }


    /**
     * 连接蓝牙
     *
     * @param address
     * @return
     */
    public boolean connect(final String address) {
        return connect(address, false);
    }

    /**
     * 连接蓝牙地址
     *
     * @param address
     * @param autoConnect 是否自动连接  默认不自动连接
     * @return
     */
    public boolean connect(final String address, boolean autoConnect) {
        if (mBluetoothAdapter == null || address == null) {
            Log.w(TAG, "BluetoothAdapter not initialized or unspecified address.");
            return false;
        }
/*
        // Previously connected device.  Try to reconnect.
        if (mBluetoothDeviceAddress != null && address.equals(mBluetoothDeviceAddress)
                && mBluetoothGatt != null) {
            Log.d(TAG, "Trying to use an existing mBluetoothGatt for connection.");
            if (mBluetoothGatt.connect()) {
                mConnectionState = STATE_CONNECTING;
                return true;
            } else {
                return false;
            }
        }
*/

        //根据地址找到蓝牙设备
        BluetoothDevice device = mBluetoothAdapter.getRemoteDevice(address);
        if (device == null) {
            Log.w(TAG, "Device not found.  Unable to connect.");
            return false;
        }
        // We want to directly connect to the device, so we are setting the autoConnect
        // parameter to false.
        //如果已经连接一个蓝牙设备，则先关闭
        if (mBluetoothGatt != null) {
            mBluetoothGatt.close();
            mBluetoothGatt = null;
        }

        /**
         * 这里设置 是否自动连接
         */
        mBluetoothGatt = device.connectGatt(BaseApplication.getInstance(), autoConnect, mGattCallback);
//        device.setPairingConfirmation();
//        device.setPin();
//         mBluetoothGatt.connect();

        Log.d(TAG, "Trying to create a new connection.");
        return true;
    }

    /**
     * Disconnects an existing connection or cancel a pending connection. The disconnection result
     * is reported asynchronously through the
     * {@code BluetoothGattCallback#onConnectionStateChange(android.bluetooth.BluetoothGatt, int, int)}
     * callback.
     */
    /**
     * 断开连接
     */
    public void disconnect() {
        if (mBluetoothAdapter == null || mBluetoothGatt == null) {
            Log.w(TAG, "BluetoothAdapter not initialized");
            return;
        }
        mBluetoothGatt.disconnect();
    }

    public boolean isConnected() {
        return mConnected;
    }

    /**
     * After using a given BLE device, the app must call this method to ensure resources are
     * released properly.
     */

    /**
     * 关闭蓝牙服务，释放资源
     */
    public void close() {
        if (mScanning) {
            stopLeScan();
            mHandler.removeCallbacks(scanTask);
        }
        if (mBluetoothGatt == null) {
            return;
        }
        mBluetoothGatt.close();
        mBluetoothGatt = null;
        mConnected = false;
        bleManager = null;
    }

    /**
     * Request a read on a given {@code BluetoothGattCharacteristic}. The read result is reported
     * asynchronously through the {@code BluetoothGattCallback#onCharacteristicRead(android.bluetooth.BluetoothGatt, android.bluetooth.BluetoothGattCharacteristic, int)}
     * callback.
     *
     * @param characteristic The characteristic to read from.
     */
    public void readCharacteristic(BluetoothGattCharacteristic characteristic) {
        if (mBluetoothAdapter == null || mBluetoothGatt == null) {
            Log.w(TAG, "BluetoothAdapter not initialized");
            return;
        }
        mBluetoothGatt.readCharacteristic(characteristic);
    }

    /**
     * Enables or disables notification on a give characteristic.
     *
     * @param characteristic Characteristic to act on.
     * @param enabled        If true, enable notification.  False otherwise.
     */
    public void setCharacteristicNotification(BluetoothGattCharacteristic characteristic,
                                              boolean enabled) {
        if (mBluetoothAdapter == null || mBluetoothGatt == null) {
            Log.w(TAG, "BluetoothAdapter not initialized");
            return;
        }
        mBluetoothGatt.setCharacteristicNotification(characteristic, enabled);
/*
        // This is specific to Heart Rate Measurement.
        if (UUID_HEART_RATE_MEASUREMENT.equals(characteristic.getUuid())) {
            BluetoothGattDescriptor descriptor = characteristic.getDescriptor(
                    UUID.fromString(SampleGattAttributes.CLIENT_CHARACTERISTIC_CONFIG));
            descriptor.setValue(BluetoothGattDescriptor.ENABLE_NOTIFICATION_VALUE);
            mBluetoothGatt.writeDescriptor(descriptor);
        }
        */
    }

    /**
     * Retrieves a list of supported GATT services on the connected device. This should be
     * invoked only after {@code BluetoothGatt#discoverServices()} completes successfully.
     *
     * @return A {@code List} of supported services.
     */
    public List<BluetoothGattService> getSupportedGattServices() {
        if (mBluetoothGatt == null) return null;

        return mBluetoothGatt.getServices();
    }


//    /**
//     * 开始监听数据变化
//     *
//     * @param context
//     * @param gattUpdateReceiver     广播
//     * @param gattUpdateIntentFilter 广播事件Action 过滤
//     * @return
//     */
//    public void startMonitor(Context context, BroadcastReceiver gattUpdateReceiver, IntentFilter gattUpdateIntentFilter) {
//        this.mGattUpdateReceiver = gattUpdateReceiver;
//        context.registerReceiver(mGattUpdateReceiver, gattUpdateIntentFilter);
//    }
//
//    public void stopMonitor(Context context) {
//        context.unregisterReceiver(mGattUpdateReceiver);
//    }


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

        mHandler.postDelayed(scanTask, SCAN_TIME);

        mScanning = true;
        //F000E0FF-0451-4000-B000-000000000000
        return mBluetoothAdapter.startLeScan(serviceUuids, mLeScanCallback);
    }

    private Runnable scanTask = new Runnable() {
        @Override
        public void run() {
            stopLeScan();
        }
    };

    public void stopLeScan() {
        if (mScanning && mBluetoothAdapter != null && mLeScanCallback != null) {
            mScanning = false;
            mBluetoothAdapter.stopLeScan(mLeScanCallback);
        }
    }

}
