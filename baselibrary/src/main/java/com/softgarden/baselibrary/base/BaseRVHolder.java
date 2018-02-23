package com.softgarden.baselibrary.base;

import android.support.annotation.IdRes;
import android.view.View;

import com.chad.library.adapter.base.BaseViewHolder;

/**
 * @author by DELL
 * @date on 2017/11/30
 * @describe
 */

public class BaseRVHolder extends BaseViewHolder {
    public BaseRVHolder(View view) {
        super(view);
    }

    public <T extends View> T $(int id) {
        return getView(id);
    }

    @Override
    public BaseViewHolder setVisible(@IdRes int viewId, boolean visible) {
        View view = getView(viewId);
        view.setVisibility(visible ? View.VISIBLE : View.GONE);
        return this;
    }
}
