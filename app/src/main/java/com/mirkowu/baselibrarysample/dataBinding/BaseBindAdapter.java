package com.mirkowu.baselibrarysample.dataBinding;

import android.databinding.DataBindingUtil;
import android.databinding.ViewDataBinding;
import android.support.annotation.LayoutRes;
import android.support.annotation.Nullable;
import android.view.View;
import android.view.ViewGroup;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.softgarden.baselibrary.base.BaseRVHolder;

import java.util.List;

/**
 * @author by DELL
 * @date on 2017/11/3
 * @describe 使用DataBinding的Adapter  可以通用
 */

public class BaseBindAdapter<T> extends BaseQuickAdapter<T, BaseRVHolder> {
    protected int variable;//BR

    public BaseBindAdapter(@LayoutRes int mLayoutResId, int variable, @Nullable List<T> data) {
        super(mLayoutResId, data);
        this.variable = variable;
    }

    public BaseBindAdapter(@LayoutRes int mLayoutResId, int variable) {
        super(mLayoutResId);
        this.variable = variable;
    }

    @Override
    protected void convert(BaseRVHolder helper, T item) {
        ViewDataBinding binding =
                (ViewDataBinding) helper.itemView.getTag(com.softgarden.baselibrary.R.id.BaseQuickAdapter_databinding_support);
        binding.setVariable(variable, item);
        dataBinding(helper, binding, item, helper.getLayoutPosition() - getHeaderLayoutCount());
        binding.executePendingBindings();
    }


    /***
     * 该方法在 executePendingBindings 前执行
     * 所以可以重写该方法  进行其他的数据绑定
     *
     * 一般不用重写
     * @param holder
     * @param binding
     * @param item
     */
    public void dataBinding(BaseRVHolder holder, ViewDataBinding binding, T item, int position) {

    }


    @Override
    protected View getItemView(int layoutResId, ViewGroup parent) {
        ViewDataBinding binding = DataBindingUtil.inflate(mLayoutInflater, layoutResId, parent, false);
        if (binding == null) {
            return super.getItemView(layoutResId, parent);
        }
        View view = binding.getRoot();
        view.setTag(com.softgarden.baselibrary.R.id.BaseQuickAdapter_databinding_support, binding);
        return view;
    }

}
