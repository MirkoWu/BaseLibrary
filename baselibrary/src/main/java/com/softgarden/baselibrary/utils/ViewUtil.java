package com.softgarden.baselibrary.utils;

import android.text.Editable;
import android.text.method.HideReturnsTransformationMethod;
import android.text.method.PasswordTransformationMethod;
import android.widget.EditText;

/**
 * @author by DELL
 * @date on 2018/8/14
 * @describe
 */
public class ViewUtil {

    public static void showPassword(EditText editText, boolean show) {
        if (show) {
            editText.setTransformationMethod(HideReturnsTransformationMethod.getInstance());
        } else {
            editText.setTransformationMethod(PasswordTransformationMethod.getInstance());
        }
    }

    /**
     * 限制小数点后2位
     * 可在 afterTextChanged（） 中使用
     *
     * @param editable
     */
    public static void limitPointNumber(Editable editable) {
        int limitPoint = 2;//限制小数点后2位

        String temp = editable.toString();
        int posDot = temp.indexOf(".");
        if (posDot < 0) {
            return;
        }
        //补零
        if (posDot == 0) {
            editable.insert(0, "0");
            return;
        }

        //删除多余的小数点
        if (temp.length() - posDot - 1 > limitPoint) {
            editable.delete(posDot + limitPoint + 1, posDot + limitPoint + 2);
        }
    }
}
