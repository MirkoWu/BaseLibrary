package com.softgarden.baselibrary.base;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.LayoutRes;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.softgarden.baselibrary.BuildConfig;
import com.softgarden.baselibrary.utils.ToastUtil;
import com.trello.rxlifecycle2.components.support.RxFragment;

import butterknife.ButterKnife;
import butterknife.Unbinder;

import static android.app.Activity.RESULT_OK;
import static com.softgarden.baselibrary.base.BaseActivity.REQUEST_LOGIN;

/**
 * Fragment 基类  已实现以下功能
 * <p>
 * 1.显示/隐藏Loading弹框
 * 2.ButterKnife 绑定数据
 * 3.控制RxJava生命周期，防止内存泄漏
 * 4.MVP模式
 * 需要时 可重写createPresenter() {@link BaseActivity#createPresenter()}  并且使用泛型 <P extends BasePresenter> 为当前Presenter
 */
public abstract class BaseFragment<P extends IBasePresenter> extends RxFragment implements IBaseDisplay {

    protected Unbinder unbinder;

    protected AppCompatActivity mActivity;
    protected Context mContext;

    private int position;
    private String title;

    public int getPosition() {
        return position;
    }

    public void setPosition(int position) {
        this.position = position;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    @Override
    public void onAttach(Context context) {
        mActivity = (AppCompatActivity) context;
        mContext = context;
        super.onAttach(context);
    }


    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View mView = inflater.inflate(getLayoutId(), container, false);
        unbinder = ButterKnife.bind(this, mView);
        return mView;
    }


    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        initPresenter();
        initialize();
    }


    @Override
    public void changeDayNightMode(boolean isNightMode) {
        if (getActivity() instanceof BaseActivity)
            ((BaseActivity) getActivity()).changeDayNightMode(isNightMode);
    }

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
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == RESULT_OK && requestCode == REQUEST_LOGIN) {
            int eventId = 0;
            if (data != null) eventId = data.getIntExtra("eventId", 0);
            backFromLogin(eventId);//从登陆界面返回  登录成功
        }
    }

    protected void backFromLogin(int eventId) {

    }


    public void startActivity(Class<? extends Activity> cls) {
        this.startActivity(new Intent(getActivity(), cls));
    }

    public void startActivityForResult(Class<? extends Activity> cls, int request) {
        this.startActivityForResult(new Intent(getActivity(), cls), request);
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        unbinder.unbind();
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


    @LayoutRes
    protected abstract int getLayoutId();

    protected abstract void initialize();


}
