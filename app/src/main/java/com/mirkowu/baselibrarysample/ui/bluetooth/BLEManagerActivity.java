package com.mirkowu.baselibrarysample.ui.bluetooth;

import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.view.View;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.mirkowu.baselibrarysample.R;
import com.mirkowu.baselibrarysample.base.RefreshActivity;
import com.mirkowu.baselibrarysample.ble.BLEClient;
import com.mirkowu.baselibrarysample.ble.BLEManager;
import com.mirkowu.baselibrarysample.ble.BLEService;
import com.mirkowu.basetoolbar.BaseToolbar;
import com.softgarden.baselibrary.utils.L;
import com.softgarden.baselibrary.utils.RxPermissionsUtil;
import com.softgarden.baselibrary.utils.StringUtil;
import com.softgarden.baselibrary.utils.ToastUtil;

public class BLEManagerActivity extends RefreshActivity implements BaseQuickAdapter.OnItemClickListener {

    public static void start(Context context) {
        Intent starter = new Intent(context, BLEManagerActivity.class);
//	    starter.putExtra( );
        context.startActivity(starter);
    }


    private BLEAdapter mAdapter;
    private BLEManager bleManager;

    private BroadcastReceiver bleReceiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            final String action = intent.getAction();
            if (BLEService.ACTION_GATT_CONNECTED.equals(action)) {  //gatt 仅仅连接蓝牙
                L.e(TAG, "Only gatt, just wait");
            } else if (BLEService.ACTION_GATT_DISCONNECTED.equals(action)) { //断开连接
                L.e(TAG, "action_gatt_disconnected");
            } else if (BLEService.ACTION_GATT_SERVICES_DISCOVERED.equals(action)) { //发现蓝牙服务，可以进行通信了
                L.e(TAG, "In what we need 发现蓝牙服务，可以进行通信了");

                // TODO: 2019/3/5
            } else if (BLEService.ACTION_DATA_AVAILABLE.equals(action)) { //收到数据
                L.e(TAG, "RECV DATA");
                byte[] data = intent.getByteArrayExtra(BLEService.EXTRA_DATA);
                if (data != null) {
                    String rec = StringUtil.byte2Hexstr(data);
                    L.d("接收数据" + rec);

                }
            }
        }
    };

    @Override
    protected int getLayoutId() {
        return R.layout.layout_recyclerview_refresh;
    }

    @Nullable
    @Override
    protected BaseToolbar.Builder setToolbar(@NonNull BaseToolbar.Builder builder) {
        return builder.setTitle("搜索蓝牙").addRightText("搜索", v -> {

        });
    }

    @Override
    protected void initialize() {
        initRecyclerView();
        //  initRefreshLayout();
        mAdapter = new BLEAdapter();
        mRecyclerView.setAdapter(mAdapter);
        mAdapter.setOnItemClickListener(this);
        startLeScan();

    }

    @Override
    public void onItemClick(BaseQuickAdapter adapter, View view, int position) {
        BluetoothDevice device = mAdapter.getItem(position);

        bleManager.connect(device.getAddress(), true);
      //  registerReceiver(bleReceiver, BLEClient.makeGattUpdateIntentFilter());
    }

    @Override
    protected void onResume() {
        super.onResume();
        registerReceiver(bleReceiver, BLEClient.makeGattUpdateIntentFilter());
    }

    @Override
    protected void onPause() {
        super.onPause();
        unregisterReceiver(bleReceiver);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        // bleManager.close();
        //todo 记得在合适的时机调用close方法来关闭蓝牙服务
        //一般在退出APP、切换蓝牙、手动断开蓝牙、退出登录 等节点
    }

    private void startLeScan() {
        bleManager = BLEManager.getInstance();
        if (bleManager.isSupportBLE(getContext())) {
            if (RxPermissionsUtil.checkGPSEnable(getContext())) {
                RxPermissionsUtil.check(this, RxPermissionsUtil.LOCATION, "",
                        new RxPermissionsUtil.OnPermissionRequestListener() {
                            @Override
                            public void onSucceed() {
                                if (bleManager.isEnable()) {

                                    /**
                                     * 请在BLEService中修改需要的服务值
                                     * UUID_SERVICE
                                     * UUID_NOTIFY
                                     */
                                    bleManager.startLeScan(new BluetoothAdapter.LeScanCallback() {
                                        @Override
                                        public void onLeScan(BluetoothDevice device, int rssi, byte[] scanRecord) {
                                            //type=1传统蓝牙  2BLE 3 双模蓝牙 0未知
                                            if (!mAdapter.getData().contains(device) && device.getType() >= 2) {
                                                mAdapter.addData(device);
                                            }
                                        }
                                    });

                                } else {
                                    bleManager.enable();
                                }
                            }

                            @Override
                            public void onFailed() {

                            }
                        });
            } else {
                ToastUtil.s("检测到GPS/位置服务功能未开启，请开启");
            }


        }
    }

    @Override
    public void loadData() {

    }
}
