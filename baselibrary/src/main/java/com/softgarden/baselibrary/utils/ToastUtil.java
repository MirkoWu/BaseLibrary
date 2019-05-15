package com.softgarden.baselibrary.utils;

import android.content.Context;
import android.os.Build;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.softgarden.baselibrary.BaseApplication;
import com.softgarden.baselibrary.R;


public class ToastUtil {

    private ToastUtil() {
        throw new UnsupportedOperationException("u can't fuck me...");
    }

    private static Toast mToast;
    private static TextView mTvMessage;


    /**
     * miui部分版本会自带包名 用此方法解决该Bug
     */
    private static void createToast(Context context, CharSequence message, int duration) {
        mToast = new Toast(context);

        LayoutInflater inflate = (LayoutInflater)
                context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View v = inflate.inflate(R.layout.transient_notification, null);
        mTvMessage = (TextView) v.findViewById(R.id.message);
        mTvMessage.setText(message);

        mToast.setView(v);
        mToast.setDuration(duration);
    }


    public static void show(CharSequence message, int duration) {
        //缓存一个Toast 这种方式体验感觉最好，Toast消失的计时会从最后一次show之后才开始计算，还可以通过setText设置不同的内容
//
//        if (mToast == null)
//            mToast = Toast.makeText(BaseApplication.getInstance().getApplicationContext(),
//                    message, duration);
//        else mToast.setText(message);

        Context context = BaseApplication.getInstance().getApplicationContext();
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {//8.0对Toast做了改动
            createToast(context, message, duration);
        } else {
            if (mToast == null) {
                createToast(context, message, duration);
            } else if (mTvMessage != null) {
                mTvMessage.setText(message);
            } else {
                mToast = null;
                show(message, duration);
            }
        }

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
     * 多语言适配时 getString跟context.getResources() 有关，必须使用当前Context
     *
     * @param msgId
     */
//    public static void s(@StringRes int msgId) {
//        s(BaseApplication.getInstance().getApplicationContext().getResources().getString(msgId));
//    }

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
     * 多语言适配时 getString跟context.getResources() 有关，必须使用当前Context
     *
     * @param msgId
     */
//    public static void l(@StringRes int msgId) {
//        l(BaseApplication.getInstance().getApplicationContext().getResources().getString(msgId));
//    }
}
