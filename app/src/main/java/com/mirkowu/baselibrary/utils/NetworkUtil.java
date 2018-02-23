package com.mirkowu.baselibrary.utils;


import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;

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
        if (connectivityManager == null)
            return false;
        return connectivityManager.getActiveNetworkInfo().getType() == ConnectivityManager.TYPE_WIFI;
    }

    /**
     * 当判断当前手机没有网络时选择是否打开网络设置
     *
     * @param context
     */
    public static void showNoNetWorkDialog(final Context context) {
//        AlertDialog.Builder builder = new AlertDialog.Builder(context);
//        builder.setIcon(R.mipmap.ic_launcher)         //
//                .setTitle(R.string.app_name)            //
//                .setMessage(R.string.network_not_open_check).setPositiveButton(R.string.go_setting, new DialogInterface.OnClickListener() {
//            @Override
//            public void onClick(DialogInterface dialog, int which) {
//                // 跳转到系统的网络设置界面
//                Intent intent = null;
//                // 先判断当前系统版本
//                if (Build.VERSION.SDK_INT > 10) {  // 3.0以上
//                    intent = new Intent(ACTION_WIRELESS_SETTINGS);
//                } else {
//                    intent = new Intent();
//                    intent.setClassName("com.android.settings", "com.android.settings.WirelessSettings");
//                }
//                context.startActivity(intent);
//            }
//        }).setNegativeButton(R.string.i_know, null).show();
    }


}
