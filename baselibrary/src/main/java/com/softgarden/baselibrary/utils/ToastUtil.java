package com.softgarden.baselibrary.utils;

import android.support.annotation.StringRes;
import android.view.Gravity;
import android.widget.Toast;

import com.softgarden.baselibrary.BaseApplication;


public class ToastUtil {

    private ToastUtil() {
        throw new UnsupportedOperationException("u can't fuck me...");
    }

    private static Toast mToast;

    /**
     * 显示 Toast
     *
     * @param message  信息
     * @param duration 显示时间长短
     */
    public static void show(CharSequence message, int duration) {
        //缓存一个Toast 这种方式体验感觉最好，Toast消失的计时会从最后一次show之后才开始计算，还可以通过setText设置不同的内容
        if (mToast == null)
            mToast = Toast.makeText(BaseApplication.getInstance().getApplicationContext(),
                    message, duration);
        else mToast.setText(message);

        mToast.setGravity(Gravity.CENTER, 0, 0);
        mToast.show();
    }

    /**
     * short Toast
     *
     * @param msg
     */
    public static void s(String msg) {
        show(msg, Toast.LENGTH_SHORT);
    }

    /**
     * short Toast
     *
     * @param msgId
     */
    public static void s(@StringRes int msgId) {
        s(BaseApplication.getInstance().getApplicationContext().getResources().getString(msgId));
    }

    /**
     * long Toast
     *
     * @param msg
     */
    public static void l(String msg) {
        show(msg, Toast.LENGTH_LONG);
    }

    /**
     * long Toast
     *
     * @param msgId
     */
    public static void l(@StringRes int msgId) {
        l(BaseApplication.getInstance().getApplicationContext().getResources().getString(msgId));
    }
}
