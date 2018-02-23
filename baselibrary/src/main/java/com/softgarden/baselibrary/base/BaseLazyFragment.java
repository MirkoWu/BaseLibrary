package com.softgarden.baselibrary.base;

/**
 * Fragment基类 支持 预加载
 */
public abstract class BaseLazyFragment extends BaseFragment {
    private static final String TAG = BaseLazyFragment.class.getSimpleName();
    /**
     * 是否能预加载
     */
    private boolean canLazyLoad = true;

    public boolean isCanLazyLoad() {
        return canLazyLoad;
    }

    /*** 修改要在 {@link #initialize()} 之前 }*/
    public void setCanLazyLoad(boolean canLazyLoad) {
        this.canLazyLoad = canLazyLoad;
    }

    /**
     * 第一次onResume中的调用onUserVisible避免操作与onFirstUserVisible操作重复
     */
    private boolean isFirstResume = true;
    private boolean isFirstVisible = true;
    private boolean isFirstInvisible = true;
    private boolean isPrepared;

    @Override
    public void onResume() {
        super.onResume();
        if (canLazyLoad) {
            if (isFirstResume) {
                isFirstResume = false;
                return;
            }
            if (getUserVisibleHint()) {
                onUserVisible();
            }
        }
    }

    @Override
    public void onPause() {
        super.onPause();
        if (canLazyLoad) {
            if (getUserVisibleHint()) {
                onUserInvisible();
            }
        }
    }


    @Override
    public void setUserVisibleHint(boolean isVisibleToUser) {
        super.setUserVisibleHint(isVisibleToUser);
        if (canLazyLoad) {
            if (isVisibleToUser) {
                if (isFirstVisible) {
                    isFirstVisible = false;
                    initPrepare();
                } else {
                    onUserVisible();
                }
            } else {
                if (isFirstInvisible) {
                    isFirstInvisible = false;
                    onFirstUserInvisible();
                } else {
                    onUserInvisible();
                }
            }
        }
    }

    public synchronized void initPrepare() {
        if (isPrepared) {
            onFirstUserVisible();
        } else {
            isPrepared = true;
        }
    }

    /**
     * 第一次fragment可见（进行初始化工作）
     */
    public void onFirstUserVisible() {
        lazyLoad();
    }


    /**
     * fragment可见（切换回来或者onResume）
     */
    public void onUserVisible() {

    }

    /**
     * 第一次fragment不可见（不建议在此处理事件）
     */
    public void onFirstUserInvisible() {

    }

    /**
     * fragment不可见（切换掉或者onPause）
     */
    public void onUserInvisible() {

    }

    @Override
    protected void initialize() {
        initEventAndData();
        initPrepare();
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        isPrepared = false;
        isFirstVisible = true;
        isFirstResume = true;
        isFirstVisible = true;
        isFirstInvisible = true;
    }

    /**
     * 此方法不进行懒加载 只用于初始设置
     */
    protected abstract void initEventAndData();

    //懒加载 一般用于网络请求
    protected abstract void lazyLoad();

}