package com.mirkowu.baselibrarysample.ui;

import android.content.Context;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import com.mirkowu.baselibrarysample.R;
import com.mirkowu.baselibrarysample.base.ToolbarActivity;
import com.mirkowu.baselibrarysample.socketUtil.ByteUtil;
import com.mirkowu.baselibrarysample.socketUtil.RxSocketManager;
import com.mirkowu.baselibrarysample.socketUtil.SocketType;
import com.mirkowu.baselibrarysample.utils.DateUtil;
import com.mirkowu.basetoolbar.BaseToolbar;
import com.softgarden.baselibrary.utils.L;
import com.softgarden.baselibrary.utils.StringUtil;
import com.softgarden.baselibrary.utils.ToastUtil;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.UnsupportedEncodingException;

import butterknife.BindView;
import butterknife.OnClick;

public class SocketTestActivity extends ToolbarActivity {

    public static void start(Context context) {
        Intent starter = new Intent(context, SocketTestActivity.class);
//    starter.putExtra();
        context.startActivity(starter);
    }

    @BindView(R.id.edt_ip)
    EditText edtIp;
    @BindView(R.id.edt_port)
    EditText edtPort;
    @BindView(R.id.btn_type)
    Button btnType;
    @BindView(R.id.btn_connect)
    Button btnConnect;
    @BindView(R.id.edt_send)
    EditText edtSend;
    @BindView(R.id.btn_send)
    Button btnSend;
    @BindView(R.id.btn_clear_rec)
    Button btnClearRec;
    @BindView(R.id.edt_rec)
    EditText edtRec;

    private boolean isTCP = true;//默认 TCP   UDP
    private boolean isConnected;

    @Override
    protected int getLayoutId() {
        return R.layout.activity_socket_test;
    }

    @Nullable
    @Override
    protected BaseToolbar.Builder setToolbar(@NonNull BaseToolbar.Builder builder) {
        return builder.setTitle("测试");
    }

    @Override
    protected void initialize() {


    }


    @OnClick({R.id.btn_type, R.id.btn_connect, R.id.btn_send, R.id.btn_clear_rec})
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.btn_type:
                isTCP = !isTCP;
                btnType.setText((isTCP ? "TCP" : "UDP") + "(点击切换)");
                break;
            case R.id.btn_connect:
                connect();
                break;
            case R.id.btn_send:

                try {
                    JSONObject jsonObject = new JSONObject();
                    jsonObject.put("UserName", "18565699138");
                    jsonObject.put("LoginTime", DateUtil.getDataStr(System.currentTimeMillis(), DateUtil.FORMAT_YMDHMS));
                    jsonObject.put("DevId", "e29d0e5d05771d2c");
                    jsonObject.put("Token", "23333");

                    String data = "5A5A00300000000100000000101000075A3238302D525331380001009feb22970100010000000000020100010001B00D";
                    sendData(StringUtil.hex2Bytes(data));
                } catch (JSONException e) {
                    e.printStackTrace();
                }

                break;
            case R.id.btn_clear_rec:
                break;
        }
    }


    private void sendData(String data) {
        try {
            byte[] head = new byte[]{1, 2, 3};
            byte[] body = data.getBytes("UTF-8");
            int length = body.length;
            byte[] count = new byte[]{(byte) (length / 256), (byte) (length % 256)};
            byte[] finalByte = ByteUtil.concat(head, count);
            finalByte = ByteUtil.concat(finalByte, body);

            RxSocketManager.getInstance().send(finalByte, false);
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }
    }

    private void sendData(byte[] data) {
        RxSocketManager.getInstance().send(data, false);

    }

    private void connect() {
        if (isConnected) {
            RxSocketManager.getInstance().close();
            return;
        }
        String ip = edtIp.getText().toString().trim();
        String port = edtPort.getText().toString().trim();
        RxSocketManager.getInstance().close();//创建之前 请现在关闭上一次的socket 释放资源
        RxSocketManager.getInstance()
                .setClient(isTCP ? SocketType.TCP : SocketType.UDP, ip, Integer.valueOf(port))
                .setResultCallback(new RxSocketManager.ResultCallback() {
                    @Override
                    public void onSucceed(byte[] data) {
                        try {
                            String rec = new String(data, "UTF-8");
                            L.d("onSucceed  " + StringUtil.byte2Hexstr(data));

                        } catch (UnsupportedEncodingException e) {
                            e.printStackTrace();
                        }
                    }

                    @Override
                    public void onFailed(Throwable t) {
                        t.printStackTrace();
                    }
                })
                .setSocketStatusListener(new RxSocketManager.OnSocketStatusListener() {
                    @Override
                    public void onConnectSucceed() {

                        getActivity().runOnUiThread(() -> {
                            L.d("------------onConnectSucceed");
                            ToastUtil.s("已连接");
//                            try {
//                                JSONObject jsonObject = new JSONObject();
//                                jsonObject.put("UserName", "18565699138");
//                                jsonObject.put("LoginTime", DateUtil.getDataStr(System.currentTimeMillis(), DateUtil.FORMAT_YMDHMS));
//                                jsonObject.put("DevId", "e29d0e5d05771d2c");
//                                jsonObject.put("Token", "23333");
//
//                                sendData(jsonObject.toString());
//                            } catch (JSONException e) {
//                                e.printStackTrace();
//                            }
//
//                            isConnected = true;
//                            if (!getActivity().isFinishing()) btnConnect.setText("断开");
                        });
                    }

                    @Override
                    public void onConnectFailed() {
                        L.d("------------onConnectFailed");
                    }

                    @Override
                    public void onDisConnected() {
                        getActivity().runOnUiThread(() -> {
                            L.d("------------onDisConnected");
                            ToastUtil.s("已断开连接");
                            isConnected = false;
                            if (!getActivity().isFinishing()) btnConnect.setText("连接");
                        });
                    }
                }).build();

    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        RxSocketManager.getInstance().close();
    }
}
