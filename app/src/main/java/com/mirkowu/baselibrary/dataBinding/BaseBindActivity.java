package com.mirkowu.baselibrary.dataBinding;

import android.databinding.DataBindingUtil;
import android.databinding.ViewDataBinding;

import com.softgarden.baselibrary.base.BaseActivity;
import com.softgarden.baselibrary.base.IBasePresenter;

/**
 * @author by DELL
 * @date on 2018/8/30
 * @describe
 */
public abstract class BaseBindActivity<P extends IBasePresenter, B extends ViewDataBinding> extends BaseActivity<P> {
    protected B binder;

    @Override
    protected void bindView() {
        binder = DataBindingUtil.setContentView(this, getLayoutId());
    }


}
