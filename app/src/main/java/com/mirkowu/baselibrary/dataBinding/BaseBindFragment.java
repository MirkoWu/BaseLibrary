package com.mirkowu.baselibrary.dataBinding;

import android.databinding.DataBindingUtil;
import android.databinding.ViewDataBinding;
import android.view.View;

import com.softgarden.baselibrary.base.BaseFragment;
import com.softgarden.baselibrary.base.IBasePresenter;

/**
 * @author by DELL
 * @date on 2018/8/30
 * @describe
 */
public abstract class BaseBindFragment<P extends IBasePresenter, B extends ViewDataBinding> extends BaseFragment<P> {
    protected B binder;

    @Override
    protected void bindView(View view) {
        binder = DataBindingUtil.bind(view);
    }


}
