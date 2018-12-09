package com.softgarden.baselibrary.utils;


import android.content.Context;
import android.content.DialogInterface;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;

import com.softgarden.baselibrary.R;
import com.softgarden.baselibrary.dialog.PromptDialog;

/**
 * 类描述：判断网络是否可以连接工具类
 */
public class NetworkUtil {

    /**
     * 判断网络连接是否已开
     * true 已打开  false 未打开
     *
     * @param context
     * @return
     */
    public static boolean isConnected(Context context) {
        boolean isConnected = false;
        ConnectivityManager conManager = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo network = conManager.getActiveNetworkInfo();
        if (network != null) {
            isConnected = conManager.getActiveNetworkInfo().isAvailable();
        }
        return isConnected;
    }

    /**
     * 判断是否是WIFI连接
     *
     * @param context
     * @return
     */
    public static boolean isWIFI(Context context) {
        ConnectivityManager connectivityManager = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
        if (connectivityManager == null || connectivityManager.getActiveNetworkInfo() == null)
            return false;
        return connectivityManager.getActiveNetworkInfo().getType() == ConnectivityManager.TYPE_WIFI;
    }


    private static volatile PromptDialog promptDialog;

    /**
     * 当判断当前手机没有网络时选择是否打开网络设置
     *
     * @param context
     */
    public static void showNoNetWorkDialog(final Context context) {
        if (promptDialog == null) {
            synchronized (PromptDialog.class) {
                if (promptDialog == null) {
                    promptDialog = new PromptDialog(context)
                            .setTitle(context.getString(R.string.base_warm_hint))
                            .setContent(context.getString(R.string.base_no_network_hint))
                            .setPositiveButton(context.getString(R.string.base_go))
                            .setNegativeButton(context.getString(R.string.base_cancel))
                            .setOnButtonClickListener(new PromptDialog.OnButtonClickListener() {
                                @Override
                                public void onButtonClick(PromptDialog dialog, boolean isPositiveClick) {
                                    if (isPositiveClick)
                                        AppUtil.openNetworkSetting(context);//前往设置界面
                                }
                            });
                    promptDialog.setOnDismissListener(new DialogInterface.OnDismissListener() {
                        @Override
                        public void onDismiss(DialogInterface dialog) {
                            promptDialog = null;
                        }
                    });
                    promptDialog.setOnCancelListener(new DialogInterface.OnCancelListener() {
                        @Override
                        public void onCancel(DialogInterface dialog) {
                            promptDialog = null;
                        }
                    });
                    //  promptDialog.show();
                }
            }
        }
        promptDialog.show();
    }
}
