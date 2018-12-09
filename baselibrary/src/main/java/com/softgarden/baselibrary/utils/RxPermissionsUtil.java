package com.softgarden.baselibrary.utils;

import android.Manifest.permission;
import android.app.Activity;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.Uri;
import android.os.Handler;
import android.provider.Settings;
import android.support.v7.app.AlertDialog;

import com.softgarden.baselibrary.R;
import com.tbruyelle.rxpermissions2.Permission;
import com.tbruyelle.rxpermissions2.RxPermissions;

import io.reactivex.Observable;
import io.reactivex.functions.Consumer;

/**
 * Created by MirkoWu on 2017/4/5 0005.
 */

public class RxPermissionsUtil {

    /*** 打电话 */
    public static final String[] PHONE = new String[]
            {permission.CALL_PHONE};
    /*** 蓝牙/WIFI 需要粗略定位 */
    public static final String[] LOCATION = new String[]
            {/*permission.BLUETOOTH,*/ permission.ACCESS_COARSE_LOCATION};
    /*** 文件存储 */
    public static final String[] STORAGE = new String[]
            {permission.WRITE_EXTERNAL_STORAGE, permission.READ_EXTERNAL_STORAGE};
    /*** 摄像头 */
    public static final String[] CAMERA = new String[]
            {permission.CAMERA};
    /*** 录音 */
    public static final String[] AUDIO = new String[]
            {permission.RECORD_AUDIO};
    /*** 摄像头+文件存储 */
    public static final String[] CAMERA_STORAGE = new String[]
            {permission.CAMERA, permission.WRITE_EXTERNAL_STORAGE, permission.READ_EXTERNAL_STORAGE};


    public static Observable<Boolean> request(Activity activity, String... permissions) {
        return new RxPermissions(activity).request(permissions);
    }

    public static Observable<Boolean> shouldShowRequestPermissionRationale(Activity activity, String... permissions) {
        return new RxPermissions(activity).shouldShowRequestPermissionRationale(activity, permissions);
    }

    public static void check(final Activity activity, final String[] permissions,
                             final String title, /*final String cancelText, String ensureText, */
                             final OnPermissionRequestListener listener) {
        new RxPermissions(activity).requestEachCombined(permissions)
                .subscribe(new Consumer<Permission>() {
                    @Override
                    public void accept(Permission permission) throws Exception {
                        if (permission.granted) {//已获取权限
                            //要延时 ，否则显示弹窗会报错
                            mHandler.postDelayed(new Runnable() {
                                @Override
                                public void run() {
                                    if (listener != null) listener.onSucceed();
                                }
                            }, 100);
                        } else if (permission.shouldShowRequestPermissionRationale) {//是否需要向用户解释为何申请权限
                            showPermissionDialog(activity, "", title, new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialog, int which) {
                                    check(activity, permissions, title, listener);
                                }
                            });
                        } else {//被拒绝，弹窗提示
                            showPermissionDialog(activity, "", title, new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialog, int which) {
                                    if (listener != null) listener.onFailed();
                                }
                            });
                        }
                    }
                });
    }

    /**
     * 提示缺少什么权限的对话框
     */
    public static void showPermissionDialog(Context context, String title, String message,
                                            DialogInterface.OnClickListener onClickListener) {

        //  new PromptDialog().setTitle(title).setContent(message).setNegativeButton().setPositiveButton().setOnButtonClickListener().show(context);
        new AlertDialog.Builder(context)
                .setTitle(title)
                .setMessage(message)
                .setNegativeButton(R.string.base_cancel, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {

                    }
                })
                .setPositiveButton(R.string.base_ok, onClickListener).show();
    }

    /**
     * 提示缺少必要权限对话框
     */
    public static void showLackPermissionDialog(final Context context) {
        new AlertDialog.Builder(context)
                .setTitle(R.string.base_prompt_message)
                .setMessage(R.string.base_permission_lack)
                .setNegativeButton(R.string.base_cancel, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {

                    }
                })
                .setPositiveButton(R.string.base_ok, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        AppUtil.openAppSetting(context);
                    }
                }).show();
    }


    public interface OnPermissionRequestListener {
        void onSucceed();

        void onFailed();
    }

    private static Handler mHandler = new Handler();
}
