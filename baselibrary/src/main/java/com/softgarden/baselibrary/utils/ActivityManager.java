package com.softgarden.baselibrary.utils;

import android.app.Activity;
import android.app.Application;

import java.lang.ref.WeakReference;
import java.util.Stack;

/**
 * Activity管理类
 * <p>
 * 添加/删除 建议在{@link Application#registerActivityLifecycleCallbacks(Application.ActivityLifecycleCallbacks)}中统一处理
 * (此方法比在BaseActivity中处理要好)
 */
public class ActivityManager {
    private static Stack<WeakReference<Activity>> mActStack = new Stack<>();

    private static class Singleton {
        private static final ActivityManager INSTANCE = new ActivityManager();
    }

    public static ActivityManager getInstance() {
        return Singleton.INSTANCE;
    }

    private ActivityManager() {
    }

    /***  添加 建议在Application中统一处理 */
    public void add(Activity activity) {
        mActStack.add(new WeakReference<>(activity));
    }

    /***  移除 建议在Application中统一处理 */
    public void remove(Activity activity) {
        for (WeakReference<Activity> temp : mActStack) {
            if (isEqualsActivity(temp, activity)) {
                mActStack.remove(temp);
                break;
            }
        }
    }

    /**
     * 获取当前Activity数量
     *
     * @return
     */
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
        for (WeakReference<Activity> temp : mActStack) {
            if (isEqualsActivity(temp, cls)) {
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
        for (WeakReference<Activity> temp : mActStack) {
            if (isEqualsActivity(temp, cls)) {
                return (T) temp.get();
            }
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
        for (int i = mActStack.size() - 1; i >= 0; i--) {
            WeakReference<Activity> temp = mActStack.get(i);
            if (isEqualsActivity(temp, cls)) {
                return (T) temp.get();
            }
        }

        return null;
    }

    /**
     * 获取当前（即最后一个） Activity
     *
     * @return
     */
    public <T extends Activity> T getCurrent() {
        if (mActStack.lastElement() != null) {
            return (T) mActStack.lastElement().get();
        }
        return null;
    }

    private boolean isEqualsActivity(WeakReference<Activity> temp, Class cls) {
        if (temp != null && temp.get() != null && temp.get().getClass().equals(cls)) {
            return true;
        }
        return false;
    }

    private boolean isEqualsActivity(WeakReference<Activity> temp, Activity activity) {
        if (temp != null && temp.get() != null && temp.get() == activity) {
            return true;
        }
        return false;
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
        for (WeakReference<Activity> temp : mActStack) {
            if (isEqualsActivity(temp, cls)) {
                finish(temp.get());
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
     * @param cls
     */
    public void finishBefore(Class cls) {
        boolean isFound = false;
        for (int i = mActStack.size() - 1; i >= 0; i--) {
            WeakReference<Activity> temp = mActStack.get(i);
            if (isFound) {
                if (temp != null && temp.get() != null) {
                    finish(temp.get());
                }
            } else if (isEqualsActivity(temp, cls)) {
                isFound = true;
            }
        }
    }

    /**
     * 结束此Activity之后的所有Activity（不包括当前的 ，结束当前需手动） 最终显示此Activity
     * 1-2-3 * 4-5
     * 1-2-3 * 5
     * 1-2-3 *
     *
     * @param cls
     */
    public void finishAfter(Class cls) {
        boolean isFound = false;
        for (WeakReference<Activity> temp : mActStack) {
            if (isFound) {
                if (temp != null && temp.get() != null) {
                    finish(temp.get());
                }
            } else if (isEqualsActivity(temp, cls)) {
                isFound = true;
            }
        }
    }


    public void finishAll() {
        for (WeakReference<Activity> temp : mActStack) {
            if (temp != null && temp.get() != null) {
                finish(temp.get());
            }
        }
    }

    public void exitApp() {
        finishAll();
        System.exit(0);
        android.os.Process.killProcess(android.os.Process.myPid());
    }
}

