package com.softgarden.baselibrary.utils;

import android.Manifest;
import android.app.Activity;

import com.tbruyelle.rxpermissions2.RxPermissions;

import io.reactivex.Observable;

/**
 * Created by MirkoWu on 2017/4/5 0005.
 */

public class RxPermissionsUtil {
    public static Observable<Boolean> requestCamera(Activity activity) {
        return new RxPermissions(activity).request(Manifest.permission.CAMERA);
    }

    public static Observable<Boolean> requestStorage(Activity activity) {
        return new RxPermissions(activity).request(Manifest.permission.WRITE_EXTERNAL_STORAGE);
    }

}
