package com.softgarden.baselibrary.base;

import android.content.DialogInterface;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.support.annotation.IdRes;
import android.support.annotation.LayoutRes;
import android.support.annotation.Nullable;
import android.support.v4.app.DialogFragment;
import android.support.v4.app.FragmentManager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;

import com.softgarden.baselibrary.BuildConfig;
import com.softgarden.baselibrary.utils.ToastUtil;
import com.trello.rxlifecycle2.components.support.RxAppCompatDialogFragment;

import butterknife.ButterKnife;
import butterknife.Unbinder;

/**
 * DialogFragment 基类
 */
public abstract class BaseDialogFragment<P extends IBasePresenter> extends RxAppCompatDialogFragment implements IBaseDisplay {
    public final String TAG = getClass().getSimpleName();

    private Unbinder unbinder;

    public <T extends View> T $(@IdRes int id) {
        return getView().findViewById(id);
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        getDialog().requestWindowFeature(Window.FEATURE_NO_TITLE);
        View mView = inflater.inflate(getLayoutId(), container, false);
        unbinder = ButterKnife.bind(this, mView);
        return mView;
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        initContentView();
        initPresenter();
        initialize();
    }


    protected void initContentView() {
        if (!isAdded()) return;
        getDialog().getWindow().setBackgroundDrawable(new ColorDrawable());
        setStyle(DialogFragment.STYLE_NO_INPUT, android.R.style.Theme_Light_NoTitleBar_Fullscreen);
    }

    public void show(FragmentManager manager) {
        show(manager, null);
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        unbinder.unbind();
    }

    @LayoutRes
    public abstract int getLayoutId();

    public abstract void initialize();

    @Override
    public synchronized void showProgressDialog() {
        if (getActivity() instanceof BaseActivity)
            ((BaseActivity) getActivity()).showProgressDialog();
    }

    @Override
    public synchronized void hideProgressDialog() {
        if (getActivity() instanceof BaseActivity)
            ((BaseActivity) getActivity()).hideProgressDialog();
    }

    @Override
    public void showError(Throwable throwable) {
        ToastUtil.s(throwable.getMessage());
        if (BuildConfig.DEBUG) throwable.printStackTrace();
    }

    @Override
    public void showReLoginDialog() {
        if (getActivity() instanceof BaseActivity)
            ((BaseActivity) getActivity()).showReLoginDialog();
    }

    @Override
    public void changeDayNightMode(boolean isNightMode) {
        if (getActivity() instanceof BaseActivity)
            ((BaseActivity) getActivity()).changeDayNightMode(isNightMode);
    }


    @Override
    public void onCancel(DialogInterface dialog) {
        super.onCancel(dialog);
        if (onDismissListener != null) onDismissListener.onDismiss(this);
    }

    @Override
    public void onDismiss(DialogInterface dialog) {
        super.onDismiss(dialog);
        if (onDismissListener != null) onDismissListener.onDismiss(this);

    }

    /**
     * 消失时触发
     *
     * @param onDismissListener
     */
    public void setOnDismissListener(OnDismissListener onDismissListener) {
        this.onDismissListener = onDismissListener;
    }

    private OnDismissListener onDismissListener;

    public interface OnDismissListener {

        void onDismiss(DialogFragment dialog);
    }


    /******************************************* MVP **********************************************/
    private P mPresenter;


    protected void initPresenter() {
        mPresenter = createPresenter();
        if (mPresenter != null) mPresenter.attachView(this);
    }

    public P getPresenter() {
        return mPresenter;
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        if (mPresenter != null) mPresenter.detachView();
    }

    /**
     * 创建Presenter 此处已重写 需要时重写即可
     *
     * @return
     */
    public P createPresenter() {
        return null;
    }

    /******************************************* MVP **********************************************/


}
