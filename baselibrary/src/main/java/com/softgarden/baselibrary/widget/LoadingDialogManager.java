package com.softgarden.baselibrary.widget;

import android.app.Activity;
import android.content.DialogInterface;

/**
 * @author by DELL
 * @date on 2017/11/3
 * @describe 加载框的管理类
 */

public class LoadingDialogManager {
    private static int mCount = 0;
    private static LoadingDialog mLoadingDialog;

    private LoadingDialogManager() {
    }

    public static void showLoading(Activity activity) {
        if (mCount == 0) {
            mLoadingDialog = new LoadingDialog(activity);
            mLoadingDialog.setOnCancelListener(new DialogInterface.OnCancelListener() {
                @Override
                public void onCancel(DialogInterface dialogInterface) {
                    mCount = 0;
                }
            });
            mLoadingDialog.show();
        }
        mCount++;
    }

    public static void dismissLoading() {
        if (mCount == 0) {
            return;
        }

        mCount--;
        if (mCount == 0) {
            mLoadingDialog.dismiss();
            mLoadingDialog = null;
        }
    }
}
