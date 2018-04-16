package com.softgarden.baselibrary.base;

import android.support.annotation.IdRes;
import android.view.View;

import com.chad.library.adapter.base.BaseViewHolder;

/**
 * ViewHolder基类 封装
 */

public class BaseRVHolder extends BaseViewHolder {
    public BaseRVHolder(View view) {
        super(view);
    }

    public <T extends View> T $(int id) {
        return getView(id);
    }



    @Override
    public BaseRVHolder setVisible(@IdRes int viewId, boolean visible) {
        View view = getView(viewId);
        view.setVisibility(visible ? View.VISIBLE : View.GONE);
        return this;
    }

    public BaseRVHolder setSelected(@IdRes int viewId, boolean selected) {
        View view = getView(viewId);
        view.setSelected(selected);
        return this;
    }

}
