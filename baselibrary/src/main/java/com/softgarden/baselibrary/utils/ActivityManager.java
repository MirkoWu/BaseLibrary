package com.softgarden.baselibrary.utils;

import android.app.Activity;
import android.app.Application;

import java.util.Stack;

/**
 * Activity管理类
 * <p>
 * 添加/删除 建议在{@link Application#registerActivityLifecycleCallbacks(Application.ActivityLifecycleCallbacks)}中统一处理
 * (此方法比在BaseActivity中处理要好)
 */
public class ActivityManager {
    private static Stack<Activity> mActStack = new Stack<>();
    private volatile static ActivityManager instance;

    public static ActivityManager getInstance() {
        if (instance == null) {
            synchronized (ActivityManager.class) {
                if (instance == null) {
                    instance = new ActivityManager();
                }
            }
        }
        return instance;
    }

    /***  添加 建议在Application中统一处理 */
    public void add(Activity activity) {
        mActStack.add(activity);
    }

    /***  移除 建议在Application中统一处理 */
    public void remove(Activity activity) {
        if (mActStack.contains(activity))
            mActStack.remove(activity);
    }

    public int getCount() {
        return mActStack.size();
    }

    /**
     * 栈内是否包含此activity
     *
     * @param cls
     * @return
     */
    public boolean isContains(Class<?> cls) {
        for (Activity act : mActStack) {
            if (act.getClass().equals(cls)) {
                return true;
            }
        }
        return false;
    }


    /**
     * 查找指定Activity 默认第一个
     *
     * @param cls
     * @return 未找到则返回 null
     */
    public <T extends Activity> T find(Class cls) {
        return findFirst(cls);
    }

    /**
     * 查找指定Activity 第一个
     *
     * @param cls
     * @return
     */
    public <T extends Activity> T findFirst(Class cls) {
        if (mActStack.isEmpty()) return null;

        for (int i = 0; i < mActStack.size(); i++) {
            Activity act = mActStack.get(i);
            if (act.getClass().equals(cls))
                return (T) act;
        }
        return null;
    }

    /**
     * 查找指定Activity 最后一个
     *
     * @param cls
     * @return
     */
    public <T extends Activity> T findLast(Class cls) {
        if (mActStack.isEmpty()) return null;

        for (int i = mActStack.size() - 1; i >= 0; i--) {
            Activity act = mActStack.get(i);
            if (act.getClass().equals(cls))
                return (T) act;
        }

        return null;
    }

    /**
     * 获取当前（即最后一个） Activity
     *
     * @return
     */
    public <T extends Activity> T getCurrent() {
        if (mActStack.isEmpty()) return null;

        return (T) mActStack.lastElement();
    }

    /**
     * 结束指定Activity
     *
     * @param activity
     */
    public void finish(Activity activity) {
        if (activity != null && !activity.isFinishing()) {
            activity.finish();
        }
        // remove(activity);//这个可以不用的 记得在Application中调用
    }

    /**
     * 结束指定Activity
     * 注：当栈中可能包含多个该Activity时，该方法会将所有的该Activity都finish
     *
     * @param cls
     */
    public void finish(Class cls) {
        if (mActStack.isEmpty()) return;

        for (Activity act : mActStack) {
            if (act.getClass().equals(cls)) {
                finish(act);
            }
        }
    }

    /**
     * 结束此Activity之前的所有Activity（不包括当前的 ，结束当前需手动） 最终显示此Activity
     * 1-2-3  * 4-5
     * 1-2  * 4-5
     * 1  * 4-5
     * * 4-5
     *
     * @param activity
     */
    public void finishBefore(Activity activity) {
        if (mActStack.contains(activity)) {
            int index = mActStack.indexOf(activity);
            if (index != -1) {
                for (int i = index - 1; i >= 0; i--) {
                    finish(mActStack.get(i));
                }
            }
        }
    }

    public void finishBefore(Class cls) {
        finishBefore(findFirst(cls));
    }

    /**
     * 结束此Activity之后的所有Activity（不包括当前的 ，结束当前需手动） 最终显示此Activity
     * 1-2-3 * 4-5
     * 1-2-3 * 5
     * 1-2-3 *
     *
     * @param activity
     */
    public void finishAfter(Activity activity) {
        if (mActStack.contains(activity)) {
            int index = mActStack.indexOf(activity);
            if (index != -1) {
                for (int i = index + 1; i < mActStack.size(); i++) {
                    finish(mActStack.get(i));
                }
            }
        }
    }

    public void finishAfter(Class cls) {
        finishAfter(findFirst(cls));
    }

    public void finishAll() {
        for (Activity act : mActStack) {
            finish(act);
        }
    }

    public void exitApp() {
        finishAll();
        System.exit(0);
        android.os.Process.killProcess(android.os.Process.myPid());
    }
}
