package com.softgarden.baselibrary.utils;

import android.content.Context;
import android.content.SharedPreferences;
import android.support.annotation.NonNull;
import android.text.TextUtils;
import android.util.Base64;

import com.softgarden.baselibrary.BaseApplication;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.Serializable;
import java.util.Map;

/**
 * Created by Administrator on 2016/7/7 0007.
 */

public class SPUtil {

    private SPUtil() {
        throw new UnsupportedOperationException("u can't fuck me...");
    }

    /**
     * 此文件名  和rx-preferences 保持一致
     * 详情见 https://github.com/f2prateek/rx-preferences
     *
     * @return
     */
    public static SharedPreferences getSP() {
        Context context = BaseApplication.getInstance().getApplicationContext();
        return context.getSharedPreferences(context.getPackageName() + "_preferences",
                Context.MODE_PRIVATE);

    }


    /**
     * 保存序列化对象到本地
     *
     * @param key
     * @param object
     */
    public static void putSerializableObject(String key, Object object) {
        if (object == null) {
            put(key, null);
            return;
        }
        if (!(object instanceof Serializable)) {
            throw new RuntimeException("SharedPreferences save bean need implements Serializable");
        }
        try {
            //先将序列化结果写到byte缓存中，其实就分配一个内存空间
            ByteArrayOutputStream bos = new ByteArrayOutputStream();
            ObjectOutputStream os = new ObjectOutputStream(bos);
            os.writeObject(object);//将对象序列化写入byte缓存
            //将序列化的数据用Base64加密
            String base64Str = Base64.encodeToString(bos.toByteArray(), Base64.NO_PADDING);
            //保存该16进制数组
            put(key, base64Str);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**
     * 从本地反序列化获取对象
     *
     * @param key
     * @return
     */
    public static <T extends Serializable> T getSerializableObject(String key) {
        if (getSP().contains(key)) {
            String string = (String) get(key, "");

            if (TextUtils.isEmpty(string)) {
                return null;
            } else {
                try {
                    //Base64解密转为数组，准备反序列化
                    byte[] stringToBytes = Base64.decode(string, Base64.NO_PADDING);
                    ByteArrayInputStream bis = new ByteArrayInputStream(stringToBytes);
                    ObjectInputStream is = new ObjectInputStream(bis);
                    //返回反序列化得到的对象
                    Object readObject = is.readObject();
                    return (T) readObject;
                } catch (IOException e) {
                    e.printStackTrace();
                } catch (ClassNotFoundException e) {
                    e.printStackTrace();
                }
            }
        }
        return null;
    }

    /**
     * 保存数据的方法，我们需要拿到保存数据的具体类型，然后根据类型调用不同的保存方法
     *
     * @param key
     * @param object
     */
    public static void put(String key, Object object) {
        SharedPreferences.Editor editor = getSP().edit();

        if (object == null) {
            editor.putString(key, null);
        } else if (object instanceof String) {
            editor.putString(key, (String) object);
        } else if (object instanceof Integer) {
            editor.putInt(key, (Integer) object);
        } else if (object instanceof Boolean) {
            editor.putBoolean(key, (Boolean) object);
        } else if (object instanceof Float) {
            editor.putFloat(key, (Float) object);
        } else if (object instanceof Long) {
            editor.putLong(key, (Long) object);
        } else if (object != null) {
            editor.putString(key, object.toString());
        }

        editor.commit();
    }

    /**
     * 得到保存数据的方法，我们根据默认值得到保存的数据的具体类型，然后调用相对于的方法获取值
     *
     * @param key
     * @param defaultObject
     * @return
     */
    public static Object get(String key, @NonNull Object defaultObject) {
        SharedPreferences sp = getSP();

        if (defaultObject instanceof String) {
            return sp.getString(key, (String) defaultObject);
        } else if (defaultObject instanceof Integer) {
            return sp.getInt(key, (Integer) defaultObject);
        } else if (defaultObject instanceof Boolean) {
            return sp.getBoolean(key, (Boolean) defaultObject);
        } else if (defaultObject instanceof Float) {
            return sp.getFloat(key, (Float) defaultObject);
        } else if (defaultObject instanceof Long) {
            return sp.getLong(key, (Long) defaultObject);
        } else {
            throw new RuntimeException("The default value defaultObject not be null,you must define Class type");
        }
    }

    /**
     * 移除某个key值已经对应的值
     *
     * @param key
     */
    public static void remove(String key) {
        SharedPreferences sp = getSP();
        SharedPreferences.Editor editor = sp.edit();
        editor.remove(key);
        editor.commit();
    }

    /**
     * 清除所有数据
     */
    public static void clear() {
        SharedPreferences sp = getSP();
        SharedPreferences.Editor editor = sp.edit();
        editor.clear();
        editor.commit();
    }

    /**
     * 查询某个key是否已经存在
     *
     * @param key
     * @return
     */
    public static boolean contains(String key) {
        SharedPreferences sp = getSP();
        return sp.contains(key);
    }

    /**
     * 返回所有的键值对
     *
     * @return
     */
    public static Map<String, ?> getAll() {
        SharedPreferences sp = getSP();
        return sp.getAll();
    }

}

